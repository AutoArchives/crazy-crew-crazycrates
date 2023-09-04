package com.badbones69.crazycrates.paper.api.plugin;

import com.badbones69.crazycrates.paper.Methods;
import com.badbones69.crazycrates.paper.api.CrazyManager;
import com.badbones69.crazycrates.paper.api.EventLogger;
import com.badbones69.crazycrates.paper.api.FileManager;
import com.badbones69.crazycrates.paper.api.plugin.registry.CrazyCratesRegistry;
import com.badbones69.crazycrates.paper.support.MetricsHandler;
import com.badbones69.crazycrates.paper.support.structures.blocks.ChestStateHandler;
import com.ryderbelserion.cluster.bukkit.BukkitPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class CrazyCratesPlugin {

    public abstract @NotNull ChestStateHandler getChestManager();

    public abstract @NotNull BukkitPlugin getBukkitPlugin();

    public abstract @NotNull CrazyManager getCrazyManager();

    public abstract @NotNull FileManager getFileManager();

    public abstract @NotNull EventLogger getEventLogger();
    
    public abstract @NotNull MetricsHandler getMetrics();

    public abstract @NotNull Methods getMethods();
    
    public void enable() {
        CrazyCratesRegistry.start(this);
    }
    
    public void disable() {
        CrazyCratesRegistry.stop();
    }
}