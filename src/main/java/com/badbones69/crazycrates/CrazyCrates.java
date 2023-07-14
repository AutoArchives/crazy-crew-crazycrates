package com.badbones69.crazycrates;

import com.badbones69.crazycrates.api.*;
import com.badbones69.crazycrates.api.utils.MiscUtils;
import com.badbones69.crazycrates.commands.engine.v2.CommandManager;
import com.badbones69.crazycrates.commands.engine.v2.example.FirstCommand;
import com.badbones69.crazycrates.commands.engine.v2.example.SecondCommand;
import com.badbones69.crazycrates.api.support.holograms.interfaces.HologramManager;
import com.badbones69.crazycrates.listeners.v2.DataListener;
import com.badbones69.crazycrates.support.structures.blocks.ChestStateHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CrazyCrates extends JavaPlugin implements Listener {

    private final ApiManager apiManager;
    private final CommandManager manager = CommandManager.create("crazycrates");

    public CrazyCrates(ApiManager apiManager) {
        this.apiManager = apiManager;
    }

    @Override
    public void onEnable() {
        MiscUtils.registerPermissions(getServer().getPluginManager());

        this.manager.addCommand(new FirstCommand());
        this.manager.addCommand(new SecondCommand());

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

    public CommandManager getManager() {
        return this.manager;
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