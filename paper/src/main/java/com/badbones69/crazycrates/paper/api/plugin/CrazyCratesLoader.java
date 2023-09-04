package com.badbones69.crazycrates.paper.api.plugin;

import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.Methods;
import com.badbones69.crazycrates.paper.api.CrazyManager;
import com.badbones69.crazycrates.paper.api.EventLogger;
import com.badbones69.crazycrates.paper.api.FileManager;
import com.badbones69.crazycrates.paper.support.MetricsHandler;
import com.badbones69.crazycrates.paper.support.structures.blocks.ChestStateHandler;
import com.ryderbelserion.cluster.bukkit.BukkitPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CrazyCratesLoader extends CrazyCratesPlugin {
    
    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private FileManager fileManager;
    private CrazyManager crazyManager;
    private ChestStateHandler chestManager;
    private EventLogger eventLogger;
    private Methods methods;

    private MetricsHandler metricsHandler;
    
    private BukkitPlugin bukkitPlugin;

    @Override
    public void enable() {
        // Must go first.
        super.enable();
        
        this.bukkitPlugin = new BukkitPlugin(this.plugin);
        this.bukkitPlugin.enable();

        this.fileManager = new FileManager();
        this.methods = new Methods();

        this.crazyManager = new CrazyManager();
        this.chestManager = new ChestStateHandler();
        this.eventLogger = new EventLogger();

        // Goes after everything else.
        this.metricsHandler = new MetricsHandler();
        this.metricsHandler.start();
    }

    @Override
    public void disable() {
        this.metricsHandler.stop();

        // Disabling the plugin must go last.
        this.bukkitPlugin.disable();
    }

    @Override
    public @NotNull ChestStateHandler getChestManager() {
        return this.chestManager;
    }

    @Override
    public @NotNull BukkitPlugin getBukkitPlugin() {
        return this.bukkitPlugin;
    }

    @Override
    public @NotNull CrazyManager getCrazyManager() {
        return this.crazyManager;
    }

    @Override
    public @NotNull FileManager getFileManager() {
        return this.fileManager;
    }

    @Override
    public @NotNull EventLogger getEventLogger() {
        return this.eventLogger;
    }

    @Override
    public @NotNull MetricsHandler getMetrics() {
        return this.metricsHandler;
    }

    @Override
    public @NotNull Methods getMethods() {
        return this.methods;
    }
}