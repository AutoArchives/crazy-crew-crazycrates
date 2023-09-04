package com.badbones69.crazycrates.paper.cratetypes;

import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.Methods;
import com.badbones69.crazycrates.paper.api.CrazyManager;
import com.badbones69.crazycrates.paper.api.events.PlayerPrizeEvent;
import com.badbones69.crazycrates.paper.api.objects.Crate;
import com.badbones69.crazycrates.paper.api.objects.Prize;
import com.badbones69.crazycrates.api.enums.types.CrateType;
import com.badbones69.crazycrates.paper.api.plugin.CrazyCratesPlugin;
import com.badbones69.crazycrates.paper.api.plugin.registry.CrazyCratesProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CrateOnTheGo implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesPlugin cratesPlugin = CrazyCratesProvider.get();
    private final @NotNull CrazyManager crazyManager = this.cratesPlugin.getCrazyManager();
    private final @NotNull Methods methods = this.cratesPlugin.getMethods();
    
    @EventHandler
    public void onCrateOpen(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = player.getInventory().getItemInMainHand();
            
            if (item == null || item.getType() == Material.AIR) return;
            
            for (Crate crate : crazyManager.getCrates()) {
                if (crate.getCrateType() == CrateType.CRATE_ON_THE_GO && methods.isSimilar(item, crate)) {
                    e.setCancelled(true);
                    crazyManager.addPlayerToOpeningList(player, crate);

                    methods.removeItem(item, player);

                    Prize prize = crate.pickPrize(player);

                    crazyManager.givePrize(player, prize, crate);
                    plugin.getServer().getPluginManager().callEvent(new PlayerPrizeEvent(player, crate, crazyManager.getOpeningCrate(player).getName(), prize));

                    if (prize.useFireworks()) methods.firework(player.getLocation().add(0, 1, 0));

                    crazyManager.removePlayerFromOpeningList(player);
                }
            }
        }
    }
}