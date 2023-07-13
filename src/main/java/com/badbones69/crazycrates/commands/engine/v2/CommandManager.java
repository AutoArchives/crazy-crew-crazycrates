package com.badbones69.crazycrates.commands.engine.v2;

import com.badbones69.crazycrates.commands.engine.v2.annotations.Hidden;
import com.badbones69.crazycrates.commands.engine.v2.builders.CommandActor;
import com.badbones69.crazycrates.commands.engine.v2.builders.CommandDataEntry;
import com.badbones69.crazycrates.commands.engine.v2.builders.CommandHelpEntry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommandManager implements CommandFlow {

    protected final HashMap<String, CommandDataEntry> commands = new HashMap<>();

    protected final LinkedList<CommandEngine> classes = new LinkedList<>();

    protected int defaultHelpPerPage = 10;

    protected static String group;

    public static CommandManager create(String commandGroup) {
        group = commandGroup;

        return new CommandManager();
    }

    @Override
    public void addCommand(CommandEngine engine) {
        // If the label already exists. We return!
        if (hasCommand(engine.getLabel())) return;

        // Create data entry.
        CommandDataEntry entry = new CommandDataEntry();

        // Set visibility if annotation is present.
        entry.setHidden(engine.getClass().isAnnotationPresent(Hidden.class));

        if (entry.isHidden()) return;

        // Add to the hash-map & linked list!
        this.commands.put(engine.getLabel(), entry);
        this.classes.add(engine);

        // Add command to the server map!
        Bukkit.getServer().getCommandMap().register(group, engine);
    }

    @Override
    public boolean hasCommand(String label) {
        return this.commands.containsKey(label);
    }

    @Override
    public CommandHelpEntry generateCommandHelp(CommandActor actor) {
        return new CommandHelpEntry(this, actor);
    }

    @Override
    public int defaultHelpPerPage() {
        return this.defaultHelpPerPage;
    }

    @Override
    public void updateHelpPerPage(int newAmount) {
        this.defaultHelpPerPage = newAmount;
    }

    @Override
    public CommandDataEntry getCommand(String label) {
        if (hasCommand(label)) return this.commands.get(label);

        return null;
    }

    @Override
    public void removeCommand(String label) {
        if (!hasCommand(label)) return;

        Command value = Bukkit.getServer().getCommandMap().getCommand(label);

        if (value != null && value.isRegistered()) value.unregister(Bukkit.getServer().getCommandMap());

        this.commands.remove(label);
    }

    @Override
    public Map<String, CommandDataEntry> getCommands() {
        return Collections.unmodifiableMap(this.commands);
    }

    public List<CommandEngine> getClasses() {
        return Collections.unmodifiableList(this.classes);
    }
}