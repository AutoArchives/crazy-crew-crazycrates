package us.crazycrew.crazycrates.common.enums;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.properties.Property;
import com.ryderbelserion.cluster.api.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.common.config.ConfigManager;
import us.crazycrew.crazycrates.common.config.types.Locale;
import us.crazycrew.crazycrates.common.utils.MiscUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Messages {

    unknown_command(Locale.unknown_command),
    no_teleporting(Locale.no_teleporting),
    no_commands(Locale.no_commands),
    no_keys(Locale.no_keys),
    no_virtual_keys(Locale.no_virtual_keys),
    feature_disabled(Locale.feature_disabled),
    correct_usage(Locale.correct_usage),

    no_prizes_found(Locale.no_prizes_found),
    no_schematics_found(Locale.no_schematics_found),

    prize_error(Locale.prize_error, true),

    internal_error(Locale.internal_error),

    must_be_player(Locale.player_requirements_must_be_player),
    must_be_console_sender(Locale.player_requirements_must_be_console_sender),
    must_be_looking_at_block(Locale.player_requirements_must_be_looking_at_block),
    target_not_online(Locale.player_requirements_target_not_online),
    same_player(Locale.player_requirements_same_player),
    no_permission(Locale.player_requirements_no_permission),
    inventory_not_empty(Locale.player_requirements_inventory_not_empty),
    obtaining_keys(Locale.player_requirements_obtaining_keys),
    too_close_to_another_player(Locale.player_requirements_too_close_to_another_player),
    required_keys(Locale.player_requirements_required_keys),

    not_a_crate(Locale.crate_requirements_not_a_crate),
    not_a_number(Locale.crate_requirements_not_a_number),
    not_on_block(Locale.crate_requirements_not_on_block),
    out_of_time(Locale.crate_requirements_out_of_time),
    preview_disabled(Locale.crate_requirements_preview_disabled),
    already_open(Locale.crate_requirements_already_open),
    in_use(Locale.crate_requirements_in_use),
    cannot_be_a_virtual_crate(Locale.crate_requirements_cannot_be_a_virtual_crate),
    need_more_room(Locale.crate_requirements_need_more_room),
    world_disabled(Locale.crate_requirements_world_disabled),

    physical_crate_created(Locale.crates_physical_crate_created, true),

    physical_crate_removed(Locale.crates_physical_crate_removed),

    command_open_crate(Locale.command_open_crate),

    command_give_player_keys(Locale.command_give_player_keys),

    command_give_cannot_give_player_keys(Locale.command_give_cannot_give_player_keys),

    command_give_everyone_keys(Locale.command_give_everyone_keys),

    command_give_offline_player_keys(Locale.command_give_offline_player_keys),

    command_take_player_keys(Locale.command_take_player_keys),

    command_take_offline_player_keys(Locale.command_take_offline_player_keys),

    command_add_item_no_item_in_hand(Locale.command_add_item_no_item_in_hand),
    command_add_item_from_hand(Locale.command_add_item_from_hand),

    command_convert_no_files_to_convert(Locale.command_convert_no_files_to_convert),

    command_convert_error_converting_files(Locale.command_convert_error_converting_files),

    command_convert_successfully_converted_files(Locale.command_convert_successfully_converted_files),

    command_reload_completed(Locale.command_reload_completed),

    command_transfer_not_enough_keys(Locale.command_transfer_not_enough_keys),

    command_transfer_keys(Locale.command_transfer_keys),

    command_transfer_keys_received(Locale.command_transfer_keys_received),

    command_keys_personal_no_virtual_keys(Locale.command_keys_personal_no_virtual_keys),

    // virtual keys header personal
    command_keys_personal_header(Locale.command_keys_personal_no_virtual_keys_header, true),

    command_keys_other_player_no_virtual_keys(Locale.command_keys_other_player_no_virtual_keys),

    // other player no virtual keys
    command_keys_other_player_header(Locale.command_keys_other_player_no_virtual_keys_header, true),

    command_keys_crate_format(Locale.command_keys_crate_format),

    player_help(Locale.player_help, true),

    admin_help(Locale.admin_help, true);

    private Property<String> property;

    private Property<List<String>> listProperty;

    private boolean isList = false;

    /**
     * Used for strings
     *
     * @param property the property
     */
    Messages(Property<String> property) {
        this.property = property;
    }

    /**
     * Used for string lists
     *
     * @param listProperty the list property
     * @param isList Defines if it's a list or not.
     */
    Messages(Property<List<String>> listProperty, boolean isList) {
        this.listProperty = listProperty;

        this.isList = isList;
    }

    private final @NotNull SettingsManager localeConfig = ConfigManager.getLocaleConfig();

    private boolean isList() {
        return this.isList;
    }

    private @NotNull List<String> getPropertyList() {
        return this.localeConfig.getProperty(this.listProperty);
    }

    private @NotNull String getProperty() {
        return this.localeConfig.getProperty(this.property);
    }

    public Component getMessage(String placeholder, String replacement) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(placeholder, replacement);

        return getMessage(placeholders);
    }

    public Component getMessage(Map<String, String> placeholders) {
        String message;

        if (isList()) {
            message = MiscUtils.convertList(getPropertyList());
        } else {
            message = getProperty();
        }

        for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
            message = message.replace(placeholder.getKey(), placeholder.getValue()).replace(placeholder.getKey().toLowerCase(), placeholder.getValue());
        }

        return ColorUtils.parse(message);
    }

    public String setPlaceholders(String message) {
        /*message = message.replaceAll("\\{crate}", "");
        message = message.replaceAll("\\{key}", "");
        message = message.replaceAll("\\{world}", "");
        message = message.replaceAll("\\{player}", "");
        message = message.replaceAll("\\{number}", "");
        message = message.replaceAll("\\{amount}", "");
        message = message.replaceAll("\\{keys}", "");
        message = message.replaceAll("\\{usage}", "");
        message = message.replaceAll("\\{prize}", "");
        message = message.replaceAll("\\{key-amount}", "");
        message = message.replaceAll("\\{prefix}", this.pluginConfig.getProperty(PluginConfig.command_prefix));
        message = message.replaceAll("\\{id}", "");

        if (player != null && PluginSupport.PLACEHOLDERAPI.isPluginEnabled()) message = PlaceholderAPI.setPlaceholders(player, message);*/

        return message;
    }
}