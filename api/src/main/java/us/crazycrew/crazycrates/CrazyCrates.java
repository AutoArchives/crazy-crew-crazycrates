package us.crazycrew.crazycrates;

import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.CrazyCratesService;
import us.crazycrew.crazycrates.api.ICrazyCrates;
import us.crazycrew.crazycrates.api.users.UserManager;
import us.crazycrew.crazycrates.platform.Server;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.config.KeyManager;
import java.io.File;
import java.util.logging.Logger;

@SuppressWarnings("ClassCanBeRecord")
public class CrazyCrates implements ICrazyCrates {

    private final Server server;

    public CrazyCrates(final Server server) {
        this.server = server;

        CrazyCratesService.register(this);

        CrazyCratesProvider.register(this);
    }

    public void reload() {
        ConfigManager.reload();
    }

    public void disable() {
        ConfigManager.save();

        CrazyCratesService.unregister();

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

    @Override
    public @NotNull UserManager getUserManager() {
        return this.server.getUserManager();
    }
}