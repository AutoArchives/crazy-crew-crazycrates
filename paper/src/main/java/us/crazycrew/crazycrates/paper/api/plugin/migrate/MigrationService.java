package us.crazycrew.crazycrates.paper.api.plugin.migrate;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.cluster.bukkit.BukkitPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.common.config.ConfigManager;
import us.crazycrew.crazycrates.common.config.LocaleConfig;
import us.crazycrew.crazycrates.common.config.MainConfig;
import us.crazycrew.crazycrates.common.config.PluginConfig;
import us.crazycrew.crazycrates.common.config.menus.CrateMainMenu;
import us.crazycrew.crazycrates.common.config.menus.CratePreviewMenu;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MigrationService {
    
    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull BukkitPlugin bukkitPlugin;

    private final @NotNull ConfigManager configManager;
    
    private SettingsManager config;
    private SettingsManager pluginConfig;
    private SettingsManager localeConfig;
    private SettingsManager crateMainConfig;
    private SettingsManager cratePreviewConfig;
    
    public MigrationService(@NotNull BukkitPlugin plugin) {
        this.bukkitPlugin = plugin;

        this.configManager = new ConfigManager(this.plugin.getDataFolder());
    }

    private final String prefix = "Settings.";

    public void migrate() {
        // Copy first
        copyPluginSettings();

        // Copy second
        copyConfigSettings();

        // Copy last
        copyMessages();
    }

    private void copyPluginSettings() {
        File input = new File(this.plugin.getDataFolder(),"config.yml");

        // The old configuration of config.yml.
        YamlConfiguration config;

        config = YamlConfiguration.loadConfiguration(input);

        if (config.getString(this.prefix + "Prefix") == null) return;

        this.configManager.load();

        this.config = this.configManager.getConfig();
        this.pluginConfig = this.configManager.getPluginConfig();
        this.localeConfig = this.configManager.getLocaleConfig();
        this.crateMainConfig = this.configManager.getMainMenuConfig();
        this.cratePreviewConfig = this.configManager.getPreviewMenuConfig();

        String oldPrefix = config.getString(this.prefix + "Prefix");
        boolean oldMetrics = config.getBoolean(this.prefix + "Toggle-Metrics");

        this.pluginConfig.setProperty(PluginConfig.toggle_metrics, oldMetrics);
        if (oldPrefix != null) this.pluginConfig.setProperty(PluginConfig.command_prefix, oldPrefix);

        config.set(this.prefix + "Prefix", null);
        config.set(this.prefix + "Toggle-Metrics", null);

        try {
            this.pluginConfig.save();

            config.save(input);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void copyConfigSettings() {
        File output = new File(this.plugin.getDataFolder(), "config-v1.yml");

        File configFile = new File(this.plugin.getDataFolder(), "config.yml");

        // The old config file.
        YamlConfiguration config;

        // The renamed config file.
        YamlConfiguration config_v1;

        // Only if the old value is found.
        config = YamlConfiguration.loadConfiguration(configFile);
        if (config.getString(this.prefix + "Enable-Crate-Menu") == null && !output.exists()) return;
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

        if (invName != null) this.crateMainConfig.setProperty(CrateMainMenu.crate_menu_title, invName);
        this.crateMainConfig.setProperty(CrateMainMenu.crate_menu_toggle, enableCrateMenu);
        this.crateMainConfig.setProperty(CrateMainMenu.crate_menu_size, invSize);

        this.crateMainConfig.setProperty(CrateMainMenu.crate_menu_filler_toggle, fillerToggle);
        if (fillerItem != null) this.crateMainConfig.setProperty(CrateMainMenu.crate_menu_filler_item, fillerItem);
        if (fillerName != null) this.crateMainConfig.setProperty(CrateMainMenu.crate_menu_filler_name, fillerName);
        this.crateMainConfig.setProperty(CrateMainMenu.crate_menu_filler_lore, fillerLore);

        if (menuItem != null) this.cratePreviewConfig.setProperty(CratePreviewMenu.crate_preview_menu_button_material, menuItem);
        if (menuName != null) this.cratePreviewConfig.setProperty(CratePreviewMenu.crate_preview_menu_button_name, menuName);

        this.cratePreviewConfig.setProperty(CratePreviewMenu.crate_preview_menu_button_lore, menuLore);

        if (nextItem != null) this.cratePreviewConfig.setProperty(CratePreviewMenu.crate_preview_next_button_material, nextItem);
        if (nextName != null) this.cratePreviewConfig.setProperty(CratePreviewMenu.crate_preview_next_button_name, nextName);

        this.cratePreviewConfig.setProperty(CratePreviewMenu.crate_preview_next_button_lore, nextLore);

        if (backItem != null) this.cratePreviewConfig.setProperty(CratePreviewMenu.crate_preview_back_button_material, backItem);
        if (backName != null) this.cratePreviewConfig.setProperty(CratePreviewMenu.crate_preview_back_button_name, backName);

        this.cratePreviewConfig.setProperty(CratePreviewMenu.crate_preview_back_button_lore, backLore);

        this.config.setProperty(Config.customizer, guiCustomizer);
        this.config.setProperty(Config.customizer_toggle, this.config.getProperty(Config.customizer).isEmpty());

        this.config.save();
        this.cratePreviewConfig.save();
        this.crateMainConfig.save();

        output.delete();
    }

    private void copyMessages() {
        // The messages.yml
        File input = new File(this.plugin.getDataFolder() + "/messages.yml");

        // The old configuration of messages.yml
        YamlConfiguration yamlConfiguration = null;

        if (input.exists()) yamlConfiguration = YamlConfiguration.loadConfiguration(input);

        if (yamlConfiguration == null) return;

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

        this.localeConfig.setProperty(Locale.unknown_command, convert(unknownCommand));
        this.localeConfig.setProperty(Locale.no_teleporting, convert(noTeleporting));
        this.localeConfig.setProperty(Locale.no_commands, convert(noCommandsWhileInCrate));
        this.localeConfig.setProperty(Locale.no_keys, convert(noKeys));
        this.localeConfig.setProperty(Locale.no_virtual_keys, convert(noVirtualKeys));

        this.localeConfig.setProperty(Locale.feature_disabled, convert(featureDisabled));
        this.localeConfig.setProperty(Locale.correct_usage, convert(correctUsage));
        this.localeConfig.setProperty(Locale.no_prizes_found, convert(noPrizesFound));
        this.localeConfig.setProperty(Locale.no_schematics_found, convert(noSchematicsFound));
        this.localeConfig.setProperty(Locale.internal_error, convert(internalError));

        this.localeConfig.setProperty(Locale.player_requirements_must_be_player, convert(mustBePlayer));
        this.localeConfig.setProperty(Locale.player_requirements_must_be_console_sender, convert(mustBeConsole));
        this.localeConfig.setProperty(Locale.player_requirements_must_be_looking_at_block, convert(mustBeLookingAtBlock));

        this.localeConfig.setProperty(Locale.player_requirements_target_not_online, convert(playerNotOnline));
        this.localeConfig.setProperty(Locale.player_requirements_same_player, convert(samePlayer));
        this.localeConfig.setProperty(Locale.player_requirements_no_permission, convert(noPermission));
        this.localeConfig.setProperty(Locale.player_requirements_inventory_not_empty, convert(inventoryFull));
        this.localeConfig.setProperty(Locale.player_requirements_obtaining_keys, convert(obtainingKeys));
        this.localeConfig.setProperty(Locale.player_requirements_too_close_to_another_player, convert(closeAnotherPlayer));

        this.localeConfig.setProperty(Locale.crate_requirements_not_a_crate, convert(notACrate));
        this.localeConfig.setProperty(Locale.crate_requirements_not_a_number, convert(notANumber));

        this.localeConfig.setProperty(Locale.crate_requirements_not_on_block, convert(notOnBlock));
        this.localeConfig.setProperty(Locale.crate_requirements_out_of_time, convert(outOfTime));
        this.localeConfig.setProperty(Locale.crate_requirements_preview_disabled, convert(previewDisabled));
        this.localeConfig.setProperty(Locale.crate_requirements_already_open, convert(crateAlreadyOpened));
        this.localeConfig.setProperty(Locale.crate_requirements_in_use, convert(quickCrateInUse));
        this.localeConfig.setProperty(Locale.crate_requirements_cannot_be_a_virtual_crate, convert(cannotBeVirtualCrate));
        this.localeConfig.setProperty(Locale.crate_requirements_need_more_room, convert(needsRoom));
        this.localeConfig.setProperty(Locale.crate_requirements_world_disabled, convert(worldDisabled));
        this.localeConfig.setProperty(Locale.crates_physical_crate_removed, convert(removedPhysCrate));

        this.localeConfig.setProperty(Locale.command_open_crate, convert(openedCrate));

        this.localeConfig.setProperty(Locale.command_give_player_keys, convert(givenPlayerKeys));
        this.localeConfig.setProperty(Locale.player_requirements_inventory_not_empty, convert(cannotGivePlayerKeys));
        this.localeConfig.setProperty(Locale.command_give_everyone_keys, convert(givenEveryoneKeys));
        this.localeConfig.setProperty(Locale.command_give_offline_player_keys, convert(givenOfflinePlayerKeys));

        this.localeConfig.setProperty(Locale.command_take_player_keys, convert(takePlayerKeys));
        this.localeConfig.setProperty(Locale.command_take_offline_player_keys, convert(takeOfflineKeys));

        this.localeConfig.setProperty(Locale.command_add_item_no_item_in_hand, convert(noItemInHand));
        this.localeConfig.setProperty(Locale.command_add_item_from_hand, convert(addedItem));

        this.localeConfig.setProperty(Locale.command_convert_no_files_to_convert, convert(filesConvertedNone));
        this.localeConfig.setProperty(Locale.command_convert_error_converting_files, convert(filesConvertedError));

        this.localeConfig.setProperty(Locale.command_reload_completed, convert(reload));

        this.localeConfig.setProperty(Locale.command_transfer_not_enough_keys, convert(transferKeys));
        this.localeConfig.setProperty(Locale.command_transfer_keys, convert(transferredKeys));
        this.localeConfig.setProperty(Locale.command_transfer_keys_received, convert(gotTransferKeys));

        this.localeConfig.setProperty(Locale.command_keys_personal_no_virtual_keys, convert(personalNoVirtualKeys));
        this.localeConfig.setProperty(Locale.command_keys_personal_no_virtual_keys_header, convert(personalHeader));

        this.localeConfig.setProperty(Locale.command_keys_other_player_no_virtual_keys, convert(otherPlayer));
        this.localeConfig.setProperty(Locale.command_keys_other_player_no_virtual_keys_header, convert(personalHeader));

        this.localeConfig.setProperty(Locale.command_keys_crate_format, convert(perCrate));

        this.localeConfig.setProperty(Locale.player_help, convert(playerHelp));
        this.localeConfig.setProperty(Locale.admin_help, convert(adminHelp));

        this.localeConfig.setProperty(Locale.crates_physical_crate_created, convert(physicalCrate));

        this.localeConfig.setProperty(Locale.player_requirements_required_keys, convert(requiredKeys));

        this.localeConfig.save();

        //TODO() Only use this when there is more then one file in locale folder.
        /*this.bukkitPlugin.getFileUtils().copyFiles(
                new File(this.plugin.getDataFolder(), "locale").toPath(),
                "locale",
                List.of(
                        "en-US.yml"
                )
        );*/
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
                .replaceAll("%keys%", "{keys}")
                .replaceAll("%usage%", "{usage}")
                .replaceAll("%prize%", "{prize}")
                .replaceAll("%key-amount%", "{key-amount}")
                .replaceAll("%prefix%", "{prefix}")
                .replaceAll("%id%", "{id}");
    }
}