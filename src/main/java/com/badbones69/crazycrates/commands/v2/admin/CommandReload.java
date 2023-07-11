package com.badbones69.crazycrates.commands.v2.admin;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.configs.types.PluginConfig;
import com.badbones69.crazycrates.commands.engine.CommandContext;
import com.badbones69.crazycrates.commands.engine.CommandEngine;
import com.badbones69.crazycrates.commands.engine.reqs.CommandRequirementsBuilder;
import com.badbones69.crazycrates.api.configs.types.Locale;
import com.badbones69.crazycrates.commands.CommandPermissions;
import com.badbones69.crazycrates.support.placeholders.InternalPlaceholderSupport;
import com.badbones69.crazycrates.support.tasks.AutoSaveTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Timer;

public class CommandReload extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final InternalPlaceholderSupport placeholderSupport = this.plugin.getPlaceholderManager();

    private final SettingsManager pluginConfig = this.plugin.getApiManager().getPluginConfig();
    private final SettingsManager locale = this.plugin.getApiManager().getLocale();

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

        if (!this.pluginConfig.getProperty(PluginConfig.AUTO_SAVE_TOGGLE)) {
            if (this.plugin.getTimer() != null) this.plugin.getTimer().cancel();
        } else {
            if (this.plugin.getTimer() == null) this.plugin.setTimer(new Timer());

            this.plugin.getTimer().schedule(new AutoSaveTask(), 0, 20 * 60 * 1000);
        }

        String message = this.locale.getProperty(Locale.RELOAD_PLUGIN);

        context.reply(this.placeholderSupport.setPlaceholders(message));
    }
}