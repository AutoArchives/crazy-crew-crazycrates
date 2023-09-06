package us.crazycrew.crazycrates.paper.api.plugin.registry;

import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesPlugin;
import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Method;

public class CrazyCratesRegistry {
    
    private static final Method start;
    private static final Method stop;

    static {
        try {
            start = CrazyCratesProvider.class.getDeclaredMethod("start", CrazyCratesPlugin.class);
            start.setAccessible(true);

            stop = CrazyCratesProvider.class.getDeclaredMethod("stop");
            stop.setAccessible(true);
        } catch (NoSuchMethodException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    @ApiStatus.Internal
    public static void start(CrazyCratesPlugin plugin) {
        try {
            start.invoke(null, plugin);
        } catch (Exception exception) {
            FancyLogger.error("Failed to enable CrazyCrates.");
            FancyLogger.debug(exception.getMessage());
        }
    }

    @ApiStatus.Internal
    public static void stop() {
        try {
            stop.invoke(null);
        } catch (Exception exception) {
            FancyLogger.error("Failed to disable CrazyCrates.");
            FancyLogger.debug(exception.getMessage());
        }
    }
}