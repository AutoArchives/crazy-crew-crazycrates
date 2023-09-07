package us.crazycrew.crazycrates.common.api;

import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.CrazyCrates;
import us.crazycrew.crazycrates.api.CrazyCratesProvider;
import us.crazycrew.crazycrates.api.platforms.Platform;
import us.crazycrew.crazycrates.common.config.ConfigManager;
import java.io.File;

public abstract class AbstractPlugin implements CrazyCrates {

    @NotNull
    public abstract ConfigManager getConfigManager();

    private final Platform.Type platform;
    private final File dataFolder;

    public AbstractPlugin(File dataFolder, Platform.Type platform) {
        this.dataFolder = dataFolder;
        this.platform = platform;
    }

    public void enable() {
        CrazyCratesProvider.start(this);
    }

    public void disable() {
        CrazyCratesProvider.stop();
    }

    @NotNull
    @Override
    public Platform.Type getPlatform() {
        return this.platform;
    }

    @NotNull
    @Override
    public File getDataFolder() {
        return this.dataFolder;
    }
}