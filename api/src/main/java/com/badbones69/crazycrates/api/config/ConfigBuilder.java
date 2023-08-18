package com.badbones69.crazycrates.api.config;

import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import com.badbones69.crazycrates.api.config.types.Locale;

public class ConfigBuilder {

    private ConfigBuilder() {}

    public static ConfigurationData buildLocale() {
        return ConfigurationDataBuilder.createConfiguration(
                Locale.class
        );
    }
}