package com.badbones69.crazycrates.commands.v2.admin;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.configs.types.Locale;
import com.badbones69.crazycrates.commands.CommandPermissions;
import com.ryderbelserion.stick.paper.commands.CommandContext;
import com.ryderbelserion.stick.paper.commands.CommandEngine;
import com.ryderbelserion.stick.paper.commands.reqs.CommandRequirementsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandReload extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    public CommandReload() {
        super();

        addAlias("reload");

        this.description = CommandPermissions.ADMIN_RELOAD.getDescription();

        this.requirements = new CommandRequirementsBuilder()
                .withRawPermission(CommandPermissions.ADMIN_RELOAD.getBuiltPermission())
                .asPlayer(false)
                .build();
    }

    @Override
    protected void perform(CommandContext context) {
        this.plugin.getApiManager().reload(true);

        //this.plugin.setCommandCore(new CommandCore());

        context.reply(this.plugin.getApiManager().getLocale().getProperty(Locale.RELOAD_PLUGIN));
    }
}