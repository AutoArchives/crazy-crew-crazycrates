package us.crazycrew.crazycrates.paper.api.plugin;

import us.crazycrew.crazycrates.api.platforms.Platform;
import us.crazycrew.crazycrates.common.CrazyCratesPlugin;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.EventLogger;
import us.crazycrew.crazycrates.paper.api.FileManager.Files;
import us.crazycrew.crazycrates.paper.api.FileManager;
import us.crazycrew.crazycrates.common.config.ConfigManager;
import us.crazycrew.crazycrates.common.config.PluginConfig;
import us.crazycrew.crazycrates.paper.api.managers.MenuManager;
import us.crazycrew.crazycrates.paper.api.plugin.migrate.ConfigValues;
import us.crazycrew.crazycrates.paper.support.MetricsHandler;
import com.ryderbelserion.cluster.bukkit.BukkitPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.support.structures.blocks.ChestManager;

public class CrazyCratesLoader {
    
    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private CrazyCratesPlugin cratesLoader;
    private BukkitPlugin bukkitPlugin;
    private MetricsHandler metrics;

    private FileManager fileManager;
    private CrazyManager crazyManager;
    private MenuManager menuManager;
    private ChestManager chestManager;
    private EventLogger eventLogger;

    private Methods methods;

    public void enable() {
        this.cratesLoader = new CrazyCratesPlugin(this.plugin.getDataFolder(), Platform.Type.PAPER);
        this.cratesLoader.enable();

        // Enable cluster bukkit api
        this.bukkitPlugin = new BukkitPlugin(this.plugin);
        this.bukkitPlugin.enable();

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

        // Convert old values to new configs.
        ConfigValues.convert();

        // Reload just in case.
        getConfigManager().reload();

        // Clean files if we have to.
        janitor();

        this.methods = new Methods();

        this.crazyManager = new CrazyManager();
        getCrazyManager().load(true);

        this.menuManager = new MenuManager();
        this.menuManager.loadButtons();

        boolean metrics = getConfigManager().getPluginConfig().getProperty(PluginConfig.TOGGLE_METRICS);

        this.metrics = new MetricsHandler();
        if (metrics) getMetrics().start();

        this.chestManager = new ChestManager();
        this.eventLogger = new EventLogger();
    }

    public static void janitor() {
        if (!Files.LOCATIONS.getFile().contains("Locations")) {
            Files.LOCATIONS.getFile().set("Locations.Clear", null);
            Files.LOCATIONS.saveFile();
        }

        if (!Files.DATA.getFile().contains("Players")) {
            Files.DATA.getFile().set("Players.Clear", null);
            Files.DATA.saveFile();
        }
    }

    public void disable() {
        this.crazyManager.reload(true);

        this.cratesLoader.disable();

        // Disable cluster bukkit api
        this.bukkitPlugin.disable();
    }

    public @NotNull ConfigManager getConfigManager() {
        return this.cratesLoader.getConfigManager();
    }

    public @NotNull FileManager getFileManager() {
        return this.fileManager;
    }

    public @NotNull Methods getMethods() {
        return this.methods;
    }

    public @NotNull CrazyManager getCrazyManager() {
        return this.crazyManager;
    }

    public @NotNull MenuManager getMenuManager() {
        return this.menuManager;
    }

    public @NotNull MetricsHandler getMetrics() {
        return this.metrics;
    }

    public @NotNull ChestManager getChestManager() {
        return this.chestManager;
    }

    public @NotNull EventLogger getEventLogger() {
        return this.eventLogger;
    }
}