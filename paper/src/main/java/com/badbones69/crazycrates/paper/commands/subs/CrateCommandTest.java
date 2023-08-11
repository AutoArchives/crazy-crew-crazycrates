package com.badbones69.crazycrates.paper.commands.subs;

import com.badbones69.crazycrates.paper.CrazyCrates;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandContext;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandEngine;
import com.ryderbelserion.lexicon.core.builders.commands.args.Argument;
import com.ryderbelserion.lexicon.core.builders.commands.args.types.IntArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CrateCommandTest extends BukkitCommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    public CrateCommandTest() {
        super("test", "test command", "/crazycrates:test", Collections.emptyList());

        addRequiredArgument(new Argument("amount", 0, new IntArgument(5)));
    }

    @Override
    public void perform(BukkitCommandContext context, String[] args) {
        context.sendLegacyMessage(String.valueOf(context.getArgAsInt(0, true, "Invalid Args: {value} {action}")));
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        List<String> completions = Arrays.asList(args);

        if (!completions.isEmpty()) {
            int relativeIndex = this.plugin.getBukkit().getManager().getCommands().size();
            int argToComplete = completions.size() + 1 - relativeIndex;
            if (getRequiredArgs().size() >= argToComplete) {
                ArrayList<Argument> arguments = new ArrayList<>();

                arguments.addAll(getRequiredArgs());
                arguments.addAll(getOptionalArgs());

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