package com.badbones69.crazycrates.api.enums;

import org.bukkit.permissions.PermissionDefault;

public enum Permissions {

    PLAYER_KEY("key", "Check the number of keys you have", PermissionDefault.OP),

    PLAYER_KEY_OTHERS("key-others", "Check the number of keys other players have", PermissionDefault.OP),

    PLAYER_TRANSFER_KEYS("transfer", "Allows you to send keys to another player.", PermissionDefault.OP),
    PLAYER_CRATE_MENU("crate-menu", "Opens the primary crate menu.", PermissionDefault.OP),

    PLAYER_HELP("help", "Gives access to the help command.", PermissionDefault.TRUE),

    PLAYER_EXCLUDE_ALL("exclude-player-give-all", "Excludes players who have this permission from give-all", PermissionDefault.FALSE),

    ADMIN_ACCESS("admin-access", "General purpose access for admins.", PermissionDefault.OP),
    ADMIN_ADD_ITEM("additem", "Adds items in-game to a prize in a crate.", PermissionDefault.OP),
    ADMIN_PREVIEW("preview", "Opens the preview of any crate for a player.", PermissionDefault.OP),

    ADMIN_LIST("list", "Displays a list of all available crates.", PermissionDefault.OP),

    ADMIN_OPEN("open", "Tries to open a crate for the player if they have a key.", PermissionDefault.OP),
    ADMIN_OPEN_OTHER("open-others", "Tries to open a crate for a player if they have a key.", PermissionDefault.OP),
    ADMIN_MASS_OPEN("mass-open", "Used to mass open a crate without an animation.", PermissionDefault.OP),
    ADMIN_FORCE_OPEN("force-open", "Opens a crate for a player for free.", PermissionDefault.OP),

    ADMIN_TELEPORT("teleport", "Teleports you to a crate.", PermissionDefault.OP),

    ADMIN_DEBUG("debug", "Allows you to debug prizes.", PermissionDefault.OP),

    ADMIN_GIVE_KEY("give-key", "Allows you to give keys to a player for crates.", PermissionDefault.OP),
    ADMIN_GIVE_RANDOM_KEY("give-random-key", "Allows you to give random keys to a player for crates.", PermissionDefault.OP),
    ADMIN_GIVE_ALL("give-all", "Gives all online players keys to use on crates", PermissionDefault.OP),

    ADMIN_TAKE_KEY("take-key", "Allows you to take keys from a player", PermissionDefault.OP),

    ADMIN_SET_CRATE("set-crate", "Set the block you are looking at as the specified crate", PermissionDefault.OP),
    ADMIN_SET_MAIN_MENU("set-menu", "Sets the block you are looking at to open the (/cc) crate menu", PermissionDefault.OP),

    ADMIN_RELOAD("reload", "Reloads the plugin.", PermissionDefault.OP),

    ADMIN_CONVERT("convert", "Converts data from other supported crate plugins to ours", PermissionDefault.OP),

    ADMIN_SCHEMATIC_ALL("schematic.*", "Gives all permissions related to schematics.", PermissionDefault.FALSE),
    ADMIN_SCHEMATIC_SAVE("schematic-save", "Saves a schematic based around selected points", PermissionDefault.OP),
    ADMIN_SCHEMATIC_SET("schematic-set", "Allows you to select 4 points to create a new schematic.", PermissionDefault.OP);

    private final String node;
    private final String description;
    private final PermissionDefault permissionDefault;

    Permissions(String node, String description, PermissionDefault permissionDefault) {
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

    public String getBuiltPermission() {
        return "crazycrates." + this.node;
    }
}