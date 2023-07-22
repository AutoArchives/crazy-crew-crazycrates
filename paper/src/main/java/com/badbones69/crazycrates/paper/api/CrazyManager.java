package com.badbones69.crazycrates.paper.api;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.Zrips.CMI.Modules.ModuleHandling.CMIModule;
import com.badbones69.crazycrates.core.ApiManager;
import com.badbones69.crazycrates.core.config.types.PluginConfig;
import com.badbones69.crazycrates.core.frame.CrazyLogger;
import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.config.types.PluginSupport;
import com.badbones69.crazycrates.paper.api.crates.CrateManager;
import com.badbones69.crazycrates.paper.api.enums.support.HologramSupport;
import com.badbones69.crazycrates.paper.api.storage.interfaces.LocationManager;
import com.badbones69.crazycrates.paper.api.storage.interfaces.UserManager;
import com.badbones69.crazycrates.paper.api.storage.types.file.yaml.crates.YamlCrateManager;
import com.badbones69.crazycrates.paper.api.storage.types.file.yaml.users.YamlUserManager;
import com.badbones69.crazycrates.paper.support.holograms.interfaces.HologramManager;
import com.badbones69.crazycrates.paper.support.holograms.types.CMIHologramSupport;
import com.badbones69.crazycrates.paper.support.holograms.types.DecentHologramSupport;
import java.io.File;

public class CrazyManager {

    private final CrazyCrates plugin = CrazyCrates.getPlugin(CrazyCrates.class);

    private UserManager userManager;
    private LocationManager locationManager;
    private HologramManager hologramManager;

    private SettingsManager pluginSupport;

    private CrateManager crateManager;

    public void load() {
        this.pluginSupport = SettingsManagerBuilder
                .withYamlFile(new File(this.plugin.getDataFolder(), "plugin-support.yml"))
                .useDefaultMigrationService()
                .configurationData(PluginSupport.class)
                .create();

        this.crateManager = new CrateManager(this.plugin.getDataFolder());
        this.crateManager.loadCrates();

        init();
    }

    public void reload(boolean serverStop) {
        if (this.userManager != null) this.userManager.save();

        if (!serverStop) {
            // If holograms are enabled.
            boolean hologramsToggle = this.pluginSupport.getProperty(PluginSupport.HOLOGRAMS_SUPPORT_ENABLED);

            // Even if the holograms are disabled. Always purge them just in-case!
            if (this.hologramManager != null) this.hologramManager.purge();

            if (hologramsToggle) {
                HologramSupport hologramType = this.pluginSupport.getProperty(PluginSupport.HOLOGRAMS_SUPPORT_TYPE);

                // Switch hologram support based on the config option.
                switch (hologramType) {
                    case cmi_holograms -> {
                        if (CMIModule.holograms.isEnabled()) {
                            this.hologramManager = new CMIHologramSupport();

                            return;
                        }

                        CrazyLogger.warn("CMI is on the server but the CMI Hologram Module is not enabled.");
                        CrazyLogger.warn("Please go to Modules.yml in CMI & turn on the hologram module: Restart is required.");
                    }

                    case decent_holograms -> {
                        this.hologramManager = new DecentHologramSupport();

                        CrazyLogger.info("DecentHologram Support is enabled.");
                    }
                }
            }

            this.plugin.getApiManager().reload();

            this.crateManager = new CrateManager(this.plugin.getDataFolder());
            this.crateManager.loadCrates();

            init();
        }
    }

    private void init() {
        this.locationManager = new YamlCrateManager(new File(this.plugin.getDataFolder(), "locations.yml"));
        this.locationManager.load();

        this.userManager = new YamlUserManager(new File(this.plugin.getDataFolder(), "users.yml"), this.crateManager, ApiManager.getPluginConfig().getProperty(PluginConfig.VERBOSE_LOGGING));
        this.userManager.load();
    }

    public SettingsManager getPluginSupport() {
        return this.pluginSupport;
    }

    public UserManager getUserManager() {
        return this.userManager;
    }

    public LocationManager getLocationManager() {
        return this.locationManager;
    }

    public HologramManager getHologramManager() {
        return this.hologramManager;
    }

    public CrateManager getCrateManager() {
        return this.crateManager;
    }
}