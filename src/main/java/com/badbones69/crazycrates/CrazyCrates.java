package com.badbones69.crazycrates;

import com.badbones69.crazycrates.api.*;
import com.badbones69.crazycrates.commands.CommandCore;
import com.badbones69.crazycrates.commands.CommandPermissions;
import com.badbones69.crazycrates.commands.v2.BaseCommand;
import com.badbones69.crazycrates.commands.v2.KeyBaseCommand;
import com.badbones69.crazycrates.commands.v2.admin.CommandAdmin;
import com.badbones69.crazycrates.commands.v2.admin.CommandHelp;
import com.badbones69.crazycrates.api.configs.types.PluginConfig;
import com.badbones69.crazycrates.api.holograms.interfaces.HologramManager;
import com.badbones69.crazycrates.commands.v2.admin.CommandReload;
import com.badbones69.crazycrates.commands.v2.admin.keys.CommandAddKeys;
import com.badbones69.crazycrates.commands.v2.admin.schematics.CommandSchematicSave;
import com.badbones69.crazycrates.commands.v2.admin.schematics.CommandSchematicSet;
import com.badbones69.crazycrates.listeners.v2.DataListener;
import com.badbones69.crazycrates.support.placeholders.InternalPlaceholderSupport;
import com.badbones69.crazycrates.support.structures.blocks.ChestStateHandler;
import com.ryderbelserion.stick.paper.utils.PaperUtils;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class CrazyCrates extends JavaPlugin implements Listener {

    private ApiManager apiManager;
    private InternalPlaceholderSupport placeholderManager;

    private FileManager fileManager;
    private CrazyManager crazyManager;
    private ChestStateHandler chestStateHandler;
    private EventLogger eventLogger;

    private boolean isEnabled;

    @Override
    public void onEnable() {
        if (PaperUtils.isSpigot()) {
            List<String> msg = List.of(
                    "We no longer support Spigot servers.",
                    "It is recommended that you switch to https://papermc.io/",
                    "The plugin will now shut-down!");

            msg.forEach(getLogger()::warning);

            getServer().getPluginManager().disablePlugin(this);

            this.isEnabled = false;

            return;
        }

        this.apiManager = new ApiManager(this, getDataFolder().toPath());
        this.apiManager.load();

        this.placeholderManager = new InternalPlaceholderSupport();

        registerPermissions(getServer().getPluginManager());

        new CommandCore();

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

        //SessionManager.endCrates();

        //QuickCrate.removeAllRewards();

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

    public FileManager getFileManager() {
        return this.fileManager;
    }

    public CrazyManager getCrazyManager() {
        return this.crazyManager;
    }

    public ChestStateHandler getChestStateHandler() {
        return this.chestStateHandler;
    }

    public EventLogger getEventLogger() {
        return this.eventLogger;
    }
}