package us.crazycrew.crazycrates.platform;

import com.ryderbelserion.cluster.utils.FileUtils;
import us.crazycrew.crazycrates.CrazyCratesProvider;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.keys.KeyManager;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public abstract class Server {

    public Server() {
        CrazyCratesProvider.register(this);
    }

    public void enable() {
        List.of(
                "CrateExample.yml",
                "WarCrateExample.yml",
                "QuickCrateExample.yml",
                "QuadCrateExample.yml"
        ).forEach(line -> FileUtils.copyFile(getCrateFolder().toPath(), "crates", line));
    }

    public void reload() {
        ConfigManager.reload();
    }

    public void disable() {
        ConfigManager.save();

        CrazyCratesProvider.unregister();
    }

    public abstract Logger getLogger();

    public abstract boolean isLogging();

    public abstract File getFolder();

    public abstract File getKeyFolder();

    public abstract File getCrateFolder();

    public abstract File[] getKeyFiles();

    public abstract File[] getCrateFiles();

    public abstract KeyManager getKeyManager();
}