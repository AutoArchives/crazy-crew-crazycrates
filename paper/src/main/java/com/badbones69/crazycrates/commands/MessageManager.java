package com.badbones69.crazycrates.commands;

import com.badbones69.crazycrates.CrazyCratesPaper;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class MessageManager {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final @NotNull BukkitCommandManager<CommandSender> bukkitCommandManager = this.plugin.getCommandManager();

    public @NotNull BukkitCommandManager<CommandSender> getBukkitCommandManager() {
        return this.bukkitCommandManager;
    }

    public abstract void build();

    public abstract void send(@NotNull CommandSender sender, @NotNull String component);

    public abstract String parse(@NotNull String message);

}