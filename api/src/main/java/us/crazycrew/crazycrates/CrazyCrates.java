package us.crazycrew.crazycrates;

import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.platform.Server;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.config.KeyManager;
import java.io.File;
import java.util.logging.Logger;

public class CrazyCrates {

    private Server server;

    public CrazyCrates(final Server server) {
        this.server = server;

        // Register the provider.
        CrazyCratesProvider.register(this);
    }

    public void enable(Server server) {
        // Load the config files.
        ConfigManager.load(server.getFolder());

        // Create server object.
        this.server = server;

        // Make key directory if it doesn't exist.
        this.server.getKeyFolder().mkdirs();
    }

    public void reload() {
        // Reload the config.
        ConfigManager.reload();

        // Make key directory if it doesn't exist.
        this.server.getKeyFolder().mkdirs();
    }

    public void disable() {
        // Save the config files.
        ConfigManager.save();

        // Unregister the provider.
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