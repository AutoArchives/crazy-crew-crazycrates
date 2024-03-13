package com.badbones69.crazycrates.platform;

import com.badbones69.crazycrates.CrazyCratesPaper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.platform.Server;
import us.crazycrew.crazycrates.platform.config.KeyManager;
import java.io.File;
import java.util.logging.Logger;

public class PaperServer implements Server {

    @NotNull
    private final CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final KeyManager keyManager;
    private final File keyFolder;

    private final File folder;

    public PaperServer() {
        this.folder = this.plugin.getDataFolder();
        this.keyFolder = new File(this.folder, "keys");

        this.keyManager = new KeyManager(this);
    }

    @Override
    public @NotNull File getFolder() {
        return this.folder;
    }

    @Override
    public @NotNull File getKeyFolder() {
        return this.keyFolder;
    }

    @Override
    public @NotNull File[] getKeyFiles() {
        return this.keyFolder.listFiles((dir, name) -> name.endsWith(".yml"));
    }

    @Override
    public @NotNull Logger getLogger() {
        return this.plugin.getLogger();
    }

    @Override
    public @NotNull KeyManager getKeyManager() {
        return this.keyManager;
    }
}