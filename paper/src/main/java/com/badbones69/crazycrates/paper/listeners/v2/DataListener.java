package com.badbones69.crazycrates.paper.listeners.v2;

import com.badbones69.crazycrates.core.ApiManager;
import com.badbones69.crazycrates.core.config.types.PluginConfig;
import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.CrazyManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.UUID;

public class DataListener implements Listener {

    private final CrazyCrates plugin = CrazyCrates.getPlugin(CrazyCrates.class);

    private final CrazyManager crazyManager = this.plugin.crazyManager();
    private final ApiManager apiManager = this.plugin.getApiManager();

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPlayedBefore()) return;

        UUID uuid = event.getPlayer().getUniqueId();

        if (this.crazyManager.getCrateManager().getCrates().isEmpty()) return;

        this.crazyManager.getCrateManager().getCrates().forEach(crate -> {
            this.crazyManager.getUserManager().addUser(uuid, crate);

            if (crate.getCrateConfig().isStartingKeysEnabled()) {
                this.crazyManager.getUserManager().addKey(uuid, crate.getCrateConfig().getStartingKeysAmount(), crate);

                if (this.apiManager.getPluginConfig().getProperty(PluginConfig.VERBOSE_LOGGING)) this.plugin.getLogger().warning("Added starting keys to " + uuid + ".");
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.crazyManager.getUserManager().saveSingular(event.getPlayer().getUniqueId());
    }
}