package us.crazycrew.crazycrates.paper.api.plugin.migrate;

import ch.jalu.configme.SettingsManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.common.config.ConfigManager;
import us.crazycrew.crazycrates.common.config.types.Config;
import us.crazycrew.crazycrates.common.config.types.Messages;
import us.crazycrew.crazycrates.common.config.types.PluginConfig;
import us.crazycrew.crazycrates.common.config.types.menus.CrateMainMenu;
import us.crazycrew.crazycrates.common.config.types.menus.CratePreviewMenu;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MigrationService {
    
    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private ConfigManager configManager;

    private SettingsManager config;
    private SettingsManager messages;
    private SettingsManager pluginConfig;
    private SettingsManager mainMenuConfig;
    private SettingsManager previewMenuConfig;

    private final String prefix = "Settings.";

    public void migrate() {
        // Copy first
        copyPluginSettings();

        // Copy second
        copyConfigSettings();

        // Copy last
        copyMessages();
    }

    private boolean copyPluginSettings() {
        File input = new File(this.plugin.getDataFolder(),"config.yml");

        // The old configuration of config.yml.
        YamlConfiguration config;

        config = YamlConfiguration.loadConfiguration(input);

        if (config.getString(this.prefix + "Prefix") == null) return false;

        this.configManager = new ConfigManager(this.plugin.getDataFolder());
        this.configManager.load();

        this.config = this.configManager.getConfig();
        this.messages = this.configManager.getMessages();
        this.pluginConfig = this.configManager.getPluginConfig();
        this.mainMenuConfig = this.configManager.getMainMenuConfig();
        this.previewMenuConfig = this.configManager.getPreviewMenuConfig();

        String oldPrefix = config.getString(this.prefix + "Prefix");
        boolean oldMetrics = config.getBoolean(this.prefix + "Toggle-Metrics");

        this.pluginConfig.setProperty(PluginConfig.toggle_metrics, oldMetrics);
        if (oldPrefix != null) this.pluginConfig.setProperty(PluginConfig.command_prefix, oldPrefix);

        config.set(this.prefix + "Prefix", null);
        config.set(this.prefix + "Toggle-Metrics", null);

        try {
            this.pluginConfig.save();

            config.save(input);

            return true;
        } catch (IOException exception) {
            exception.printStackTrace();

            return false;
        }
    }

    private boolean copyConfigSettings() {
        File output = new File(this.plugin.getDataFolder(), "config-v1.yml");

        File configFile = new File(this.plugin.getDataFolder(), "config.yml");

        // The old config file.
        YamlConfiguration config;

        // The renamed config file.
        YamlConfiguration config_v1;

        // Only if the old value is found.
        config = YamlConfiguration.loadConfiguration(configFile);

        if (config.getString(this.prefix + "Enable-Crate-Menu") == null && !output.exists()) return false;

        configFile.renameTo(output);

        // Load the v1 output
        config_v1 = YamlConfiguration.loadConfiguration(output);

        boolean enableCrateMenu = config_v1.getBoolean(this.prefix + "Enable-Crate-Menu");
        boolean crateLogFile = config_v1.getBoolean(this.prefix + "Crate-Actions.Log-File");
        boolean crateLogConsole = config_v1.getBoolean(this.prefix + "Crate-Actions.Log-Console");

        String invName = config_v1.getString(this.prefix + "InventoryName");
        int invSize = config_v1.getInt(this.prefix + "InventorySize");

        boolean knockBack = config_v1.getBoolean(this.prefix + "KnockBack");

        boolean physAcceptsVirtual = config_v1.getBoolean(this.prefix + "Physical-Accepts-Virtual-Keys");
        boolean physAcceptsPhys = config_v1.getBoolean(this.prefix + "Physical-Accepts-Physical-Keys");
        boolean virtualAcceptsPhys = config_v1.getBoolean(this.prefix + "Virtual-Accepts-Physical-Keys");
        boolean giveVirtualKeysInventoryMessage = config_v1.getBoolean(this.prefix + "Give-Virtual-Keys-When-Inventory-Full-Message");
        boolean giveVirtualKeysInventory = config_v1.getBoolean(this.prefix + "Give-Virtual-Keys-When-Inventory-Full");

        //TODO() Move this to per crate.
        String needKeySound = config_v1.getString(this.prefix + "Need-Key-Sound");

        //TODO() Move this to QuadCrate type.
        int quadCrateTimer = config_v1.getInt(this.prefix + "QuadCrate.Timer");

        List<String> disabledWorlds = config_v1.getStringList(this.prefix + "DisabledWorlds");

        String menuName = config_v1.getString(this.prefix + "Preview.Buttons.Menu.Name");

        String menuItem = config_v1.getString(this.prefix + "Preview.Buttons.Menu.Item");
        List<String> menuLore = config_v1.getStringList(this.prefix + "Preview.Buttons.Menu.Lore");

        String nextName = config_v1.getString(this.prefix + "Preview.Buttons.Next.Name");
        String nextItem = config_v1.getString(this.prefix + "Preview.Buttons.Next.Item");
        List<String> nextLore = config_v1.getStringList(this.prefix + "Preview.Buttons.Next.Lore");

        String backName = config_v1.getString(this.prefix + "Preview.Buttons.Back.Name");
        String backItem = config_v1.getString(this.prefix + "Preview.Buttons.Back.Item");
        List<String> backLore = config_v1.getStringList(this.prefix + "Preview.Buttons.Back.Lore");

        boolean fillerToggle = config_v1.getBoolean(this.prefix + "Filler.Toggle");
        String fillerItem = config_v1.getString(this.prefix + "Filler.Item");
        String fillerName = config_v1.getString(this.prefix + "Filler.Name");
        List<String> fillerLore = config_v1.getStringList(this.prefix + "Filler.Lore");
        List<String> guiCustomizer = config_v1.getStringList(this.prefix + "GUI-Customizer");

        this.config.setProperty(Config.log_to_file, crateLogFile);
        this.config.setProperty(Config.log_to_console, crateLogConsole);

        this.config.setProperty(Config.crate_knock_back, knockBack);

        this.config.setProperty(Config.physical_accepts_virtual, physAcceptsVirtual);
        this.config.setProperty(Config.physical_accepts_physical, physAcceptsPhys);
        this.config.setProperty(Config.virtual_accepts_physical_keys, virtualAcceptsPhys);

        this.config.setProperty(Config.give_virtual_keys, giveVirtualKeysInventory);
        this.config.setProperty(Config.give_virtual_keys_message, giveVirtualKeysInventoryMessage);

        if (needKeySound != null) {
            this.config.setProperty(Config.key_sound_toggle, needKeySound.isBlank());

            this.config.setProperty(Config.key_sound_name, needKeySound);
        }

        this.config.setProperty(Config.quad_crate_timer, quadCrateTimer);

        this.config.setProperty(Config.disabled_worlds, disabledWorlds);
        this.config.setProperty(Config.disabled_worlds_toggle, this.config.getProperty(Config.disabled_worlds).isEmpty());

        if (invName != null) this.mainMenuConfig.setProperty(CrateMainMenu.crate_menu_title, convert(invName));
        this.mainMenuConfig.setProperty(CrateMainMenu.crate_menu_toggle, enableCrateMenu);
        this.mainMenuConfig.setProperty(CrateMainMenu.crate_menu_size, invSize);

        this.mainMenuConfig.setProperty(CrateMainMenu.crate_menu_filler_toggle, fillerToggle);
        if (fillerItem != null) this.mainMenuConfig.setProperty(CrateMainMenu.crate_menu_filler_item, fillerItem);
        if (fillerName != null) this.mainMenuConfig.setProperty(CrateMainMenu.crate_menu_filler_name, convert(fillerName));
        this.mainMenuConfig.setProperty(CrateMainMenu.crate_menu_filler_lore, convert(fillerLore));

        if (menuItem != null) this.previewMenuConfig.setProperty(CratePreviewMenu.crate_preview_menu_button_material, menuItem);
        if (menuName != null) this.previewMenuConfig.setProperty(CratePreviewMenu.crate_preview_menu_button_name, convert(menuName));

        this.previewMenuConfig.setProperty(CratePreviewMenu.crate_preview_menu_button_lore, convert(menuLore));

        if (nextItem != null) this.previewMenuConfig.setProperty(CratePreviewMenu.crate_preview_next_button_material, nextItem);
        if (nextName != null) this.previewMenuConfig.setProperty(CratePreviewMenu.crate_preview_next_button_name, convert(nextName));

        this.previewMenuConfig.setProperty(CratePreviewMenu.crate_preview_next_button_lore, convert(nextLore));

        if (backItem != null) this.previewMenuConfig.setProperty(CratePreviewMenu.crate_preview_back_button_material, backItem);
        if (backName != null) this.previewMenuConfig.setProperty(CratePreviewMenu.crate_preview_back_button_name, convert(backName));

        this.previewMenuConfig.setProperty(CratePreviewMenu.crate_preview_back_button_lore, convert(backLore));

        this.config.setProperty(Config.customizer, guiCustomizer);
        this.config.setProperty(Config.customizer_toggle, this.config.getProperty(Config.customizer).isEmpty());

        this.config.save();
        this.previewMenuConfig.save();
        this.mainMenuConfig.save();

        output.delete();

        return true;
    }

    private boolean copyMessages() {
        // The messages.yml
        File input = new File(this.plugin.getDataFolder(), "messages.yml");

        if (!input.exists()) return false;

        // The old configuration of messages.yml
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(input);

        // All the values of the old file.
        String unknownCommand = yamlConfiguration.getString("Messages.Unknown-Command");

        String noTeleporting = yamlConfiguration.getString("Messages.No-Teleporting");

        String noCommandsWhileInCrate = yamlConfiguration.getString("Messages.No-Commands-While-In-Crate");

        String noKeys = yamlConfiguration.getString("Messages.No-Key");

        String noVirtualKeys = yamlConfiguration.getString("Messages.No-Virtual-Key");

        String noPrizesFound = yamlConfiguration.getString("Messages.No-Prizes-Found");

        String noSchematicsFound = yamlConfiguration.getString("Messages.No-Schematics-Found");

        String internalError = yamlConfiguration.getString("Messages.Internal-Error");

        String mustBePlayer = yamlConfiguration.getString("Messages.Must-Be-A-Player");
        String mustBeConsole = yamlConfiguration.getString("Messages.Must-Be-A-Console-Sender");
        String mustBeLookingAtBlock = yamlConfiguration.getString("Messages.Must-Be-Looking-At-A-Block");

        String featureDisabled = yamlConfiguration.getString("Messages.Feature-Disabled");

        String correctUsage = yamlConfiguration.getString("Messages.Correct-Usage");

        String playerNotOnline = yamlConfiguration.getString("Messages.Not-Online");

        String samePlayer = yamlConfiguration.getString("Messages.Same-Player");

        String noPermission = yamlConfiguration.getString("Messages.No-Permission");

        String inventoryFull = yamlConfiguration.getString("Messages.Inventory-Full");

        String cannotGive = yamlConfiguration.getString("Messages.Cannot-Give-Player-Keys");

        String obtainingKeys = yamlConfiguration.getString("Messages.Obtaining-Keys");

        String closeAnotherPlayer = yamlConfiguration.getString("Messages.To-Close-To-Another-Player");

        String notACrate = yamlConfiguration.getString("Messages.Not-A-Crate");
        String notANumber = yamlConfiguration.getString("Messages.Not-A-Number");
        String notOnBlock = yamlConfiguration.getString("Messages.Not-On-Block");
        String outOfTime = yamlConfiguration.getString("Messages.Out-Of-Time");

        String previewDisabled = yamlConfiguration.getString("Messages.Preview-Disabled");

        String crateAlreadyOpened = yamlConfiguration.getString("Messages.Crate-Already-Opened");

        String quickCrateInUse = yamlConfiguration.getString("Messages.Quick-Crate-In-Use");

        String cannotBeVirtualCrate = yamlConfiguration.getString("Messages.Cant-Be-A-Virtual-Crate");
        String needsRoom = yamlConfiguration.getString("Messages.Needs-More-Room");

        String worldDisabled = yamlConfiguration.getString("Messages.World-Disabled");
        String removedPhysCrate = yamlConfiguration.getString("Messages.Removed-Physical-Crate");

        String openedCrate = yamlConfiguration.getString("Messages.Opened-A-Crate");

        String givenPlayerKeys = yamlConfiguration.getString("Messages.Given-A-Player-Keys");

        List<String> prizeError = Collections.singletonList(yamlConfiguration.getString("Messages.Prize-Error"));

        String cannotGivePlayerKeys = yamlConfiguration.getString("Messages.Cannot-Give-Player-Keys");

        String givenEveryoneKeys = yamlConfiguration.getString("Messages.Given-Everyone-Keys");

        String givenOfflinePlayerKeys = yamlConfiguration.getString("Messages.Given-Offline-Player-Keys");

        String takePlayerKeys = yamlConfiguration.getString("Messages.Take-A-Player-Keys");

        String takeOfflineKeys = yamlConfiguration.getString("Messages.Take-Offline-Player-Keys");

        String noItemInHand = yamlConfiguration.getString("Messages.No-Item-In-Hand");
        String addedItem = yamlConfiguration.getString("Messages.Added-Item-With-Editor");

        String filesConvertedNone = yamlConfiguration.getString("Messages.Files-Converted.No-Files-To-Convert");
        String filesConvertedError = yamlConfiguration.getString("Messages.Files-Converted.Error-Converting-Files");

        String reload = yamlConfiguration.getString("Messages.Reload");

        String transferKeys = yamlConfiguration.getString("Messages.Transfer-Keys.Not-Enough-Keys");
        String transferredKeys = yamlConfiguration.getString("Messages.Transfer-Keys.Transferred-Keys");

        String gotTransferKeys = yamlConfiguration.getString("Messages.Transfer-Keys.Received-Transferred-Keys");

        String personalNoVirtualKeys = yamlConfiguration.getString("Messages.Keys.Personal.No-Virtual-Keys");
        List<String> personalHeader = yamlConfiguration.getStringList("Messages.Keys.Personal.Header");

        String otherPlayer = yamlConfiguration.getString("Messages.Keys.Other-Player.No-Virtual-Keys");
        List<String> otherHeader = yamlConfiguration.getStringList("Messages.Keys.Other-Player.Header");

        String perCrate = yamlConfiguration.getString("Messages.Keys.Per-Crate");

        List<String> playerHelp = yamlConfiguration.getStringList("Messages.Help");
        List<String> adminHelp = yamlConfiguration.getStringList("Messages.Admin-Help");

        List<String> physicalCrate = yamlConfiguration.getStringList("Messages.Created-Physical-Crate");

        String requiredKeys = yamlConfiguration.getString("Messages.Required-Keys");

        this.messages.setProperty(Messages.unknown_command, convert(unknownCommand));
        this.messages.setProperty(Messages.no_teleporting, convert(noTeleporting));
        this.messages.setProperty(Messages.no_commands, convert(noCommandsWhileInCrate));
        this.messages.setProperty(Messages.no_keys, convert(noKeys));
        this.messages.setProperty(Messages.no_virtual_keys, convert(noVirtualKeys));

        this.messages.setProperty(Messages.feature_disabled, convert(featureDisabled));
        this.messages.setProperty(Messages.correct_usage, convert(correctUsage));
        this.messages.setProperty(Messages.no_prizes_found, convert(noPrizesFound));
        this.messages.setProperty(Messages.no_schematics_found, convert(noSchematicsFound));
        this.messages.setProperty(Messages.internal_error, convert(internalError));

        this.messages.setProperty(Messages.player_requirements_must_be_player, convert(mustBePlayer));
        this.messages.setProperty(Messages.player_requirements_must_be_console_sender, convert(mustBeConsole));
        this.messages.setProperty(Messages.player_requirements_must_be_looking_at_block, convert(mustBeLookingAtBlock));

        this.messages.setProperty(Messages.player_requirements_target_not_online, convert(playerNotOnline));
        this.messages.setProperty(Messages.player_requirements_same_player, convert(samePlayer));
        this.messages.setProperty(Messages.player_requirements_no_permission, convert(noPermission));
        this.messages.setProperty(Messages.player_requirements_inventory_not_empty, convert(inventoryFull));
        this.messages.setProperty(Messages.player_requirements_obtaining_keys, convert(obtainingKeys));
        this.messages.setProperty(Messages.player_requirements_too_close_to_another_player, convert(closeAnotherPlayer));

        this.messages.setProperty(Messages.crate_requirements_not_a_crate, convert(notACrate));
        this.messages.setProperty(Messages.crate_requirements_not_a_number, convert(notANumber));

        this.messages.setProperty(Messages.crate_requirements_not_on_block, convert(notOnBlock));
        this.messages.setProperty(Messages.crate_requirements_out_of_time, convert(outOfTime));
        this.messages.setProperty(Messages.crate_requirements_preview_disabled, convert(previewDisabled));
        this.messages.setProperty(Messages.crate_requirements_already_open, convert(crateAlreadyOpened));
        this.messages.setProperty(Messages.crate_requirements_in_use, convert(quickCrateInUse));
        this.messages.setProperty(Messages.crate_requirements_cannot_be_a_virtual_crate, convert(cannotBeVirtualCrate));
        this.messages.setProperty(Messages.crate_requirements_need_more_room, convert(needsRoom));
        this.messages.setProperty(Messages.crate_requirements_world_disabled, convert(worldDisabled));
        this.messages.setProperty(Messages.crates_physical_crate_removed, convert(removedPhysCrate));

        this.messages.setProperty(Messages.command_open_crate, convert(openedCrate));

        this.messages.setProperty(Messages.command_give_player_keys, convert(givenPlayerKeys));
        this.messages.setProperty(Messages.command_give_cannot_give_player_keys, convert(cannotGive));
        this.messages.setProperty(Messages.player_requirements_inventory_not_empty, convert(cannotGivePlayerKeys));
        this.messages.setProperty(Messages.command_give_everyone_keys, convert(givenEveryoneKeys));
        this.messages.setProperty(Messages.command_give_offline_player_keys, convert(givenOfflinePlayerKeys));

        this.messages.setProperty(Messages.command_take_player_keys, convert(takePlayerKeys));
        this.messages.setProperty(Messages.command_take_offline_player_keys, convert(takeOfflineKeys));

        this.messages.setProperty(Messages.command_add_item_no_item_in_hand, convert(noItemInHand));
        this.messages.setProperty(Messages.command_add_item_from_hand, convert(addedItem));

        this.messages.setProperty(Messages.command_convert_no_files_to_convert, convert(filesConvertedNone));
        this.messages.setProperty(Messages.command_convert_error_converting_files, convert(filesConvertedError));

        this.messages.setProperty(Messages.command_reload_completed, convert(reload));

        this.messages.setProperty(Messages.command_transfer_not_enough_keys, convert(transferKeys));
        this.messages.setProperty(Messages.command_transfer_keys, convert(transferredKeys));
        this.messages.setProperty(Messages.command_transfer_keys_received, convert(gotTransferKeys));

        this.messages.setProperty(Messages.command_keys_personal_no_virtual_keys, convert(personalNoVirtualKeys));
        this.messages.setProperty(Messages.command_keys_personal_virtual_keys_header, convert(personalHeader));

        this.messages.setProperty(Messages.command_keys_other_player_no_virtual_keys, convert(otherPlayer));
        this.messages.setProperty(Messages.command_keys_other_player_virtual_keys_header, convert(otherHeader));

        this.messages.setProperty(Messages.command_keys_crate_format, convert(perCrate));

        this.messages.setProperty(Messages.player_help, convert(playerHelp));
        this.messages.setProperty(Messages.admin_help, convert(adminHelp));

        this.messages.setProperty(Messages.prize_error, convert(prizeError));

        this.messages.setProperty(Messages.command_convert_successfully_converted_files, "&aPlugin conversion has succeeded!");

        this.messages.setProperty(Messages.crates_physical_crate_created, convert(physicalCrate));

        this.messages.setProperty(Messages.player_requirements_required_keys, convert(requiredKeys));

        this.messages.save();

        input.renameTo(new File(this.plugin.getDataFolder(), "old-messages.yml"));
        input.delete();

        //TODO() Only use this when there is more then one file in locale folder.
        /*this.bukkitPlugin.getFileUtils().copyFiles(
                new File(this.plugin.getDataFolder(), "locale").toPath(),
                "locale",
                List.of(
                        "en-US.yml"
                )
        );*/

        return true;
    }

    // Convert old placeholder lists.
    private List<String> convert(List<String> list) {
        List<String> newList = new ArrayList<>();

        list.forEach(line -> newList.add(convert(line)));

        return newList;
    }

    // Convert old placeholders.
    private String convert(String message) {
        return message
                .replaceAll("%crate%", "{crate}")
                .replaceAll("%key%", "{key}")
                .replaceAll("%world%", "{world}")
                .replaceAll("%player%", "{player}")
                .replaceAll("%number%", "{number}")
                .replaceAll("%amount%", "{amount}")
                .replaceAll("%page%", "{page}")
                .replaceAll("%keys%", "{keys}")
                .replaceAll("%usage%", "{usage}")
                .replaceAll("%prize%", "{prize}")
                .replaceAll("%key-amount%", "{key-amount}")
                .replaceAll("%prefix%", "{prefix}")
                .replaceAll("%id%", "{id}");
    }
}