package com.badbones69.crazycrates.commands.engine.v2.example;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.v2.CommandContext;
import com.badbones69.crazycrates.commands.engine.v2.CommandEngine;
import com.ryderbelserion.stick.core.StickLogger;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Collections;

public class SecondCommand extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    public SecondCommand() {
        super("second", "second example command", Collections.emptyList());
    }

    @Override
    protected void perform(CommandContext context) {
        StickLogger.info(getDescription());
    }
}