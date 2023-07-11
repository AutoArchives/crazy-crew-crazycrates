package com.badbones69.crazycrates;

import com.badbones69.crazycrates.api.*;
import com.badbones69.crazycrates.commands.CommandPermissions;
import com.badbones69.crazycrates.commands.v2.BaseCommand;
import com.badbones69.crazycrates.commands.v2.KeyBaseCommand;
import com.badbones69.crazycrates.commands.v2.admin.CommandAdmin;
import com.badbones69.crazycrates.commands.v2.admin.CommandHelp;
import com.badbones69.crazycrates.api.configs.types.PluginConfig;
import com.badbones69.crazycrates.api.support.holograms.interfaces.HologramManager;
import com.badbones69.crazycrates.commands.v2.admin.CommandReload;
import com.badbones69.crazycrates.commands.v2.admin.keys.CommandAddKeys;
import com.badbones69.crazycrates.commands.v2.admin.schematics.CommandSchematicSave;
import com.badbones69.crazycrates.commands.v2.admin.schematics.CommandSchematicSet;
import com.badbones69.crazycrates.listeners.v2.DataListener;
import com.badbones69.crazycrates.support.placeholders.InternalPlaceholderSupport;
import com.badbones69.crazycrates.support.structures.blocks.ChestStateHandler;
import com.badbones69.crazycrates.support.tasks.AutoSaveTask;
import com.ryderbelserion.stick.paper.PaperCore;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Timer;

public class CrazyCrates extends JavaPlugin implements Listener {

    private final ApiManager apiManager;
    private InternalPlaceholderSupport placeholderManager;

    public CrazyCrates(ApiManager apiManager, PaperCore paperCore) {
        this.apiManager = apiManager;
    }

    private Timer timer;

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public Timer getTimer() {
        return this.timer;
    }

    private boolean isEnabled;

    @Override
    public void onEnable() {
        if (this.apiManager.getPluginConfig().getProperty(PluginConfig.AUTO_SAVE_TOGGLE)) {
            this.timer = new Timer();

            this.timer.schedule(new AutoSaveTask(), 0, 20 * 60 * 1000);
        }

        this.placeholderManager = new InternalPlaceholderSupport();

        registerPermissions(getServer().getPluginManager());

        BaseCommand baseCommand = new BaseCommand();

        KeyBaseCommand keyBaseCommand = new KeyBaseCommand();

        baseCommand.addSubCommand(new CommandHelp(baseCommand));

        // Admin Commands.
        baseCommand.addSubCommand(new CommandAdmin());
        baseCommand.addSubCommand(new CommandReload());

        baseCommand.addSubCommand(new CommandSchematicSave());
        baseCommand.addSubCommand(new CommandSchematicSet());

        baseCommand.addSubCommand(new CommandAddKeys());

        PluginCommand command = getCommand(baseCommand.prefix);

        PluginCommand keyCommand = getCommand(keyBaseCommand.prefix);

        if (command != null) {
            command.setExecutor(baseCommand);
            command.setTabCompleter(baseCommand);
        }

        if (keyCommand != null) {
            keyCommand.setExecutor(keyBaseCommand);
            keyCommand.setTabCompleter(keyBaseCommand);
        }

        getServer().getPluginManager().registerEvents(new DataListener(), this);

        this.isEnabled = true;
    }

    @Override
    public void onDisable() {
        if (!this.isEnabled) return;

        if (this.timer != null) this.timer.cancel();

        if (this.apiManager.getUserManager() != null) this.apiManager.getUserManager().save();

        if (this.apiManager.getHolograms() != null) this.apiManager.getHolograms().purge();
    }

    public void registerPermissions(PluginManager pluginManager) {
        for (CommandPermissions permission : CommandPermissions.values()) {
            if (pluginManager.getPermission(permission.getBuiltPermission()) != null) return;

            pluginManager.addPermission(new Permission(permission.getBuiltPermission(), permission.getDescription(), permission.getPermissionDefault()));
        }
    }

    public ApiManager getApiManager() {
        return this.apiManager;
    }

    public InternalPlaceholderSupport getPlaceholderManager() {
        return this.placeholderManager;
    }

    public boolean verbose() {
        return getApiManager().getPluginConfig().getProperty(PluginConfig.VERBOSE_LOGGING);
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