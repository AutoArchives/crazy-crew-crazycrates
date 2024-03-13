package com.badbones69.crazycrates;

import com.badbones69.crazycrates.platform.PaperServer;
import com.ryderbelserion.cluster.ClusterPackage;
import org.bukkit.plugin.java.JavaPlugin;
import us.crazycrew.crazycrates.CrazyCrates;

public class CrazyCratesPaper extends JavaPlugin {

    private ClusterPackage cluster;
    private CrazyCrates instance;

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        this.cluster = new ClusterPackage(this);

        this.instance = new CrazyCrates(new PaperServer());
    }

    @Override
    public void onDisable() {
        if (this.cluster != null) {
            this.cluster.getCluster().disable();
        }

        if (this.instance != null) {
            this.instance.disable();
        }
    }
}