package com.badbones69.crazycrates.commands.engine.v2;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.Argument;
import com.ryderbelserion.stick.core.utils.AdventureUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class CommandEngine extends BukkitCommand {

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

            /*for (CommandEngine engine : this.plugin.getManager().getClasses()) {
                boolean isPresent = context.getArgs().stream().findFirst().isPresent();

                if (isPresent) {
                    label.append(" ").append(context.getArgs().get(0));

                    context.removeArgs(0);
                    context.setLabel(label.toString());

                    engine.execute(context);
                    return;
                }
            }*/
        }

        if (!validate(context)) return;

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
            sendValidFormat();
            return false;
        }

        if (context.getArgs().size() > this.requiredArgs.size() + this.optionalArgs.size()) {
            context.reply("Too many args.");
            sendValidFormat();
            return false;
        }

        return true;
    }

    private void sendValidFormat() {
        /*ArrayList<Argument> arguments = new ArrayList<>();

        arguments.addAll(this.requiredArgs);
        arguments.addAll(this.optionalArgs);

        this.requiredArgs.sort(Comparator.comparing(Argument::order));

        //if (context.isPlayer()) {
        //String format = "/" + getPrefix() + context.getAlias();

        //Component component = AdventureUtils.parse(format);
        TextComponent.@NotNull Builder emptyComponent = Component.text();

        StringBuilder types = new StringBuilder();

        for (Argument arg : arguments) {
            //String value = this.optionalArgs.contains(arg) ? " (" + arg.name() + ") " : " <" + arg.name() + ">";

            //String msg = this.optionalArgs.contains(arg) ? this.locale.getProperty(Locale.OPTIONAL_ARGUMENT) : this.locale.getProperty(Locale.REQUIRED_ARGUMENT);

            //Component argComponent = AdventureUtils.parse(value).hoverEvent(HoverEvent.showText(AdventureUtils.parse(this.placeholderSupport.setPlaceholders(msg)))).asComponent();

            //emptyComponent.append(argComponent);

            //boolean isPresent = arg.argumentType().getPossibleValues().stream().findFirst().isPresent();

            //if (isPresent) types.append(" ").append(arg.argumentType().getPossibleValues().stream().findFirst().get());
        }

        //Component finalComponent = component
        //        .hoverEvent(HoverEvent.showText(AdventureUtils.parse("<gold>Click me to insert into chat</gold>")))
        //        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, format + types))
        //        .append(emptyComponent.build());

        //context.reply(finalComponent);

        //    return;
        //}

        //StringBuilder format = new StringBuilder("/" + getPrefix() + context.getAlias());

        for (Argument arg : arguments) {
            String value = this.optionalArgs.contains(arg) ? "(" + arg.name() + ") " : "<" + arg.name() + "> ";

            //format.append(value);
        }

        //context.reply(format.toString());*/
    }

    public List<Argument> getRequiredArgs() {
        return Collections.unmodifiableList(this.requiredArgs);
    }

    public List<Argument> getOptionalArgs() {
        return Collections.unmodifiableList(this.optionalArgs);
    }
}