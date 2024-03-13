package com.badbones69.crazycrates;

import com.badbones69.crazycrates.platform.PaperServer;
import org.bukkit.plugin.java.JavaPlugin;
import us.crazycrew.crazycrates.CrazyCrates;

public class CrazyCratesPaper extends JavaPlugin {

    private CrazyCrates instance;

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        this.instance = new CrazyCrates(new PaperServer());
    }

    @Override
    public void onDisable() {
        if (this.instance != null) {
            this.instance.disable();
        }
    }
}