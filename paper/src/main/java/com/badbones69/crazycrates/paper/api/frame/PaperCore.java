package com.badbones69.crazycrates.paper.api.frame;

import com.badbones69.crazycrates.paper.api.v2.ApiManager;
import com.badbones69.crazycrates.core.config.types.PluginConfig;
import com.badbones69.crazycrates.core.frame.CrazyCore;
import com.badbones69.crazycrates.core.frame.storage.FileHandler;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.nio.file.Path;

public class PaperCore extends CrazyCore {

    private final FileHandler fileHandler;
    private final JavaPlugin plugin;
    private final ApiManager apiManager;

    private BukkitAudiences adventure;

    public PaperCore(JavaPlugin plugin, ApiManager apiManager) {
        this.plugin = plugin;
        this.apiManager = apiManager;

        // Create directory.
        File file = this.plugin.getDataFolder();
        file.mkdir();

        this.fileHandler = new FileHandler();
    }

    @Override
    public void enable() {
        this.adventure = BukkitAudiences.create(this.plugin);
        super.enable();
    }

    @Override
    public void disable() {
        super.disable();
    }

    @Override
    public Path getDirectory() {
        return null;
    }

    @Override
    public String getPrefix() {
        return this.apiManager.getPluginConfig().getProperty(PluginConfig.COMMAND_PREFIX);
    }

    @Override
    public FileHandler getFileHandler() {
        return this.fileHandler;
    }

    @Override
    public @NotNull AudienceProvider adventure() {
        return this.adventure;
    }
}