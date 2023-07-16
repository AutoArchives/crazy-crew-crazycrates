package com.badbones69.crazycrates.paper.api.frame;

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
    private final String prefix;
    private final String consolePrefix;

    private BukkitAudiences adventure;

    public PaperCore(JavaPlugin plugin, String prefix, String consolePrefix) {
        this.plugin = plugin;

        this.prefix = prefix;
        this.consolePrefix = consolePrefix;

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
        return this.plugin.getDataFolder().toPath();
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public String getConsolePrefix() {
        return this.consolePrefix;
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