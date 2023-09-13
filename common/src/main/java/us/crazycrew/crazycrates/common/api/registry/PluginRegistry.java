package us.crazycrew.crazycrates.common.api.registry;

import org.jetbrains.annotations.ApiStatus;
import us.crazycrew.crazycrates.api.CrazyCratesProvider;
import us.crazycrew.crazycrates.api.CrazyCrates;
import java.lang.reflect.Method;

public class PluginRegistry {

    private static final Method start;
    private static final Method stop;

    static {
        try {
            start = CrazyCratesProvider.class.getDeclaredMethod("start", CrazyCrates.class);
            start.setAccessible(true);

            stop = CrazyCratesProvider.class.getDeclaredMethod("stop");
            stop.setAccessible(true);
        } catch (NoSuchMethodException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    @ApiStatus.Internal
    public static void start(CrazyCrates plugin) {
        try {
            start.invoke(null, plugin);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @ApiStatus.Internal
    public static void stop() {
        try {
            stop.invoke(null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}