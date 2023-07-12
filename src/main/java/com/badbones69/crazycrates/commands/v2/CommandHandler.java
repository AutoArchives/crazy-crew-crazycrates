package com.badbones69.crazycrates.commands.v2;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.v2.CommandManager;
import com.badbones69.crazycrates.commands.engine.v2.example.FirstCommand;
import com.badbones69.crazycrates.commands.engine.v2.example.SecondCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHandler {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private CommandManager manager;

    public void load() {
        this.manager = CommandManager.create(this.plugin.getName().toLowerCase());

        this.manager.addCommand(new FirstCommand());
        this.manager.addCommand(new SecondCommand());
    }

    public CommandManager getManager() {
        return this.manager;
    }
}