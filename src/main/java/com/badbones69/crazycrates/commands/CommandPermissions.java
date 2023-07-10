package com.badbones69.crazycrates.commands;

import org.bukkit.entity.HumanEntity;
import org.bukkit.permissions.PermissionDefault;

public enum CommandPermissions {

    // schematic.save -> schematic-save
    // schematic.set -> schematic-set
    // admin.open.others -> admin.open-others
    // admin.mass.open -> admin.mass-open
    // admin.forceopen -> admin.force-open
    // admin.givekey -> admin.give-key
    // admin.giverandomkey -> admin.give-random-key
    // admin.giveall -> admin.give-all
    // admin.set -> admin.set-crate
    // admin.setmenu -> admin.set-menu

    // player.key.others -> player.key-others
    // player/menu -> player.crate-menu
    // excludes.player.giveall -> excludes.player.give-all

    PLAYER_HELP("player.help", "Gives access to the help command.", PermissionDefault.TRUE),

    PLAYER_KEY("player.key", "Check the number of keys you have", PermissionDefault.OP),

    PLAYER_KEY_OTHERS("player.key-others", "Check the number of keys other players have", PermissionDefault.OP),

    PLAYER_TRANSFER_KEYS("player.transfer", "Allows you to send keys to another player.", PermissionDefault.OP),
    PLAYER_CRATE_MENU("player.crate-menu", "Opens the primary crate menu.", PermissionDefault.OP),

    PLAYER_EXCLUDE_ALL("exclude.player.give-all", "Excludes players who have this permission from give-all", PermissionDefault.FALSE),

    ADMIN_ACCESS("admin.access", "General purpose access for admins.", PermissionDefault.OP),
    ADMIN_ADD_ITEM("admin.additem", "Adds items in-game to a prize in a crate.", PermissionDefault.OP),
    ADMIN_PREVIEW("admin.preview", "Opens the preview of any crate for a player.", PermissionDefault.OP),

    ADMIN_LIST("admin.list", "Displays a list of all available crates.", PermissionDefault.OP),

    ADMIN_OPEN("admin.open", "Tries to open a crate for the player if they have a key.", PermissionDefault.OP),
    ADMIN_OPEN_OTHER("admin.open-others", "Tries to open a crate for a player if they have a key.", PermissionDefault.OP),
    ADMIN_MASS_OPEN("admin.mass-open", "Used to mass open a crate without an animation.", PermissionDefault.OP),
    ADMIN_FORCE_OPEN("admin.force-open", "Opens a crate for a player for free.", PermissionDefault.OP),

    ADMIN_TELEPORT("admin.teleport", "Teleports you to a crate.", PermissionDefault.OP),

    ADMIN_GIVE_KEY("admin.give-key", "Allows you to give keys to a player for crates.", PermissionDefault.OP),
    ADMIN_GIVE_RANDOM_KEY("admin.give-random-key", "Allows you to give random keys to a player for crates.", PermissionDefault.OP),
    ADMIN_GIVE_ALL("admin.give-all", "Gives all online players keys to use on crates", PermissionDefault.OP),

    ADMIN_TAKE_KEY("admin.take-key", "Allows you to take keys from a player", PermissionDefault.OP),

    ADMIN_SET_CRATE("admin.set-crate", "Set the block you are looking at as the specified crate", PermissionDefault.OP),
    ADMIN_SET_MAIN_MENU("admin.set-menu", "Sets the block you are looking at to open the (/cc) crate menu", PermissionDefault.OP),

    ADMIN_RELOAD("admin.reload", "Reloads the plugin.", PermissionDefault.OP),

    ADMIN_CONVERT("admin.convert", "Converts data from other supported crate plugins to ours", PermissionDefault.OP),

    ADMIN_SCHEMATIC_ALL("admin.schematic.*", "Gives all permissions related to schematics.", PermissionDefault.FALSE),
    ADMIN_SCHEMATIC_SAVE("admin.schematic-save", "Saves a schematic based around selected points", PermissionDefault.OP),
    ADMIN_SCHEMATIC_SET("admin.schematic-set", "Allows you to select 4 points to create a new schematic.", PermissionDefault.OP);

    private final String node;
    private final String description;
    private final PermissionDefault permissionDefault;

    CommandPermissions(String node, String description, PermissionDefault permissionDefault) {
        this.node = node;
        this.description = description;
        this.permissionDefault = permissionDefault;
    }

    public String getNode() {
        return this.node;
    }

    public String getDescription() {
        return this.description;
    }

    public PermissionDefault getPermissionDefault() {
        return this.permissionDefault;
    }

    public String getBuiltPermission(String action) {
        return "crazycrates." + action + "." + this.node;
    }

    public String getBuiltPermission() {
        return "crazycrates.command." + this.node;
    }

    public boolean hasPermission(HumanEntity entity, CommandPermissions commandPermissions) {
        return entity.hasPermission(commandPermissions.getBuiltPermission());
    }

    public boolean hasPermission(HumanEntity entity, String rawPermission) {
        return entity.hasPermission(rawPermission);
    }
}