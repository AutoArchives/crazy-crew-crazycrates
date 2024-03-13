package com.badbones69.crazycrates;

import com.badbones69.crazycrates.api.FileManager;
import com.badbones69.crazycrates.platform.PaperServer;
import com.badbones69.crazycrates.tasks.BukkitUserManager;
import com.badbones69.crazycrates.tasks.crates.BukkitCrateManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.CrazyCrates;
import us.crazycrew.crazycrates.platform.config.KeyManager;
import java.util.UUID;

public class CrazyCratesPaper extends JavaPlugin {

    private CrazyCrates instance;
    private PaperServer server;

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        // Create this instance first.
        this.instance = new CrazyCrates(this.server = new PaperServer(new BukkitCrateManager(), new BukkitUserManager(), new KeyManager()));

        FileManager fileManager = new FileManager();
        fileManager.registerDefaultGenerateFiles("CrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("QuadCrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("CosmicCrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("QuickCrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("WarCrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("CasinoExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("classic.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("nether.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("outdoors.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("sea.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("soul.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("wooden.nbt", "/schematics", "/schematics")
                .registerCustomFilesFolder("/crates")
                .registerCustomFilesFolder("/schematics")
                .setup();

        this.server.getCrateManager().loadCrates();

        this.instance.getUserManager().setKeys(5, UUID.randomUUID(), "CrateExample");
    }

    @Override
    public void onDisable() {
        if (this.instance != null) {
            this.instance.disable();
        }
    }

    @NotNull
    public PaperServer getPaperServer() {
        return this.server;
    }
}