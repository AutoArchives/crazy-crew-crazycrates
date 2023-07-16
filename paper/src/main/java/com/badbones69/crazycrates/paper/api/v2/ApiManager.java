package com.badbones69.crazycrates.paper.api.v2;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.Zrips.CMI.Modules.ModuleHandling.CMIModule;
import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.frame.PaperCore;
import com.badbones69.crazycrates.paper.api.v2.configs.ConfigBuilder;
import com.badbones69.crazycrates.paper.api.v2.configs.types.PluginConfig;
import com.badbones69.crazycrates.paper.api.v2.configs.types.legacy.LocaleMigration;
import com.badbones69.crazycrates.paper.api.v2.configs.types.sections.PluginSupportSection;
import com.badbones69.crazycrates.paper.api.v2.crates.CrateManager;
import com.badbones69.crazycrates.paper.api.v2.enums.support.HologramSupport;
import com.badbones69.crazycrates.paper.api.v2.support.InternalPlaceholderSupport;
import com.badbones69.crazycrates.paper.api.v2.support.holograms.interfaces.HologramManager;
import com.badbones69.crazycrates.paper.api.v2.support.holograms.types.CMIHologramSupport;
import com.badbones69.crazycrates.paper.api.v2.support.holograms.types.DecentHologramSupport;
import com.badbones69.crazycrates.paper.api.v2.storage.interfaces.LocationManager;
import com.badbones69.crazycrates.paper.api.v2.storage.interfaces.UserManager;
import com.badbones69.crazycrates.paper.api.v2.storage.types.file.yaml.crates.YamlCrateManager;
import com.badbones69.crazycrates.paper.api.v2.storage.types.file.yaml.users.YamlUserManager;
import com.ryderbelserion.crazycrates.core.frame.CrazyLogger;
import com.ryderbelserion.crazycrates.core.frame.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ApiManager {

    private final Path path;
    private final CrazyCrates plugin;

    public ApiManager(Path path, CrazyCrates plugin) {
        this.path = path;
        this.plugin = plugin;
    }

    private UserManager userManager;
    private LocationManager locationManager;
    private CrateManager crateManager;

    private SettingsManager pluginConfig;
    private PaperCore paperCore;
    private SettingsManager locale;
    private SettingsManager config;

    private InternalPlaceholderSupport placeholderSupport;

    private HologramManager holograms;

    public ApiManager load(boolean serverStart) {
        // Create plugin-config.yml
        File pluginConfig = new File(this.path.toFile(), "plugin-config.yml");

        this.pluginConfig = SettingsManagerBuilder
                .withYamlFile(pluginConfig)
                .useDefaultMigrationService()
                .configurationData(ConfigBuilder.buildPluginConfig())
                .create();

        if (serverStart) {
            this.paperCore = new PaperCore(this.plugin, this);

            this.paperCore.enable();
        }

        //this.paperCore.setProjectPrefix(this.pluginConfig.getProperty(PluginConfig.CONSOLE_PREFIX));

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

        this.placeholderSupport = new InternalPlaceholderSupport(this);

        // Re-initialize crate manager.
        this.crateManager = new CrateManager(this.path.toFile());
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

                    CrazyLogger.warn("CMI support is enabled by you but the CMI Hologram Module is not enabled.");
                    CrazyLogger.warn("Please go to Modules.yml in CMI & turn on the hologram module: Restart is required.");
                }

                case decent_holograms -> {
                    this.holograms = new DecentHologramSupport();
                    CrazyLogger.warn("DecentHologram Support is enabled.");
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
            this.crateManager = new CrateManager(this.path.toFile());
            this.crateManager.loadCrates();

            // Initialize user data.
            init();
        }
    }

    private void init() {
        this.locationManager = new YamlCrateManager(new File(this.path.toFile(), "locations.yml"), this);
        this.locationManager.load();

        this.userManager = new YamlUserManager(new File(this.path.toFile(), "users.yml"), this.crateManager, this, this.pluginConfig.getProperty(PluginConfig.VERBOSE_LOGGING));
        this.userManager.load();
    }

    private void migrateLocale(File localeDir) {
        File messages = new File(this.path.toFile(), "messages.yml");
        File newFile = new File(localeDir, "en-US.yml");

        if (!localeDir.exists()) {
            if (!localeDir.mkdirs()) {
                CrazyLogger.severe("Could not create crates directory! " +  localeDir.getAbsolutePath());
                return;
            }

            if (messages.exists()) {
                File renamedFile = new File(this.path.toFile(), "en-US.yml");

                if (messages.renameTo(renamedFile)) CrazyLogger.info("Renamed " + messages.getName() + " to " + renamedFile.getName());

                try {
                    Files.move(renamedFile.toPath(), newFile.toPath());
                    CrazyLogger.warn("Moved " + renamedFile.getPath() + " to " + newFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                LocaleMigration localeMigration = new LocaleMigration(renamedFile);

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

    // Placeholders.
    public InternalPlaceholderSupport getPlaceholderSupport() {
        return this.placeholderSupport;
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