package com.badbones69.crazycrates.paper.api.config;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import com.badbones69.crazycrates.paper.CrazyCrates;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class ConfigManager {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private SettingsManager pluginConfig;

    public void load() {
        // Create the plugin-support.yml file object.
        File pluginConfig = new File(this.plugin.getDataFolder(), "plugin-config.yml");

        // Bind it to settings manager
        this.pluginConfig = SettingsManagerBuilder
                .withYamlFile(pluginConfig)
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