package com.badbones69.crazycrates.paper.listeners;

import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.CrazyManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;

public class MiscListener implements Listener {

    private final CrazyCrates plugin = CrazyCrates.getPlugin(CrazyCrates.class);

    private final CrazyManager crazyManager = plugin.getCrazyManager();

    @EventHandler
    public void onPlayerPickUp(PlayerAttemptPickupItemEvent event) {
        if (crazyManager.isDisplayReward(event.getItem())) {
            event.setCancelled(true);
            return;
        }

        if (crazyManager.isInOpeningList(event.getPlayer())) {
            // DrBot Start
            //if (crazyManager.getOpeningCrate(event.getPlayer()).getCrateType().equals(CrateType.QUICK_CRATE)) return;
            // DrBot End
            event.setCancelled(true);
        }
    }
}