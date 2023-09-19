package us.crazycrew.crazycrates.paper.api.plugin;

import org.jetbrains.annotations.Nullable;
import us.crazycrew.crazycrates.api.platforms.Platform;
import us.crazycrew.crazycrates.common.CrazyCratesPlugin;
import us.crazycrew.crazycrates.common.config.ConfigManager;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.EventLogger;
import us.crazycrew.crazycrates.paper.api.FileManager.Files;
import us.crazycrew.crazycrates.paper.api.FileManager;
import us.crazycrew.crazycrates.common.config.types.PluginConfig;
import us.crazycrew.crazycrates.paper.api.plugin.frame.BukkitUserManager;
import us.crazycrew.crazycrates.paper.api.managers.MenuManager;
import us.crazycrew.crazycrates.paper.api.plugin.migrate.MigrationService;
import us.crazycrew.crazycrates.paper.support.MetricsHandler;
import com.ryderbelserion.cluster.bukkit.BukkitPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.support.structures.blocks.ChestManager;
import java.io.File;

public class CrazyHandler extends CrazyCratesPlugin {
    
    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private BukkitUserManager userManager;

    private BukkitPlugin bukkitPlugin;
    private MetricsHandler metrics;

    private ChestManager chestManager;
    private CrazyManager crazyManager;
    private FileManager fileManager;
    private MenuManager menuManager;
    private EventLogger eventLogger;

    private Methods methods;

    public CrazyHandler(File dataFolder) {
        super(dataFolder, Platform.type.paper);
    }

    public void install() {
        // Enable cluster bukkit api
        this.bukkitPlugin = new BukkitPlugin(this.plugin);
        this.bukkitPlugin.enable();

        // Run migration checks
        // The migration check will only stay until 2.5
        MigrationService service = new MigrationService();
        service.migrate();

        // Enable crazycrates api
        super.enable(this.plugin.getServer().getConsoleSender());

        //TODO() Only use this when there is more then one file in locale folder.
        /*this.bukkitPlugin.getFileUtils().copyFiles(
                new File(this.plugin.getDataFolder(), "locale").toPath(),
                "locale",
                List.of(
                        "en-US.yml"
                )
        );*/

        this.fileManager = new FileManager();
        this.fileManager
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

        // Clean files if we have to.
        janitor();

        this.methods = new Methods();

        this.crazyManager = new CrazyManager();
        this.userManager = new BukkitUserManager();

        this.crazyManager.load(true);

        this.menuManager = new MenuManager();
        this.menuManager.loadButtons();

        boolean metrics = ConfigManager.getPluginConfig().getProperty(PluginConfig.toggle_metrics);

        this.metrics = new MetricsHandler();
        if (metrics) this.metrics.start();

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

    public void uninstall() {
        // Reload plugin
        this.crazyManager.reload(true);

        // Disable crazycrates api
        super.disable();

        // Disable cluster bukkit api
        this.bukkitPlugin.disable();
    }

    @Override
    public @Nullable String identifyClassLoader(ClassLoader classLoader) throws Exception {
        Class<?> classLoaderClass = Class.forName("org.bukkit.plugin.java.PluginClassLoader");

        if (classLoaderClass.isInstance(classLoader)) {
            return this.plugin.getName();
        }

        return null;
    }

    /**
     * Inherited methods.
     */
    @Override
    public @NotNull ConfigManager getConfigManager() {
        return super.getConfigManager();
    }

    @Override
    public @NotNull BukkitUserManager getUserManager() {
        return this.userManager;
    }

    /**
     * Internal methods.
     */
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