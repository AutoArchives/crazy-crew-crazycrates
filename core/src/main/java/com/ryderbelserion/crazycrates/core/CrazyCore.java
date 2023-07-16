package com.ryderbelserion.crazycrates.core;

import net.kyori.adventure.platform.AudienceProvider;
import com.ryderbelserion.crazycrates.core.storage.FileHandler;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public abstract class CrazyCore {

    public static @NotNull CrazyCore api() {
        return Provider.api();
    }

    protected static final class Provider {
        static CrazyCore api;

        static @NotNull CrazyCore api() {
            return Provider.api;
        }
    }

    public void enable() {

    }

    public void disable() {

    }

    public abstract Path getDirectory();

    public abstract String getPrefix();

    public abstract FileHandler getFileHandler();

    public abstract @NotNull AudienceProvider adventure();
}