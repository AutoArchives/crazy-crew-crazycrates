package us.crazycrew.crazycrates.paper.listeners;

import org.bukkit.plugin.java.JavaPlugin;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyHandler;
import java.util.UUID;

public class MiscListener implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyHandler crazyHandler = this.plugin.getCrazyHandler();
    private final @NotNull CrazyManager crazyManager = this.crazyHandler.getCrazyManager();

    @EventHandler
    public void onPlayerPickUp(PlayerAttemptPickupItemEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (this.crazyManager.isDisplayReward(event.getItem())) {
            event.setCancelled(true);
            return;
        }

        if (this.crazyManager.isInOpeningList(uuid)) {
            // DrBot Start
            if (this.crazyManager.getOpeningCrate(uuid).getCrateType().equals(CrateType.QUICK_CRATE)) return;
            // DrBot End
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();

        this.crazyHandler.getCrazyManager().setNewPlayerKeys(player);

        this.crazyHandler.getUserManager().loadOfflinePlayersKeys(player, this.crazyManager.getCrates());
    }
}