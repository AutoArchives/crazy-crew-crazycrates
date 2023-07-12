package com.badbones69.crazycrates.commands.v2;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.utils.MiscUtils;
import com.badbones69.crazycrates.commands.engine.v1.CommandEngine;
import com.badbones69.crazycrates.commands.engine.v2.builders.requirements.CommandRequirementsBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;

public class CrateBaseCommand extends CommandEngine implements TabCompleter {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final SettingsManager config = this.plugin.getApiManager().getPluginConfig();
    private final SettingsManager pluginConfig = this.plugin.getApiManager().getPluginConfig();

    public CrateBaseCommand() {
        setPrefix("crazycrates");

        //setCommandEntryData(new CommandDataEntry());

        //getCommandDataEntry().setExcludeValidation(true);

        this.requirements = new CommandRequirementsBuilder()
                .asPlayer(false)
                .build();
    }

    @Override
    protected void perform() {
        //getCommandHelp().generateHelp(1, this.pluginConfig.getProperty(PluginConfig.MAX_HELP_PAGE_ENTRIES));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (this.requirements.isPlayer() && !MiscUtils.hasPermission((HumanEntity) sender, this.requirements.getPermission())) return Collections.emptyList();

        return handleTabComplete(args);
    }
}