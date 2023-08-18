package com.badbones69.crazycrates.folia.support.libraries;

import com.badbones69.crazycrates.folia.CrazyCrates;
import org.bukkit.plugin.java.JavaPlugin;

public enum PluginSupport {

    DECENT_HOLOGRAMS("DecentHolograms"),
    CMI("CMI"),
    PLACEHOLDERAPI("PlaceholderAPI");
    
    private final String name;

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    PluginSupport(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public boolean isPluginEnabled() {
        return plugin.getServer().getPluginManager().isPluginEnabled(name);
    }
}