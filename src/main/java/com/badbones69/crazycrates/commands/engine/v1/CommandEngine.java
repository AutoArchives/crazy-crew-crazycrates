package com.badbones69.crazycrates.commands.engine.v1;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.ApiManager;
import com.badbones69.crazycrates.api.configs.types.Locale;
import com.badbones69.crazycrates.commands.engine.v2.builders.requirements.CommandRequirements;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.Argument;
import com.badbones69.crazycrates.api.support.InternalPlaceholderSupport;
import com.ryderbelserion.stick.core.utils.AdventureUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public CommandRequirements requirements;

    public void execute() {
        //if (!this.requirements.checkRequirements(true, context)) return;

        //if (!this.commandData.get(aliasUsed).isExcludeValidation()) if (!inputValidation(context)) return;

        perform();
    }

    public void addAlias(String alias) {
        this.aliases.add(alias);
    }

    protected abstract void perform();

    private boolean inputValidation() {
        /*if (context.getArgs().size() < this.requiredArgs.size()) {
            context.reply(this.placeholderSupport.setPlaceholders(this.locale.getProperty(Locale.NOT_ENOUGH_ARGS)));
            sendValidFormat(context);
            return false;
        }

        if (context.getArgs().size() > this.requiredArgs.size() + this.optionalArgs.size()) {
            context.reply(this.placeholderSupport.setPlaceholders(this.locale.getProperty(Locale.TOO_MANY_ARGS)));
            sendValidFormat(context);
            return false;
        }*/

        return true;
    }

    private void sendValidFormat() {
        ArrayList<Argument> arguments = new ArrayList<>();

        arguments.addAll(this.requiredArgs);
        arguments.addAll(this.optionalArgs);

        this.requiredArgs.sort(Comparator.comparing(Argument::order));

        //if (context.isPlayer()) {
            //String format = "/" + getPrefix() + context.getAlias();

            //Component component = AdventureUtils.parse(format);
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

        //context.reply(format.toString());
    }
}