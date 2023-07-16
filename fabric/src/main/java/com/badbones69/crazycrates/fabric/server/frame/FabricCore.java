package com.badbones69.crazycrates.fabric.server.frame;

import com.badbones69.crazycrates.core.frame.CrazyCore;
import com.badbones69.crazycrates.core.frame.storage.FileHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public class FabricCore extends CrazyCore {

    private final FabricServerAudiences adventure;

    public FabricCore(MinecraftServer server) {
        this.adventure = FabricServerAudiences.of(server);
    }

    @Override
    public Path getDirectory() {
        return FabricLoader.getInstance().getGameDir().resolve("crazycrates");
    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public String getConsolePrefix() {
        return null;
    }

    @Override
    public FileHandler getFileHandler() {
        return null;
    }

    @Override
    public @NotNull AudienceProvider adventure() {
        return this.adventure;
    }
}