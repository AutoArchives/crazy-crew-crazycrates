package com.badbones69.crazycrates.commands.v2.admin;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.CommandContext;
import com.badbones69.crazycrates.commands.engine.CommandEngine;
import com.badbones69.crazycrates.commands.engine.reqs.CommandRequirementsBuilder;
import com.badbones69.crazycrates.api.configs.types.Locale;
import com.badbones69.crazycrates.commands.CommandPermissions;
import com.badbones69.crazycrates.support.placeholders.InternalPlaceholderSupport;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandReload extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final InternalPlaceholderSupport placeholderSupport = this.plugin.getPlaceholderManager();

    private final SettingsManager locale = plugin.getApiManager().getLocale();

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

        String message = this.locale.getProperty(Locale.RELOAD_PLUGIN);

        context.reply(this.placeholderSupport.setPlaceholders(message));
    }
}