package us.crazycrew.crazycrates.common.config.types;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.PropertyInitializer;
import java.util.List;

public class Messages implements SettingsHolder {

    protected Messages() {}

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

    public static final Property<String> unknown_command = PropertyInitializer.newProperty("misc.unknown-command", "<red>This command is not known.</red>");

    public static final Property<String> no_teleporting = PropertyInitializer.newProperty("misc.no-teleporting", "<red>You may not teleport away while opening</red> <gold>{crate}.</gold>");

    public static final Property<String> no_commands = PropertyInitializer.newProperty("misc.no-commands", "<red>You are not allowed to use commands while opening</red> <gold>{crate}.</gold>");

    public static final Property<String> no_keys = PropertyInitializer.newProperty("misc.no-keys", "<red>You need a {key} <red>in your hand to use</red> <gold>{crate}.</gold>");

    public static final Property<String> no_virtual_keys = PropertyInitializer.newProperty("misc.no-virtual-keys", "<red>You need</red> {key} <red>to open</red> <gold>{crate}.</gold>");

    public static final Property<String> feature_disabled = PropertyInitializer.newProperty("misc.feature-disabled", "<red>This feature is disabled. We have no ETA on when this will function.</red>");

    public static final Property<String> correct_usage = PropertyInitializer.newProperty("misc.correct-usage", "<red>The correct usage for this command is</red> <yellow>{usage}</yellow>");

    // Errors
    public static final Property<String> no_prizes_found = PropertyInitializer.newProperty("errors.no-prizes-found", "<red>This crate contains no prizes that you can win.</red>");

    public static final Property<String> key_refund = PropertyInitializer.newProperty("errors.key-refund", "<red>There was an error with prize {crate} so you've been refunded your key");

    public static final Property<String> no_schematics_found = PropertyInitializer.newProperty("errors.no-schematics-found", "<red>No schematic were found, Please re-generate them by deleting the folder or checking for errors!</red>");

    public static final Property<List<String>> prize_error = PropertyInitializer.newListProperty("errors.prize-error", List.of(
            "<red>An error has occurred while trying to give you the prize</red> <gold>{prize}.</gold>",
            "<yellow>This has occurred in</yellow> <gold>{crate}.</gold> <yellow>Please notify your owner.</yellow>"
    ));

    public static final Property<String> internal_error = PropertyInitializer.newProperty("errors.internal-error", "<red>An internal error has occurred. Please check the console for the full error.</red>");

    // Players
    public static final Property<String> player_requirements_must_be_player = PropertyInitializer.newProperty("player.requirements.must-be-player", "<red>You must be a player to use this command.</red>");

    public static final Property<String> player_requirements_must_be_console_sender = PropertyInitializer.newProperty("player.requirements.must-be-console-sender", "<red>You must be using console to use this command.</red>");

    public static final Property<String> player_requirements_must_be_looking_at_block = PropertyInitializer.newProperty("player.requirements.must-be-looking-at-block", "<red>You must be looking at a block.</red>");

    public static final Property<String> player_requirements_target_not_online = PropertyInitializer.newProperty("player.target-not-online", "<red>The player</red> <gold>{player}</gold> <red>is not online.</red>");

    public static final Property<String> player_requirements_same_player = PropertyInitializer.newProperty("player.target-same-player", "<red>You cannot use this command on yourself.</red>");

    public static final Property<String> player_requirements_no_permission = PropertyInitializer.newProperty("player.no-permission", "<red>You do not have permission to use that command!</red>");

    public static final Property<String> player_requirements_inventory_not_empty = PropertyInitializer.newProperty("player.inventory-not-empty", "<red>Inventory is not empty, Please make room before opening</red> <gold>{crate}.</gold>");

    public static final Property<String> player_requirements_obtaining_keys = PropertyInitializer.newProperty("player.obtaining-keys", "<gray>You have been given <gold>{amount} {key}</gold> <gray>Keys.");

    public static final Property<String> player_requirements_too_close_to_another_player = PropertyInitializer.newProperty("player.too-close-to-another-player", "<red>You are too close to a player that is opening their Crate.</red>");

    public static final Property<String> player_requirements_required_keys = PropertyInitializer.newProperty("player.required-keys", "<gray>You need</gray> <red>%key-amount%</red> <gray>keys to open</gray> <red>%crate%.</red> <gray>You have</gray> <red>%amount%.</red>");

    // Crates
    public static final Property<String> crate_requirements_not_a_crate = PropertyInitializer.newProperty("crates.requirements.not-a-crate", "<red>There is no crate called</red> <gold>{crate}.</gold>");

    public static final Property<String> crate_requirements_no_permission = PropertyInitializer.newProperty("crates.requirements.no-permission", "<red>You do not have permission to use that crate</red>");

    public static final Property<String> crate_requirements_not_a_number = PropertyInitializer.newProperty("crates.requirements.not-a-number", "<gold>{number}</gold> <red>is not a number.</red>");
    public static final Property<String> crate_requirements_not_on_block = PropertyInitializer.newProperty("crates.requirements.not-on-block", "<red>You must be standing on a block to use</red> <gold>{crate}.</gold>");

    public static final Property<String> crate_requirements_out_of_time = PropertyInitializer.newProperty("crates.out-of-time", "<red>You took</red> <green>5 Minutes</green> <red>to open the</red> <gold>{crate}</gold> <red>so it closed.</red>");

    public static final Property<String> crate_requirements_preview_disabled = PropertyInitializer.newProperty("crates.crate-preview-disabled", "<red>The preview for</red> <gold>{crate}</gold> <gray>is currently disabled.</gray>");

    public static final Property<String> crate_requirements_already_open = PropertyInitializer.newProperty("crates.crate-already-open", "<red>You are already opening</red> <gold>{crate}.</gold>");

    public static final Property<String> crate_requirements_in_use = PropertyInitializer.newProperty("crates.crate-in-use", "<gold>{crate}</gold> <red>is already in use. Please wait until it finishes!</red>");

    public static final Property<String> crate_requirements_cannot_be_a_virtual_crate = PropertyInitializer.newProperty("crates.cannot-be-a-virtual-crate", "<gold>{crate}</gold> <red>cannot be used as a Virtual Crate. You have it set to <gold>{cratetype}.</gold>");

    public static final Property<String> crate_requirements_need_more_room = PropertyInitializer.newProperty("crates.need-more-room", "<red>There is not enough space to open that here.</red>");

    public static final Property<String> crate_requirements_world_disabled = PropertyInitializer.newProperty("crates.world-disabled", "<red>Crates are disabled in</red> <gold>{world}.</gold>");

    public static final Property<List<String>> crates_physical_crate_created = PropertyInitializer.newListProperty("crates.physical-crate.created",
            List.of(
                    "<gray>You have set that block to</gray> <gold>{crate}.</gold>",
                    "<gray>To remove</gray> <gold>{crate}</gold>, <gray>Shift Click Break in Creative to remove.</gray>"
            ));

    public static final Property<String> crates_physical_crate_removed = PropertyInitializer.newProperty("crates.physical-crate.removed", "<gray>You have removed</gray> <gold>{id}.</gold>");

    // Commands

    public static final Property<String> command_open_crate = PropertyInitializer.newProperty("command.open.opened-a-crate", "<gray>You have opened the</gray> <gold>{crate}</gold> <gray>crate for</gray> <gold>{player}.</gold>");

    public static final Property<String> command_give_player_keys = PropertyInitializer.newProperty("command.give.given-player-keys", "<gray>You have given</gray> <gold>{player} {amount} Keys.</gold>");

    @Comment("This is only sent if they have a full inventory.")
    public static final Property<String> command_give_cannot_give_player_keys = PropertyInitializer.newProperty("command.give.cannot-give-player-keys", "<gray>You have been given</gray> <gold>{amount} {key}</gold> <gray>virtual keys because your inventory is full.</gray>");

    public static final Property<String> command_give_everyone_keys = PropertyInitializer.newProperty("command.give.given-everyone-keys", "<gray>You have given everyone</gray> <gold>{amount} Keys.</gold>");

    public static final Property<String> command_give_offline_player_keys = PropertyInitializer.newProperty("command.give.given-offline-player-keys", "<gray>You have given</gray> <gold>{amount} key(s)</gold> <gray>to the offline player</gray> <gold>{player}.</gold>");

    public static final Property<String> command_take_player_keys = PropertyInitializer.newProperty("command.take.take-player-keys", "<gray>You have taken</gray> <gold>{amount} key(s)</gold> <gray>from</gray> <gold>{player}.</gold>");

    public static final Property<String> command_take_offline_player_keys = PropertyInitializer.newProperty("command.take.take-offline-player-keys", "<gray>You have taken</gray> <gold>{amount} key(s)</gold> <gray>from the offline player</gray> <gold>{player}.</gold>");

    public static final Property<String> command_add_item_no_item_in_hand = PropertyInitializer.newProperty("command.additem.no-item-in-hand", "<red>You need to have an item in your hand to add it to</red> <gold>{crate}.</gold>");

    public static final Property<String> command_add_item_from_hand = PropertyInitializer.newProperty("command.additem.add-item-from-hand", "<gray>The item has been added to</gray> <gold>{crate}</gold> <gray>as</gray> <gold>Prize #{prize}.</gold>");

    public static final Property<String> command_convert_no_files_to_convert = PropertyInitializer.newProperty("command.convert.no-files-to-convert", "<red>No available plugins to convert files.</red>");

    public static final Property<String> command_convert_error_converting_files = PropertyInitializer.newProperty("command.convert.error-converting-files", "<red>An error has occurred while trying to convert files. We could not convert</red> <gold>{file}</gold> <red>so please check the console.</red>");

    public static final Property<String> command_convert_successfully_converted_files = PropertyInitializer.newProperty("command.convert.successfully-converted-files", "<green>Plugin Conversion has succeeded!</green>");

    public static final Property<String> command_reload_completed = PropertyInitializer.newProperty("command.reload.reload-completed", "<green>Plugin reload has been completed.</green>");

    public static final Property<String> command_transfer_not_enough_keys = PropertyInitializer.newProperty("command.transfer.not-enough-keys", "<red>You do not have enough keys to transfer.</red>");

    public static final Property<String> command_transfer_keys = PropertyInitializer.newProperty("command.transfer.transferred-keys", "<gray>You have transferred</gray> <red>{amount} {crate}</red> <gray>keys to</gray> <red>{player}.</red>");

    public static final Property<String> command_transfer_keys_received = PropertyInitializer.newProperty("command.transfer.transferred-keys-received", "<gray>You have received</gray> <red>{amount} {crate}</red> <gray>keys from</gray> <red>{player}.</red>");

    public static final Property<String> command_keys_personal_no_virtual_keys = PropertyInitializer.newProperty("command.keys.personal.no-virtual-keys", "<dark_gray>(</dark_gray><gold>!</gold><dark_gray>)</dark_gray> <gray>You currently do not have any virtual keys.</gray>");

    public static final Property<List<String>> command_keys_personal_virtual_keys_header = PropertyInitializer.newListProperty("command.keys.personal.virtual-keys-header", List.of(
            "<dark_gray>(</dark_gray><gold>!</gold><dark_gray>)</dark_gray> <gray>A list of your current amount of keys.</gray>"
    ));

    public static final Property<String> command_keys_other_player_no_virtual_keys = PropertyInitializer.newProperty("command.keys.other-player.no-virtual-keys", "<dark_gray>(</dark_gray><gold>!</gold><dark_gray>)</dark_gray> <gray>The player</gray> <red>{player} <gray>does not have any keys.</gray>");

    public static final Property<List<String>> command_keys_other_player_virtual_keys_header = PropertyInitializer.newListProperty("command.keys.other-player.virtual-keys-header", List.of(
            "<dark_gray>(</dark_gray><gold>!</gold><dark_gray>)</dark_gray> <gray>A list of</gray> <red>{player}''s</red> <gray>current amount of keys.</gray>"
    ));

    @Comment("This is related to what will show in the output above.")
    public static final Property<String> command_keys_crate_format = PropertyInitializer.newProperty("command.keys.crate-format", "{crate} <dark_gray>»</dark_gray> <gold>{keys} keys.</gold>");

    public static final Property<List<String>> player_help = PropertyInitializer.newListProperty("command.help.player-help", List.of(
            " <dark_green>Crazy Crates Player Help!</dark_green>",
            " ",
            " <dark_gray>»</dark_gray></dark_gray> <gold>/key [player]</gold> <gray>»</gray> <yellow>Check how many keys a player has.</yellow>",
            " <dark_gray>»</dark_gray> <gold>/cc</gold> <gray>»</gray> <yellow>Opens the crate menu.</yellow>"
    ));

    public static final Property<List<String>> admin_help = PropertyInitializer.newListProperty("command.help.admin-help", List.of(
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