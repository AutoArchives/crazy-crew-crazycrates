package com.badbones69.crazycrates.loader;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.ApiManager;
import com.ryderbelserion.stick.paper.PaperCore;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CrazyStarter implements PluginBootstrap {

    private PaperCore paperCore;
    private ApiManager apiManager;

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        this.paperCore = new PaperCore(context.getConfiguration().getName(), context.getDataDirectory());

        this.apiManager = new ApiManager(context.getDataDirectory(), this.paperCore);
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        this.apiManager.setServer(Bukkit.getServer());
        this.apiManager.load(true);

        return new CrazyCrates(this.apiManager, this.paperCore);
    }
}