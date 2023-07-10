package com.badbones69.crazycrates.commands.v2.admin;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.configs.types.Locale;
import com.badbones69.crazycrates.api.configs.types.PluginConfig;
import com.badbones69.crazycrates.commands.CommandPermissions;
import com.badbones69.crazycrates.commands.v2.BaseCommand;
import com.ryderbelserion.stick.paper.commands.CommandContext;
import com.ryderbelserion.stick.paper.commands.CommandEngine;
import com.ryderbelserion.stick.paper.commands.reqs.CommandRequirementsBuilder;
import com.ryderbelserion.stick.paper.commands.sender.args.Argument;
import com.ryderbelserion.stick.paper.commands.sender.args.builder.IntArgument;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHelp extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final BaseCommand baseCommand;

    private final SettingsManager config = plugin.getApiManager().getPluginConfig();
    private final SettingsManager locale = plugin.getApiManager().getLocale();

    public CommandHelp(BaseCommand baseCommand) {
        super();

        this.baseCommand = baseCommand;

        addAlias("help");

        this.requiredArgs.add(new Argument("page", 0, new IntArgument(2)));

        this.description = CommandPermissions.PLAYER_HELP.getDescription();

        this.requirements = new CommandRequirementsBuilder()
                .withRawPermission(CommandPermissions.PLAYER_HELP.getBuiltPermission())
                .asPlayer(false)
                .build();
    }

    @Override
    protected void perform(CommandContext context) {
        int arg = context.getArgAsInt(0, true, this.locale.getProperty(Locale.NOT_A_NUMBER), "\\{number}");

        this.baseCommand.generateHelp(arg, this.config.getProperty(PluginConfig.MAX_HELP_PAGE_ENTRIES), context);
    }
}