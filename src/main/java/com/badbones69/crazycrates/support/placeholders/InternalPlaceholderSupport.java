package com.badbones69.crazycrates.support.placeholders;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.configs.types.PluginConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class InternalPlaceholderSupport {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final SettingsManager pluginConfig = this.plugin.getApiManager().getPluginConfig();

    public String setPlaceholders(String placeholder) {
        placeholder = placeholder.replaceAll("\\{prefix}", this.pluginConfig.getProperty(PluginConfig.COMMAND_PREFIX));

        return placeholder;
    }
}