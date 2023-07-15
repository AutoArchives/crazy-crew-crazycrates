package com.badbones69.crazycrates.commands.engine.v3.core;

import cloud.commandframework.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class CloudCommandEngine {

    public abstract void registerCommand();

    protected abstract void perform(@NotNull CommandContext<@NotNull CommandSender> context);

}