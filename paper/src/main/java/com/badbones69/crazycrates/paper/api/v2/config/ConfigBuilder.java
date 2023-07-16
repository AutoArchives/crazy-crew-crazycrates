package com.badbones69.crazycrates.paper.api.v2.config;

import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import com.badbones69.crazycrates.paper.api.v2.config.sections.PluginSupportSection;
import com.badbones69.crazycrates.core.config.types.Config;
import com.badbones69.crazycrates.core.config.types.Locale;
import com.badbones69.crazycrates.core.config.types.PluginConfig;

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