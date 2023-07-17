package com.badbones69.crazycrates.paper;

import com.badbones69.crazycrates.core.ApiManager;
import com.badbones69.crazycrates.paper.api.CrazyManager;
import com.badbones69.crazycrates.paper.api.EventLogger;
import com.badbones69.crazycrates.paper.api.FileManager;
import com.badbones69.crazycrates.paper.api.v2.utils.MiscUtils;
import com.badbones69.crazycrates.paper.commands.engine.paper.BukkitCommandManager;
import com.badbones69.crazycrates.paper.listeners.v2.DataListener;
import com.badbones69.crazycrates.paper.support.structures.blocks.ChestStateHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CrazyCrates extends JavaPlugin implements Listener {

    private ApiManager apiManager;
    private CrazyManager crazyManager;

    @Override
    public void onEnable() {
        this.apiManager = new ApiManager();
        this.apiManager.load();

        this.crazyManager = new CrazyManager();
        this.crazyManager.load(true);

        MiscUtils.registerPermissions(getServer().getPluginManager());

        // Create instance.
        BukkitCommandManager manager = BukkitCommandManager.create(this, "crazycrates", "base command", context -> {});

        // Enable some compat improvements.
        manager.registerCompatibility();

        // Add the command.
        //manager.getCloudCommandManager().addCommand(new ExampleCommand(manager));

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