package com.badbones69.crazycrates.folia.support;

import com.badbones69.crazycrates.folia.CrazyCrates;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class MetricsHandler {

    private final com.badbones69.crazycrates.folia.CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    public void start() {
        new Metrics(plugin, 4514);

        plugin.getLogger().info("Metrics has been enabled.");
    }
}