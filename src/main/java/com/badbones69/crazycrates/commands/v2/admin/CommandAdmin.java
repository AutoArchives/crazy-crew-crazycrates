package com.badbones69.crazycrates.commands.v2.admin;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.v1.CommandContext;
import com.badbones69.crazycrates.commands.engine.v1.CommandEngine;
import com.badbones69.crazycrates.commands.engine.v2.builders.requirements.CommandRequirementsBuilder;
import com.badbones69.crazycrates.api.enums.Permissions;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandAdmin extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final SettingsManager pluginConfig = this.plugin.getApiManager().getPluginConfig();
    private final SettingsManager locale = this.plugin.getApiManager().getLocale();

    public CommandAdmin() {
        addAlias("admin");

        //setCommandEntryData(new CommandDataEntry());

        //getCommandDataEntry().setDescription("Opens up a menu to easily get keys.");

        this.requirements = new CommandRequirementsBuilder()
                .withRawPermission(Permissions.ADMIN_ACCESS.getBuiltPermission())
                .asPlayer(true)
                .build();
    }

    @Override
    protected void perform(CommandContext context) {

    }
}