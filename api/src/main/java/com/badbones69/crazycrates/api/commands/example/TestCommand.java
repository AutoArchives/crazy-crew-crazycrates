package com.badbones69.crazycrates.api.commands.example;

import com.badbones69.crazycrates.api.commands.CommandEngine;
import com.badbones69.crazycrates.api.commands.CommandInfo;
import com.badbones69.crazycrates.api.commands.reqs.CommandRequirements;
import com.badbones69.crazycrates.api.commands.reqs.CommandRequirementsBuilder;

public class TestCommand extends CommandEngine {

    public TestCommand() {
        addAlias("test");

        CommandRequirements builder = new CommandRequirementsBuilder()
                .asPlayer(false)
                .withRawPermission("test.permission")
                .build();

        setRequirements(builder);
    }

    @Override
    protected boolean execute(CommandInfo info) {
        info.reply("<red>Guten Tag!</red>");

        return true;
    }
}