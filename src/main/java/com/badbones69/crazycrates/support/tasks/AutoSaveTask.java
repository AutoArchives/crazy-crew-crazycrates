package com.badbones69.crazycrates.support.tasks;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.ApiManager;
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