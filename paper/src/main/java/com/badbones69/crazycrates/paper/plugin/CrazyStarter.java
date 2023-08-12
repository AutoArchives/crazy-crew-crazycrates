package com.badbones69.crazycrates.paper.plugin;

import com.badbones69.crazycrates.api.ConfigManager;
import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.commands.provider.BukkitLocaleProvider;
import com.ryderbelserion.lexicon.bukkit.BukkitImpl;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class CrazyStarter implements PluginBootstrap {

    private ConfigManager configManager;

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        this.configManager = new ConfigManager(context.getDataDirectory());

        this.configManager.load();
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        BukkitImpl bukkit = new BukkitImpl(context, Bukkit.getConsoleSender(), "<gradient:#e91e63:#e03d74>CrazyCrates</gradient> ");

        bukkit.setLocaleProvider(new BukkitLocaleProvider());

        return new CrazyCrates(bukkit, this.configManager);
    }
}