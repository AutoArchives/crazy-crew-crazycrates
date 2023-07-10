package com.badbones69.crazycrates.api;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.Zrips.CMI.Modules.ModuleHandling.CMIModule;
import com.badbones69.crazycrates.api.configs.ConfigBuilder;
import com.badbones69.crazycrates.api.configs.types.PluginConfig;
import com.badbones69.crazycrates.api.configs.types.legacy.LocaleMigration;
import com.badbones69.crazycrates.api.configs.types.sections.PluginSupportSection;
import com.badbones69.crazycrates.api.crates.CrateManager;
import com.badbones69.crazycrates.api.enums.support.HologramSupport;
import com.badbones69.crazycrates.api.support.holograms.interfaces.HologramManager;
import com.badbones69.crazycrates.api.support.holograms.types.CMIHologramSupport;
import com.badbones69.crazycrates.api.support.holograms.types.DecentHologramSupport;
import com.badbones69.crazycrates.api.storage.interfaces.LocationManager;
import com.badbones69.crazycrates.api.storage.interfaces.UserManager;
import com.badbones69.crazycrates.api.storage.types.file.yaml.crates.YamlCrateManager;
import com.badbones69.crazycrates.api.storage.types.file.yaml.users.YamlUserManager;
import com.ryderbelserion.stick.core.utils.FileUtils;
import com.ryderbelserion.stick.paper.Stick;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ApiManager {

    private final JavaPlugin plugin;
    private final Path path;

    public ApiManager(JavaPlugin plugin, Path path) {
        this.plugin = plugin;

        this.path = path;

        new Stick(path, plugin.getName());
    }

    private UserManager userManager;
    private LocationManager locationManager;
    private CrateManager crateManager;

    private SettingsManager pluginConfig;
    private SettingsManager locale;
    private SettingsManager config;

    private HologramManager holograms;

    public ApiManager load() {
        // Create plugin-config.yml
        File pluginConfig = new File(this.path.toFile(), "plugin-config.yml");

        this.pluginConfig = SettingsManagerBuilder
                .withYamlFile(pluginConfig)
                .useDefaultMigrationService()
                .configurationData(ConfigBuilder.buildPluginConfig())
                .create();

        // Migrate the locale to the new directory if it's still Messages.yml!
        File localeDir = new File(this.path.toFile(), "locale");
        migrateLocale(localeDir);

        File localeFile = new File(localeDir, this.pluginConfig.getProperty(PluginConfig.LOCALE_FILE) + ".yml");

        this.locale = SettingsManagerBuilder
                .withYamlFile(localeFile)
                .useDefaultMigrationService()
                .configurationData(ConfigBuilder.buildLocale())
                .create();

        // Create config.yml
        File config = new File(this.path.toFile(), "config.yml");

        this.config = SettingsManagerBuilder
                .withYamlFile(config)
                .useDefaultMigrationService()
                .configurationData(ConfigBuilder.buildConfig())
                .create();

        // Re-initialize crate manager.
        this.crateManager = new CrateManager(this.plugin);
        this.crateManager.loadCrates();

        // Initialize user data.
        init();

        return this;
    }

    public void reload(boolean reloadCommand) {
        // If holograms are enabled.
        boolean hologramsToggle = this.pluginConfig.getProperty(PluginSupportSection.HOLOGRAMS_SUPPORT_ENABLED);

        if (hologramsToggle) {
            // If not null, purge all holograms.
            if (this.holograms != null) this.holograms.purge();

            HologramSupport hologramType = this.pluginConfig.getProperty(PluginSupportSection.HOLOGRAMS_SUPPORT_TYPE);

            // Switch hologram support based on the config option.
            switch (hologramType) {
                case cmi_holograms -> {
                    if (CMIModule.holograms.isEnabled()) {
                        this.holograms = new CMIHologramSupport();

                        return;
                    }

                    this.plugin.getLogger().warning("CMI support is enabled by you but the CMI Hologram Module is not enabled.");
                    this.plugin.getLogger().warning("Please go to Modules.yml in CMI & turn on the hologram module: Restart is required.");
                }

                case decent_holograms -> {
                    this.holograms = new DecentHologramSupport();
                    this.plugin.getLogger().warning("DecentHologram Support is enabled.");
                }
            }
        }

        if (this.userManager != null) this.userManager.save();

        // If the command is /crazycrates reload.
        if (reloadCommand) {
            // Reload configs.
            this.pluginConfig.reload();
            this.config.reload();

            this.locale.reload();

            File localeDir = new File(this.path.toFile(), "locale");
            File localeFile = new File(localeDir, this.pluginConfig.getProperty(PluginConfig.LOCALE_FILE) + ".yml");

            this.locale = SettingsManagerBuilder
                    .withYamlFile(localeFile)
                    .useDefaultMigrationService()
                    .configurationData(ConfigBuilder.buildLocale())
                    .create();

            // Re-initialize crate manager.
            this.crateManager = new CrateManager(this.plugin);
            this.crateManager.loadCrates();

            // Initialize user data.
            init();
        }
    }

    private void init() {
        this.locationManager = new YamlCrateManager(new File(this.path.toFile(), "locations.yml"), this.plugin, this);
        this.locationManager.load();

        this.userManager = new YamlUserManager(new File(this.path.toFile(), "users.yml"), this.crateManager, this.plugin, this.pluginConfig.getProperty(PluginConfig.VERBOSE_LOGGING));
        this.userManager.load();
    }

    private void migrateLocale(File localeDir) {
        File messages = new File(this.path.toFile(), "messages.yml");
        File newFile = new File(localeDir, "en-US.yml");

        if (!localeDir.exists()) {
            if (!localeDir.mkdirs()) {
                this.plugin.getLogger().severe("Could not create crates directory! " +  localeDir.getAbsolutePath());
                return;
            }

            if (messages.exists()) {
                File renamedFile = new File(this.path.toFile(), "en-US.yml");

                if (messages.renameTo(renamedFile)) this.plugin.getLogger().info("Renamed " + messages.getName() + " to " + renamedFile.getName());

                try {
                    Files.move(renamedFile.toPath(), newFile.toPath());
                    this.plugin.getLogger().warning("Moved " + renamedFile.getPath() + " to " + newFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                LocaleMigration localeMigration = new LocaleMigration(renamedFile, this.plugin);

                localeMigration.load();
            }
        }

        FileUtils.extract("/locale/", this.path, false);
    }

    // Config Management.
    public SettingsManager getPluginConfig() {
        return this.pluginConfig;
    }

    public SettingsManager getLocale() {
        return this.locale;
    }

    public SettingsManager getConfig() {
        return this.config;
    }

    // Crate Management.
    public HologramManager getHolograms() {
        return this.holograms;
    }

    public CrateManager getCrateManager() {
        return this.crateManager;
    }

    public LocationManager getLocationManager() {
        return this.locationManager;
    }

    // User Management.
    public UserManager getUserManager() {
        return this.userManager;
    }
}