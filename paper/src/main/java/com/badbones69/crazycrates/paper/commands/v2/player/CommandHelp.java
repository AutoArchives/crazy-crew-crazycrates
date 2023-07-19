package com.badbones69.crazycrates.paper.commands.v2.player;

import cloud.commandframework.CommandHelpHandler;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys;
import cloud.commandframework.minecraft.extras.MinecraftHelp;
import com.badbones69.crazycrates.core.config.types.PluginConfig;
import com.badbones69.crazycrates.core.frame.command.CloudCommandEngine;
import com.badbones69.crazycrates.core.frame.command.Sender;
import com.badbones69.crazycrates.core.frame.utils.AdventureUtils;
import com.badbones69.crazycrates.paper.api.CrazyConfig;
import com.badbones69.crazycrates.paper.api.frame.command.v1.BukkitCommandManager;
import com.badbones69.crazycrates.paper.api.v2.enums.Permissions;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

public class CommandHelp extends CloudCommandEngine implements CrazyConfig {

    private final BukkitCommandManager manager = this.plugin.getCommandManager();

    private MinecraftHelp<@NotNull Sender> minecraftHelp;

    @Override
    public void registerCommand() {
        this.manager.registerCommand(builder -> builder.literal("help")
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, AdventureUtils.parse(Permissions.PLAYER_HELP.getDescription()))
                .permission(Permissions.PLAYER_HELP.getBuiltPermission())
                .argument(StringArgument.<Sender>builder("page").greedy().asOptional()
                        .withSuggestionsProvider((context, input) -> this.manager.getManager()
                                .createCommandHelpHandler().queryRootIndex(context.getSender())
                                .getEntries().stream().map(CommandHelpHandler.VerboseHelpEntry::getSyntaxString).toList())
                        .build())
                .handler(this::perform));

        this.minecraftHelp = MinecraftHelp.createNative("/crazycrates help", this.manager.getManager());

        this.minecraftHelp.setHelpColors(MinecraftHelp.HelpColors.of(
                NamedTextColor.RED,
                NamedTextColor.WHITE,
                NamedTextColor.WHITE,
                NamedTextColor.GOLD,
                TextColor.color(245,66,135)
        ));

        this.minecraftHelp.setMaxResultsPerPage(pluginConfig.getProperty(PluginConfig.MAX_HELP_PAGE_ENTRIES));

        this.minecraftHelp.setMessage(MinecraftHelp.MESSAGE_HELP_TITLE, "CrazyCrates Help");
    }

    @Override
    protected void perform(@NotNull CommandContext<@NotNull Sender> context) {
        String query = context.<String>getOptional("page").orElse("");

        this.minecraftHelp.queryCommands(query, context.getSender());
    }
}