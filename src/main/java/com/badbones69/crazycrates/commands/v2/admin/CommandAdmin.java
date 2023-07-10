package com.badbones69.crazycrates.commands.v2.admin;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.CommandPermissions;
import com.ryderbelserion.stick.paper.commands.CommandContext;
import com.ryderbelserion.stick.paper.commands.CommandEngine;
import com.ryderbelserion.stick.paper.commands.reqs.CommandRequirementsBuilder;
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