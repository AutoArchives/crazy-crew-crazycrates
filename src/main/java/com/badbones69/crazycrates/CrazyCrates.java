package com.badbones69.crazycrates;

import com.badbones69.crazycrates.api.*;
import com.badbones69.crazycrates.api.utils.MiscUtils;
import com.badbones69.crazycrates.api.support.holograms.interfaces.HologramManager;
import com.badbones69.crazycrates.commands.engine.v3.paper.BukkitCommandManager;
import com.badbones69.crazycrates.commands.engine.v3.paper.example.ExampleCommand;
import com.badbones69.crazycrates.listeners.v2.DataListener;
import com.badbones69.crazycrates.support.structures.blocks.ChestStateHandler;
import com.ryderbelserion.stick.core.StickLogger;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.logging.Logger;

public class CrazyCrates extends JavaPlugin implements Listener {

    private final ApiManager apiManager;

    public CrazyCrates(ApiManager apiManager) {
        this.apiManager = apiManager;
    }

    @Override
    public @NotNull Logger getLogger() {
        return StickLogger.getLogger();
    }

    @Override
    public void onEnable() {
        MiscUtils.registerPermissions(getServer().getPluginManager());

        // Create instance.
        BukkitCommandManager manager = BukkitCommandManager.create(this, "crazycrates", "base command", context -> {});

        // Enable some compat improvements.
        manager.registerCompatibility();

        // Add the command.
        manager.getCloudCommandManager().addCommand(new ExampleCommand(manager));

        getServer().getPluginManager().registerEvents(new DataListener(), this);
    }

    @Override
    public void onDisable() {
        if (this.apiManager.getUserManager() != null) this.apiManager.getUserManager().save();
        if (this.apiManager.getHolograms() != null) this.apiManager.getHolograms().purge();
    }

    public ApiManager getApiManager() {
        return this.apiManager;
    }

    public HologramManager getHolograms() {
        return getApiManager().getHolograms();
    }

    // TODO() Remove
    public FileManager getFileManager() {
        return new FileManager();
    }

    public CrazyManager getCrazyManager() {
        return new CrazyManager();
    }

    public ChestStateHandler getChestStateHandler() {
        return new ChestStateHandler();
    }

    public EventLogger getEventLogger() {
        return new EventLogger();
    }
}