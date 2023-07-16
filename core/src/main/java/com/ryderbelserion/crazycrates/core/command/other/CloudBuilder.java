package com.ryderbelserion.crazycrates.core.command.other;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.CommandExecutionHandler;
import com.ryderbelserion.crazycrates.core.command.CloudCommandManager;
import com.ryderbelserion.crazycrates.core.command.CloudCommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.function.UnaryOperator;

public interface CloudBuilder {

    @NotNull CommandManager<@NotNull CloudCommandSender> getManager();

    String getName();

    String getDescription();

    CommandExecutionHandler<CloudCommandSender> getHandler();

    default void registerCommand(@NotNull UnaryOperator<Command.@NotNull Builder<@NotNull CloudCommandSender>> builder) {
        this.getManager().command(builder.apply(getRoot()));
    }

    Command.@NotNull Builder<@NotNull CloudCommandSender> getRoot();

    void registerCompatibility();

    @NotNull CloudCommandManager getCloudCommandManager();

}