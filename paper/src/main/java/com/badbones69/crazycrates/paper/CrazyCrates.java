package com.badbones69.crazycrates.paper;

import com.badbones69.crazycrates.paper.api.*;
import com.badbones69.crazycrates.paper.api.v2.ApiManager;
import com.badbones69.crazycrates.paper.api.v2.utils.MiscUtils;
import com.badbones69.crazycrates.paper.support.holograms.interfaces.HologramManager;
import com.badbones69.crazycrates.paper.commands.engine.paper.BukkitCommandManager;
import com.badbones69.crazycrates.paper.listeners.v2.DataListener;
import com.badbones69.crazycrates.paper.support.structures.blocks.ChestStateHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CrazyCrates extends JavaPlugin implements Listener {

    private ApiManager apiManager;

    @Override
    public void onEnable() {
        this.apiManager = new ApiManager(getDataFolder().toPath(), this);

        this.apiManager.load(true);

        MiscUtils.registerPermissions(getServer().getPluginManager());

        // Create instance.
        BukkitCommandManager manager = BukkitCommandManager.create(this, "crazycrates", "base command", context -> {});

        // Enable some compat improvements.
        manager.registerCompatibility();

        // Add the command.
        //manager.getCloudCommandManager().addCommand(new ExampleCommand(manager));

        getServer().getPluginManager().registerEvents(new DataListener(), this);
    }

    @Override
    public void onDisable() {
        if (this.apiManager.getUserManager() != null) this.apiManager.getUserManager().save();
        if (this.apiManager.getHolograms() != null) this.apiManager.getHolograms().purge();

        if (this.apiManager.getPaperCore() != null) this.apiManager.getPaperCore().disable();
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