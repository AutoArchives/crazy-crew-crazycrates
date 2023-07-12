package com.badbones69.crazycrates.commands.v2.admin.schematics;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.support.InternalPlaceholderSupport;
import com.badbones69.crazycrates.commands.engine.v1.CommandContext;
import com.badbones69.crazycrates.commands.engine.v1.CommandEngine;
import com.badbones69.crazycrates.commands.engine.v2.builders.requirements.CommandRequirementsBuilder;
import com.badbones69.crazycrates.api.configs.types.Locale;
import com.badbones69.crazycrates.api.enums.Permissions;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandSchematicSave extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final InternalPlaceholderSupport placeholderSupport = this.plugin.getApiManager().getPlaceholderSupport();

    private final SettingsManager pluginConfig = this.plugin.getApiManager().getPluginConfig();
    private final SettingsManager locale = this.plugin.getApiManager().getLocale();

    public CommandSchematicSave() {
        addAlias("schem-save");

        //setCommandEntryData(new CommandDataEntry());

        //getCommandDataEntry().setDescription(Permissions.ADMIN_SCHEMATIC_SAVE.getDescription());

        this.requirements = new CommandRequirementsBuilder()
                .withRawPermission(Permissions.ADMIN_SCHEMATIC_SAVE.getBuiltPermission())
                .asPlayer(true)
                .build();
    }

    @Override
    protected void perform(CommandContext context) {
        //context.reply(this.placeholderSupport.setPlaceholders(this.locale.getProperty(Locale.FEATURE_DISABLED)));
    }
}