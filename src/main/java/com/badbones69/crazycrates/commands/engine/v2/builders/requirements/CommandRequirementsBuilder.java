package com.badbones69.crazycrates.commands.engine.v2.builders.requirements;

import org.bukkit.permissions.Permission;

public class CommandRequirementsBuilder {

    private boolean asPlayer = false;
    private Permission permission = null;
    private String rawPermission = "";

    public CommandRequirementsBuilder asPlayer(boolean value) {
        this.asPlayer = value;
        return this;
    }

    public CommandRequirementsBuilder withPermission(Permission permission) {
        this.permission = permission;
        return this;
    }

    public CommandRequirementsBuilder withRawPermission(String rawPermission) {
        this.rawPermission = rawPermission;
        return this;
    }

    public CommandRequirements build() {
        return new CommandRequirements(asPlayer, permission, rawPermission);
    }
}