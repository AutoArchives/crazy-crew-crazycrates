package com.badbones69.crazycrates.paper.api.v2.config;

import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import com.badbones69.crazycrates.paper.api.v2.config.types.PluginSupport;

public class ConfigBuilder {

    private ConfigBuilder() {}

    public static ConfigurationData pluginSupport() {
        return ConfigurationDataBuilder.createConfiguration(
                PluginSupport.class
        );
    }
}