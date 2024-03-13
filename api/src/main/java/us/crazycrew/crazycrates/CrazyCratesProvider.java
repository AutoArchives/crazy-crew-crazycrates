package us.crazycrew.crazycrates;

import org.jetbrains.annotations.ApiStatus;

/**
 * A class used to initialize the api so other plugins can use it.
 *
 * @author Ryder Belserion
 * @version 0.4
 */
public class CrazyCratesProvider {

    private static CrazyCrates instance;

    public static CrazyCrates get() {
        if (instance == null) {
            throw new IllegalStateException("CrazyCrates is not loaded.");
        }

        return instance;
    }

    @ApiStatus.Internal
    private CrazyCratesProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    @ApiStatus.Internal
    static void register(CrazyCrates instance) {
        if (CrazyCratesProvider.instance != null) {
            instance.getLogger().warning("CrazyCrates is already enabled.");

            return;
        }

        CrazyCratesProvider.instance = instance;
    }

    @ApiStatus.Internal
    static void unregister() {
        CrazyCratesProvider.instance = null;
    }
}