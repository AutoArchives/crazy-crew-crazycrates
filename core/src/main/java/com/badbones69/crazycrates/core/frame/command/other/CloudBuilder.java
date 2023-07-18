package com.badbones69.crazycrates.core.frame.command.other;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.CommandExecutionHandler;
import cloud.commandframework.minecraft.extras.AudienceProvider;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import com.badbones69.crazycrates.core.frame.command.CloudCommandManager;
import com.badbones69.crazycrates.core.frame.command.Sender;
import com.badbones69.crazycrates.core.frame.utils.AdventureUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import java.util.function.UnaryOperator;

public interface CloudBuilder {

    @NotNull CommandManager<@NotNull Sender> getManager();

    String getName();

    String getDescription();

    CommandExecutionHandler<Sender> getHandler();

    default MinecraftExceptionHandler<Sender> exceptions() {
        return new MinecraftExceptionHandler<>();
    }

    default @NonNull MinecraftExceptionHandler<Sender> createInvalidSyntax(String prefix, String hoverText, String clickValue) {
        return exceptions().withInvalidSyntaxHandler()
                .withDecorator(component ->
                        Component.text().append(AdventureUtils.parse(prefix))
                                .hoverEvent(AdventureUtils.parse(hoverText))
                                .clickEvent(ClickEvent.runCommand(clickValue))
                                .append(component)
                                .build());
    }

    default void registerCommand(@NotNull UnaryOperator<Command.@NotNull Builder<@NotNull Sender>> builder) {
        this.getManager().command(builder.apply(getRoot()));
    }

    Command.@NotNull Builder<@NotNull Sender> getRoot();

    void registerCompatibility();

    @NotNull CloudCommandManager getCloudCommandManager();

}