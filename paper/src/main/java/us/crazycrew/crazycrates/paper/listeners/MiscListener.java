package us.crazycrew.crazycrates.paper.listeners;

import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesPlugin;

import us.crazycrew.crazycrates.api.enums.types.CrateType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MiscListener implements Listener {

    private final @NotNull CrazyCratesPlugin cratesPlugin = null;
    private final @NotNull CrazyManager crazyManager = this.cratesPlugin.getCrazyManager();

    @EventHandler
    public void onPlayerPickUp(PlayerAttemptPickupItemEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (crazyManager.isDisplayReward(event.getItem())) {
            event.setCancelled(true);
            return;
        }

        if (crazyManager.isInOpeningList(uuid)) {
            // DrBot Start
            if (crazyManager.getOpeningCrate(uuid).getCrateType().equals(CrateType.QUICK_CRATE)) return;
            // DrBot End
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();

        this.cratesPlugin.getCrazyManager().setNewPlayerKeys(uuid);
        this.cratesPlugin.getCrazyManager().loadOfflinePlayersKeys(uuid);
    }
}