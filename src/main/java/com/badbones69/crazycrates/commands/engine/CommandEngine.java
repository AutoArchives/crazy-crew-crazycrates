package com.badbones69.crazycrates.commands.engine;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.ApiManager;
import com.badbones69.crazycrates.commands.engine.requirements.CommandRequirements;
import com.badbones69.crazycrates.commands.engine.sender.CommandData;
import com.badbones69.crazycrates.commands.engine.sender.args.Argument;
import com.badbones69.crazycrates.api.configs.types.Locale;
import com.badbones69.crazycrates.api.support.InternalPlaceholderSupport;
import com.ryderbelserion.stick.core.utils.AdventureUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final ApiManager apiManager = this.plugin.getApiManager();
    private final SettingsManager locale = this.apiManager.getLocale();
    private final InternalPlaceholderSupport placeholderSupport = this.apiManager.getPlaceholderSupport();

    private final LinkedList<String> aliases = new LinkedList<>();

    public final LinkedList<Argument> requiredArgs = new LinkedList<>();
    public final LinkedList<Argument> optionalArgs = new LinkedList<>();

    private final HashMap<String, CommandData> commandData = new HashMap<>();

    private final LinkedList<CommandEngine> subCommands = new LinkedList<>();

    public CommandRequirements requirements;
    private CommandHelpEntry commandHelpEntry;

    private boolean excludeInput = false;

    public void setExclude(boolean excludeInput) {
        this.excludeInput = excludeInput;
    }

    private String prefix;

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }

    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public void execute(CommandContext context) {
        String aliasUsed = context.getAlias();

        if (!context.getArgs().isEmpty()) {
            for (CommandEngine command : this.subCommands) {
                boolean exists = context.getArgs().stream().findFirst().isPresent();

                if (exists) {
                    String value = context.getArgs().stream().findFirst().get();

                    if (command.aliases.contains(value)) {
                        aliasUsed += " " + context.getArgs().get(0);

                        context.removeArgs(0);
                        context.setAlias(aliasUsed);
                        command.execute(context);
                        return;
                    }
                }
            }
        }

        if (!this.requirements.checkRequirements(true, context)) return;

        if (!this.excludeInput) {
            if (!inputValidation(context)) return;
        }

        perform(context);
    }

    public void addAlias(String alias) {
        this.aliases.add(alias);
    }

    public void removeAlias(String alias) {
        this.aliases.remove(alias);
    }

    public void addSubCommand(CommandEngine engine) {
        String alias = engine.aliases.getFirst();

        this.subCommands.add(engine);
        this.commandData.put(alias, new CommandData(engine.description));

        if (this.commandHelpEntry == null) this.commandHelpEntry = new CommandHelpEntry(this.apiManager, this.subCommands);

        if (!engine.prefix.isEmpty() || !engine.prefix.isBlank()) engine.prefix = this.prefix;

        engine.excludeInput = this.excludeInput;
    }

    public CommandHelpEntry getCommandHelp() {
        return this.commandHelpEntry;
    }

    public void removeSubCommand(CommandEngine engine) {
        this.subCommands.forEach(command -> {
            if (command.aliases.getFirst().equals(engine.aliases.getFirst())) {
                this.subCommands.remove(engine);
            }
        });
    }

    protected abstract void perform(CommandContext context);

    private boolean inputValidation(CommandContext context) {
        if (context.getArgs().size() < this.requiredArgs.size()) {
            context.reply(this.placeholderSupport.setPlaceholders(this.locale.getProperty(Locale.NOT_ENOUGH_ARGS)));
            sendValidFormat(context);
            return false;
        }

        if (context.getArgs().size() > this.requiredArgs.size() + this.optionalArgs.size()) {
            context.reply(this.placeholderSupport.setPlaceholders(this.locale.getProperty(Locale.TOO_MANY_ARGS)));
            sendValidFormat(context);
            return false;
        }

        return true;
    }

    private void sendValidFormat(CommandContext context) {
        ArrayList<Argument> arguments = new ArrayList<>();

        arguments.addAll(this.requiredArgs);
        arguments.addAll(this.optionalArgs);

        this.requiredArgs.sort(Comparator.comparing(Argument::order));

        if (context.isPlayer()) {
            String format = "/" + this.prefix + context.getAlias();

            Component component = AdventureUtils.parse(format);
            TextComponent.@NotNull Builder emptyComponent = Component.text();

            StringBuilder types = new StringBuilder();

            for (Argument arg : arguments) {
                String value = this.optionalArgs.contains(arg) ? " (" + arg.name() + ") " : " <" + arg.name() + ">";

                String msg = this.optionalArgs.contains(arg) ? this.locale.getProperty(Locale.OPTIONAL_ARGUMENT) : this.locale.getProperty(Locale.REQUIRED_ARGUMENT);

                Component argComponent = AdventureUtils.parse(value).hoverEvent(HoverEvent.showText(AdventureUtils.parse(this.placeholderSupport.setPlaceholders(msg)))).asComponent();

                emptyComponent.append(argComponent);

                boolean isPresent = arg.argumentType().getPossibleValues().stream().findFirst().isPresent();

                if (isPresent) types.append(" ").append(arg.argumentType().getPossibleValues().stream().findFirst().get());
            }

            Component finalComponent = component
                    .hoverEvent(HoverEvent.showText(AdventureUtils.parse("<gold>Click me to insert into chat</gold>")))
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, format + types))
                    .append(emptyComponent.build());

            context.reply(finalComponent);

            return;
        }

        StringBuilder format = new StringBuilder("/" + this.prefix + context.getAlias());

        for (Argument arg : arguments) {
            String value = this.optionalArgs.contains(arg) ? "(" + arg.name() + ") " : "<" + arg.name() + "> ";

            format.append(value);
        }

        context.reply(format.toString());
    }

    public List<String> handleTabComplete(String[] args) {
        List<String> argArray = Arrays.asList(args);

        if (argArray.size() == 1) {
            List<String> completions = new ArrayList<>();

            if (argArray.get(0).isEmpty()) {
                this.subCommands.forEach(value -> completions.add(value.aliases.get(0)));
            } else {
                for (CommandEngine subCommand : this.subCommands) {
                    for (String alias : subCommand.aliases) {
                        if (alias.toLowerCase().startsWith(argArray.get(0))) {
                            completions.add(alias);
                        }
                    }
                }
            }

            return completions;
        }

        if (argArray.size() >= 2) {
            int relativeIndex = 2;
            int commandIndex = 0;

            CommandEngine commandTab = this;

            while (!this.subCommands.isEmpty()) {
                CommandEngine foundCommand = null;

                for (CommandEngine subCommand : subCommands) {
                    if (subCommand.aliases.contains(argArray.get(commandIndex).toLowerCase())) {
                        foundCommand = subCommand;
                    }
                }

                commandIndex++;
                if (foundCommand != null) commandTab = foundCommand; else break;
                relativeIndex++;
            }

            int argToComplete = argArray.size() + 1 - relativeIndex;
            if (commandTab.requiredArgs.size() >= argToComplete) {
                ArrayList<Argument> arguments = new ArrayList<>();

                arguments.addAll(commandTab.requiredArgs);
                arguments.addAll(commandTab.optionalArgs);

                ArrayList<String> possibleValues = new ArrayList<>();

                for (Argument argument : arguments) {
                    if (argument.order() == argToComplete) {
                        List<String> possibleValuesArgs = argument.argumentType().getPossibleValues();

                        possibleValues = new ArrayList<>(possibleValuesArgs);
                        break;
                    }
                }

                if (!commandTab.subCommands.isEmpty()) {
                    for (CommandEngine value : commandTab.subCommands) {
                        for (String alias : value.aliases) {
                            if (alias.toLowerCase().startsWith(argArray.get(argToComplete + 1))) {
                                possibleValues.add(alias);
                            }
                        }
                    }
                }

                return possibleValues;
            }
        }

        return Collections.emptyList();
    }

    public List<String> getAliases() {
        return Collections.unmodifiableList(this.aliases);
    }

    public List<CommandEngine> getSubCommands() {
        return Collections.unmodifiableList(this.subCommands);
    }
}