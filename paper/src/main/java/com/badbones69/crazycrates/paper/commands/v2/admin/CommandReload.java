package com.badbones69.crazycrates.paper.commands.v2.admin;

import com.badbones69.crazycrates.core.ApiManager;
import com.badbones69.crazycrates.core.config.types.Locale;
import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.enums.Permissions;
import com.badbones69.crazycrates.paper.api.frame.command.CommandContext;
import com.badbones69.crazycrates.paper.api.frame.command.CommandEngine;
import com.badbones69.crazycrates.paper.api.frame.command.CommandManager;
import com.badbones69.crazycrates.paper.support.PlaceholderSupport;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Collections;

public class CommandReload extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final CommandManager manager = this.plugin.getCommandManager();

    public CommandReload() {
        super("reload", Permissions.ADMIN_RELOAD.getDescription(), "/crazycrates:reload", Collections.emptyList());
    }

    @Override
    protected void perform(CommandContext context, String[] args) {
        if (context.isPlayer() && !context.getPlayer().hasPermission(Permissions.ADMIN_RELOAD.getBuiltPermission())) {
            context.reply(PlaceholderSupport.setPlaceholders(ApiManager.getLocale().getProperty(Locale.NO_PERMISSION)));
            return;
        }

        this.plugin.crazyManager().reload(false);
        context.reply(PlaceholderSupport.setPlaceholders(ApiManager.getLocale().getProperty(Locale.RELOAD_PLUGIN)));
    }
}