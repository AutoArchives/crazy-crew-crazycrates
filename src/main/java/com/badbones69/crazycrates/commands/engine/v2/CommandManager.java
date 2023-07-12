package com.badbones69.crazycrates.commands.engine.v2;

import com.badbones69.crazycrates.commands.engine.v2.annotations.Hidden;
import com.badbones69.crazycrates.commands.engine.v2.builders.CommandDataEntry;
import com.badbones69.crazycrates.commands.engine.v2.builders.CommandHelpEntry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private final HashMap<String, CommandDataEntry> commands = new HashMap<>();

    private CommandHelpEntry entry;
    private static String group;

    public static CommandManager create(String commandGroup) {
        group = commandGroup;

        return new CommandManager();
    }

    public CommandHelpEntry getEntry() {
        return this.entry;
    }

    public void addCommand(CommandEngine engine) {
        // If the label already exists. We return!
        if (hasCommand(engine.getLabel())) return;

        // Create data entry.
        CommandDataEntry entry = new CommandDataEntry();

        // Set visibility if annotation is present.
        entry.setHidden(engine.getClass().isAnnotationPresent(Hidden.class));

        if (entry.isHidden()) return;

        // Add to the hash-map!
        this.commands.put(engine.getLabel(), entry);

        // Create help entry if entry is null.
        if (this.entry == null) this.entry = new CommandHelpEntry(this, engine);

        // Add command to the server map!
        Bukkit.getServer().getCommandMap().register(group, engine);
    }

    public boolean hasCommand(String label) {
        return this.commands.containsKey(label);
    }

    public CommandDataEntry getCommand(String label) {
        if (hasCommand(label)) return this.commands.get(label);

        return null;
    }

    public void removeCommand(String label) {
        if (!hasCommand(label)) return;

        Command value = Bukkit.getServer().getCommandMap().getCommand(label);

        if (value != null) value.unregister(Bukkit.getServer().getCommandMap());

        this.commands.remove(label);
    }

    public Map<String, CommandDataEntry> getCommands() {
        return Collections.unmodifiableMap(this.commands);
    }
}