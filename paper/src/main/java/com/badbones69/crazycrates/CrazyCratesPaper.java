package com.badbones69.crazycrates;

import com.badbones69.crazycrates.api.FileManager;
import com.badbones69.crazycrates.api.builders.types.CrateAdminMenu;
import com.badbones69.crazycrates.api.builders.types.CrateMainMenu;
import com.badbones69.crazycrates.api.builders.types.CratePreviewMenu;
import com.badbones69.crazycrates.api.builders.types.CrateTierMenu;
import com.badbones69.crazycrates.api.utils.FileUtils;
import com.badbones69.crazycrates.api.utils.MiscUtils;
import com.badbones69.crazycrates.api.utils.MsgUtils;
import com.badbones69.crazycrates.commands.CommandManager;
import com.badbones69.crazycrates.listeners.BrokeLocationsListener;
import com.badbones69.crazycrates.listeners.CrateControlListener;
import com.badbones69.crazycrates.listeners.MiscListener;
import com.badbones69.crazycrates.listeners.crates.CosmicCrateListener;
import com.badbones69.crazycrates.listeners.crates.CrateOpenListener;
import com.badbones69.crazycrates.listeners.crates.MobileCrateListener;
import com.badbones69.crazycrates.listeners.crates.QuadCrateListener;
import com.badbones69.crazycrates.listeners.crates.WarCrateListener;
import com.badbones69.crazycrates.listeners.other.EntityDamageListener;
import com.badbones69.crazycrates.platform.PaperServer;
import com.badbones69.crazycrates.support.PluginSupport;
import com.badbones69.crazycrates.support.metrics.MetricsManager;
import com.badbones69.crazycrates.support.placeholders.PlaceholderAPISupport;
import com.badbones69.crazycrates.tasks.BukkitUserManager;
import com.badbones69.crazycrates.tasks.InventoryManager;
import com.badbones69.crazycrates.tasks.crates.BukkitCrateManager;
import com.ryderbelserion.cluster.ClusterPackage;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.CrazyCrates;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.config.impl.ConfigKeys;
import java.util.List;
import java.util.Timer;
import static com.badbones69.crazycrates.api.utils.MiscUtils.isLogging;
import static com.badbones69.crazycrates.api.utils.MiscUtils.registerPermissions;

public class CrazyCratesPaper extends JavaPlugin {

    private final @NotNull BukkitCommandManager<CommandSender> commandManager = BukkitCommandManager.create(this);

    private ClusterPackage cluster;
    private CrazyCrates instance;

    private final Timer timer;

    public CrazyCratesPaper() {
        this.timer = new Timer();
    }

    public Timer getTimer() {
        return this.timer;
    }

    @Override
    public void onLoad() {
        ConfigManager.load(getDataFolder());

        int radius = DedicatedServer.getServer().getSpawnProtectionRadius();

        if (radius > 0) {
            if (isLogging()) {
                List.of(
                        "The spawn protection is set to " + radius,
                        "Crates placed in the spawn protection will not function",
                        "correctly as spawn protection overrides everything",
                        "",
                        "Change the value in server.properties to 0 then restart"
                ).forEach(getLogger()::warning);
            }
        }
    }

    private InventoryManager inventoryManager;
    private BukkitCrateManager crateManager;
    private BukkitUserManager userManager;

    private FileManager fileManager;
    private MetricsManager metrics;

    @Override
    public void onEnable() {
        registerPermissions();

        FileUtils.loadFiles();

        this.cluster = new ClusterPackage(this);

        this.fileManager = new FileManager();
        this.fileManager.registerDefaultGenerateFiles("CrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("QuadCrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("CosmicCrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("QuickCrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("WarCrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("CasinoExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("classic.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("nether.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("outdoors.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("sea.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("soul.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("wooden.nbt", "/schematics", "/schematics")
                .registerCustomFilesFolder("/crates")
                .registerCustomFilesFolder("/schematics")
                .setup();

        this.inventoryManager = new InventoryManager();
        this.crateManager = new BukkitCrateManager();
        this.userManager = new BukkitUserManager();

        this.instance = new CrazyCrates(new PaperServer(this.userManager));

        this.inventoryManager.loadButtons();

        this.crateManager.loadHolograms();

        this.instance.getKeyManager().loadKeys();

        this.crateManager.loadCrates();

        List.of(
                new CratePreviewMenu.CratePreviewListener(),
                new CrateAdminMenu.CrateAdminListener(),
                new CrateMainMenu.CrateMenuListener(),
                new CrateTierMenu.CrateTierListener(),

                new BrokeLocationsListener(),
                new CrateControlListener(),
                new EntityDamageListener(),
                new MobileCrateListener(),
                new CosmicCrateListener(),
                new QuadCrateListener(),
                new CrateOpenListener(),
                new WarCrateListener(),
                new MiscListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        CommandManager commandManager = new CommandManager();
        commandManager.load();

        if (MiscUtils.isLogging()) {
            String prefix = ConfigManager.getConfig().getProperty(ConfigKeys.console_prefix);

            for (PluginSupport value : PluginSupport.values()) {
                if (value.isPluginEnabled()) {
                    getServer().getConsoleSender().sendMessage(MsgUtils.color(prefix + "&6&l" + value.name() + " &a&lFOUND"));
                } else {
                    getServer().getConsoleSender().sendMessage(MsgUtils.color(prefix + "&6&l" + value.name() + " &c&lNOT FOUND"));
                }
            }
        }

        if (PluginSupport.PLACEHOLDERAPI.isPluginEnabled()) {
            if (MiscUtils.isLogging()) getLogger().info("PlaceholderAPI support is enabled!");

            new PlaceholderAPISupport().register();
        }

        if (ConfigManager.getConfig().getProperty(ConfigKeys.toggle_metrics)) {
            this.metrics = new MetricsManager();

            this.metrics.start();
        }

        if (MiscUtils.isLogging()) getLogger().info("You can disable logging by going to the plugin-config.yml and setting verbose to false.");
    }

    @Override
    public void onDisable() {
        /*if (this.timer != null) {
            this.timer.cancel();
        }

        this.server.getCrateManager().purgeRewards();

        HologramManager holograms = this.server.getCrateManager().getHolograms();

        if (holograms != null && !holograms.isMapEmpty()) {
            holograms.removeAllHolograms();
        }

        if (this.cluster != null) {
            this.cluster.getCluster().disable();
        }

        if (this.instance != null) {
            this.instance.disable();
        }*/
    }

    public @NotNull BukkitCommandManager<CommandSender> getCommandManager() {
        return this.commandManager;
    }

    public @NotNull BukkitCrateManager getCrateManager() {
        return this.crateManager;
    }

    public @NotNull BukkitUserManager getUserManager() {
        return this.userManager;
    }

    public @NotNull InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    public @NotNull FileManager getFileManager() {
        return this.fileManager;
    }

    public @NotNull CrazyCrates getInstance() {
        return this.instance;
    }

    public @NotNull MetricsManager getMetrics() {
        return this.metrics;
    }
}