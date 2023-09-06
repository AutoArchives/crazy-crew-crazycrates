package us.crazycrew.crazycrates.paper.api.plugin;

import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.EventLogger;
import us.crazycrew.crazycrates.paper.api.FileManager;
import us.crazycrew.crazycrates.common.config.ConfigManager;
import us.crazycrew.crazycrates.common.config.PluginConfig;
import us.crazycrew.crazycrates.paper.api.plugin.migrate.ConfigValues;
import us.crazycrew.crazycrates.paper.api.managers.MenuManager;
import us.crazycrew.crazycrates.paper.support.MetricsHandler;
import us.crazycrew.crazycrates.paper.support.structures.blocks.ChestStateHandler;
import com.ryderbelserion.cluster.bukkit.BukkitPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CrazyCratesLoader extends CrazyCratesPlugin {
    
    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private BukkitPlugin bukkitPlugin;
    private ConfigManager configManager;
    private MetricsHandler metricsHandler;

    private FileManager fileManager;
    private CrazyManager crazyManager;
    private MenuManager menuManager;
    private ChestStateHandler chestManager;
    private EventLogger eventLogger;
    private Methods methods;

    @Override
    public void enable() {
        // Must go first.
        super.enable();
        
        this.bukkitPlugin = new BukkitPlugin(this.plugin);
        this.bukkitPlugin.enable("<white>[<gradient:#FE5F55:#6b55b5>CrazyCrates</gradient>]</white>");

        this.configManager = new ConfigManager(this.plugin.getDataFolder());
        this.configManager.load();

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

        // convert old values to new configs.
        ConfigValues.convert();

        // Reload just in case.
        this.configManager.reload();

        // Clean files if we have to.
        janitor();

        this.methods = new Methods();

        this.crazyManager = new CrazyManager();
        this.crazyManager.load(true);

        this.menuManager = new MenuManager();
        this.menuManager.loadButtons();

        boolean metrics = this.configManager.getPluginConfig().getProperty(PluginConfig.TOGGLE_METRICS);

        this.metricsHandler = new MetricsHandler();

        if (metrics) {
            this.metricsHandler.start();
        }

        this.chestManager = new ChestStateHandler();
        this.eventLogger = new EventLogger();
    }

    public static void janitor() {
        if (!FileManager.Files.LOCATIONS.getFile().contains("Locations")) {
            FileManager.Files.LOCATIONS.getFile().set("Locations.Clear", null);
            FileManager.Files.LOCATIONS.saveFile();
        }

        if (!FileManager.Files.DATA.getFile().contains("Players")) {
            FileManager.Files.DATA.getFile().set("Players.Clear", null);
            FileManager.Files.DATA.saveFile();
        }
    }

    @Override
    public void disable() {
        this.crazyManager.reload(true);

        // Disabling the plugin must go last.
        super.disable();
        this.bukkitPlugin.disable();
    }

    @Override
    public @NotNull ChestStateHandler getChestManager() {
        return this.chestManager;
    }

    @Override
    public @NotNull ConfigManager getConfigManager() {
        return this.configManager;
    }

    @Override
    public @NotNull BukkitPlugin getBukkitPlugin() {
        return this.bukkitPlugin;
    }

    @Override
    public @NotNull CrazyManager getCrazyManager() {
        return this.crazyManager;
    }

    @Override
    public @NotNull MenuManager getMenuManager() {
        return this.menuManager;
    }

    @Override
    public @NotNull FileManager getFileManager() {
        return this.fileManager;
    }

    @Override
    public @NotNull EventLogger getEventLogger() {
        return this.eventLogger;
    }

    @Override
    public @NotNull MetricsHandler getMetrics() {
        return this.metricsHandler;
    }

    @Override
    public @NotNull Methods getMethods() {
        return this.methods;
    }
}