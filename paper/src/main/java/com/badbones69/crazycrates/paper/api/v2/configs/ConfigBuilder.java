package com.badbones69.crazycrates.paper.api.v2.configs;

import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import com.badbones69.crazycrates.paper.api.v2.configs.types.Config;
import com.badbones69.crazycrates.paper.api.v2.configs.types.Locale;
import com.badbones69.crazycrates.paper.api.v2.configs.types.PluginConfig;
import com.badbones69.crazycrates.paper.api.v2.configs.types.sections.PluginSupportSection;

public class ConfigBuilder {

    private ConfigBuilder() {}

    public static ConfigurationData buildPluginConfig() {
        return ConfigurationDataBuilder.createConfiguration(
                PluginConfig.class,
                PluginSupportSection.class
        );
    }

    public static ConfigurationData buildConfig() {
        return ConfigurationDataBuilder.createConfiguration(
                Config.class
        );
    }

    public static ConfigurationData buildLocale() {
        return ConfigurationDataBuilder.createConfiguration(
                Locale.class
        );
    }
}