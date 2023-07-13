package com.badbones69.crazycrates.api.configs.types;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import java.util.List;
import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class Locale implements SettingsHolder {

    public Locale() {}

    @Override
    public void registerComments(CommentsConfiguration conf) {
        String[] header = {
                "Submit your translations here: https://github.com/Crazy-Crew/CrazyCrates/discussions/categories/translations",
                "",
                "Legacy color codes such as &7,&c no longer work. You must use MiniMessage",
                "https://docs.advntr.dev/minimessage/format.html#color"
        };

        String[] deprecation = {
                "",
                "Warning: This section is subject to change so it is considered deprecated.",
                "This is your warning before the change happens.",
                ""
        };

        conf.setComment("general", header);
    }

    public static final Property<String> UNKNOWN_COMMAND = newProperty("general.unknown-command", "{prefix}<red>The command {command} is not known.</red>");

    public static final Property<String> INVALID_SYNTAX = newProperty("general.invalid-syntax", "{prefix}<gold>{value}</gold> <red>is an invalid {action}.</red>");

    public static final Property<String> NO_PERMISSION = newProperty("general.no-permission", "{prefix}<red>You do not have permission to use that command!</red>");

    public static final Property<String> REQUIRED_ARGUMENT = newProperty("general.checks.required-argument", "{prefix}<red>This argument is not optional</red>");

    public static final Property<String> OPTIONAL_ARGUMENT = newProperty("general.checks.optional-argument", "{prefix}<green>This argument is optional</green>");

    public static final Property<String> NOT_ENOUGH_ARGS = newProperty("general.checks.not-enough-args", "{prefix}<red>You did not supply enough arguments.</red>");

    public static final Property<String> TOO_MANY_ARGS = newProperty("general.checks.too-many-args", "{prefix}<red>You put more arguments then I can handle.</red>");

    public static final Property<String> NO_PRIZES_FOUND = newProperty("general.errors.no-prizes.found", "{prefix}<red>{crate} contains no prizes that you can win.</red>");

    public static final Property<String> INTERNAL_ERROR = newProperty("general.errors.internal-error", "{prefix}<red>An internal error has occurred. Please check the console for the full error.</red>");

    public static final Property<String> NO_SCHEMATICS_FOUND = newProperty("general.errors.no-schematics-found", "{prefix}<red>No schematic were found, Please re-generate them by deleting the folder or checking for errors!</red>");

    public static final Property<String> PRIZE_ERROR = newProperty("general.errors.prize-error", "{prefix}<red>An error has occurred in</red> <gold>{crate}</gold> <red>for</red> <gold>#{prize}. Contact your owner!</gold>");

    public static final Property<String> PLAYER_DOES_NOT_EXIST = newProperty("general.player-checks.invalid", "{prefix}<red>The player does not exist</red>");

    public static final Property<String> MUST_BE_PLAYER = newProperty("general.player-checks.must-be-player", "{prefix}<red>You must be a player to use this command.</red>");

    public static final Property<String> TARGET_SAME_PLAYER = newProperty("general.player-checks.target-same-player", "{prefix}<red>You cannot use this command on yourself.</red>");

    public static final Property<String> MUST_BE_LOOKING_AT_BLOCK = newProperty("general.player-checks.must-be-looking-at-block", "{prefix}<red>You must be looking at a block.</red>");

    public static final Property<String> MUST_BE_CONSOLE_SENDER = newProperty("general.player-checks.must-be-console-sender", "{prefix}<red>You must be using console to use this command.</red>");

    public static final Property<String> TARGET_NOT_ONLINE = newProperty("general.player-checks.target-not-online", "{prefix}<red>The player</red> <gold>{player}</gold> <red>is not online.</red>");

    public static final Property<String> GRABBING_OFFLINE_PLAYER = newProperty("general.player-checks.grabbing-offline-player", "{prefix}<red>Grabbing the offline player!</red> <yellow>I hope you got the name spelled right!</yellow>");

    public static final Property<String> NO_KEYS = newProperty("misc.no-keys", "{prefix}<red>You need a</red> {key} <red>in your hand to use</red> <gold>{crate}.</gold>");

    public static final Property<String> NO_VIRTUAL_KEYS = newProperty("misc.no-virtual-keys", "{prefix}<red>You need</red> {key} <red>to open</red> <gold>{crate}.</gold>");

    public static final Property<String> NO_TELEPORTING = newProperty("misc.no-teleporting", "{prefix}<red>You may not teleport away while opening</red> <gold>{crate}.</gold>");

    public static final Property<String> NO_COMMANDS = newProperty("misc.no-commands", "{prefix}<red>You are not allowed to use commands while opening</red> <gold>{crate}.</gold>");

    public static final Property<String> FEATURE_DISABLED = newProperty("misc.feature-disabled", "{prefix}<red>This feature is disabled. We have no ETA on when this will function.</red>");

    public static final Property<String> OBTAINING_KEYS = newProperty("player.obtaining-keys", "{prefix}<gray>You have been given</gray> <gold>{amount} {key}</gold> <gray>keys.</gray>");

    public static final Property<String> TOO_CLOSE_TO_ANOTHER_PLAYER = newProperty("player.too-close-to-another-player", "{prefix}<red>You are too close to a player that is opening their crate.</red>");

    public static final Property<String> INVENTORY_NOT_EMPTY = newProperty("player.inventory-not-empty", "{prefix}<red>Inventory is not empty, Please make room before opening</red> <gold>{crate}.</gold>");


    public static final Property<String> WORLD_DISABLED = newProperty("crates.world-disabled", "{prefix}<red>Crates are disabled in</red> <gold>{world}.</gold>");

    public static final Property<String> CRATE_ALREADY_OPEN = newProperty("crates.already-open", "{prefix}<red>You are already opening</red> <gold>{crate}.</gold>");

    public static final Property<String> NEED_MORE_ROOM = newProperty("crates.need-more-room", "{prefix}<red>There is not enough space to open {crate} here.</red>");

    public static final Property<List<String>> PHYSICAL_CRATE_CREATED = newListProperty("crates.physical-crate.created", List.of(
            "{prefix}<gray>You have set that block to</gray> <gold>{crate}.</gold>",
            "{prefix}<gray>To remove</gray> <gold>{crate},</gold> <gray>Shift-click break in creative to remove.</gray>"
    ));

    public static final Property<String> NOT_A_CRATE = newProperty("crates.requirements.not-a-crate", "{prefix}<red>There is no crate called</red> <gold>{crate}.</gold>");

    public static final Property<String> PHYSICAL_CRATE_REMOVED = newProperty("crates.physical-crate.removed", "{prefix}<gray>You have removed</gray> <gold>{id}.</gold>");

    public static final Property<String> NOT_ON_BLOCK = newProperty("crates.not-on-block", "{prefix}<red>You must be standing on a block to use</red> <gold>{crate}.</gold>");

    public static final Property<String> CRATE_IN_USE = newProperty("crates.in-use", "{prefix}<gold>{crate}</gold> <red>is already in use. Please wait until it finishes!</red>");

    public static final Property<String> PREVIEW_DISABLED = newProperty("crates.preview-disabled", "{prefix}<red>The preview for</red> <gold>{crate}</gold> <red>is currently disabled.</red>");

    public static final Property<String> OUT_OF_TIME = newProperty("crates.out-of-time", "{prefix}<red>You took</red> <green>5 Minutes</green> <red>to open the</red> <gold>{crate}</gold> <red>so it closed.</red>");

    public static final Property<String> CANNOT_BE_VIRTUAL_CRATE = newProperty("crates.cannot-be-a-virtual-crate", "{prefix}<gold>{crate}</gold> <red>cannot be used as a Virtual Crate. You have it set to</red> <gold>{cratetype}.</gold>");

    public static final Property<String> OPENED_A_CRATE = newProperty("command.open.opened-a-crate", "{prefix}<gray>You have opened the</gray> <gold>{crate}</gold> <gray>crate for</gray> <gold>{player}.</gold>");

    public static final Property<String> GIVEN_PLAYER_KEYS = newProperty("command.give.given-player-keys", "{prefix}<gray>You have given</gray> <gold>{player} {amount} keys.</gold>");

    public static final Property<String> CANNOT_GIVE_PLAYER_KEYS = newProperty("command.give.cannot-give-player-keys", "{prefix}<gray>You have been given</gray> <gold>{amount} {key}</gold> <gray>virtual keys because your inventory was full.</gray>");

    public static final Property<String> GIVEN_EVERYONE_KEYS = newProperty("command.give.given-everyone-keys", "{prefix}<gray>You have given everyone</gray> <gold>{amount} keys.</gold>");

    public static final Property<String> GIVEN_OFFLINE_PLAYER_KEYS = newProperty("command.give.given-offline-player-keys", "{prefix}<gray>You have given</gray> <gold>{amount} key(s) <gray>to the offline player</gray> <gold>{player}.</gold>");

    public static final Property<String> TAKE_PLAYER_KEYS = newProperty("command.take.take-player-keys", "{prefix}<gray>You have taken</gray> <gold>{amount} key(s)</gold> <gray>from</gray> <gold>{player}.</gold>");

    public static final Property<String> TAKE_OFFLINE_PLAYER_KEYS = newProperty("command.take.take-offline-player-keys", "{prefix}<gray>You have taken</gray> <gold>{amount} key(s)</gold> <gray>from the offline player</gray> <gold>{player}.</gold>");

    public static final Property<String> NO_ITEM_IN_HAND = newProperty("command.additem.no-item-in-hand", "{prefix}<red>You need to have an item in your hand to add it to</red> <gold>{crate}.</gold>");

    public static final Property<String> ADD_ITEM_FROM_HAND = newProperty("command.additem.add-item-from-hand", "{prefix}<gray>The item has been added to</gray> <gold>{crate}</gold> <gray>as</gray> <gold>Prize #{prize}.</gold>");

    public static final Property<String> RELOAD_PLUGIN = newProperty("command.reload.reload-plugin", "{prefix}<gold>You have reloaded the plugin!</gold>");

    public static final Property<String> NOT_ENOUGH_KEYS = newProperty("command.transfer-not-enough-keys", "{prefix}<red>You do not have enough keys to transfer.</red>");

    public static final Property<String> TRANSFERRED_KEYS = newProperty("command.transfer.transferred-keys", "{prefix}<gray>You have transferred</gray> <red>{amount} {crate}</red> <gray>keys to</gray> <red>{player}.</red>");

    public static final Property<String> TRANSFERRED_KEYS_RECEIVED = newProperty("command.transfer.transferred-keys-received", "{prefix}<gray>You have received</gray> <red>{amount} {crate}</red> <gray>keys from</gray> <red>{player}.</red>");

    public static final Property<String> NO_VIRTUAL_KEYS_PERSONAL = newProperty("command.keys.personal.no-virtual-keys", "{prefix}<dark_gray>(</dark_gray><gold>!</gold><dark_gray>)</dark_gray> <gray>You currently do not have any virtual keys.</gray>");

    public static final Property<List<String>> NO_VIRTUAL_KEYS_PERSONAL_HEADER = newListProperty("command.keys.personal.header", List.of(
            "<dark_gray>(</dark_gray><gold>!</gold><dark_gray>)</dark_gray> <gray>A list of your current amount of keys.</gray>"
    ));

    public static final Property<String> NO_VIRTUAL_KEYS_OTHER = newProperty("command.keys.other-player.no-virtual-keys", "{prefix}<dark_gray>(</dark_gray><gold>!</gold><dark_gray>)</dark_gray> <gray>The player</gray> <red>{player}</red> <gray>does not have any keys.</gray>");

    public static final Property<List<String>> NO_VIRTUAL_KEYS_OTHER_HEADER = newListProperty("command.keys.other-player.header", List.of(
            "<dark_gray>(</dark_gray><gold>!</gold><dark_gray>)</dark_gray> <gray>A list of</gray> <red>{player}''s</red> <gray>current amount of keys.</gray>"
    ));

    public static final Property<String> CRATE_FORMAT = newProperty("keys.crate-format", "{crate} <dark_gray>»</dark_gray> <gold>{keys} keys.</gold>");
}