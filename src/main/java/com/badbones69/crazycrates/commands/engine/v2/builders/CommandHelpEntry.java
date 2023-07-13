package com.badbones69.crazycrates.commands.engine.v2.builders;

import com.badbones69.crazycrates.commands.engine.v2.CommandEngine;
import com.badbones69.crazycrates.commands.engine.v2.CommandManager;

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

            actor.reply("/" + command.getLabel());
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