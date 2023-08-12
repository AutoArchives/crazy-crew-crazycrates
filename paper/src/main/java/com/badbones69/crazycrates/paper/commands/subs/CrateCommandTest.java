package com.badbones69.crazycrates.paper.commands.subs;

import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandContext;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandEngine;
import com.ryderbelserion.lexicon.core.builders.commands.args.Argument;
import com.ryderbelserion.lexicon.core.builders.commands.args.types.DoubleArgument;
import com.ryderbelserion.lexicon.core.builders.commands.args.types.IntArgument;
import java.util.Collections;

public class CrateCommandTest extends BukkitCommandEngine {

    public CrateCommandTest() {
        super("test", "test command", "/crazycrates:test", Collections.emptyList());

        addRequiredArgument(new Argument("amount", 0, new IntArgument(15)));
        addOptionalArgument(new Argument("double", 1, new DoubleArgument(15)));
    }

    @Override
    public void perform(BukkitCommandContext context, String[] args) {
        int amount = context.getArgAsInt(0, true, "&cFailed to parse &e{value} &cusing &e{action}");
        double value = context.getArgAsDouble(0, true, "&cFailed to parse &e{value} &cusing &e{action}");

        context.sendLegacyMessage("Amount: " + amount);
        context.sendLegacyMessage("Value: " + value);
    }
}