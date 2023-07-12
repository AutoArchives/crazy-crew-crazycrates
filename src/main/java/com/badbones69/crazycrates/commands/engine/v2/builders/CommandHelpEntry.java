package com.badbones69.crazycrates.commands.engine.v2.builders;

import com.badbones69.crazycrates.commands.engine.v2.CommandEngine;
import com.badbones69.crazycrates.commands.engine.v2.CommandManager;
import com.ryderbelserion.stick.core.StickLogger;

public class CommandHelpEntry {

    private final CommandManager manager;
    private final CommandEngine engine;

    public CommandHelpEntry(CommandManager manager, CommandEngine engine) {
        this.manager = manager;
        this.engine = engine;
    }

    public void buildHelpMenu(int page, int maxPage) {
        int startPage = maxPage * (page - 1);

        if (page <= 0 || startPage >= this.manager.getCommands().size()) {
            //context.reply(this.pluginConfig.getProperty(PluginConfig.INVALID_HELP_PAGE).replaceAll("\\{page}", String.valueOf(page)));
            return;
        }

        //context.reply(this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_HEADER).replaceAll("\\{page}", String.valueOf(page)));

        for (int current = startPage; current < (startPage + maxPage); current++) {
            if (this.manager.getCommands().size() - 1 < current) continue;

            CommandDataEntry command = this.manager.getCommands().get(this.engine.getLabel());

            if (command.isHidden()) continue;

            StickLogger.info("Command: /" + this.engine.getLabel());
        }
    }
}