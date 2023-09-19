package us.crazycrew.crazycrates.common.config;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import us.crazycrew.crazycrates.common.config.types.menus.CrateMainMenu;
import us.crazycrew.crazycrates.common.config.types.Messages;
import us.crazycrew.crazycrates.common.config.types.Config;
import us.crazycrew.crazycrates.common.config.types.PluginConfig;
import us.crazycrew.crazycrates.common.config.types.menus.CratePreviewMenu;
import java.io.File;

public class ConfigManager {

    private final File dataFolder;

    public ConfigManager(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    private static SettingsManager pluginConfig;
    private static SettingsManager messages;
    private static SettingsManager config;

    private static SettingsManager mainMenuConfig;
    private static SettingsManager previewMenuConfig;

    public void load() {
        // Create the plugin-config.yml file.
        File pluginConfigFile = new File(this.dataFolder, "plugin-config.yml");

        // Bind it to settings manager
        pluginConfig = SettingsManagerBuilder
                .withYamlFile(pluginConfigFile)
                .useDefaultMigrationService()
                .configurationData(createPluginConfig())
                .create();

        File messagesFile = new File(this.dataFolder, "/locale/" + pluginConfig.getProperty(PluginConfig.plugin_locale) + ".yml");

        messages = SettingsManagerBuilder
                .withYamlFile(messagesFile)
                .useDefaultMigrationService()
                .configurationData(Messages.class)
                .create();

        File configFile = new File(this.dataFolder, "config.yml");

        config = SettingsManagerBuilder
                .withYamlFile(configFile)
                .useDefaultMigrationService()
                .configurationData(ConfigurationDataBuilder.createConfiguration(Config.class))
                .create();

        File mainMenuFile = new File(this.dataFolder, "/menus/crate-menu.yml");

        mainMenuConfig = SettingsManagerBuilder
                .withYamlFile(mainMenuFile)
                .useDefaultMigrationService()
                .configurationData(ConfigurationDataBuilder.createConfiguration(CrateMainMenu.class))
                .create();

        File previewMenuFile = new File(this.dataFolder, "/menus/preview-menu.yml");

        previewMenuConfig = SettingsManagerBuilder
                .withYamlFile(previewMenuFile)
                .useDefaultMigrationService()
                .configurationData(ConfigurationDataBuilder.createConfiguration(CratePreviewMenu.class))
                .create();
    }

    public void reload() {
        // Reload plugin-config.yml
        pluginConfig.reload();

        // Save en-US.yml
        messages.reload();

        //File messagesFile = new File(this.dataFolder, "/locale/" + pluginConfig.getProperty(PluginConfig.plugin_locale) + ".yml");

        //TODO() Re-creating this causes the enum to be null... re-visit this idea
        //messages = SettingsManagerBuilder
        //        .withYamlFile(messagesFile)
        //        .useDefaultMigrationService()
        //        .configurationData(Messages.class)
        //        .create();

        // Reload crate-menu.yml
        mainMenuConfig.reload();

        // Reload preview-menu.yml
        previewMenuConfig.reload();
    }

    public static SettingsManager getConfig() {
        return config;
    }

    public static SettingsManager getPluginConfig() {
        return pluginConfig;
    }

    public static SettingsManager getMessages() {
        return messages;
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