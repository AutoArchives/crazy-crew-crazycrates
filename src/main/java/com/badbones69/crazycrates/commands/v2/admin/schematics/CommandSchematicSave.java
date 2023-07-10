package com.badbones69.crazycrates.commands.v2.admin.schematics;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.CommandContext;
import com.badbones69.crazycrates.commands.engine.CommandEngine;
import com.badbones69.crazycrates.commands.engine.reqs.CommandRequirementsBuilder;
import com.badbones69.crazycrates.api.configs.types.Locale;
import com.badbones69.crazycrates.commands.CommandPermissions;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandSchematicSave extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final SettingsManager pluginConfig = plugin.getApiManager().getPluginConfig();
    private final SettingsManager locale = plugin.getApiManager().getLocale();

    public CommandSchematicSave() {
        super();

        addAlias("schem-save");

        this.description = CommandPermissions.ADMIN_SCHEMATIC_SAVE.getDescription();

        this.requirements = new CommandRequirementsBuilder()
                .withRawPermission(CommandPermissions.ADMIN_SCHEMATIC_SAVE.getBuiltPermission())
                .asPlayer(true)
                .build();
    }

    @Override
    protected void perform(CommandContext context) {
        context.reply(this.plugin.getPlaceholderManager().setPlaceholders(this.locale.getProperty(Locale.FEATURE_DISABLED)));
    }
}