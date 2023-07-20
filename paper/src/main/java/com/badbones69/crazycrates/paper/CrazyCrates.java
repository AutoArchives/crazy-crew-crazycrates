package com.badbones69.crazycrates.paper;

import com.badbones69.crazycrates.core.ApiManager;
import com.badbones69.crazycrates.paper.api.CrazyManager;
import com.badbones69.crazycrates.paper.api.frame.PaperCore;
import com.badbones69.crazycrates.paper.api.frame.command.CommandManager;
import com.badbones69.crazycrates.paper.api.v1.EventLogger;
import com.badbones69.crazycrates.paper.api.v1.FileManager;
import com.badbones69.crazycrates.paper.commands.v2.admin.CommandReload;
import com.badbones69.crazycrates.paper.utils.MiscUtils;
import com.badbones69.crazycrates.paper.listeners.v2.DataListener;
import com.badbones69.crazycrates.paper.support.structures.blocks.ChestStateHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class CrazyCrates extends JavaPlugin implements Listener {

    private final ApiManager apiManager;
    private final PaperCore paperCore;

    private CrazyManager crazyManager;
    private CommandManager commandManager;

    public CrazyCrates(ApiManager apiManager, PaperCore paperCore) {
        this.apiManager = apiManager;
        this.paperCore = paperCore;
    }

    @Override
    public void onEnable() {
        // CrazyLogger can be used after this loads.
        this.crazyManager = new CrazyManager();
        this.crazyManager.load();

        MiscUtils.registerPermissions(getServer().getPluginManager());

        this.commandManager = CommandManager.create();

        List.of(
                new CommandReload()
        ).forEach(this.commandManager::addCommand);

        getServer().getPluginManager().registerEvents(new DataListener(), this);
    }

    @Override
    public void onDisable() {
        if (this.crazyManager != null) {
            this.crazyManager.reload(true);

            if (this.crazyManager.getUserManager() != null) this.crazyManager.getUserManager().save();
            if (this.crazyManager.getHologramManager() != null) this.crazyManager.getHologramManager().purge();

            if (this.paperCore != null) this.paperCore.disable();
        }
    }

    public ApiManager getApiManager() {
        return this.apiManager;
    }

    public CrazyManager crazyManager() {
        return this.crazyManager;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
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