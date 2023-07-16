package com.badbones69.crazycrates.core.frame;

import com.badbones69.crazycrates.core.frame.storage.FileHandler;
import net.kyori.adventure.platform.AudienceProvider;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Field;
import java.nio.file.Path;

public abstract class CrazyCore {

    public static @NotNull CrazyCore api() {
        return Provider.api();
    }

    public CrazyCore() {
        try {
            Field api = Provider.class.getDeclaredField("api");
            api.setAccessible(true);
            api.set(null, this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected static final class Provider {
        static CrazyCore api;

        static @NotNull CrazyCore api() {
            return Provider.api;
        }
    }

    public void enable() {}

    public void disable() {}

    public abstract Path getDirectory();

    public abstract String getPrefix();

    public abstract String getConsolePrefix();

    public abstract FileHandler getFileHandler();

    public abstract @NotNull AudienceProvider adventure();
}