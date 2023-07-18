package com.badbones69.crazycrates.paper.api;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.paper.CrazyCrates;
import org.bukkit.plugin.java.JavaPlugin;

public interface CrazyConfig {

    CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    SettingsManager config = plugin.getApiManager().getConfig();

    SettingsManager pluginConfig = plugin.getApiManager().getPluginConfig();

    SettingsManager locale = plugin.getApiManager().getLocale();

    SettingsManager support = plugin.crazyManager().getPluginSupport();
}