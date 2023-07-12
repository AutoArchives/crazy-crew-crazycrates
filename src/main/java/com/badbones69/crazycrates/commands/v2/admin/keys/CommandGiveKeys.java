package com.badbones69.crazycrates.commands.v2.admin.keys;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.v1.CommandContext;
import com.badbones69.crazycrates.commands.engine.v1.CommandEngine;
import com.badbones69.crazycrates.commands.engine.v2.builders.requirements.CommandRequirementsBuilder;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.Argument;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.builder.IntArgument;
import com.badbones69.crazycrates.api.enums.Permissions;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.builder.custom.CrateArgument;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.builder.custom.KeyArgument;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.builder.custom.PlayerArgument;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandGiveKeys extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final SettingsManager pluginConfig = this.plugin.getApiManager().getPluginConfig();
    private final SettingsManager locale = this.plugin.getApiManager().getLocale();

    public CommandGiveKeys() {
        addAlias("give");

        //setCommandEntryData(new CommandDataEntry());

        //getCommandDataEntry().setDescription(Permissions.ADMIN_GIVE_KEY.getDescription());

        this.requiredArgs.add(new Argument("key-type", 0, new KeyArgument()));
        this.requiredArgs.add(new Argument("crate-name", 1, new CrateArgument()));
        this.requiredArgs.add(new Argument("amount", 2, new IntArgument(20)));
        this.requiredArgs.add(new Argument("player", 3, new PlayerArgument()));

        this.requirements = new CommandRequirementsBuilder()
                .withRawPermission(Permissions.ADMIN_GIVE_KEY.getBuiltPermission())
                .asPlayer(false)
                .build();
    }

    @Override
    protected void perform(CommandContext context) {

    }
}