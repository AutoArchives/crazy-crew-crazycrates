package com.badbones69.crazycrates.paper.api.frame.command;

public class CommandManager implements CommandFlow {

    @Override
    public void addCommand(CommandEngine engine) {
        engine.registerCommand();
    }
}