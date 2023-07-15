package com.badbones69.crazycrates.commands.engine.v3.paper.example;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys;
import com.badbones69.crazycrates.commands.engine.v3.core.CloudCommandEngine;
import com.badbones69.crazycrates.commands.engine.v3.paper.BukkitCommandManager;
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
                .flag(
                        this.manager.getManager().flagBuilder("type")
                                .withDescription(ArgumentDescription.of("the key type.")).build()
                )
                .handler(this::perform));
    }

    @Override
    protected void perform(@NotNull CommandContext<@NotNull CommandSender> context) {
        //String value = context.flags().getValue("type", KeyType.PHYSICAL_KEY.getName());

        //context.getSender().sendMessage(AdventureUtils.parse("<red>" + value + "</red>"));
    }
}