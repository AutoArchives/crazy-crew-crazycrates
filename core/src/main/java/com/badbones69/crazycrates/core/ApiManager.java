package com.badbones69.crazycrates.core;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.badbones69.crazycrates.core.config.ConfigBuilder;
import com.badbones69.crazycrates.core.config.types.PluginConfig;
import com.badbones69.crazycrates.core.config.types.legacy.LocaleMigration;
import com.badbones69.crazycrates.core.frame.CrazyCore;
import com.badbones69.crazycrates.core.frame.CrazyLogger;
import com.badbones69.crazycrates.core.frame.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ApiManager {
    private final Path path = CrazyCore.api().getDirectory();

    private SettingsManager locale;
    private SettingsManager config;
    private SettingsManager pluginConfig;

    public ApiManager load() {
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
        File config = new File(CrazyCore.api().getDirectory().toFile(), "config.yml");

        this.config = SettingsManagerBuilder
                .withYamlFile(config)
                .useDefaultMigrationService()
                .configurationData(ConfigBuilder.buildConfig())
                .create();

        return this;
    }

    public void reload() {
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
}