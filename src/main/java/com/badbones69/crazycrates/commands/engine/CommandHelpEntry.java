package com.badbones69.crazycrates.commands.engine;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.api.ApiManager;
import com.badbones69.crazycrates.api.configs.types.PluginConfig;
import com.badbones69.crazycrates.api.support.InternalPlaceholderSupport;
import com.badbones69.crazycrates.commands.engine.builder.ComponentBuilder;
import com.badbones69.crazycrates.commands.engine.sender.CommandData;
import com.badbones69.crazycrates.commands.engine.sender.args.Argument;
import net.kyori.adventure.text.event.ClickEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import static com.ryderbelserion.stick.core.utils.AdventureUtils.hover;
import static com.ryderbelserion.stick.core.utils.AdventureUtils.send;

public class CommandHelpEntry {

    private final SettingsManager pluginConfig;
    private final InternalPlaceholderSupport placeholderSupport;

    private final LinkedList<CommandEngine> subCommands;

    private final HashMap<String, CommandData> commandData = new HashMap<>();

    public CommandHelpEntry(ApiManager apiManager, LinkedList<CommandEngine> subCommands) {
        this.pluginConfig = apiManager.getPluginConfig();
        this.placeholderSupport = apiManager.getPlaceholderSupport();

        this.subCommands = subCommands;
    }

    public void generateHelp(int page, int maxPage, CommandContext context) {
        int startPage = maxPage * (page - 1);

        if (page <= 0 || startPage >= this.subCommands.size()) {
            context.reply(this.pluginConfig.getProperty(PluginConfig.INVALID_HELP_PAGE).replaceAll("\\{page}", String.valueOf(page)));
            return;
        }

        context.reply(this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_HEADER).replaceAll("\\{page}", String.valueOf(page)));

        for (int i = startPage; i < (startPage + maxPage); i++) {
            if (this.subCommands.size() - 1 < i) continue;

            CommandEngine command = this.subCommands.get(i);

            CommandData data = this.getCommand(command.getAliases().get(0));

            if (data.isVisible()) continue;

            StringBuilder base = new StringBuilder("/" + command.getPrefix() + " " + command.getAliases().get(0));

            String format = this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_FORMAT)
                    .replaceAll("\\{command}", base.toString())
                    .replaceAll("\\{description}", data.getDescription());

            ArrayList<Argument> arguments = new ArrayList<>();

            arguments.addAll(command.optionalArgs);
            arguments.addAll(command.requiredArgs);

            arguments.sort(Comparator.comparingInt(Argument::order));

            if (context.isPlayer()) {
                StringBuilder types = new StringBuilder();

                ComponentBuilder builder = new ComponentBuilder();

                for (Argument arg : arguments) {
                    String value = command.optionalArgs.contains(arg) ? " (" + arg.name() + ") " : " <" + arg.name() + ">";

                    types.append(value);
                }

                builder.setMessage(format.replaceAll("\\{args}", String.valueOf(types)));

                String hoverShit = base.append(types).toString();

                String hoverFormat = this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_HOVER_FORMAT);

                builder.hover(this.placeholderSupport.setPlaceholders(hoverFormat).replaceAll("\\{commands}", hoverShit)).click(hoverShit, ClickEvent.Action.valueOf(this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_HOVER_ACTION).toUpperCase()));

                context.reply(builder.build());
            }
        }

        String footer = this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_FOOTER);

        if (context.isPlayer()) {
            String text = this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_GO_TO_PAGE);

            if (page > 1) {
                int number = page-1;

                hover(context.getPlayer(), footer.replaceAll("\\{page}", String.valueOf(page)),  text.replaceAll("\\{page}", String.valueOf(number)), this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_BACK), "/crazycrates help " + number, ClickEvent.Action.RUN_COMMAND);
            } else if (page < this.subCommands.size()) {
                int number = page+1;

                hover(context.getPlayer(), footer.replaceAll("\\{page}", String.valueOf(page)),  text.replaceAll("\\{page}", String.valueOf(number)), this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_NEXT), "/crazycrates help " + number, ClickEvent.Action.RUN_COMMAND);
            }
        } else {
            send(context.getSender(), footer.replaceAll("\\{page}", String.valueOf(page)), false, "");
        }
    }

    public boolean hasCommand(String command) {
        return this.commandData.containsKey(command);
    }

    public CommandData getCommand(String command) {
        if (hasCommand(command)) return this.commandData.get(command);

        return null;
    }

    public boolean isVisible(String command) {
        if (hasCommand(command)) {
            CommandData data = getCommand(command);

            return data.isVisible();
        }

        return false;
    }

    public void setVisible(String command) {
        if (hasCommand(command)) {
            CommandData data = getCommand(command);

            data.setVisible(!isVisible(command));
        }
    }

    public Map<String, CommandData> getCommandData() {
        return Collections.unmodifiableMap(this.commandData);
    }
}