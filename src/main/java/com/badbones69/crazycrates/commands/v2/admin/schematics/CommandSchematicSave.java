package com.badbones69.crazycrates.commands.v2.admin.schematics;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.configs.types.Locale;
import com.badbones69.crazycrates.commands.CommandPermissions;
import com.ryderbelserion.stick.paper.commands.CommandContext;
import com.ryderbelserion.stick.paper.commands.CommandEngine;
import com.ryderbelserion.stick.paper.commands.reqs.CommandRequirementsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandSchematicSave extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

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
        context.reply(this.plugin.getApiManager().getLocale().getProperty(Locale.FEATURE_DISABLED));
    }
}