package com.badbones69.crazycrates.paper.commands.engine.paper;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.brigadier.CloudBrigadierManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.execution.CommandExecutionHandler;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.ryderbelserion.crazycrates.core.command.CloudCommandManager;
import com.ryderbelserion.crazycrates.core.command.other.CloudActor;
import com.ryderbelserion.crazycrates.core.command.other.CloudBuilder;
import com.ryderbelserion.stick.core.utils.AdventureUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.function.Function;

public class BukkitCommandManager implements CloudBuilder, CloudActor {

    private PaperCommandManager<@NotNull CommandSender> paperManager;

    private final CloudCommandManager cloudCommandManager;

    private final String name;
    private final String description;
    private final CommandExecutionHandler<CommandSender> handler;

    public static BukkitCommandManager create(JavaPlugin plugin, String name, String description, CommandExecutionHandler<CommandSender> handler) {
        return new BukkitCommandManager(plugin, name, description, handler);
    }

    public BukkitCommandManager(JavaPlugin plugin, String name, String description, CommandExecutionHandler<CommandSender> handler) {
        // Create paper manager.
        try {
            this.paperManager = new PaperCommandManager<>(plugin, CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.name = name;
        this.description = description;
        this.handler = handler;

        // Create cloud manager.
        this.cloudCommandManager = new CloudCommandManager();
    }

    @Override
    public void registerCompatibility() {
        // Check if platform has brigadier compatibility.
        if (this.paperManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            // Register brigadier
            this.paperManager.registerBrigadier();

            // Get brigadier manager.
            CloudBrigadierManager<CommandSender, ?> brigadier = this.paperManager.brigadierManager();

            // Set native number suggestions to false.
            if (brigadier != null) brigadier.setNativeNumberSuggestions(false);
        }

        // If the platform has capability for async completions, register async completions.
        if (this.paperManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) this.paperManager.registerAsynchronousCompletions();
    }

    @Override
    public @NotNull CloudCommandManager getCloudCommandManager() {
        return this.cloudCommandManager;
    }

    @Override
    public @NotNull CommandManager<@NotNull CommandSender> getManager() {
        return this.paperManager;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public CommandExecutionHandler<CommandSender> getHandler() {
        return this.handler;
    }

    @Override
    public Command.@NotNull Builder<@NotNull CommandSender> getRoot() {
        return this.paperManager.commandBuilder(getName())
                .meta(CommandMeta.DESCRIPTION, getDescription())
                .handler(getHandler());
    }

    @Override
    public void reply(Audience audience, String message) {
        if (message.isBlank() || message.isEmpty()) return;

        Component component = AdventureUtils.parse(message);

        audience.sendMessage(component);
    }

    @Override
    public void reply(Audience audience, Component component) {
        audience.sendMessage(component);
    }
}