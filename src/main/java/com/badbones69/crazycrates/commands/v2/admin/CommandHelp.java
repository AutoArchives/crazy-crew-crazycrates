package com.badbones69.crazycrates.commands.v2.admin;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.v1.CommandEngine;
import com.badbones69.crazycrates.commands.engine.v2.builders.requirements.CommandRequirementsBuilder;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.Argument;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.builder.IntArgument;
import com.badbones69.crazycrates.api.enums.Permissions;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHelp extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final SettingsManager pluginConfig = this.plugin.getApiManager().getPluginConfig();
    private final SettingsManager locale = this.plugin.getApiManager().getLocale();

    public CommandHelp() {
        addAlias("help");

        //setCommandEntryData(new CommandDataEntry());

        //getCommandDataEntry().setDescription(Permissions.PLAYER_HELP.getDescription());

        this.requiredArgs.add(new Argument("page", 0, new IntArgument(2)));

        this.requirements = new CommandRequirementsBuilder()
                .withRawPermission(Permissions.PLAYER_HELP.getBuiltPermission())
                .asPlayer(false)
                .build();
    }

    @Override
    protected void perform() {
        //int arg = context.getArgAsInt(0, true);

        //context.getHelpEntry().generateHelp(arg, this.pluginConfig.getProperty(PluginConfig.MAX_HELP_PAGE_ENTRIES), context);
    }
}