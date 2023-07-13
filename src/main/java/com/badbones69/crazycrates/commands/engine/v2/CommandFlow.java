package com.badbones69.crazycrates.commands.engine.v2;

import com.badbones69.crazycrates.commands.engine.v2.builders.CommandActor;
import com.badbones69.crazycrates.commands.engine.v2.builders.CommandDataEntry;
import com.badbones69.crazycrates.commands.engine.v2.builders.CommandHelpEntry;
import java.util.Map;

public interface CommandFlow {

    void addCommand(CommandEngine engine);

    boolean hasCommand(String label);

    CommandHelpEntry generateCommandHelp(CommandActor actor);

    int defaultHelpPerPage();

    void updateHelpPerPage(int newAmount);

    CommandDataEntry getCommand(String label);

    void removeCommand(String label);

    Map<String, CommandDataEntry> getCommands();
}