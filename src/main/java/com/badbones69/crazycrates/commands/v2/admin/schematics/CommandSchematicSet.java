package com.badbones69.crazycrates.commands.v2.admin.schematics;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.CommandContext;
import com.badbones69.crazycrates.commands.engine.CommandEngine;
import com.badbones69.crazycrates.commands.engine.requirements.CommandRequirementsBuilder;
import com.badbones69.crazycrates.api.configs.types.Locale;
import com.badbones69.crazycrates.api.enums.Permissions;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandSchematicSet extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final SettingsManager pluginConfig = plugin.getApiManager().getPluginConfig();
    private final SettingsManager locale = plugin.getApiManager().getLocale();

    public CommandSchematicSet() {
        super();

        addAlias("schem-set");

        setDescription(Permissions.ADMIN_SCHEMATIC_SET.getDescription());

        this.requirements = new CommandRequirementsBuilder()
                .withRawPermission(Permissions.ADMIN_SCHEMATIC_SET.getBuiltPermission())
                .asPlayer(true)
                .build();
    }

    @Override
    protected void perform(CommandContext context) {
        context.reply(this.plugin.getPlaceholderManager().setPlaceholders(this.locale.getProperty(Locale.FEATURE_DISABLED)));
    }
}