package us.crazycrew.crazycrates.common.config;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import us.crazycrew.crazycrates.common.config.menus.CrateMainMenu;
import us.crazycrew.crazycrates.common.config.menus.CratePreviewMenu;
import java.io.File;

public class ConfigManager {

    private final File dataFolder;

    public ConfigManager(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    private SettingsManager pluginConfig;
    private SettingsManager localeConfig;
    private SettingsManager mainConfig;

    private SettingsManager mainMenuConfig;
    private SettingsManager previewMenuConfig;

    public void load() {
        // Create the plugin-config.yml file.
        File pluginConfigFile = new File(this.dataFolder, "plugin-config.yml");

        // Bind it to settings manager
        this.pluginConfig = SettingsManagerBuilder
                .withYamlFile(pluginConfigFile)
                .useDefaultMigrationService()
                .configurationData(createPluginConfig())
                .create();

        File localeFile = new File(this.dataFolder, "/locale/" + this.pluginConfig.getProperty(PluginConfig.PLUGIN_LOCALE) + ".yml");

        this.localeConfig = SettingsManagerBuilder
                .withYamlFile(localeFile)
                .useDefaultMigrationService()
                .configurationData(LocaleConfig.class)
                .create();

        File configFile = new File(this.dataFolder, "config.yml");

        this.mainConfig = SettingsManagerBuilder
                .withYamlFile(configFile)
                .useDefaultMigrationService()
                .configurationData(ConfigurationDataBuilder.createConfiguration(MainConfig.class))
                .create();

        File mainMenuFile = new File(this.dataFolder, "/menus/crate-menu.yml");

        this.mainMenuConfig = SettingsManagerBuilder
                .withYamlFile(mainMenuFile)
                .useDefaultMigrationService()
                .configurationData(ConfigurationDataBuilder.createConfiguration(CrateMainMenu.class))
                .create();

        File previewMenuFile = new File(this.dataFolder, "/menus/preview-menu.yml");

        this.previewMenuConfig = SettingsManagerBuilder
                .withYamlFile(previewMenuFile)
                .useDefaultMigrationService()
                .configurationData(ConfigurationDataBuilder.createConfiguration(CratePreviewMenu.class))
                .create();
    }

    public void reload() {
        // Reload plugin-config.yml
        this.pluginConfig.reload();

        this.localeConfig.save();

        File localeFile = new File(this.dataFolder, "/locale/" + this.pluginConfig.getProperty(PluginConfig.PLUGIN_LOCALE) + ".yml");

        this.localeConfig = SettingsManagerBuilder
                .withYamlFile(localeFile)
                .useDefaultMigrationService()
                .configurationData(LocaleConfig.class)
                .create();

        // Reload crate-menu.yml
        this.mainMenuConfig.reload();

        // Reload preview-menu.yml
        this.previewMenuConfig.reload();
    }

    public SettingsManager getPluginConfig() {
        return this.pluginConfig;
    }

    public SettingsManager getLocaleConfig() {
        return this.localeConfig;
    }

    public SettingsManager getConfig() {
        return this.mainConfig;
    }

    public SettingsManager getMainMenuConfig() {
        return this.mainMenuConfig;
    }

    public SettingsManager getPreviewMenuConfig() {
        return this.previewMenuConfig;
    }

    private ConfigurationData createPluginConfig() {
        return ConfigurationDataBuilder.createConfiguration(PluginConfig.class);
    }
}