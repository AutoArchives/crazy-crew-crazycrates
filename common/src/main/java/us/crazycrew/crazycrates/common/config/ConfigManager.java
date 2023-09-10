package us.crazycrew.crazycrates.common.config;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import us.crazycrew.crazycrates.common.config.menus.CrateMainMenu;
import us.crazycrew.crazycrates.common.config.menus.CratePreviewMenu;
import us.crazycrew.crazycrates.common.config.types.Locale;
import us.crazycrew.crazycrates.common.config.types.Config;
import us.crazycrew.crazycrates.common.config.types.PluginConfig;
import java.io.File;

public class ConfigManager {

    private final File dataFolder;

    public ConfigManager(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    private static SettingsManager pluginConfig;
    private static SettingsManager localeConfig;
    private static SettingsManager mainConfig;

    private static SettingsManager mainMenuConfig;
    private static SettingsManager previewMenuConfig;

    public void load() {
        // Create the plugin-config.yml file.
        File pluginConfigFile = new File(this.dataFolder, "plugin-config.yml");

        // Bind it to settings manager
        ConfigManager.pluginConfig = SettingsManagerBuilder
                .withYamlFile(pluginConfigFile)
                .useDefaultMigrationService()
                .configurationData(createPluginConfig())
                .create();

        File localeFile = new File(this.dataFolder, "/locale/" + ConfigManager.pluginConfig.getProperty(PluginConfig.plugin_locale) + ".yml");

        ConfigManager.localeConfig = SettingsManagerBuilder
                .withYamlFile(localeFile)
                .useDefaultMigrationService()
                .configurationData(Locale.class)
                .create();

        File configFile = new File(this.dataFolder, "config.yml");

        ConfigManager.mainConfig = SettingsManagerBuilder
                .withYamlFile(configFile)
                .useDefaultMigrationService()
                .configurationData(ConfigurationDataBuilder.createConfiguration(Config.class))
                .create();

        File mainMenuFile = new File(this.dataFolder, "/menus/crate-menu.yml");

        ConfigManager.mainMenuConfig = SettingsManagerBuilder
                .withYamlFile(mainMenuFile)
                .useDefaultMigrationService()
                .configurationData(ConfigurationDataBuilder.createConfiguration(CrateMainMenu.class))
                .create();

        File previewMenuFile = new File(this.dataFolder, "/menus/preview-menu.yml");

        ConfigManager.previewMenuConfig = SettingsManagerBuilder
                .withYamlFile(previewMenuFile)
                .useDefaultMigrationService()
                .configurationData(ConfigurationDataBuilder.createConfiguration(CratePreviewMenu.class))
                .create();
    }

    public void reload() {
        // Reload plugin-config.yml
        ConfigManager.pluginConfig.reload();

        ConfigManager.localeConfig.save();

        File localeFile = new File(this.dataFolder, "/locale/" + ConfigManager.pluginConfig.getProperty(PluginConfig.plugin_locale) + ".yml");

        ConfigManager.localeConfig = SettingsManagerBuilder
                .withYamlFile(localeFile)
                .useDefaultMigrationService()
                .configurationData(Locale.class)
                .create();

        // Reload crate-menu.yml
        ConfigManager.mainMenuConfig.reload();

        // Reload preview-menu.yml
        ConfigManager.previewMenuConfig.reload();
    }

    public static SettingsManager getConfig() {
        return mainConfig;
    }

    public static SettingsManager getPluginConfig() {
        return pluginConfig;
    }

    public static SettingsManager getLocaleConfig() {
        return localeConfig;
    }

    public static SettingsManager getMainMenuConfig() {
        return mainMenuConfig;
    }

    public static SettingsManager getPreviewMenuConfig() {
        return previewMenuConfig;
    }

    private ConfigurationData createPluginConfig() {
        return ConfigurationDataBuilder.createConfiguration(PluginConfig.class);
    }
}