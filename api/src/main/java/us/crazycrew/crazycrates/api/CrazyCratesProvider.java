package us.crazycrew.crazycrates.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class CrazyCratesProvider {

    @ApiStatus.Internal
    private CrazyCratesProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    private static CrazyCrates plugin = null;

    public static @NotNull CrazyCrates get() {
        CrazyCrates instance = CrazyCratesProvider.plugin;

        if (instance == null) throw new NotYetAvailable();

        return plugin;
    }

    @ApiStatus.Internal
    public static void start(CrazyCrates plugin) {
        if (CrazyCratesProvider.plugin != null) return;

        CrazyCratesProvider.plugin = plugin;
    }

    @ApiStatus.Internal
    public static void stop() {
        if (CrazyCratesProvider.plugin == null) return;

        CrazyCratesProvider.plugin = null;
    }

    private static final class NotYetAvailable extends IllegalStateException {

        private static final String message = """
                CrazyCrates API isn't available!
                A few reasons of why this could be happening:
                 1. The plugin failed to enable or is not in the plugins folder.
                 2. The plugin trying to use CrazyCrates doesn't depend on CrazyCrates.
                 3. The plugin trying to use CrazyCrates is using the API before CrazyCrates is enabled.
                 """;

        NotYetAvailable() {
            super(message);
        }
    }
}