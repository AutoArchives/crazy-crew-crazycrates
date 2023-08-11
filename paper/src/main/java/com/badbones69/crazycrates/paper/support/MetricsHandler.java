package com.badbones69.crazycrates.paper.support;

import com.badbones69.crazycrates.paper.CrazyCrates;
import org.bstats.bukkit.Metrics;

public class MetricsHandler {

    private final CrazyCrates plugin = CrazyCrates.getPlugin();

    public void start() {
        new Metrics(plugin, 4514);

        plugin.getLogger().info("Metrics has been enabled.");
    }
}