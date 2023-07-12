package com.badbones69.crazycrates;

import com.badbones69.crazycrates.api.*;
import com.badbones69.crazycrates.api.configs.types.Locale;
import com.badbones69.crazycrates.api.utils.MiscUtils;
import com.badbones69.crazycrates.commands.engine.v2.CommandManager;
import com.badbones69.crazycrates.commands.engine.v2.example.FirstCommand;
import com.badbones69.crazycrates.commands.engine.v2.example.SecondCommand;
import com.badbones69.crazycrates.api.configs.types.PluginConfig;
import com.badbones69.crazycrates.api.support.holograms.interfaces.HologramManager;
import com.badbones69.crazycrates.listeners.v2.DataListener;
import com.badbones69.crazycrates.support.structures.blocks.ChestStateHandler;
import com.badbones69.crazycrates.support.tasks.AutoSaveTask;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Timer;

public class CrazyCrates extends JavaPlugin implements Listener {

    private final ApiManager apiManager;
    private final CommandManager manager = CommandManager.create("crazycrates");

    public CrazyCrates(ApiManager apiManager) {
        this.apiManager = apiManager;
    }

    private Timer timer;

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public Timer getTimer() {
        return this.timer;
    }

    @Override
    public void onEnable() {
        if (this.apiManager.getPluginConfig().getProperty(PluginConfig.AUTO_SAVE_TOGGLE)) {
            this.timer = new Timer();

            this.timer.schedule(new AutoSaveTask(), 0, 20 * 60 * 1000);
        }

        MiscUtils.registerPermissions(getServer().getPluginManager());

        this.manager.getEntry().setPermissionMessage(this.apiManager.getPlaceholderSupport().setPlaceholders(this.apiManager.getLocale().getProperty(Locale.NO_PERMISSION)));

        this.manager.addCommand(new FirstCommand());
        this.manager.addCommand(new SecondCommand());

        getServer().getPluginManager().registerEvents(new DataListener(), this);
    }

    @Override
    public void onDisable() {
        if (this.timer != null) this.timer.cancel();

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