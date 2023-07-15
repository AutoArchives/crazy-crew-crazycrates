package com.badbones69.crazycrates.commands.engine.v3.core.other;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.CommandExecutionHandler;
import com.badbones69.crazycrates.commands.engine.v3.core.CloudCommandManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.function.UnaryOperator;

public interface CloudBuilder {

    @NotNull CommandManager<@NotNull CommandSender> getManager();

    String getName();

    String getDescription();

    CommandExecutionHandler<CommandSender> getHandler();

    default void registerCommand(@NotNull UnaryOperator<Command.@NotNull Builder<@NotNull CommandSender>> builder) {
        this.getManager().command(builder.apply(getRoot()));
    }

    Command.@NotNull Builder<@NotNull CommandSender> getRoot();

    void registerCompatibility();

    @NotNull CloudCommandManager getCloudCommandManager();

}