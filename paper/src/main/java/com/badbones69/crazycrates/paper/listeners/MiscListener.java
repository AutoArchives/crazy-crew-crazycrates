package com.badbones69.crazycrates.paper.listeners;

import com.badbones69.crazycrates.paper.api.CrazyManager;
import com.badbones69.crazycrates.api.enums.types.CrateType;
import com.badbones69.crazycrates.paper.api.plugin.CrazyCratesPlugin;
import com.badbones69.crazycrates.paper.api.plugin.registry.CrazyCratesProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.jetbrains.annotations.NotNull;

public class MiscListener implements Listener {

    private final @NotNull CrazyCratesPlugin cratesPlugin = CrazyCratesProvider.get();
    private final @NotNull CrazyManager crazyManager = this.cratesPlugin.getCrazyManager();

    @EventHandler
    public void onPlayerPickUp(PlayerAttemptPickupItemEvent event) {
        if (crazyManager.isDisplayReward(event.getItem())) {
            event.setCancelled(true);
            return;
        }

        if (crazyManager.isInOpeningList(event.getPlayer())) {
            // DrBot Start
            if (crazyManager.getOpeningCrate(event.getPlayer()).getCrateType().equals(CrateType.QUICK_CRATE)) return;
            // DrBot End
            event.setCancelled(true);
        }
    }
}