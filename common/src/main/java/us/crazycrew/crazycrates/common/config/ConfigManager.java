package us.crazycrew.crazycrates.common.config;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import java.io.File;

public class ConfigManager {

    private final File dataFolder;

    public ConfigManager(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    private SettingsManager pluginConfig;

    public void load() {
        // Create the plugin-support.yml file.
        File pluginConfigFile = new File(this.dataFolder, "plugin-config.yml");

        // Bind it to settings manager
        this.pluginConfig = SettingsManagerBuilder
                .withYamlFile(pluginConfigFile)
                .useDefaultMigrationService()
                .configurationData(createPluginConfig())
                .create();
    }

    public void reload() {
        // Reload plugin-config.yml
        this.pluginConfig.reload();
    }

    public SettingsManager getPluginConfig() {
        return this.pluginConfig;
    }

    private ConfigurationData createPluginConfig() {
        return ConfigurationDataBuilder.createConfiguration(PluginConfig.class);
    }
}