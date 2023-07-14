package com.badbones69.crazycrates.commands.engine.v2.builders;

import com.badbones69.crazycrates.commands.engine.v2.CommandEngine;
import com.badbones69.crazycrates.commands.engine.v2.CommandManager;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.Argument;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class CommandHelpEntry {

    private final CommandManager manager;
    private final CommandActor actor;

    private int page = 1;
    private int perPage;
    private int totalPages;
    private int totalResults;
    private boolean lastPage;

    public CommandHelpEntry(CommandManager manager, CommandActor actor) {
        this.manager = manager;
        this.actor = actor;

        this.perPage = manager.defaultHelpPerPage();
    }

    public void showHelp() {
        this.showHelp(this.actor);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    public void showHelp(CommandActor actor) {
        int min = this.perPage * (this.page - 1);
        int max = min + this.perPage;

        this.totalResults = this.manager.getCommands().size();

        this.totalPages = this.totalResults / this.perPage;

        if (min >= this.totalResults) {
            actor.reply("No results found.");
            //context.reply(this.pluginConfig.getProperty(PluginConfig.INVALID_HELP_PAGE).replaceAll("\\{page}", String.valueOf(page)));
            return;
        }

        Map<String, CommandDataEntry> entries = this.manager.getCommands();

        for (int value = min; value < max; value++) {
            if (this.totalResults - 1 < value) continue;

            CommandEngine command = this.manager.getClasses().get(value);

            CommandDataEntry dataEntry = entries.get(command.getLabel());

            //boolean isHidden = entries.get(this.engine.getLabel()).isHidden();

            StringBuilder baseFormat = new StringBuilder("/" + command.getLabel());

            // Only add aliases if the list isn't empty.
            if (!command.getAliases().isEmpty()) baseFormat.append(" ").append(command.getAliases().get(0));

            ArrayList<Argument> arguments = new ArrayList<>();

            arguments.addAll(command.getOptionalArgs());
            arguments.addAll(command.getRequiredArgs());

            arguments.sort(Comparator.comparingInt(Argument::order));

            if (actor.isPlayer()) {
                /*
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

                //context.reply(builder.build());
                 */
            }

            /*String footer = this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_FOOTER);

            if (context.isPlayer()) {
                String text = this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_GO_TO_PAGE);

                if (page > 1) {
                    int number = page-1;

                    //hover(context.getPlayer(), footer.replaceAll("\\{page}", String.valueOf(page)),  text.replaceAll("\\{page}", String.valueOf(number)), this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_BACK), "/crazycrates help " + number, ClickEvent.Action.RUN_COMMAND);
                } else if (page < this.subCommands.size()) {
                    int number = page+1;

                    //hover(context.getPlayer(), footer.replaceAll("\\{page}", String.valueOf(page)),  text.replaceAll("\\{page}", String.valueOf(number)), this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_NEXT), "/crazycrates help " + number, ClickEvent.Action.RUN_COMMAND);
                }
            } else {
                //send(context.getSender(), footer.replaceAll("\\{page}", String.valueOf(page)), false, "");
            }*/
        }

        this.lastPage = max >= this.totalResults;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public void setPage(int page, int perPage) {
        this.setPage(page);
        this.setPerPage(perPage);
    }

    public int getPage() {
        return this.page;
    }

    public int getPerPage() {
        return this.perPage;
    }

    public int getTotalResults() {
        return this.totalResults;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public boolean isLastPage() {
        return this.lastPage;
    }
}