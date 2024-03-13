package com.badbones69.crazycrates.platform;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.tasks.BukkitUserManager;
import com.badbones69.crazycrates.tasks.InventoryManager;
import com.badbones69.crazycrates.tasks.crates.BukkitCrateManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.platform.Server;
import us.crazycrew.crazycrates.platform.config.KeyManager;
import java.io.File;
import java.util.logging.Logger;

public class PaperServer implements Server {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final KeyManager keyManager;
    private final BukkitUserManager userManager;
    private final File keyFolder;

    private final File folder;

    public PaperServer(BukkitUserManager userManager) {
        this.folder = this.plugin.getDataFolder();

        this.keyFolder = new File(this.folder, "keys");
        this.keyFolder.mkdirs();

        this.keyManager = new KeyManager(this);

        this.userManager = userManager;
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

    @Override
    public @NotNull BukkitUserManager getUserManager() {
        return this.userManager;
    }
}