package com.badbones69.crazycrates.paper.api.plugin.registry;

import com.badbones69.crazycrates.paper.api.plugin.CrazyCratesPlugin;
import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import org.jetbrains.annotations.NotNull;
import static org.jetbrains.annotations.ApiStatus.Internal;

public class CrazyCratesProvider {

    @Internal
    private CrazyCratesProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    private static CrazyCratesPlugin plugin = null;

    public static @NotNull CrazyCratesPlugin get() {
        CrazyCratesPlugin instance = CrazyCratesProvider.plugin;

        if (instance == null) throw new NotYetAvailable();

        return plugin;
    }

    @Internal
    static void start(CrazyCratesPlugin plugin) {
        if (CrazyCratesProvider.plugin != null) {
            FancyLogger.error("CrazyCrates already has a variable assigned to it! You cannot overwrite it.");
            return;
        }

        CrazyCratesProvider.plugin = plugin;
    }

    @Internal
    static void stop() {
        if (CrazyCratesProvider.plugin == null) {
            FancyLogger.error("CrazyCrates cannot be set as null because it is already null.");
            return;
        }

        CrazyCratesProvider.plugin = null;
    }

    private static final class NotYetAvailable extends IllegalStateException {

        private static final String message = """
                CrazyCrates API isn't available!
                A few reasons of why this could be happening:
                 1. The plugin failed to enable or is not in the plugins folder.
                 2. The plugin trying to use the CrazyCrates doesn't depend on CrazyCrates.
                 3. The plugin trying to use CrazyCrates is using the API before CrazyCrates is enabled.
                 """;

        NotYetAvailable() {
            super(message);
        }
    }
}