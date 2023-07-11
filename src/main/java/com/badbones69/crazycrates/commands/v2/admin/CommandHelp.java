package com.badbones69.crazycrates.commands.v2.admin;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.configs.types.PluginConfig;
import com.badbones69.crazycrates.commands.engine.CommandContext;
import com.badbones69.crazycrates.commands.engine.CommandEngine;
import com.badbones69.crazycrates.commands.engine.requirements.CommandRequirementsBuilder;
import com.badbones69.crazycrates.commands.engine.sender.args.Argument;
import com.badbones69.crazycrates.commands.engine.sender.args.builder.IntArgument;
import com.badbones69.crazycrates.api.enums.Permissions;
import com.badbones69.crazycrates.commands.v2.BaseCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHelp extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final SettingsManager pluginConfig = plugin.getApiManager().getPluginConfig();
    private final SettingsManager locale = plugin.getApiManager().getLocale();

    public CommandHelp() {
        super();

        addAlias("help");

        this.requiredArgs.add(new Argument("page", 0, new IntArgument(2)));

        setDescription(Permissions.PLAYER_HELP.getDescription());

        this.requirements = new CommandRequirementsBuilder()
                .withRawPermission(Permissions.PLAYER_HELP.getBuiltPermission())
                .asPlayer(false)
                .build();
    }

    @Override
    protected void perform(CommandContext context) {
        int arg = context.getArgAsInt(0, true);

        this.baseCommand.getCommandHelp().generateHelp(arg, this.pluginConfig.getProperty(PluginConfig.MAX_HELP_PAGE_ENTRIES), context);
    }
}