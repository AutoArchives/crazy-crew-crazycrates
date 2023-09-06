package us.crazycrew.crazycrates.common;

import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.CrazyCrates;
import us.crazycrew.crazycrates.api.CrazyCratesProvider;
import us.crazycrew.crazycrates.api.platforms.Platform;
import us.crazycrew.crazycrates.common.config.ConfigManager;
import java.io.File;

public class CrazyCratesPlugin extends CrazyCrates {

    private final Platform.Type platform;
    private final File dataFolder;
    private ConfigManager configManager;

    public CrazyCratesPlugin(Platform.Type platform, File dataFolder) {
        this.platform = platform;

        this.dataFolder = dataFolder;
    }

    public void enable() {
        CrazyCratesProvider.start(this);

        this.configManager = new ConfigManager(this.dataFolder);
        this.configManager.load();
    }

    public void disable() {
        CrazyCratesProvider.stop();
    }

    @Override
    public @NotNull Platform.Type getPlatform() {
        return this.platform;
    }

    public @NotNull ConfigManager getConfigManager() {
        return this.configManager;
    }
}