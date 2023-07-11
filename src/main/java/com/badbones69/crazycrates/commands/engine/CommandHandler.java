package com.badbones69.crazycrates.commands.engine;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.v2.BaseCommand;
import com.badbones69.crazycrates.commands.v2.KeyBaseCommand;
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
        BaseCommand baseCommand = new BaseCommand();

        KeyBaseCommand keyBaseCommand = new KeyBaseCommand();

        baseCommand.addSubCommand(new CommandHelp(baseCommand));

        // Admin Commands.
        baseCommand.addSubCommand(new CommandAdmin());
        baseCommand.addSubCommand(new CommandReload());

        baseCommand.addSubCommand(new CommandSchematicSave());
        baseCommand.addSubCommand(new CommandSchematicSet());

        baseCommand.addSubCommand(new CommandAddKeys());
    }
}