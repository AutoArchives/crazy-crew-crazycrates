package com.badbones69.crazycrates.commands.v2.admin.keys;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.CommandContext;
import com.badbones69.crazycrates.commands.engine.CommandEngine;
import com.badbones69.crazycrates.commands.engine.reqs.CommandRequirementsBuilder;
import com.badbones69.crazycrates.commands.engine.sender.args.Argument;
import com.badbones69.crazycrates.commands.engine.sender.args.builder.IntArgument;
import com.badbones69.crazycrates.commands.CommandPermissions;
import com.badbones69.crazycrates.commands.v2.args.CrateArgument;
import com.badbones69.crazycrates.commands.v2.args.KeyArgument;
import com.badbones69.crazycrates.commands.v2.args.PlayerArgument;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandAddKeys extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final SettingsManager pluginConfig = plugin.getApiManager().getPluginConfig();
    private final SettingsManager locale = plugin.getApiManager().getLocale();

    public CommandAddKeys() {
        super();

        addAlias("give");

        this.requiredArgs.add(new Argument("key-type", 0, new KeyArgument()));
        this.requiredArgs.add(new Argument("crate-name", 1, new CrateArgument()));
        this.requiredArgs.add(new Argument("amount", 2, new IntArgument(20)));
        this.requiredArgs.add(new Argument("player", 3, new PlayerArgument()));

        this.description = CommandPermissions.ADMIN_GIVE_KEY.getDescription();

        this.requirements = new CommandRequirementsBuilder()
                .withRawPermission(CommandPermissions.ADMIN_GIVE_KEY.getBuiltPermission())
                .asPlayer(false)
                .build();
    }

    @Override
    protected void perform(CommandContext context) {

    }
}