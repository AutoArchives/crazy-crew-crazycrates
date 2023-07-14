package com.badbones69.crazycrates.commands.engine.v2;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.Argument;
import com.ryderbelserion.stick.core.utils.AdventureUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class CommandEngine extends Command {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    public final LinkedList<Argument> requiredArgs = new LinkedList<>();
    public final LinkedList<Argument> optionalArgs = new LinkedList<>();

    // TODO() Add a usage message /crazycrates help <page>
    protected CommandEngine(@NotNull String name, @NotNull String description, List<String> aliases) {
        super(name, description, "", aliases);
    }

    public void execute(CommandContext context, String[] args) {
        perform(context, args);
    }

    public void execute(CommandContext context) {
        StringBuilder label = new StringBuilder(getLabel());

        if (!context.getArgs().isEmpty()) {

            for (CommandEngine engine : this.plugin.getManager().getClasses()) {
                boolean isPresent = context.getArgs().stream().findFirst().isPresent();

                if (isPresent) {
                    label.append(" ").append(context.getArgs().get(0));

                    context.removeArgs(0);
                    context.setLabel(label.toString());

                    engine.execute(context);
                    return;
                }
            }
        }

        //if (this.plugin.getManager().getCommand(getLabel()).isHidden()) return;

        perform(context, new String[0]);
    }

    protected abstract void perform(CommandContext context, String[] args);

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        List<String> arguments = Arrays.asList(args);

        CommandContext context = new CommandContext(
                sender,
                arguments
        );

        if (arguments.isEmpty()) {
            execute(context);
            return true;
        }

        execute(context, args);

        return true;
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