package com.badbones69.crazycrates.paper.commands.subs.admin;

import com.badbones69.crazycrates.api.enums.Permissions;
import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.CrazyManager;
import com.badbones69.crazycrates.paper.api.enums.settings.Messages;
import com.badbones69.crazycrates.paper.api.objects.Crate;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandContext;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandEngine;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.List;

public class CrateCommandDebug extends BukkitCommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final CrazyManager crazyManager = this.plugin.getCrazyManager();

    @SuppressWarnings("deprecation")
    public CrateCommandDebug() {
        super("debug", Permissions.ADMIN_DEBUG.getDescription(), "/crazycrates:debug", Collections.emptyList());

        setPermission(Permissions.ADMIN_DEBUG.getBuiltPermission());

        setPermissionMessage(Messages.NO_PERMISSION.getMessage());
    }

    @Override
    public void perform(BukkitCommandContext context, String[] args) {
        if (!context.isPlayer()) {
            context.sendLegacyMessage(Messages.MUST_BE_A_PLAYER.getMessage());
            return;
        }

        Crate crate = this.crazyManager.getCrateFromName(args[0]);

        if (crate != null) {
            crate.getPrizes().forEach(prize -> this.crazyManager.givePrize(context.getPlayer(), prize, crate));
            return;
        }

        context.sendLegacyMessage(Messages.NOT_A_CRATE.getMessage("%Crate%", args[0]));
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 2) return Collections.emptyList();

        return this.plugin.getFileManager().getAllCratesNames().stream().toList();
    }
}