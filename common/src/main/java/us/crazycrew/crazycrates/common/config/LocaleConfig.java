package us.crazycrew.crazycrates.common.config;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.PropertyInitializer;
import java.util.List;

public class LocaleConfig implements SettingsHolder {

    protected LocaleConfig() {
    }

    @Override
    public void registerComments(CommentsConfiguration conf) {
        String[] header = {
                "Support: https://discord.gg/badbones-s-live-chat-182615261403283459",
                "Github: https://github.com/Crazy-Crew",
                "",
                "Issues: https://github.com/Crazy-Crew/CrazyCrates/issues",
                "Features: https://github.com/Crazy-Crew/CrazyCrates/issues",
                "",
                "Docs: https://docs.crazycrew.us/crazycrates/home"
        };

        conf.setComment("misc", header);
    }

    public static final Property<String> UNKNOWN_COMMAND = PropertyInitializer.newProperty("misc.unknown-command", "<red>This command is not known.</red>");

    public static final Property<String> NO_TELEPORTING = PropertyInitializer.newProperty("misc.no-teleporting", "<red>You may not teleport away while opening</red> <gold>{crate}.</gold>");

    public static final Property<String> NO_COMMANDS = PropertyInitializer.newProperty("misc.no-commands", "<red>You are not allowed to use commands while opening</red> <gold>{crate}.</gold>");

    public static final Property<String> NO_KEYS = PropertyInitializer.newProperty("misc.no-keys", "<red>You need a {key} <red>in your hand to use</red> <gold>{crate}.</gold>");

    public static final Property<String> NO_VIRTUAL_KEYS = PropertyInitializer.newProperty("misc.no-virtual-keys", "<red>You need</red> {key} <red>to open</red> <gold>{crate}.</gold>");

    public static final Property<String> FEATURE_DISABLED = PropertyInitializer.newProperty("misc.feature-disabled", "<red>This feature is disabled. We have no ETA on when this will function.</red>");

    public static final Property<String> CORRECT_USAGE = PropertyInitializer.newProperty("misc.correct-usage", "<red>The correct usage for this command is</red> <yellow>{usage}</yellow>");

    // Errors
    public static final Property<String> NO_PRIZES_FOUND = PropertyInitializer.newProperty("errors.no-prizes-found", "<red>This crate contains no prizes that you can win.</red>");

    public static final Property<String> NO_SCHEMATICS_FOUND = PropertyInitializer.newProperty("errors.no-schematics-found", "<red>No schematic were found, Please re-generate them by deleting the folder or checking for errors!</red>");

    public static final Property<List<String>> PRIZE_ERROR = PropertyInitializer.newListProperty("errors.prize-error", List.of(
            "<red>An error has occurred while trying to give you the prize</red> <gold>{prize}.</gold>",
            "<yellow>This has occurred in</yellow> <gold>{crate}.</gold> <yellow>Please notify your owner.</yellow>"
    ));

    public static final Property<String> INTERNAL_ERROR = PropertyInitializer.newProperty("errors.internal-error", "<red>An internal error has occurred. Please check the console for the full error.</red>");

    // Players
    public static final Property<String> PLAYER_REQUIREMENTS_TOO_MANY_ARGS = PropertyInitializer.newProperty("player.requirements.too-many-args", "<red>You put more arguments then I can handle.</red>");

    public static final Property<String> PLAYER_REQUIREMENTS_NOT_ENOUGH_ARGS = PropertyInitializer.newProperty("player.requirements.not-enough-args", "<red>You did not supply enough arguments.</red>");
    public static final Property<String> PLAYER_REQUIREMENTS_MUST_BE_PLAYER = PropertyInitializer.newProperty("player.requirements.must-be-player", "<red>You must be a player to use this command.</red>");

    public static final Property<String> PLAYER_REQUIREMENTS_MUST_BE_CONSOLE_SENDER = PropertyInitializer.newProperty("player.requirements.must-be-console-sender", "<red>You must be using console to use this command.</red>");

    public static final Property<String> PLAYER_REQUIREMENTS_MUST_BE_LOOKING_AT_BLOCK = PropertyInitializer.newProperty("player.requirements.must-be-looking-at-block", "<red>You must be looking at a block.</red>");

    public static final Property<String> PLAYER_REQUIREMENTS_TARGET_NOT_ONLINE = PropertyInitializer.newProperty("player.target-not-online", "<red>The player</red> <gold>{player}</gold> <red>is not online.</red>");

    public static final Property<String> PLAYER_REQUIREMENTS_SAME_PLAYER = PropertyInitializer.newProperty("player.target-same-player", "<red>You cannot use this command on yourself.</red>");

    public static final Property<String> PLAYER_REQUIREMENTS_NO_PERMISSION = PropertyInitializer.newProperty("player.no-permission", "<red>You do not have permission to use that command!</red>");

    public static final Property<String> PLAYER_REQUIREMENTS_INVENTORY_NOT_EMPTY = PropertyInitializer.newProperty("player.inventory-not-empty", "<red>Inventory is not empty, Please make room before opening</red> <gold>{crate}.</gold>");

    public static final Property<String> PLAYER_REQUIREMENTS_OBTAINING_KEYS = PropertyInitializer.newProperty("player.obtaining-keys", "gray>You have been given <gold>{amount} {key}</gold> <gray>Keys.");

    public static final Property<String> PLAYER_REQUIREMENTS_TOO_CLOSE_TO_ANOTHER_PLAYER = PropertyInitializer.newProperty("player.too-close-to-another-player", "<red>You are too close to a player that is opening their Crate.</red>");

    public static final Property<String> PLAYER_REQUIREMENTS_REQUIRED_KEYS = PropertyInitializer.newProperty("player.required-keys", "<gray>You need</gray> <red>%key-amount%</red> <gray>keys to open</gray> <red>%crate%.</red> <gray>You have</gray> <red>%amount%.</red>");

    // Crates
    public static final Property<String> CRATE_REQUIREMENTS_NOT_A_CRATE = PropertyInitializer.newProperty("crates.requirements.not-a-crate", "<red>There is no crate called</red> <gold>{crate}.</gold>");

    public static final Property<String> CRATE_REQUIREMENTS_NOT_A_NUMBER = PropertyInitializer.newProperty("crates.requirements.not-a-number", "<gold>{number}</gold> <red>is not a number.</red>");
    public static final Property<String> CRATE_REQUIREMENTS_NOT_ON_BLOCK = PropertyInitializer.newProperty("crates.requirements.not-on-block", "<red>You must be standing on a block to use</red> <gold>{crate}.</gold>");

    public static final Property<String> CRATE_REQUIREMENTS_MUST_OUT_OF_TIME = PropertyInitializer.newProperty("crates.out-of-time", "<red>You took</red> <green>5 Minutes</green> <red>to open the</red> <gold>{crate}</gold> <red>so it closed.</red>");

    public static final Property<String> CRATE_REQUIREMENTS_PREVIEW_DISABLED = PropertyInitializer.newProperty("crates.crate-preview-disabled", "<red>The preview for</red> <gold>{crate}</gold> <gray>is currently disabled.</gray>");

    public static final Property<String> CRATE_REQUIREMENTS_ALREADY_OPEN = PropertyInitializer.newProperty("crates.crate-already-open", "<red>You are already opening</red> <gold>{crate}.</gold>");

    public static final Property<String> CRATE_REQUIREMENTS_IN_USE = PropertyInitializer.newProperty("crates.crate-in-use", "<gold>{crate}</gold> <red>is already in use. Please wait until it finishes!</red>");

    public static final Property<String> CRATE_REQUIREMENTS_CANNOT_BE_A_VIRTUAL_CRATE = PropertyInitializer.newProperty("crates.cannot-be-a-virtual-crate", "<gold>{crate}</gold> <red>cannot be used as a Virtual Crate. You have it set to <gold>{cratetype}.</gold>");

    public static final Property<String> CRATE_REQUIREMENTS_NEED_MORE_ROOM = PropertyInitializer.newProperty("crates.need-more-room", "<red>There is not enough space to open that here.</red>");

    public static final Property<String> CRATE_REQUIREMENTS_WORLD_DISABLED = PropertyInitializer.newProperty("crates.world-disabled", "<red>Crates are disabled in</red> <gold>{world}.</gold>");

    public static final Property<List<String>> CRATES_PHYSICAL_CRATE_CREATED = PropertyInitializer.newListProperty("crates.physical-crate.created",
            List.of(
                    "<gray>You have set that block to</gray> <gold>{crate}.</gold>",
                    "<gray>To remove</gray> <gold>{crate}</gold>, <gray>Shift Click Break in Creative to remove.</gray>"
            ));

    public static final Property<String> CRATES_PHYSICAL_CRATE_REMOVED = PropertyInitializer.newProperty("crates.physical-crate.removed", "<gray>You have removed</gray> <gold>{id}.</gold>");

    // Commands

    public static final Property<String> COMMAND_OPEN_CRATE = PropertyInitializer.newProperty("command.open.opened-a-crate", "<gray>You have opened the</gray> <gold>{crate}</gold> <gray>crate for</gray> <gold>{player}.</gold>");

    public static final Property<String> COMMAND_GIVE_PLAYER_KEYS = PropertyInitializer.newProperty("command.give.given-player-keys", "<gray>You have given</gray> <gold>{player} {amount} Keys.</gold>");

    @Comment("This is only sent if they have a full inventory.")
    public static final Property<String> COMMAND_GIVE_CANNOT_GIVE_PLAYER_KEYS = PropertyInitializer.newProperty("command.give.cannot-give-player-keys", "<gray>You have been given</gray> <gold>{amount} {key}</gold> <gray>virtual keys because your inventory was full.</gray>");

    public static final Property<String> COMMAND_GIVE_EVERYONE_KEYS = PropertyInitializer.newProperty("command.give.given-everyone-keys", "<gray>You have given everyone</gray> <gold>{amount} Keys.</gold>");

    public static final Property<String> COMMAND_GIVE_OFFLINE_PLAYER_KEYS = PropertyInitializer.newProperty("command.give.given-offline-player-keys", "<gray>You have given</gray> <gold>{amount} key(s)</gold> <gray>to the offline player</gray> <gold>{player}.</gold>");

    public static final Property<String> COMMAND_TAKE_PLAYER_KEYS = PropertyInitializer.newProperty("command.take.take-player-keys", "<gray>You have taken</gray> <gold>{amount} key(s)</gold> <gray>from</gray> <gold>{player}.</gold>");

    public static final Property<String> COMMAND_TAKE_OFFLINE_PLAYER_KEYS = PropertyInitializer.newProperty("command.take.take-offline-player-keys", "<gray>You have taken</gray> <gold>{amount} key(s)</gold> <gray>from the offline player</gray> <gold>{player}.</gold>");

    public static final Property<String> COMMAND_ADDITEM_NO_ITEM_IN_HAND = PropertyInitializer.newProperty("command.additem.no-item-in-hand", "<red>You need to have an item in your hand to add it to</red> <gold>{crate}.</gold>");

    public static final Property<String> COMMAND_ADD_ITEM_FROM_HAND = PropertyInitializer.newProperty("command.additem.add-item-from-hand", "<gray>The item has been added to</gray> <gold>{crate}</gold> <gray>as</gray> <gold>Prize #{prize}.</gold>");

    public static final Property<String> COMMAND_CONVERT_NO_FILES_TO_CONVERT = PropertyInitializer.newProperty("command.convert.no-files-to-convert", "<red>No available plugins to convert files.</red>");

    public static final Property<String> COMMAND_CONVERT_ERROR_CONVERTING_FILES = PropertyInitializer.newProperty("command.convert.error-converting-files", "<red>An error has occurred while trying to convert files. We could not convert</red> <gold>{file}</gold> <red>so please check the console.</red>");

    public static final Property<String> COMMAND_CONVERT_SUCCESSFULLY_CONVERTED_FILES = PropertyInitializer.newProperty("command.convert.successfully-converted-files", "<green>Plugin Conversion has succeeded!</green>");

    public static final Property<String> COMMAND_RELOAD_COMPLETED = PropertyInitializer.newProperty("command.reload.reload-completed", "<green>Plugin reload has been completed.</green>");

    public static final Property<String> COMMAND_TRANSFER_NOT_ENOUGH_KEYS = PropertyInitializer.newProperty("command.transfer.not-enough-keys", "<red>You do not have enough keys to transfer.</red>");

    public static final Property<String> COMMAND_TRANSFER_KEYS = PropertyInitializer.newProperty("command.transfer.transferred-keys", "gray>You have transferred</gray> <red>{amount} {crate}</red> <gray>keys to</gray> <red>{player}.</red>");

    public static final Property<String> COMMAND_TRANSFER_KEYS_RECEIVED = PropertyInitializer.newProperty("command.transfer.transferred-keys-received", "<gray>You have received</gray> <red>{amount} {crate}</red> <gray>keys from</gray> <red>{player}.</red>");

    public static final Property<String> COMMAND_KEYS_PERSONAL_NO_VIRTUAL_KEYS = PropertyInitializer.newProperty("command.keys.personal.no-virtual-keys", "<dark_gray>(/dark_gray><gold>!</gold><dark_gray>)/dark_gray> <gray>You currently do not have any virtual keys.</gray>");

    public static final Property<List<String>> COMMAND_KEYS_PERSONAL_NO_VIRTUAL_KEYS_HEADER = PropertyInitializer.newListProperty("command.keys.personal.virtual-keys-header", List.of(
            "<dark_gray>(/dark_gray><gold>!</gold><dark_gray>)/dark_gray> <gray>A list of your current amount of keys.</gray>"
    ));

    public static final Property<String> COMMAND_KEYS_OTHER_PLAYER_NO_VIRTUAL_KEYS = PropertyInitializer.newProperty("command.keys.other-player.no-virtual-keys", "<dark_gray>(/dark_gray><gold>!</gold><dark_gray>)/dark_gray> <gray>The player</gray> <red>{player} <gray>does not have any keys.</gray>");

    public static final Property<List<String>> COMMAND_KEYS_OTHER_PLAYER_NO_VIRTUAL_KEYS_HEADER = PropertyInitializer.newListProperty("command.keys.other-player.virtual-keys-header", List.of(
            "<dark_gray>(</dark_gray><gold>!</gold><dark_gray>)</dark_gray> <gray>A list of</gray> <red>{player}''s</red> <gray>current amount of keys.</gray>"
    ));

    @Comment("This is related to what will show in the output above.")
    public static final Property<String> COMMAND_KEYS_CRATE_FORMAT = PropertyInitializer.newProperty("command.keys.crate-format", "{crate} <dark_gray>»</dark_gray> <gold>{keys} keys.</gold>");

    public static final Property<List<String>> PLAYER_HELP = PropertyInitializer.newListProperty("command.help.player-help", List.of(
            " <dark_green>Crazy Crates Player Help!</dark_green>",
            " ",
            " <dark_gray>»</dark_gray></dark_gray> <gold>/key [player]</gold> <gray>»</gray> <yellow>Check how many keys a player has.</yellow>",
            " <dark_gray>»</dark_gray> <gold>/cc</gold> <gray>»</gray> <yellow>Opens the crate menu.</yellow>"
    ));

    public static final Property<List<String>> ADMIN_HELP = PropertyInitializer.newListProperty("command.help.admin-help", List.of(
            " <red>Crazy Crates Admin Help</red>",
            " ",
            " <dark_gray>»</dark_gray> <gold>/cc additem [crate] [prize]</gold> <gray>-</gray> <yellow>Add items in-game to a prize in a crate.</yellow>",
            " <dark_gray>»</dark_gray> <gold>/cc preview [crate] [player]</gold> <gray>-</gray> <yellow>Opens the preview of a crate for a player.</yellow>",
            " <dark_gray>»</dark_gray> <gold>/cc list</gold> <gray>-</gray> <yellow>Lists all crates.</yellow>",
            " <dark_gray>»</dark_gray> <gold>/cc open [crate] [player]</gold> <gray>-</gray> <yellow>Tries to open a crate for a player if they have a key.</yellow>",
            " <dark_gray>»</dark_gray> <gold>/cc forceopen [crate] [player]</gold> <gray>-</gray> <yellow>Opens a crate for a player for free.</yellow>",
            " <dark_gray>»</dark_gray> <gold>/cc tp [location]</gold> <gray>-</gray> <yellow>Teleport to a Crate.</yellow>",
            " <dark_gray>»</dark_gray> <gold>/cc give [physical/virtual] [crate] [amount] [player]</gold> <gray>-</gray> <yellow>Allows you to take keys from a player.</yellow>",
            " <dark_gray>»</dark_gray> <gold>/cc set [crate]</gold> <gray>-</gray> <yellow>Set the block you are looking at as a crate.</yellow>",
            " <dark_gray>»</dark_gray> <gold>/cc set Menu</gold> <gray>-</gray> <yellow>Set the block you are looking at to open the</yellow> <red>/cc menu.</red>",
            " <dark_gray>»</dark_gray> <gold>/cc reload</gold> <gray>-</gray> <yellow>Reloads the config/data files.</yellow>",
            " <dark_gray>»</dark_gray> <gold>/cc set1/set2</gold> <gray>-</gray> <yellow>Sets position <red>#1 or #2</red> for when making a new schematic for QuadCrates.</yellow>",
            " <dark_gray>»</dark_gray> <gold>/cc save [file name]</gold> <gray>-</gray> <yellow>Create a new nbt file in the schematics folder.</yellow>",
            " <dark_gray>»</dark_gray> <gold>/cc mass-open [amount]</gold> <gray>-</gray> <yellow>Mass opens crates. Defaults to 10 but can be changed in the crate config files.</yellow>",
            " ",
            " <dark_gray>»</dark_gray> <gold>/key [player]</gold> <gray>-</gray> <yellow>Check how many keys a player has.</yellow>",
            " <dark_gray>»</dark_gray> <gold>/cc</gold> <gray>-</gray> <yellow>Opens the crate menu.</yellow>",
            " ",
            "<gray>You can find a list of permissions @</gray> <yellow>https://docs.crazycrew.us/crazycrates/info/commands/v2/permissions</yellow>"
    ));
}