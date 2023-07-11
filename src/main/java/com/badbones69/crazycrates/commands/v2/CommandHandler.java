package com.badbones69.crazycrates.commands.v2;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.v2.admin.CommandAdmin;
import com.badbones69.crazycrates.commands.v2.admin.CommandHelp;
import com.badbones69.crazycrates.commands.v2.admin.CommandReload;
import com.badbones69.crazycrates.commands.v2.admin.keys.CommandAddKeys;
import com.badbones69.crazycrates.commands.v2.admin.schematics.CommandSchematicSave;
import com.badbones69.crazycrates.commands.v2.admin.schematics.CommandSchematicSet;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHandler {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    public void load() {
        CrateBaseCommand crateBaseCommand = new CrateBaseCommand();

        KeyBaseCommand keyBaseCommand = new KeyBaseCommand();

        crateBaseCommand.addSubCommand(new CommandHelp());

        // Admin Commands.
        crateBaseCommand.addSubCommand(new CommandAdmin());
        crateBaseCommand.addSubCommand(new CommandReload());

        crateBaseCommand.addSubCommand(new CommandSchematicSave());
        crateBaseCommand.addSubCommand(new CommandSchematicSet());

        crateBaseCommand.addSubCommand(new CommandAddKeys());
    }
}