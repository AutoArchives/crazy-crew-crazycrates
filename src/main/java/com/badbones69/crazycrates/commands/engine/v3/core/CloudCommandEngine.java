package com.badbones69.crazycrates.commands.engine.v3.core;

import cloud.commandframework.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class CloudCommandEngine {

    public abstract void registerCommand();

    //TODO() I don't think this will work with cloud, the way the command tree in cloud is made. I can delete all commands but individual ones seems iffy.
    //public abstract void unregisterCommand();

    protected abstract void perform(@NotNull CommandContext<@NotNull CommandSender> context);

}