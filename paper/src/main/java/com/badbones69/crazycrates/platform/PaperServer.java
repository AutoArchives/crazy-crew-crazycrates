package com.badbones69.crazycrates.platform;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.tasks.BukkitUserManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.ICrazyCrates;
import us.crazycrew.crazycrates.platform.Server;
import us.crazycrew.crazycrates.platform.config.KeyManager;
import java.io.File;
import java.util.logging.Logger;

public class PaperServer implements Server, ICrazyCrates {

    @NotNull
    private final CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final File keyFolder;

    private final File folder;

    public PaperServer() {
        this.folder = this.plugin.getDataFolder();
        this.keyFolder = new File(this.folder, "keys");
    }

    @NotNull
    @Override
    public File getFolder() {
        return this.folder;
    }

    @Override
    public File getKeyFolder() {
        return this.keyFolder;
    }

    @NotNull
    @Override
    public Logger getLogger() {
        return this.plugin.getLogger();
    }

    @NotNull
    @Override
    public BukkitUserManager getUserManager() {
        return this.plugin.getUserManager();
    }
}