package com.badbones69.crazycrates.commands.engine.v2;

import com.badbones69.crazycrates.commands.engine.v2.annotations.Hidden;
import com.badbones69.crazycrates.commands.engine.v2.builders.CommandActor;
import com.badbones69.crazycrates.commands.engine.v2.builders.CommandDataEntry;
import com.badbones69.crazycrates.commands.engine.v2.builders.CommandHelpEntry;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.Argument;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommandManager implements CommandFlow {

    private final HashMap<String, CommandDataEntry> commands = new HashMap<>();

    private final LinkedList<CommandEngine> classes = new LinkedList<>();

    private int defaultHelpPerPage = 10;

    private static String group;

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

    public void updatePermissionMessage(String message) {
        //permissionMessage(AdventureUtils.parse(message));
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

    @Override
    public List<CommandEngine> getClasses() {
        return Collections.unmodifiableList(this.classes);
    }

    @Override
    public List<String> handleTabComplete(String[] args, CommandEngine engine) {
        List<String> completions = Arrays.asList(args);

        if (completions.size() >= 1) {
            int relativeIndex = this.classes.size();
            int argToComplete = completions.size() + 1 - relativeIndex;
            if (engine.requiredArgs.size() >= argToComplete) {
                ArrayList<Argument> arguments = new ArrayList<>();

                arguments.addAll(engine.requiredArgs);
                arguments.addAll(engine.optionalArgs);

                ArrayList<String> possibleValues = new ArrayList<>();

                for (Argument argument : arguments) {
                    if (argument.order() == argToComplete) {
                        List<String> possibleValuesArgs = argument.argumentType().getPossibleValues();

                        possibleValues = new ArrayList<>(possibleValuesArgs);
                        break;
                    }
                }

                return possibleValues;
            }
        }

        return Collections.emptyList();
    }
}