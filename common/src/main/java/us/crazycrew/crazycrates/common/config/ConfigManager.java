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

    private SettingsManager pluginConfig;
    private SettingsManager messages;
    private SettingsManager config;

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

        File messagesFile = new File(this.dataFolder, "/messages/" + this.pluginConfig.getProperty(PluginConfig.plugin_locale) + ".yml");

        this.messages = SettingsManagerBuilder
                .withYamlFile(messagesFile)
                .useDefaultMigrationService()
                .configurationData(Locale.class)
                .create();

        File configFile = new File(this.dataFolder, "config.yml");

        this.config = SettingsManagerBuilder
                .withYamlFile(configFile)
                .useDefaultMigrationService()
                .configurationData(ConfigurationDataBuilder.createConfiguration(Config.class))
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

        this.messages.save();

        File messagesFile = new File(this.dataFolder, "/messages/" + this.pluginConfig.getProperty(PluginConfig.plugin_locale) + ".yml");

        this.messages = SettingsManagerBuilder
                .withYamlFile(messagesFile)
                .useDefaultMigrationService()
                .configurationData(Locale.class)
                .create();

        // Reload crate-menu.yml
        this.mainMenuConfig.reload();

        // Reload preview-menu.yml
        this.previewMenuConfig.reload();
    }

    public SettingsManager getConfig() {
        return this.config;
    }

    public SettingsManager getPluginConfig() {
        return this.pluginConfig;
    }

    public SettingsManager getMessages() {
        return this.messages;
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