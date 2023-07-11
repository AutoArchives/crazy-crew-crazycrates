package com.badbones69.crazycrates.listeners.v2;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.ApiManager;
import com.badbones69.crazycrates.api.configs.types.PluginConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class DataListener implements Listener {

    private final CrazyCrates plugin = CrazyCrates.getPlugin(CrazyCrates.class);

    private final ApiManager apiManager = plugin.getApiManager();

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPlayedBefore()) return;

        UUID uuid = event.getPlayer().getUniqueId();

        if (this.apiManager.getCrateManager().getCrates().isEmpty()) return;

        this.apiManager.getCrateManager().getCrates().forEach(crate -> {
            this.apiManager.getUserManager().addUser(uuid, crate);

            if (crate.getCrateConfig().isStartingKeysEnabled()) {
                this.apiManager.getUserManager().addKey(uuid, crate.getCrateConfig().getStartingKeysAmount(), crate);

                if (this.plugin.verbose()) this.plugin.getLogger().warning("Added starting keys to " + uuid + ".");
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.apiManager.getUserManager().saveSingular(event.getPlayer().getUniqueId());
    }
}