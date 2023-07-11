package com.badbones69.crazycrates.commands.engine.requirements;

import com.badbones69.crazycrates.commands.engine.CommandContext;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.Permission;

public class CommandRequirements {

    private final boolean asPlayer;
    private Permission permission;
    private String rawPermission;

    public CommandRequirements(boolean asPlayer, Permission permission, String rawPermission) {
        this.asPlayer = asPlayer;

        if (permission != null) this.permission = permission;

        if (!rawPermission.isEmpty() || !rawPermission.isBlank()) this.rawPermission = rawPermission;
    }

    public boolean checkRequirements(boolean notifySender, CommandContext context) {
        if (asPlayer && !context.isPlayer()) {
            //if (notifySender) context.reply(StickCore.api().commandRequirementNotPlayer());

            // The command is not valid.
            return false;
        }

        // The sender is console sender so automatically valid.
        if (context.getSender() instanceof ConsoleCommandSender) return true;

        if (this.permission != null && !context.hasPermission(this.permission) || this.rawPermission != null && !context.hasPermission(this.rawPermission)) {
            String value = this.permission != null ? this.permission.getName() : this.rawPermission;

            //if (notifySender) context.reply(StickCore.api().commandRequirementNoPermission().replaceAll("\\{permission}", value));

            // The command is not valid.
            return false;
        }

        // The command is valid.
        return true;
    }

    public Permission getPermission() {
        return this.permission;
    }

    public String getRawPermission() {
        return this.rawPermission;
    }

    public boolean isPlayer() {
        return this.asPlayer;
    }
}