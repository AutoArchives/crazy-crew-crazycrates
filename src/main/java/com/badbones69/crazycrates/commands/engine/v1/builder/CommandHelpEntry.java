package com.badbones69.crazycrates.commands.engine.v1.builder;

import com.badbones69.crazycrates.commands.engine.v2.CommandFlow;
import com.badbones69.crazycrates.commands.engine.v2.CommandManager;

public class CommandHelpEntry {

    private final CommandManager commandManager;

    private int page = 1;
    private int perPage;

    public CommandHelpEntry(CommandManager manager, CommandFlow flow) {
        this.commandManager = manager;

        this.perPage = manager.defaultHelpPerPage();
    }

    public void showHelp() {
        this.generateHelp();
    }

    private void generateHelp() {
        /*int startPage = maxPage * (page - 1);

        for (int i = startPage; i < (startPage + maxPage); i++) {
            if (this.subCommands.size() - 1 < i) continue;

            CommandEngine command = this.subCommands.get(i);

            CommandDataEntry data = getCommand(command.getAliases().get(0));

            if (data.isHidden()) continue;

            StringBuilder base = new StringBuilder("/" + command.getPrefix() + " " + command.getAliases().get(0));

            String format = this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_FORMAT)
                    .replaceAll("\\{command}", base.toString())
                    .replaceAll("\\{description}", "Description.");

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

                //context.reply(builder.build());
            }
        }

        String footer = this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_FOOTER);

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

    /*
    public boolean hasCommand(String command) {
        return this.commandData.containsKey(command);
    }

    public CommandDataEntry getCommand(String command) {
        if (hasCommand(command)) return this.commandData.get(command);

        return null;
    }

    public Map<String, CommandDataEntry> getCommandData() {
        return Collections.unmodifiableMap(this.commandData);
    }*/
}