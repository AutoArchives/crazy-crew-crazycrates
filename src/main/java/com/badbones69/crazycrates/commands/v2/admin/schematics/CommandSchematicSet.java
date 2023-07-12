package com.badbones69.crazycrates.commands.v2.admin.schematics;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.support.InternalPlaceholderSupport;
import com.badbones69.crazycrates.commands.engine.v1.CommandEngine;
import com.badbones69.crazycrates.commands.engine.v2.builders.requirements.CommandRequirementsBuilder;
import com.badbones69.crazycrates.api.enums.Permissions;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandSchematicSet extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final InternalPlaceholderSupport placeholderSupport = this.plugin.getApiManager().getPlaceholderSupport();

    private final SettingsManager pluginConfig = this.plugin.getApiManager().getPluginConfig();
    private final SettingsManager locale = this.plugin.getApiManager().getLocale();

    public CommandSchematicSet() {
        addAlias("schem-set");

        //setCommandEntryData(new CommandDataEntry());

        //getCommandDataEntry().setDescription(Permissions.ADMIN_SCHEMATIC_SET.getDescription());

        this.requirements = new CommandRequirementsBuilder()
                .withRawPermission(Permissions.ADMIN_SCHEMATIC_SET.getBuiltPermission())
                .asPlayer(true)
                .build();
    }

    @Override
    protected void perform() {
        //context.reply(this.placeholderSupport.setPlaceholders(this.locale.getProperty(Locale.FEATURE_DISABLED)));
    }
}