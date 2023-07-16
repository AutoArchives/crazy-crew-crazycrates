package com.badbones69.crazycrates.core.frame.command.other;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.CommandExecutionHandler;
import com.badbones69.crazycrates.core.frame.command.CloudCommandManager;
import com.badbones69.crazycrates.core.frame.command.Sender;
import org.jetbrains.annotations.NotNull;
import java.util.function.UnaryOperator;

public interface CloudBuilder {

    @NotNull CommandManager<@NotNull Sender> getManager();

    String getName();

    String getDescription();

    CommandExecutionHandler<Sender> getHandler();

    default void registerCommand(@NotNull UnaryOperator<Command.@NotNull Builder<@NotNull Sender>> builder) {
        this.getManager().command(builder.apply(getRoot()));
    }

    Command.@NotNull Builder<@NotNull Sender> getRoot();

    void registerCompatibility();

    @NotNull CloudCommandManager getCloudCommandManager();

}