package com.badbones69.crazycrates.paper.commands.provider;

import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandHelpEntry;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandManager;
import com.ryderbelserion.lexicon.core.builders.commands.interfaces.CommandActor;
import com.ryderbelserion.lexicon.core.builders.provider.command.CommandProvider;

public class BukkitCommandProvider implements CommandProvider {

    private int defaultHelpPerPage = 10;

    public BukkitCommandHelpEntry generateHelpCommand(BukkitCommandManager manager, CommandActor actor) {
        return new BukkitCommandHelpEntry(manager, actor);
    }

    @Override
    public int defaultHelpPerPage() {
        return this.defaultHelpPerPage;
    }

    @Override
    public void updateHelpPerPage(int amount) {
        this.defaultHelpPerPage = amount;
    }
}