package com.badbones69.crazycrates.commands.v2.admin;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.CommandContext;
import com.badbones69.crazycrates.commands.engine.CommandEngine;
import com.badbones69.crazycrates.commands.engine.reqs.CommandRequirementsBuilder;
import com.badbones69.crazycrates.commands.CommandPermissions;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandAdmin extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final SettingsManager pluginConfig = plugin.getApiManager().getPluginConfig();
    private final SettingsManager locale = plugin.getApiManager().getLocale();

    public CommandAdmin() {
        super();

        addAlias("admin");

        this.description = "Opens up a menu to easily get keys.";

        this.requirements = new CommandRequirementsBuilder()
                .withRawPermission(CommandPermissions.ADMIN_ACCESS.getBuiltPermission())
                .asPlayer(true)
                .build();
    }

    @Override
    protected void perform(CommandContext context) {

    }
}