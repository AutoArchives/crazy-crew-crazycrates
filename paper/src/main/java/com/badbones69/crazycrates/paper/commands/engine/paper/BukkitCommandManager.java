package com.badbones69.crazycrates.paper.commands.engine.paper;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.brigadier.CloudBrigadierManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.execution.CommandExecutionHandler;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.badbones69.crazycrates.core.frame.command.CloudCommandManager;
import com.badbones69.crazycrates.core.frame.command.Sender;
import com.badbones69.crazycrates.core.frame.command.other.CloudActor;
import com.badbones69.crazycrates.core.frame.command.other.CloudBuilder;
import com.badbones69.crazycrates.core.frame.utils.AdventureUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BukkitCommandManager implements CloudBuilder, CloudActor {

    private PaperCommandManager<@NotNull Sender> paperManager;

    private final CloudCommandManager cloudCommandManager;

    private final String name;
    private final String description;
    private final CommandExecutionHandler<Sender> handler;

    public static BukkitCommandManager create(JavaPlugin plugin, String name, String description, CommandExecutionHandler<Sender> handler) {
        return new BukkitCommandManager(plugin, name, description, handler);
    }

    public BukkitCommandManager(JavaPlugin plugin, String name, String description, CommandExecutionHandler<Sender> handler) {
        // Create paper manager.
        try {
            this.paperManager = new PaperCommandManager<>(plugin, CommandExecutionCoordinator.simpleCoordinator(),
                    BukkitSender::create,
                    Sender::getSender);
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
            CloudBrigadierManager<Sender, ?> brigadier = this.paperManager.brigadierManager();

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
    public @NotNull CommandManager<@NotNull Sender> getManager() {
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
    public CommandExecutionHandler<Sender> getHandler() {
        return this.handler;
    }

    @Override
    public Command.@NotNull Builder<@NotNull Sender> getRoot() {
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