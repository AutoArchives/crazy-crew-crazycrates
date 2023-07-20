package com.badbones69.crazycrates.core;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.badbones69.crazycrates.core.config.ConfigBuilder;
import com.badbones69.crazycrates.core.config.types.PluginConfig;
import com.badbones69.crazycrates.core.config.types.legacy.LocaleMigration;
import com.badbones69.crazycrates.core.frame.CrazyLogger;
import com.badbones69.crazycrates.core.frame.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ApiManager {

    private final Path path;

    public ApiManager(Path path) {
        this.path = path;
    }

    private static SettingsManager locale;
    private static SettingsManager config;
    private static SettingsManager pluginConfig;

    public ApiManager load() {
        File pluginConfigFile = new File(this.path.toFile(), "plugin-config.yml");

        pluginConfig = SettingsManagerBuilder
                .withYamlFile(pluginConfigFile)
                .useDefaultMigrationService()
                .configurationData(ConfigBuilder.buildPluginConfig())
                .create();

        // Migrate the locale to the new directory if it's still Messages.yml!
        File localeDir = new File(this.path.toFile(), "locale");
        migrateLocale(localeDir);

        File localeFile = new File(localeDir, pluginConfig.getProperty(PluginConfig.LOCALE_FILE) + ".yml");

        locale = SettingsManagerBuilder
                .withYamlFile(localeFile)
                .useDefaultMigrationService()
                .configurationData(ConfigBuilder.buildLocale())
                .create();

        // Create config.yml
        File configFile = new File(this.path.toFile(), "config.yml");

        config = SettingsManagerBuilder
                .withYamlFile(configFile)
                .useDefaultMigrationService()
                .configurationData(ConfigBuilder.buildConfig())
                .create();

        return this;
    }

    public void reload() {
        // Reload configs.
        pluginConfig.reload();
        config.reload();

        locale.reload();

        File localeDir = new File(this.path.toFile(), "locale");
        File localeFile = new File(localeDir, pluginConfig.getProperty(PluginConfig.LOCALE_FILE) + ".yml");

        locale = SettingsManagerBuilder
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
    public static SettingsManager getPluginConfig() {
        return pluginConfig;
    }

    public static SettingsManager getLocale() {
        return locale;
    }

    public static SettingsManager getConfig() {
        return config;
    }
}