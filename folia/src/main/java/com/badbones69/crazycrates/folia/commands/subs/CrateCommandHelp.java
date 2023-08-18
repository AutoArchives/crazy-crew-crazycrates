package com.badbones69.crazycrates.folia.commands.subs;

/*
import com.badbones69.crazycrates.api.enums.Permissions;
import com.badbones69.crazycrates.paper.api.enums.settings.Messages;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandContext;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandEngine;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrateCommandHelp extends BukkitCommandEngine {

    @SuppressWarnings("deprecation")
    public CrateCommandHelp() {
        super("help", Permissions.PLAYER_HELP.getDescription(), "/crazycrates:help", Collections.emptyList());

        setPermission(Permissions.PLAYER_HELP.getBuiltPermission());

        setPermissionMessage(Messages.NO_PERMISSION.getMessage());
    }

    @Override
    public void perform(BukkitCommandContext context, String[] args) {
        if (args.length == 2) {
            context.sendLegacyMessage(Messages.CORRECT_USAGE.getMessage().replaceAll("%usage%", this.usageMessage + " [player,admin]"));
            return;
        }

        if (args[0].equals("player")) {
            context.sendLegacyMessage(Messages.HELP.getMessage());
            return;
        }

        if (!context.hasPermission(Permissions.ADMIN_ACCESS.getBuiltPermission())) {
            context.sendLegacyMessage(Messages.NO_PERMISSION.getMessage());
            return;
        }

        if (args[0].equals("admin")) context.sendLegacyMessage(Messages.ADMIN_HELP.getMessage());
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        // args[1] = player/admin
        // args[2] = out of bounds so return empty list, don't need to duplicate a string argument.
        if (args.length == 2) return Collections.emptyList();

        ArrayList<String> completions = new ArrayList<>();

        completions.add("player");

        if (sender.hasPermission(Permissions.ADMIN_ACCESS.getBuiltPermission())) completions.add("admin");

        return completions;
    }
}
 */