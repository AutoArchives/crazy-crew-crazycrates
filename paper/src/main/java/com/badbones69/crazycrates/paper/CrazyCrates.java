package com.badbones69.crazycrates.paper;

import com.badbones69.crazycrates.api.utils.MiscUtils;
import com.badbones69.crazycrates.paper.api.FileManager.Files;
import com.badbones69.crazycrates.paper.api.managers.quadcrates.SessionManager;
import com.badbones69.crazycrates.paper.commands.subs.CrateCommandHelp;
import com.badbones69.crazycrates.paper.commands.subs.CrateCommandKey;
import com.badbones69.crazycrates.paper.commands.subs.CrateCommandMenu;
import com.badbones69.crazycrates.paper.commands.subs.CrateCommandTest;
import com.badbones69.crazycrates.paper.commands.subs.admin.CrateCommandDebug;
import com.badbones69.crazycrates.paper.commands.subs.admin.CrateCommandReload;
import com.badbones69.crazycrates.paper.cratetypes.CSGO;
import com.badbones69.crazycrates.paper.cratetypes.Cosmic;
import com.badbones69.crazycrates.paper.cratetypes.CrateOnTheGo;
import com.badbones69.crazycrates.paper.cratetypes.QuadCrate;
import com.badbones69.crazycrates.paper.cratetypes.QuickCrate;
import com.badbones69.crazycrates.paper.cratetypes.Roulette;
import com.badbones69.crazycrates.paper.cratetypes.War;
import com.badbones69.crazycrates.paper.cratetypes.Wheel;
import com.badbones69.crazycrates.paper.cratetypes.Wonder;
import com.badbones69.crazycrates.paper.listeners.BrokeLocationsListener;
import com.badbones69.crazycrates.paper.listeners.CrateControlListener;
import com.badbones69.crazycrates.paper.listeners.FireworkDamageListener;
import com.badbones69.crazycrates.paper.listeners.MenuListener;
import com.badbones69.crazycrates.paper.listeners.MiscListener;
import com.badbones69.crazycrates.paper.listeners.PreviewListener;
import com.badbones69.crazycrates.paper.support.MetricsHandler;
import com.badbones69.crazycrates.paper.support.libraries.PluginSupport;
import com.badbones69.crazycrates.paper.support.placeholders.PlaceholderAPISupport;
import com.ryderbelserion.lexicon.bukkit.BukkitImpl;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.logging.Logger;

public class CrazyCrates extends JavaPlugin {

    private final BukkitImpl bukkit;
    private Starter starter;

    public CrazyCrates(BukkitImpl bukkit) {
        this.bukkit = bukkit;
    }

    @Override
    public @NotNull Logger getLogger() {
        return this.bukkit.getLogUtils().getLogger();
    }

    @Override
    public void onEnable() {
        // Enable bukkit impl
        this.bukkit.setPlugin(this);
        this.bukkit.enable(true);

        // Register permissions to server
        MiscUtils.registerPermissions(getServer().getPluginManager());

        // Set command namespace
        this.bukkit.getManager().setNamespace("crazycrates");

        // Create starter
        this.starter = new Starter();
        // enable all the goodies
        this.starter.run();

        // Load files
        this.starter.getFileManager().setLog(true)
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

        // Clean files
        this.starter.cleanFiles();

        // Enable commands
        List.of(
                new CrateCommandReload(),
                new CrateCommandDebug(),
                new CrateCommandMenu(),
                new CrateCommandHelp(),
                new CrateCommandKey(),
                new CrateCommandTest()
        ).forEach(this.bukkit.getManager()::addCommand);

        boolean metricsEnabled = Files.CONFIG.getFile().getBoolean("Settings.Toggle-Metrics");

        if (metricsEnabled) {
            MetricsHandler metricsHandler = new MetricsHandler();

            metricsHandler.start();
        }

        enable();
    }

    @Override
    public void onDisable() {
        this.bukkit.disable();

        SessionManager.endCrates();

        QuickCrate.removeAllRewards();

        if (this.starter.getCrazyManager().getHologramController() != null) this.starter.getCrazyManager().getHologramController().removeAllHolograms();
    }

    private void enable() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new MenuListener(), this);
        pluginManager.registerEvents(new PreviewListener(), this);
        pluginManager.registerEvents(new FireworkDamageListener(), this);
        pluginManager.registerEvents(new CrateControlListener(), this);
        pluginManager.registerEvents(new MiscListener(), this);

        pluginManager.registerEvents(new War(), this);
        pluginManager.registerEvents(new CSGO(), this);
        pluginManager.registerEvents(new Wheel(), this);
        pluginManager.registerEvents(new Wonder(), this);
        pluginManager.registerEvents(new Cosmic(), this);
        pluginManager.registerEvents(new Roulette(), this);
        pluginManager.registerEvents(new QuickCrate(), this);
        pluginManager.registerEvents(new CrateOnTheGo(), this);
        pluginManager.registerEvents(new QuadCrate(), this);

        this.starter.getCrazyManager().loadCrates();

        if (!this.starter.getCrazyManager().getBrokeCrateLocations().isEmpty()) pluginManager.registerEvents(new BrokeLocationsListener(), this);

        if (PluginSupport.PLACEHOLDERAPI.isPluginEnabled()) new PlaceholderAPISupport().register();

        for (PluginSupport value : PluginSupport.values()) {
            if (value.isPluginEnabled()) {
                getLogger().info("<gold>" + value.name() + "</gold> <bold><green>FOUND</green></bold>");
            } else {
                getLogger().info("<gold>" + value.name() + "</gold> <bold><red>NOT FOUND</red></bold>");
            }
        }
    }

    public BukkitImpl getBukkit() {
        return this.bukkit;
    }

    public Starter getStarter() {
        return this.starter;
    }
}