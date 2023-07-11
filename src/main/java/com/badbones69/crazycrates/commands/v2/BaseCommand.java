package com.badbones69.crazycrates.commands.v2;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.CommandContext;
import com.badbones69.crazycrates.commands.engine.CommandEngine;
import com.badbones69.crazycrates.commands.engine.reqs.CommandRequirementsBuilder;
import com.badbones69.crazycrates.api.configs.types.PluginConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class BaseCommand extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final SettingsManager config = plugin.getApiManager().getPluginConfig();

    public BaseCommand() {
        super();

        this.prefix = "crazycrates";

        this.ignoreInput = true;

        this.requirements = new CommandRequirementsBuilder()
                .asPlayer(false)
                .build();
    }

    @Override
    protected void perform(CommandContext context) {
        getCommandHelp().generateHelp(1, this.pluginConfig.getProperty(PluginConfig.MAX_HELP_PAGE_ENTRIES), context);
    }
}