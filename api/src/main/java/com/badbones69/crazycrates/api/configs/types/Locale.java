package com.badbones69.crazycrates.api.configs.types;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import java.util.List;
import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class Locale implements SettingsHolder {

    public Locale() {}

    private static final String settings = "Settings.";

    @Override
    public void registerComments(CommentsConfiguration conf) {
        String[] header = {
                "Github: https://github.com/Crazy-Crew",
                "",
                "Issues: https://github.com/Crazy-Crew/CrazyCrates/issues",
                "Features: https://github.com/Crazy-Crew/CrazyCrates/discussions/categories/features"
        };

        conf.setComment("misc", header);
    }

    public static final Property<String> UNKNOWN_COMMAND = newProperty("misc.unknown-command", "<red>The command {command} is not known.</red>");

    public static final Property<String> NO_TELEPORTING = newProperty("misc.no-teleporting", "<red>You may not teleport away while opening</red> <gold>{crate}.</gold>");

    public static final Property<String> NO_COMMANDS = newProperty("misc.no-commands", "<red>You are not allowed to use commands while opening</red> <gold>{crate}.</gold>");

    public static final Property<String> NO_KEYS = newProperty("misc.no-keys", "<red>You need a</red> {key} <red>in your hand to use</red> <gold>{crate}.</gold>");

    public static final Property<String> NO_VIRTUAL_KEYS = newProperty("misc.no-virtual-keys", "<red>You need</red> {key} <red>to open</red> <gold>{crate}.</gold>");

    public static final Property<String> FEATURE_DISABLED = newProperty("misc.feature-disabled", "<red>This feature is disabled. We have no ETA on when this will function.</red>");

    //TODO() Removed.
    //public static final Property<String> CORRECT_USAGE = newProperty("misc.correct-usage", "<red>The correct usage for this command is</red> <gold>{usage}</gold>");

    public static final Property<String> NO_PRIZES_FOUND = newProperty("errors.no-prizes.found", "<red>{crate} contains no prizes that you can win.</red>");

    public static final Property<String> NO_SCHEMATICS_FOUND = newProperty("errors.no-schematics-found", "<red>No schematic were found, Please re-generate them by deleting the folder or checking for errors!</red>");

    public static final Property<String> PRIZE_ERROR = newProperty("errors.prize-error", "<red>An error has occurred in</red> <gold>{crate}</gold> <red>for</red> <gold>#{prize}. Contact your owner!</gold>");

    public static final Property<String> INTERNAL_ERROR = newProperty("errors.internal-error", "<red>An internal error has occurred. Please check the console for the full error.</red>");

    public static final Property<String> OPTIONAL_ARGUMENT = newProperty("player.requirements.optional-argument", "<green>This argument is optional</green>");

    public static final Property<String> REQUIRED_ARGUMENT = newProperty("player.requirements.required-argument", "<red>This argument is not optional</red>");

    public static final Property<String> TOO_MANY_ARGS = newProperty("player.requirements.too-many-args", "<red>You put more arguments then I can handle.</red>");

    public static final Property<String> NOT_ENOUGH_ARGS = newProperty("player.requirements.not-enough-args", "<red>You did not supply enough arguments.</red>");

    public static final Property<String> MUST_BE_PLAYER = newProperty("player.requirements.must-be-player", "<red>You must be a player to use this command.</red>");

    public static final Property<String> MUST_BE_CONSOLE_SENDER = newProperty("player.requirements.must-be-console-sender", "<red>You must be using console to use this command.</red>");

    public static final Property<String> MUST_BE_LOOKING_AT_BLOCK = newProperty("player.requirements.must-be-looking-at-block", "<red>You must be looking at a block.</red>");

    public static final Property<String> TARGET_NOT_ONLINE = newProperty("player.target-not-online", "<red>The player</red> <gold>{player}</gold> <red>is not online.</red>");

    public static final Property<String> GRABBING_OFFLINE_PLAYER = newProperty("player.grabbing-offline-player", "<red>Grabbing the offline player!</red> <yellow>I hope you got the name spelled right!</yellow>");

    public static final Property<String> TARGET_SAME_PLAYER = newProperty("player.target-same-player", "<red>You cannot use this command on yourself.</red>");

    public static final Property<String> NO_PERMISSION = newProperty("player.no-permission", "<red>You do not have permission to use that command!</red>");

    public static final Property<String> INVENTORY_NOT_EMPTY = newProperty("player.inventory-not-empty", "<red>Inventory is not empty, Please make room before opening</red> <gold>{crate}.</gold>");

    public static final Property<String> OBTAINING_KEYS = newProperty("player.obtaining-keys", "<gray>You have been given</gray> <gold>{amount} {key}</gold> <gray>keys.</gray>");

    public static final Property<String> TOO_CLOSE_TO_ANOTHER_PLAYER = newProperty("player.too-close-to-another-player", "<red>You are too close to a player that is opening their crate.</red>");

    public static final Property<String> NOT_A_CRATE = newProperty("crates.requirements.not-a-crate", "<red>There is no crate called</red> <gold>{crate}.</gold>");

    public static final Property<String> NOT_A_NUMBER = newProperty("crates.requirements.not-a-number", "<gold>{number}</gold> <red>is not a number.</red>");

    public static final Property<String> NOT_ON_BLOCK = newProperty("crates.not-on-block", "<red>You must be standing on a block to use</red> <gold>{crate}.</gold>");

    public static final Property<String> OUT_OF_TIME = newProperty("crates.out-of-time", "<red>You took</red> <green>5 Minutes</green> <red>to open the</red> <gold>{crate}</gold> <red>so it closed.</red>");

    public static final Property<String> PREVIEW_DISABLED = newProperty("crates.preview-disabled", "<red>The preview for</red> <gold>{crate}</gold> <red>is currently disabled.</red>");

    public static final Property<String> CRATE_ALREADY_OPEN = newProperty("crates.already-open", "<red>You are already opening</red> <gold>{crate}.</gold>");

    public static final Property<String> CRATE_IN_USE = newProperty("crates.in-use", "<gold>{crate}</gold> <red>is already in use. Please wait until it finishes!</red>");

    public static final Property<String> CANNOT_BE_VIRTUAL_CRATE = newProperty("crates.cannot-be-a-virtual-crate", "<gold>{crate}</gold> <red>cannot be used as a Virtual Crate. You have it set to</red> <gold>{cratetype}.</gold>");

    public static final Property<String> PHYSICAL_CRATE_REMOVED = newProperty("crates.physical-crate.removed", "<gray>You have removed</gray> <gold>{id}.</gold>");

    public static final Property<String> OPENED_A_CRATE = newProperty("command.open.opened-a-crate", "<gray>You have opened the</gray> <gold>{crate}</gold> <gray>crate for</gray> <gold>{player}.</gold>");

    public static final Property<String> GIVEN_PLAYER_KEYS = newProperty("command.give.given-player-keys", "<gray>You have given</gray> <gold>{player} {amount} keys.</gold>");

    public static final Property<String> CANNOT_GIVE_PLAYER_KEYS = newProperty("command.give.cannot-give-player-keys", "<gray>You have been given</gray> <gold>{amount} {key}</gold> <gray>virtual keys because your inventory was full.</gray>");

    public static final Property<String> GIVEN_EVERYONE_KEYS = newProperty("command.give.given-everyone-keys", "<gray>You have given everyone</gray> <gold>{amount} keys.</gold>");

    public static final Property<String> GIVEN_OFFLINE_PLAYER_KEYS = newProperty("command.give.given-offline-player-keys", "<gray>You have given</gray> <gold>{amount} key(s) <gray>to the offline player</gray> <gold>{player}.</gold>");

    public static final Property<String> TAKE_PLAYER_KEYS = newProperty("command.take.take-player-keys", "<gray>You have taken</gray> <gold>{amount} key(s)</gold> <gray>from</gray> <gold>{player}.</gold>");

    public static final Property<String> TAKE_OFFLINE_PLAYER_KEYS = newProperty("command.take.take-offline-player-keys", "<gray>You have taken</gray> <gold>{amount} key(s)</gold> <gray>from the offline player</gray> <gold>{player}.</gold>");

    public static final Property<String> NEED_MORE_ROOM = newProperty("crates.need-more-room", "<red>There is not enough space to open {crate} here.</red>");

    public static final Property<String> WORLD_DISABLED = newProperty("crates.world-disabled", "<red>Crates are disabled in</red> <gold>{world}.</gold>");

    public static final Property<List<String>> PHYSICAL_CRATE_CREATED = newListProperty("crates.physical-crate.created", List.of(
            "<gray>You have set that block to</gray> <gold>{crate}.</gold>",
            "<gray>To remove</gray> <gold>{crate},</gold> <gray>Shift-click break in creative to remove.</gray>"
    ));

    public static final Property<String> NO_ITEM_IN_HAND = newProperty("command.additem.no-item-in-hand", "<red>You need to have an item in your hand to add it to</red> <gold>{crate}.</gold>");

    public static final Property<String> ADD_ITEM_FROM_HAND = newProperty("command.additem.add-item-from-hand", "<gray>The item has been added to</gray> <gold>{crate}</gold> <gray>as</gray> <gold>Prize #{prize}.</gold>");

    public static final Property<String> RELOAD_PLUGIN = newProperty("command.reload.reload-plugin", "<gold>You have reloaded the plugin!</gold>");

    public static final Property<String> NOT_ENOUGH_KEYS = newProperty("command.transfer-not-enough-keys", "<red>You do not have enough keys to transfer.</red>");

    public static final Property<String> TRANSFERRED_KEYS = newProperty("command.transfer.transferred-keys", "<gray>You have transferred</gray> <red>{amount} {crate}</red> <gray>keys to</gray> <red>{player}.</red>");

    public static final Property<String> TRANSFERRED_KEYS_RECEIVED = newProperty("command.transfer.transferred-keys-received", "<gray>You have received</gray> <red>{amount} {crate}</red> <gray>keys from</gray> <red>{player}.</red>");

    public static final Property<String> NO_VIRTUAL_KEYS_PERSONAL = newProperty("command.keys.personal.no-virtual-keys", "<dark_gray>(</dark_gray><gold>!</gold><dark_gray>)</dark_gray> <gray>You currently do not have any virtual keys.</gray>");

    public static final Property<List<String>> NO_VIRTUAL_KEYS_PERSONAL_HEADER = newListProperty("command.keys.personal.header", List.of(
            "<dark_gray>(</dark_gray><gold>!</gold><dark_gray>)</dark_gray> <gray>A list of your current amount of keys.</gray>"
    ));

    public static final Property<String> NO_VIRTUAL_KEYS_OTHER = newProperty("command.keys.other-player.no-virtual-keys", "<dark_gray>(</dark_gray><gold>!</gold><dark_gray>)</dark_gray> <gray>The player</gray> <red>{player}</red> <gray>does not have any keys.</gray>");

    public static final Property<List<String>> NO_VIRTUAL_KEYS_OTHER_HEADER = newListProperty("command.keys.other-player.header", List.of(
            "<dark_gray>(</dark_gray><gold>!</gold><dark_gray>)</dark_gray> <gray>A list of</gray> <red>{player}''s</red> <gray>current amount of keys.</gray>"
    ));

    public static final Property<String> CRATE_FORMAT = newProperty("keys.crate-format", "{crate} <dark_gray>»</dark_gray> <gold>{keys} keys.</gold>");
}