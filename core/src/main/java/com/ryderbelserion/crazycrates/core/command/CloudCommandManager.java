package com.ryderbelserion.crazycrates.core.command;

public class CloudCommandManager implements CloudCommandFlow {

    // TODO() Expand on this class somehow in the future.
    @Override
    public void addCommand(CloudCommandEngine engine) {
        // Actually registers the command.
        engine.registerCommand();
    }
}