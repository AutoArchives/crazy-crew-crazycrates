package com.badbones69.crazycrates.api.support;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.api.ApiManager;
import com.badbones69.crazycrates.api.configs.types.PluginConfig;

public class InternalPlaceholderSupport {

    private final SettingsManager pluginConfig;

    public InternalPlaceholderSupport(ApiManager apiManager) {
        this.pluginConfig = apiManager.getPluginConfig();
    }

    public String setPlaceholders(String placeholder) {
        placeholder = placeholder.replaceAll("\\{prefix}", this.pluginConfig.getProperty(PluginConfig.COMMAND_PREFIX));

        return placeholder;
    }
}