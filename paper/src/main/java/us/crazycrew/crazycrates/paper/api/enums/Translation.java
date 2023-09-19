package us.crazycrew.crazycrates.paper.api.enums;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.properties.Property;
import com.ryderbelserion.cluster.api.utils.ColorUtils;
import com.ryderbelserion.cluster.bukkit.utils.LegacyUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.common.config.ConfigManager;
import us.crazycrew.crazycrates.common.config.types.Messages;
import us.crazycrew.crazycrates.common.config.types.PluginConfig;
import us.crazycrew.crazycrates.common.utils.MiscUtils;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Translation {

    unknown_command(Messages.unknown_command),
    no_teleporting(Messages.no_teleporting),
    no_commands(Messages.no_commands),
    no_keys(Messages.no_keys),
    no_virtual_keys(Messages.no_virtual_keys),
    feature_disabled(Messages.feature_disabled),
    correct_usage(Messages.correct_usage),

    no_prizes_found(Messages.no_prizes_found),
    no_schematics_found(Messages.no_schematics_found),

    prize_error(Messages.prize_error, true),

    internal_error(Messages.internal_error),

    must_be_player(Messages.player_requirements_must_be_player),
    must_be_console_sender(Messages.player_requirements_must_be_console_sender),
    must_be_looking_at_block(Messages.player_requirements_must_be_looking_at_block),
    target_not_online(Messages.player_requirements_target_not_online),
    same_player(Messages.player_requirements_same_player),
    no_permission(Messages.player_requirements_no_permission),
    inventory_not_empty(Messages.player_requirements_inventory_not_empty),
    obtaining_keys(Messages.player_requirements_obtaining_keys),
    too_close_to_another_player(Messages.player_requirements_too_close_to_another_player),
    required_keys(Messages.player_requirements_required_keys),

    not_a_crate(Messages.crate_requirements_not_a_crate),
    crate_no_permission(Messages.crate_requirements_no_permission),
    not_a_number(Messages.crate_requirements_not_a_number),
    not_on_block(Messages.crate_requirements_not_on_block),
    out_of_time(Messages.crate_requirements_out_of_time),
    preview_disabled(Messages.crate_requirements_preview_disabled),
    already_open(Messages.crate_requirements_already_open),
    in_use(Messages.crate_requirements_in_use),
    cannot_be_a_virtual_crate(Messages.crate_requirements_cannot_be_a_virtual_crate),
    need_more_room(Messages.crate_requirements_need_more_room),
    world_disabled(Messages.crate_requirements_world_disabled),

    physical_crate_created(Messages.crates_physical_crate_created, true),

    physical_crate_removed(Messages.crates_physical_crate_removed),

    command_open_crate(Messages.command_open_crate),

    command_give_player_keys(Messages.command_give_player_keys),

    command_give_cannot_give_player_keys(Messages.command_give_cannot_give_player_keys),

    command_give_everyone_keys(Messages.command_give_everyone_keys),

    command_give_offline_player_keys(Messages.command_give_offline_player_keys),

    command_take_player_keys(Messages.command_take_player_keys),

    command_take_offline_player_keys(Messages.command_take_offline_player_keys),

    command_add_item_no_item_in_hand(Messages.command_add_item_no_item_in_hand),
    command_add_item_from_hand(Messages.command_add_item_from_hand),

    command_convert_no_files_to_convert(Messages.command_convert_no_files_to_convert),

    command_convert_error_converting_files(Messages.command_convert_error_converting_files),

    command_convert_successfully_converted_files(Messages.command_convert_successfully_converted_files),

    command_reload_completed(Messages.command_reload_completed),

    command_transfer_not_enough_keys(Messages.command_transfer_not_enough_keys),

    command_transfer_keys(Messages.command_transfer_keys),

    command_transfer_keys_received(Messages.command_transfer_keys_received),

    command_keys_personal_no_virtual_keys(Messages.command_keys_personal_no_virtual_keys),

    // virtual keys header personal
    command_keys_personal_virtual_keys_header(Messages.command_keys_personal_virtual_keys_header, true),

    command_keys_other_player_no_virtual_keys(Messages.command_keys_other_player_no_virtual_keys),

    // other player no virtual keys
    command_keys_other_player_virtual_keys_header(Messages.command_keys_other_player_virtual_keys_header, true),

    command_keys_crate_format(Messages.command_keys_crate_format),

    player_help(Messages.player_help, true),

    admin_help(Messages.admin_help, true);

    private Property<String> property;

    private Property<List<String>> listProperty;

    private boolean isList = false;

    private Component message;
    private String legacy;

    /**
     * Used for strings
     *
     * @param property the property
     */
    Translation(Property<String> property) {
        this.property = property;
    }

    /**
     * Used for string lists
     *
     * @param listProperty the list property
     * @param isList Defines if it's a list or not.
     */
    Translation(Property<List<String>> listProperty, boolean isList) {
        this.listProperty = listProperty;

        this.isList = isList;
    }

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final ConfigManager configManager = this.plugin.getCrazyHandler().getConfigManager();
    private final SettingsManager messages = ConfigManager.getMessages();

    private boolean isList() {
        return this.isList;
    }

    private @NotNull List<String> getPropertyList(Property<List<String>> properties) {
        return this.messages.getProperty(properties);
    }

    private @NotNull String getProperty(Property<String> property) {
        return this.messages.getProperty(property);
    }

    public Translation getMessage() {
        return getMessage(new HashMap<>());
    }

    public Translation getMessage(String placeholder, String replacement) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(placeholder, replacement);

        return getMessage(placeholders);
    }

    public Translation getMessage(Map<String, String> placeholders) {
        boolean useAdventure = ConfigManager.getPluginConfig().getProperty(PluginConfig.use_mini_message);

        // Get the string first.
        String message;

        if (isList()) {
            message = MiscUtils.convertList(getPropertyList(this.listProperty));
        } else {
            message = getProperty(this.property);
        }

        if (!placeholders.isEmpty()) {
            for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
                message = message.replace(placeholder.getKey(), placeholder.getValue()).replace(placeholder.getKey().toLowerCase(), placeholder.getValue());
            }
        }

        if (useAdventure) this.message = ColorUtils.parse(message); else this.legacy = LegacyUtils.color(message);

        return this;
    }

    public Component toComponent() {
        return this.message;
    }

    public String toString() {
        return this.legacy;
    }

    public List<Component> toListComponent() {
        ArrayList<Component> components = new ArrayList<>();

        getPropertyList(this.listProperty).forEach(line -> components.add(ColorUtils.parse(line)));

        return components;
    }

    public List<String> toListString() {
        ArrayList<String> components = new ArrayList<>();

        getPropertyList(this.listProperty).forEach(line -> components.add(LegacyUtils.color(line)));

        return components;
    }
}