package us.crazycrew.crazycrates;

import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.CrazyCratesService;
import us.crazycrew.crazycrates.api.ICrazyCrates;
import us.crazycrew.crazycrates.api.users.UserManager;
import us.crazycrew.crazycrates.platform.Server;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.config.KeyManager;
import java.util.logging.Logger;

public class CrazyCrates implements ICrazyCrates {

    private final Server server;

    public CrazyCrates(Server server) {
        // Create server object.
        this.server = server;

        // Make key directory if it doesn't exist.
        this.server.getKeyFolder().mkdirs();

        // Register legacy provider.
        CrazyCratesService.register(this);

        // Register provider.
        CrazyCratesProvider.register(this);
    }

    public void reload() {
        // Reload the config
        ConfigManager.reload();

        // Make key directory if it doesn't exist.
        this.server.getKeyFolder().mkdirs();
    }

    public void disable() {
        // Save the config files.
        ConfigManager.save();

        // Unregister legacy provider.
        CrazyCratesService.unregister();

        // Unregister provider.
        CrazyCratesProvider.unregister();
    }

    @NotNull
    @Override
    public UserManager getUserManager() {
        return getServer().getUserManager();
    }

    @NotNull
    public Server getServer() {
        return this.server;
    }

    public Logger getLogger() {
        return getServer().getLogger();
    }
}