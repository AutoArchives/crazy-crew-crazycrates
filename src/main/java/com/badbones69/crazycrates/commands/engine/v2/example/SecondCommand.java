package com.badbones69.crazycrates.commands.engine.v2.example;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.v2.CommandContext;
import com.badbones69.crazycrates.commands.engine.v2.CommandEngine;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecondCommand extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    public SecondCommand() {
        super("second", "second example command", Collections.emptyList());

        //this.requiredArgs.add(new Argument("amount", 0, new IntArgument(50)));
        //this.optionalArgs.add(new Argument("player", 1, new PlayerArgument()));
    }

    @Override
    protected void perform(CommandContext context, String[] args) {
        //int arg = context.getArgAsInt(0, false);
        //Player player = context.getArgAsPlayer(1, false);

        //context.reply("Amount: " + arg);
        //context.reply("Player: " + player.getName());
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        //return this.plugin.getManager().handleTabComplete(args, this);

        if (args.length == 1) {
            ArrayList<String> suggestions = new ArrayList<>();

            for (EntityType type : EntityType.values()) {
                if (type.isSpawnable()) suggestions.add(type.name());
            }

            return suggestions;
        }

        return new ArrayList<>();
    }
}