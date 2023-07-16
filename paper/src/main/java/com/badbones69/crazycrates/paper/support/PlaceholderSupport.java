package com.badbones69.crazycrates.paper.support;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.paper.api.v2.ApiManager;
import com.badbones69.crazycrates.core.config.types.PluginConfig;

public class PlaceholderSupport {

    private final SettingsManager pluginConfig;

    public PlaceholderSupport(ApiManager apiManager) {
        this.pluginConfig = apiManager.getPluginConfig();
    }

    public String setPlaceholders(String placeholder) {
        placeholder = placeholder.replaceAll("\\{prefix}", this.pluginConfig.getProperty(PluginConfig.COMMAND_PREFIX));

        return placeholder;
    }
}