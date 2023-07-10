package com.badbones69.crazycrates.commands.v2.admin.schematics;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.configs.types.Locale;
import com.badbones69.crazycrates.api.configs.types.PluginConfig;
import com.badbones69.crazycrates.commands.CommandPermissions;
import com.ryderbelserion.stick.paper.commands.CommandContext;
import com.ryderbelserion.stick.paper.commands.CommandEngine;
import com.ryderbelserion.stick.paper.commands.reqs.CommandRequirementsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandSchematicSet extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final SettingsManager pluginConfig = plugin.getApiManager().getPluginConfig();
    private final SettingsManager locale = plugin.getApiManager().getLocale();

    public CommandSchematicSet() {
        super();

        addAlias("schem-set");

        this.description = CommandPermissions.ADMIN_SCHEMATIC_SET.getDescription();

        this.requirements = new CommandRequirementsBuilder()
                .withRawPermission(CommandPermissions.ADMIN_SCHEMATIC_SET.getBuiltPermission())
                .asPlayer(true)
                .build();
    }

    @Override
    protected void perform(CommandContext context) {
        context.reply(this.plugin.getPlaceholderManager().setPlaceholders(this.locale.getProperty(Locale.FEATURE_DISABLED)));
    }
}