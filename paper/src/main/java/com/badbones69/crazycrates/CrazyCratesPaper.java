package com.badbones69.crazycrates;

import com.badbones69.crazycrates.api.FileManager;
import com.badbones69.crazycrates.commands.CommandManager;
import com.badbones69.crazycrates.platform.PaperServer;
import com.badbones69.crazycrates.support.metrics.MetricsManager;
import com.badbones69.crazycrates.tasks.InventoryManager;
import com.badbones69.crazycrates.tasks.crates.CrateManager;
import com.ryderbelserion.cluster.ClusterFactory;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.platform.Server;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import java.util.List;
import java.util.Timer;

public class CrazyCratesPaper extends JavaPlugin {

    private final @NotNull BukkitCommandManager<CommandSender> commandManager = BukkitCommandManager.create(this);

    private InventoryManager inventoryManager;
    private CrateManager crateManager;
    private FileManager fileManager;
    private MetricsManager metrics;
    private ClusterFactory factory;
    private Server instance;
    private Timer timer;

    @Override
    public void onLoad() {
        this.factory = new ClusterFactory(this);

        this.instance = new PaperServer();
        this.instance.enable();

        this.timer = new Timer();

        ConfigManager.load();
    }

    @Override
    public void onEnable() {
        this.fileManager = new FileManager();

        List.of(
                "classic.nbt",
                "nether.nbt",
                "outdoors.nbt",
                "sea.nbt",
                "soul.nbt",
                "wooden.nbt"
        ).forEach(file -> this.fileManager.registerDefaultGenerateFiles(file, "/schematics", "/schematics"));

        this.fileManager.registerCustomFilesFolder("/schematics").setup();

        this.crateManager = new CrateManager();
        this.crateManager.load();

        CommandManager commandManager = new CommandManager();
        commandManager.load();

        /*

        this.inventoryManager = new InventoryManager();

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

        if (MiscUtils.isLogging()) getLogger().info("You can disable logging by going to the plugin-config.yml and setting verbose to false.");*/
    }

    @Override
    public void onDisable() {
        if (this.instance != null) this.instance.disable();

        if (this.factory != null) this.factory.disable();

        if (this.timer != null) this.timer.cancel();

        /*
        this.server.getCrateManager().purgeRewards();

        HologramManager holograms = this.server.getCrateManager().getHolograms();

        if (holograms != null && !holograms.isMapEmpty()) {
            holograms.removeAllHolograms();
        }*/
    }

    public CrateManager getCrateManager() {
        return this.crateManager;
    }

    public @NotNull Server getInstance() {
        return this.instance;
    }

    public @NotNull BukkitCommandManager<CommandSender> getCommandManager() {
        return this.commandManager;
    }

    public @NotNull InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    public @NotNull FileManager getFileManager() {
        return this.fileManager;
    }

    public @NotNull MetricsManager getMetrics() {
        return this.metrics;
    }

    public @NotNull Timer getTimer() {
        return this.timer;
    }
}