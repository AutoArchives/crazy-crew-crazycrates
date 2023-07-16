package com.badbones69.crazycrates.paper.support;

import com.badbones69.crazycrates.core.ApiManager;
import com.badbones69.crazycrates.core.config.types.PluginConfig;
import com.badbones69.crazycrates.paper.CrazyCrates;
import org.bukkit.plugin.java.JavaPlugin;

public class PlaceholderSupport {

    private static final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private static final ApiManager apiManager = plugin.getApiManager();

    public static String setPlaceholders(String placeholder) {
        placeholder = placeholder.replaceAll("\\{prefix}", apiManager.getPluginConfig().getProperty(PluginConfig.COMMAND_PREFIX));

        return placeholder;
    }
}