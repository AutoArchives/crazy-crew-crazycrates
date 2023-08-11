package com.badbones69.crazycrates.paper.commands;

import com.badbones69.crazycrates.api.enums.Permissions;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandEngine;
import java.util.Collections;

public class CrateCommandReload extends BukkitCommandEngine {

    public CrateCommandReload() {
        super("reload", Permissions.ADMIN_RELOAD.getDescription(), "/crazycrates:reload", Collections.emptyList());
    }
}