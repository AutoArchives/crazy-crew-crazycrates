package com.badbones69.crazycrates.paper.api.plugin.registry;

import com.badbones69.crazycrates.paper.api.plugin.CrazyCratesPlugin;
import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class CrazyCratesProvider {

    private static CrazyCratesPlugin plugin = null;

    public static @NotNull CrazyCratesPlugin get() {
        CrazyCratesPlugin instance = CrazyCratesProvider.plugin;

        if (instance == null) throw new RuntimeException("Failed to use the get() method. Contact the developer.");

        return plugin;
    }

    @ApiStatus.Internal
    public static void start(CrazyCratesPlugin plugin) {
        if (CrazyCratesProvider.plugin != null) {
            FancyLogger.error("CrazyCrates already has a variable assigned to it! You cannot overwrite it.");
            return;
        }

        CrazyCratesProvider.plugin = plugin;
    }

    @ApiStatus.Internal
    public static void stop() {
        if (CrazyCratesProvider.plugin == null) {
            FancyLogger.warn("CrazyCrates cannot be set as null because it is already null.");
            return;
        }

        CrazyCratesProvider.plugin = null;
    }
}