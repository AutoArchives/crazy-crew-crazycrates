package com.badbones69.crazycrates.paper.commands.engine.paper.example;

/*
import cloud.commandframework.context.CommandContext;
import com.badbones69.crazycrates.paper.commands.engine.paper.BukkitCommandManager;
import com.ryderbelserion.crazycrates.core.frame.command.CloudCommandEngine;
import com.ryderbelserion.stick.core.utils.AdventureUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ExampleCommand extends CloudCommandEngine {

    private final BukkitCommandManager manager;

    public ExampleCommand(BukkitCommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void registerCommand() {
        this.manager.registerCommand(builder -> builder.literal("addkey")
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, AdventureUtils.parse("<red>Add keys to a player.</red>"))
                .handler(this::perform));
    }

    @Override
    protected void perform(@NotNull CommandContext<@NotNull CommandSender> context) {
        this.manager.reply(context.getSender(), "<red>Guten Tag!</red>");
    }
}
 */