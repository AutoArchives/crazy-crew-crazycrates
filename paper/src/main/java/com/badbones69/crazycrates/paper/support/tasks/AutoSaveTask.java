package com.badbones69.crazycrates.paper.support.tasks;

import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.v2.ApiManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.TimerTask;

public class AutoSaveTask extends TimerTask {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final ApiManager apiManager = this.plugin.getApiManager();

    @Override
    public void run() {
        this.apiManager.getUserManager().save();
    }
}