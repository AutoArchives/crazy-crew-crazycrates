package com.badbones69.crazycrates.paper;

import com.badbones69.crazycrates.core.ApiManager;
import com.badbones69.crazycrates.core.config.types.PluginConfig;
import com.badbones69.crazycrates.paper.api.frame.PaperCore;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ALL")
public class CrazyStarter implements PluginBootstrap {

    private ApiManager apiManager;

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        this.apiManager = new ApiManager(context.getDataDirectory());
        this.apiManager.load();
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        PaperCore paperCore = new PaperCore(context.getDataDirectory(), Bukkit.getConsoleSender(), ApiManager.getPluginConfig().getProperty(PluginConfig.COMMAND_PREFIX), ApiManager.getPluginConfig().getProperty(PluginConfig.CONSOLE_PREFIX));
        paperCore.enable();

        return new CrazyCrates(this.apiManager, paperCore);
    }
}