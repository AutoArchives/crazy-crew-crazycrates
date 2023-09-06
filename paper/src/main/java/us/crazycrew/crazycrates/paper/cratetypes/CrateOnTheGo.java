package us.crazycrew.crazycrates.paper.cratetypes;

import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.events.PlayerPrizeEvent;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.objects.Prize;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;
import java.util.UUID;

public class CrateOnTheGo implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesLoader cratesLoader = this.plugin.getCratesLoader();
    private final @NotNull CrazyManager crazyManager = this.cratesLoader.getCrazyManager();
    private final @NotNull Methods methods = this.cratesLoader.getMethods();
    
    @EventHandler
    public void onCrateOpen(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = player.getInventory().getItemInMainHand();
            
            if (item == null || item.getType() == Material.AIR) return;
            
            for (Crate crate : crazyManager.getCrates()) {
                if (crate.getCrateType() == CrateType.CRATE_ON_THE_GO && methods.isSimilar(item, crate)) {
                    e.setCancelled(true);
                    crazyManager.addPlayerToOpeningList(uuid, crate);

                    methods.removeItem(item, player);

                    Prize prize = crate.pickPrize(player);

                    crazyManager.givePrize(player, prize, crate);
                    plugin.getServer().getPluginManager().callEvent(new PlayerPrizeEvent(uuid, crate, crazyManager.getOpeningCrate(uuid).getName(), prize));

                    if (prize.useFireworks()) methods.firework(player.getLocation().add(0, 1, 0));

                    crazyManager.removePlayerFromOpeningList(uuid);
                }
            }
        }
    }
}