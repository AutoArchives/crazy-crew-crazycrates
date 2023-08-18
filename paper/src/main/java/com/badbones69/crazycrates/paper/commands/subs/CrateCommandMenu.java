package com.badbones69.crazycrates.paper.commands.subs;

/*
import com.badbones69.crazycrates.api.enums.Permissions;
import com.badbones69.crazycrates.paper.api.FileManager.Files;
import com.badbones69.crazycrates.paper.api.enums.settings.Messages;
import com.badbones69.crazycrates.paper.listeners.MenuListener;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandContext;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandEngine;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.Collections;

public class CrateCommandMenu extends BukkitCommandEngine {

    @SuppressWarnings("deprecation")
    public CrateCommandMenu() {
        super("menu", Permissions.PLAYER_CRATE_MENU.getDescription(), "/crazycrates:menu", Collections.emptyList());

        setPermission(Permissions.PLAYER_CRATE_MENU.getBuiltPermission());

        setPermissionMessage(Messages.NO_PERMISSION.getMessage());
    }

    @Override
    public void perform(BukkitCommandContext context, String[] args) {
        if (!context.isPlayer()) {
            context.sendLegacyMessage(Messages.MUST_BE_A_PLAYER.getMessage());
            return;
        }

        FileConfiguration config = Files.CONFIG.getFile();

        if (config.getBoolean("Settings.Enable-Crate-Menu")) {
            MenuListener.openGUI(context.getPlayer());

            return;
        }

        context.sendLegacyMessage(Messages.FEATURE_DISABLED.getMessage());
    }
}
 */