package com.badbones69.crazycrates.folia.cratetypes;

import com.badbones69.crazycrates.api.crates.CrateType;
import com.badbones69.crazycrates.folia.api.events.PlayerPrizeEvent;
import com.badbones69.crazycrates.folia.utils.Methods;
import com.badbones69.crazycrates.folia.CrazyCrates;
import com.badbones69.crazycrates.folia.api.CrazyManager;
import com.badbones69.crazycrates.folia.api.objects.Crate;
import com.badbones69.crazycrates.folia.api.objects.Prize;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CrateOnTheGo implements Listener {

    private static final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private static final CrazyManager crazyManager = plugin.getCrazyManager();
    
    @EventHandler
    public void onCrateOpen(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = player.getInventory().getItemInMainHand();
            
            if (item == null || item.getType() == Material.AIR) return;
            
            for (Crate crate : crazyManager.getCrates()) {
                if (crate.getCrateType() == CrateType.CRATE_ON_THE_GO && com.badbones69.crazycrates.folia.utils.Methods.isSimilar(item, crate)) {
                    e.setCancelled(true);
                    crazyManager.addPlayerToOpeningList(player, crate);

                    com.badbones69.crazycrates.folia.utils.Methods.removeItem(item, player);

                    Prize prize = crate.pickPrize(player);

                    crazyManager.givePrize(player, prize, crate);
                    plugin.getServer().getPluginManager().callEvent(new PlayerPrizeEvent(player, crate, crazyManager.getOpeningCrate(player).getName(), prize));

                    if (prize.useFireworks()) Methods.firework(player.getLocation().add(0, 1, 0));

                    crazyManager.removePlayerFromOpeningList(player);
                }
            }
        }
    }
}