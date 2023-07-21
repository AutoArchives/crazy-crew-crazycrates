package com.badbones69.crazycrates.paper.api;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.core.ApiManager;
import com.badbones69.crazycrates.paper.CrazyCrates;
import org.bukkit.plugin.java.JavaPlugin;

public interface CrazyConfig {

    CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    SettingsManager config = ApiManager.getConfig();

    SettingsManager pluginConfig = ApiManager.getPluginConfig();

    SettingsManager locale = ApiManager.getLocale();

    SettingsManager support = plugin.crazyManager().getPluginSupport();
}