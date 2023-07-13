package com.badbones69.crazycrates.commands.engine.v2;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.Argument;
import com.ryderbelserion.stick.core.StickLogger;
import com.ryderbelserion.stick.core.utils.AdventureUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class CommandEngine extends Command {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final LinkedList<Argument> requiredArgs = new LinkedList<>();
    private final LinkedList<Argument> optionalArgs = new LinkedList<>();

    // TODO() Add a usage message /crazycrates help <page>
    protected CommandEngine(@NotNull String name, @NotNull String description, List<String> aliases) {
        super(name, description, "", aliases);
    }

    public void execute(CommandContext context) {
        perform(context);
    }

    protected abstract void perform(CommandContext context);

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        CommandContext context = new CommandContext(
                sender,
                new ArrayList<>(Arrays.asList(args))
        );

        execute(context);

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        List<String> arrays = Arrays.asList(args);

        arrays.forEach(StickLogger::warn);

        return arrays;
    }

    public void updatePermissionMessage(String message) {
        permissionMessage(AdventureUtils.parse(message));
    }

    private boolean validate(CommandContext context) {
        if (context.getArgs().size() < this.requiredArgs.size()) {
            context.reply("Not enough args.");
            return false;
        }

        if (context.getArgs().size() > this.requiredArgs.size() + this.optionalArgs.size()) {
            context.reply("Too many args.");
            return false;
        }

        return true;
    }

    public List<Argument> getRequiredArgs() {
        return Collections.unmodifiableList(this.requiredArgs);
    }

    public List<Argument> getOptionalArgs() {
        return Collections.unmodifiableList(this.optionalArgs);
    }
}