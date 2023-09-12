package us.crazycrew.crazycrates.common.enums;

public enum Permissions {

    crazy_crates_player_key("player.key", "Check the number of keys you have."),
    crazy_crates_player_key_others("player.key.others", "Check the number of keys a player has."),
    crazy_crates_player_transfer_keys("player.transfer", "Allows players to send virtual keys to another player."),
    crazy_crates_player_menu("player.menu", "Opens the primary crate menu."),
    crazy_crates_player_help("player.help", "Shows the help menu for Crazy Crates."),
    crazy_crates_player_exclude_give_all("exclude.player.giveall", "Permission to prevent a player from getting keys from /GiveAll."),
    crazy_crates_admin_help("admin.help", "Shows the help menu for admins."),
    crazy_crates_admin_access("admin.access", "General purpose access for admins."),
    crazy_crates_admin_add_item("admin.additem", "Adds items in-game to a prize in a crate."),
    crazy_crates_admin_preview("admin.preview", "Opens the preview of any crate for a player."),
    crazy_crates_admin_list("admin.list", "Displays a list of all available crates."),
    crazy_crates_admin_open("admin.open", "Tries to open a crate for the player if they have a key."),
    crazy_crates_admin_open_other("admin.open.others", "Tries to open a crate for a player if they have a key."),
    crazy_crates_admin_mass_open("admin.massopen", "Used to mass open a crate without an animation."),
    crazy_crates_admin_force_open("admin.forceopen", "Opens a crate for a player for free."),
    crazy_crates_admin_teleport("admin.teleport", "Teleports to a crate."),
    crazy_crates_admin_give_key("admin.givekey", "Give a key(s) to a player to use on a crate."),
    crazy_crates_admin_give_random_key("admin.giverandomkey", "Give a random key(s) to a player to use on a crate."),
    crazy_crates_admin_give_all("admin.giveall", "Gives all online players keys to use on crates."),
    crazy_crates_admin_take_key("admin.takekey", "Allows you to take keys from a player."),
    crazy_crates_admin_set_crate("admin.set", "Set the block you are looking at as the specified crate."),
    crazy_crates_admin_set_menu("admin.setmenu", "Sets the block you are looking at to open the (/cc) crate menu."),
    crazy_crates_admin_reload("admin.reload", "Reloads the entire plugin."),
    crazy_crates_admin_debug("admin.debug", "Debugs the plugin."),
    crazy_crates_admin_convert("admin.convert", "Converts data from other supported crate plugins into crazy crates."),
    crazy_crates_admin_schematic("admin.schematic.*", "Gives all permissions related to schematics."),
    crazy_crates_admin_schematic_set("admin.schematic.set", "Sets the positions #1 or #2 when making a new schematic for quadcrates."),
    crazy_crates_admin_schematic_save("admin.schematic.save", "Saves the new schematic file to the schematics folder.");

    private final String defaultPermission;
    private final String description;

    /**
     * A constructor to build a permission
     *
     * @param defaultPermission the default permission
     * @param description the permission description
     */
    Permissions(String defaultPermission, String description) {
        this.defaultPermission = defaultPermission;
        this.description = description;
    }

    /**
     * Get a built permission with an action type.
     *
     * @param action the action type i.e "command"
     * @return a completed permission
     */
    public String getPermission(String action) {
        return "crazycrates." + action + "." + this.defaultPermission;
    }

    /**
     * Get a built permission with no action type.
     *
     * @return a completed permission
     */
    public String getPermission() {
        return "crazycrates.command." + this.defaultPermission;
    }

    /**
     * Get the description of the permission.
     *
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }
}