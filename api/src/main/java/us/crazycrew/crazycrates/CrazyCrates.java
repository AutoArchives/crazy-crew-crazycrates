package us.crazycrew.crazycrates;

import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.platform.Server;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.config.KeyManager;
import java.io.File;
import java.util.logging.Logger;

@SuppressWarnings("ClassCanBeRecord")
public class CrazyCrates {

    private final Server server;

    public CrazyCrates(final Server server) {
        this.server = server;

        ConfigManager.load(this.server.getFolder());

        CrazyCratesProvider.register(this);
    }

    public void reload() {
        ConfigManager.reload();
    }

    public void disable() {
        ConfigManager.save();

        CrazyCratesProvider.unregister();
    }

    public @NotNull KeyManager getKeyManager() {
        return this.server.getKeyManager();
    }

    public @NotNull File[] getKeyFiles() {
        return this.server.getKeyFiles();
    }

    public @NotNull File getKeyFolder() {
        return this.server.getKeyFolder();
    }

    public @NotNull Logger getLogger() {
        return this.server.getLogger();
    }

    public @NotNull File getFolder() {
        return this.server.getFolder();
    }

    public @NotNull Server getServer() {
        return this.server;
    }
}