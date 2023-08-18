package com.badbones69.crazycrates.folia;

import com.badbones69.crazycrates.api.ConfigManager;
import com.badbones69.crazycrates.api.utils.MiscUtils;
import com.badbones69.crazycrates.folia.api.EventLogger;
import com.badbones69.crazycrates.folia.cratetypes.Roulette;
import com.badbones69.crazycrates.folia.listeners.FireworkDamageListener;
import com.badbones69.crazycrates.folia.support.libraries.PluginSupport;
import com.badbones69.crazycrates.folia.support.placeholders.PlaceholderAPISupport;
import com.badbones69.crazycrates.folia.api.CrazyManager;
import com.badbones69.crazycrates.folia.api.FileManager;
import com.badbones69.crazycrates.folia.api.managers.quadcrates.SessionManager;
import com.badbones69.crazycrates.folia.support.structures.blocks.ChestStateHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CrazyCrates extends JavaPlugin {

    private final ConfigManager configManager;

    private FileManager fileManager;
    private CrazyManager crazyManager;
    private com.badbones69.crazycrates.folia.api.EventLogger eventLogger;
    private ChestStateHandler chestHandler;

    public CrazyCrates(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void onEnable() {
        // Enable bukkit impl
        //this.bukkit.setPlugin(this);
        //this.bukkit.enable(true);

        // Register permissions to server
        MiscUtils.registerPermissions(getServer().getPluginManager());

        // Set command namespace
        //this.bukkit.getManager().setNamespace("crazycrates");

        this.fileManager = new FileManager();

        this.fileManager.setLog(true)
                .registerDefaultGenerateFiles("CrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("QuadCrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("CosmicCrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("QuickCrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("classic.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("nether.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("outdoors.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("sea.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("soul.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("wooden.nbt", "/schematics", "/schematics")
                .registerCustomFilesFolder("/crates")
                .registerCustomFilesFolder("/schematics")
                .setup();

        this.crazyManager = new CrazyManager();
        this.eventLogger = new com.badbones69.crazycrates.folia.api.EventLogger();
        this.chestHandler = new ChestStateHandler();

        this.crazyManager.janitor();

        // Enable commands
        //List.of(
        //        new CrateCommandReload(),
        //        new CrateCommandDebug(),
        //        new CrateCommandMenu(),
        //        new CrateCommandHelp(),
        //        new CrateCommandKey()
        //).forEach(this.bukkit.getManager()::addCommand);

        enable();
    }

    @Override
    public void onDisable() {
        //this.bukkit.disable();

        SessionManager.endCrates();

        com.badbones69.crazycrates.folia.cratetypes.QuickCrate.removeAllRewards();

        if (this.crazyManager.getHologramController() != null) this.crazyManager.getHologramController().removeAllHolograms();
    }

    private void enable() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new com.badbones69.crazycrates.folia.listeners.MenuListener(), this);
        pluginManager.registerEvents(new com.badbones69.crazycrates.folia.listeners.PreviewListener(), this);
        pluginManager.registerEvents(new FireworkDamageListener(), this);
        pluginManager.registerEvents(new com.badbones69.crazycrates.folia.listeners.CrateControlListener(), this);
        pluginManager.registerEvents(new com.badbones69.crazycrates.folia.listeners.MiscListener(), this);

        pluginManager.registerEvents(new com.badbones69.crazycrates.folia.cratetypes.War(), this);
        pluginManager.registerEvents(new com.badbones69.crazycrates.folia.cratetypes.CSGO(), this);
        pluginManager.registerEvents(new com.badbones69.crazycrates.folia.cratetypes.Wheel(), this);
        pluginManager.registerEvents(new com.badbones69.crazycrates.folia.cratetypes.Wonder(), this);
        pluginManager.registerEvents(new com.badbones69.crazycrates.folia.cratetypes.Cosmic(), this);
        pluginManager.registerEvents(new Roulette(), this);
        pluginManager.registerEvents(new com.badbones69.crazycrates.folia.cratetypes.QuickCrate(), this);
        pluginManager.registerEvents(new com.badbones69.crazycrates.folia.cratetypes.CrateOnTheGo(), this);
        pluginManager.registerEvents(new com.badbones69.crazycrates.folia.cratetypes.QuadCrate(), this);

        this.crazyManager.loadCrates();

        if (!this.crazyManager.getBrokeCrateLocations().isEmpty()) pluginManager.registerEvents(new com.badbones69.crazycrates.folia.listeners.BrokeLocationsListener(), this);

        if (com.badbones69.crazycrates.folia.support.libraries.PluginSupport.PLACEHOLDERAPI.isPluginEnabled()) new PlaceholderAPISupport().register();

        for (com.badbones69.crazycrates.folia.support.libraries.PluginSupport value : PluginSupport.values()) {
            if (value.isPluginEnabled()) {
                getLogger().info("<gold>" + value.name() + "</gold> <bold><green>FOUND</green></bold>");
            } else {
                getLogger().info("<gold>" + value.name() + "</gold> <bold><red>NOT FOUND</red></bold>");
            }
        }
    }

    //public BukkitImpl getBukkit() {
        //return this.bukkit;
    //}

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }

    public CrazyManager getCrazyManager() {
        return this.crazyManager;
    }

    public EventLogger getEventLogger() {
        return this.eventLogger;
    }

    public ChestStateHandler getChestHandler() {
        return this.chestHandler;
    }
}