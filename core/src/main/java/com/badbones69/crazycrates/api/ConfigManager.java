package com.badbones69.crazycrates.api;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.badbones69.crazycrates.api.config.ConfigBuilder;
import com.ryderbelserion.lexicon.core.utils.FileUtils;
import java.io.File;
import java.nio.file.Path;

public class ConfigManager {

    private final Path path;

    public ConfigManager(Path path) {
        this.path = path;
    }

    private static SettingsManager locale;

    public void load() {
        File localeDir = new File(this.path.toFile(), "locale");

        FileUtils.extract("/locale/", this.path, false);

        File localeFile = new File(localeDir, "en-US.yml");

        locale = SettingsManagerBuilder
                .withYamlFile(localeFile)
                .useDefaultMigrationService()
                .configurationData(ConfigBuilder.buildLocale())
                .create();
    }

    public void reload() {
        locale.reload();
    }

    public static SettingsManager getLocale() {
        return locale;
    }
}