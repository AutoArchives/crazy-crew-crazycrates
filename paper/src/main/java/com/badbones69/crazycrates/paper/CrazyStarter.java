package com.badbones69.crazycrates.paper;

import com.badbones69.crazycrates.api.ConfigManager;
import com.badbones69.crazycrates.paper.api.FileManager;
import com.badbones69.crazycrates.paper.commands.provider.BukkitLocaleProvider;
import com.badbones69.crazycrates.paper.support.placeholders.PlaceholderManager;
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
    private FileManager fileManager;
    private PlaceholderManager placeholderManager;

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        this.configManager = new ConfigManager(context.getDataDirectory());
        this.configManager.load();

        this.fileManager = new FileManager();

        this.fileManager.setLog(true)
                .registerDefaultGenerateFiles("CrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("QuadCrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("CosmicCrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("QuickCrateExample.yml", "/crates", "/crates")
                .registerDefaultGenerateFiles("classic.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("nether.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("outdoors.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("sea.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("soul.nbt", "/schematics", "/schematics")
                .registerDefaultGenerateFiles("wooden.nbt", "/schematics", "/schematics")
                .registerCustomFilesFolder("/crates")
                .registerCustomFilesFolder("/schematics")
                .setup();

        this.placeholderManager = new PlaceholderManager();
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        BukkitImpl bukkit = new BukkitImpl(context, Bukkit.getConsoleSender(), "<gradient:#e91e63:#e03d74>CrazyCrates</gradient> ");

        bukkit.setLocaleProvider(new BukkitLocaleProvider());

        return new CrazyCrates(bukkit, this.configManager, this.fileManager, this.placeholderManager);
    }
}