package us.crazycrew.crazycrates.common;

import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.platforms.Platform;
import us.crazycrew.crazycrates.common.config.ConfigManager;
import us.crazycrew.crazycrates.common.config.api.AbstractPlugin;
import us.crazycrew.crazycrates.common.config.types.PluginConfig;

import java.io.File;

public abstract class CrazyCratesPlugin extends AbstractPlugin {

    private ConfigManager configManager;

    public CrazyCratesPlugin(File dataFolder, Platform.Type platform) {
        super(dataFolder, platform);
    }

    public void enable() {
        super.enablePlugin();

        this.configManager = new ConfigManager(getDataFolder());
        this.configManager.load();

        FancyLogger.setName(this.configManager.getPluginConfig().getProperty(PluginConfig.console_prefix));

        super.apiWasLoadedByOurPlugin();
    }

    public void disable() {
        super.disablePlugin();

        this.configManager.reload();
    }

    @NotNull
    @Override
    public ConfigManager getConfigManager() {
        return this.configManager;
    }
}