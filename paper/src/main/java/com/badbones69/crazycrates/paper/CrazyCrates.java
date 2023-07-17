package com.badbones69.crazycrates.paper;

import com.badbones69.crazycrates.core.ApiManager;
import com.badbones69.crazycrates.paper.api.CrazyManager;
import com.badbones69.crazycrates.paper.api.v1.EventLogger;
import com.badbones69.crazycrates.paper.api.v1.FileManager;
import com.badbones69.crazycrates.paper.commands.v2.admin.CommandReload;
import com.badbones69.crazycrates.paper.commands.v2.admin.keys.CommandGiveKeys;
import com.badbones69.crazycrates.paper.commands.v2.admin.schematics.CommandSchematicSave;
import com.badbones69.crazycrates.paper.commands.v2.admin.schematics.CommandSchematicSet;
import com.badbones69.crazycrates.paper.utils.MiscUtils;
import com.badbones69.crazycrates.paper.api.frame.command.BukkitCommandManager;
import com.badbones69.crazycrates.paper.listeners.v2.DataListener;
import com.badbones69.crazycrates.paper.support.structures.blocks.ChestStateHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class CrazyCrates extends JavaPlugin implements Listener {

    private ApiManager apiManager;
    private CrazyManager crazyManager;
    private BukkitCommandManager manager;

    @Override
    public void onEnable() {
        this.apiManager = new ApiManager(this.getDataFolder().toPath());
        this.apiManager.load();

        // CrazyLogger can be used after this loads.
        this.crazyManager = new CrazyManager();
        this.crazyManager.load(true);

        MiscUtils.registerPermissions(getServer().getPluginManager());

        // Create instance.
        this.manager = BukkitCommandManager.create(this, "crazycrates", "base command", context -> {});

        // Enable some compat improvements.
        this.manager.registerCompatibility();

        List.of(
                new CommandReload(),
                new CommandSchematicSave(),
                new CommandSchematicSet(),
                new CommandGiveKeys()
        ).forEach(this.manager.getCloudCommandManager()::addCommand);

        getServer().getPluginManager().registerEvents(new DataListener(), this);
    }

    @Override
    public void onDisable() {
        if (this.crazyManager != null) {
            this.crazyManager.reload(true);

            if (this.crazyManager.getUserManager() != null) this.crazyManager.getUserManager().save();
            if (this.crazyManager.getHologramManager() != null) this.crazyManager.getHologramManager().purge();

            if (this.crazyManager.getPaperCore() != null) this.crazyManager.getPaperCore().disable();
        }
    }

    public ApiManager getApiManager() {
        return this.apiManager;
    }

    public BukkitCommandManager getCommandManager() {
        return this.manager;
    }

    public CrazyManager crazyManager() {
        return this.crazyManager;
    }

    public com.badbones69.crazycrates.paper.api.v1.CrazyManager getCrazyManager() {
        return null;
    }

    // TODO() Remove
    public FileManager getFileManager() {
        return new FileManager();
    }

    public ChestStateHandler getChestStateHandler() {
        return new ChestStateHandler();
    }

    public EventLogger getEventLogger() {
        return new EventLogger();
    }
}