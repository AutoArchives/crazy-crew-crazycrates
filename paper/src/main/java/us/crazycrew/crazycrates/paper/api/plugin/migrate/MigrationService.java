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

        this.pluginConfig.setProperty(PluginConfig.TOGGLE_METRICS, oldMetrics);
        if (oldPrefix != null) this.pluginConfig.setProperty(PluginConfig.COMMAND_PREFIX, oldPrefix);

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

        this.config.setProperty(MainConfig.LOG_TO_FILE, crateLogFile);
        this.config.setProperty(MainConfig.LOG_TO_CONSOLE, crateLogConsole);

        this.config.setProperty(MainConfig.CRATE_KNOCK_BACK, knockBack);

        this.config.setProperty(MainConfig.PHYSICAL_ACCEPTS_VIRTUAL, physAcceptsVirtual);
        this.config.setProperty(MainConfig.PHYSICAL_ACCEPTS_PHYSICAL, physAcceptsPhys);
        this.config.setProperty(MainConfig.VIRTUAL_ACCEPTS_PHYSICAL_KEYS, virtualAcceptsPhys);

        this.config.setProperty(MainConfig.GIVE_VIRTUAL_KEYS, giveVirtualKeysInventory);
        this.config.setProperty(MainConfig.GIVE_VIRTUAL_KEYS_MESSAGE, giveVirtualKeysInventoryMessage);

        if (needKeySound != null) {
            this.config.setProperty(MainConfig.KEY_SOUND_TOGGLE, needKeySound.isBlank());

            this.config.setProperty(MainConfig.KEY_SOUND_NAME, needKeySound);
        }

        this.config.setProperty(MainConfig.QUAD_CRATE_TIMER, quadCrateTimer);

        this.config.setProperty(MainConfig.DISABLED_WORLDS, disabledWorlds);
        this.config.setProperty(MainConfig.DISABLED_WORLDS_TOGGLE, this.config.getProperty(MainConfig.DISABLED_WORLDS).isEmpty());

        if (invName != null) this.crateMainConfig.setProperty(CrateMainMenu.CRATE_MENU_TITLE, invName);
        this.crateMainConfig.setProperty(CrateMainMenu.CRATE_MENU_TOGGLE, enableCrateMenu);
        this.crateMainConfig.setProperty(CrateMainMenu.CRATE_MENU_SIZE, invSize);

        this.crateMainConfig.setProperty(CrateMainMenu.CRATE_MENU_FILLER_TOGGLE, fillerToggle);
        if (fillerItem != null) this.crateMainConfig.setProperty(CrateMainMenu.CRATE_MENU_FILLER_ITEM, fillerItem);
        if (fillerName != null) this.crateMainConfig.setProperty(CrateMainMenu.CRATE_MENU_FILLER_NAME, fillerName);
        this.crateMainConfig.setProperty(CrateMainMenu.CRATE_MENU_FILLER_LORE, fillerLore);

        if (menuItem != null) this.cratePreviewConfig.setProperty(CratePreviewMenu.CRATE_PREVIEW_MENU_BUTTON_MATERIAL, menuItem);
        if (menuName != null) this.cratePreviewConfig.setProperty(CratePreviewMenu.CRATE_PREVIEW_MENU_BUTTON_NAME, menuName);

        this.cratePreviewConfig.setProperty(CratePreviewMenu.CRATE_PREVIEW_MENU_BUTTON_LORE, menuLore);

        if (nextItem != null) this.cratePreviewConfig.setProperty(CratePreviewMenu.CRATE_PREVIEW_NEXT_BUTTON_MATERIAL, nextItem);
        if (nextName != null) this.cratePreviewConfig.setProperty(CratePreviewMenu.CRATE_PREVIEW_NEXT_BUTTON_NAME, nextName);

        this.cratePreviewConfig.setProperty(CratePreviewMenu.CRATE_PREVIEW_NEXT_BUTTON_LORE, nextLore);

        if (backItem != null) this.cratePreviewConfig.setProperty(CratePreviewMenu.CRATE_PREVIEW_BACK_BUTTON_MATERIAL, backItem);
        if (backName != null) this.cratePreviewConfig.setProperty(CratePreviewMenu.CRATE_PREVIEW_BACK_BUTTON_NAME, backName);

        this.cratePreviewConfig.setProperty(CratePreviewMenu.CRATE_PREVIEW_BACK_BUTTON_LORE, backLore);

        this.config.setProperty(MainConfig.CUSTOMIZER, guiCustomizer);
        this.config.setProperty(MainConfig.CUSTOMIZER_TOGGLE, this.config.getProperty(MainConfig.CUSTOMIZER).isEmpty());

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

        this.localeConfig.setProperty(LocaleConfig.UNKNOWN_COMMAND, convert(unknownCommand));
        this.localeConfig.setProperty(LocaleConfig.NO_TELEPORTING, convert(noTeleporting));
        this.localeConfig.setProperty(LocaleConfig.NO_COMMANDS, convert(noCommandsWhileInCrate));
        this.localeConfig.setProperty(LocaleConfig.NO_KEYS, convert(noKeys));
        this.localeConfig.setProperty(LocaleConfig.NO_VIRTUAL_KEYS, convert(noVirtualKeys));

        this.localeConfig.setProperty(LocaleConfig.FEATURE_DISABLED, convert(featureDisabled));
        this.localeConfig.setProperty(LocaleConfig.CORRECT_USAGE, convert(correctUsage));
        this.localeConfig.setProperty(LocaleConfig.NO_PRIZES_FOUND, convert(noPrizesFound));
        this.localeConfig.setProperty(LocaleConfig.NO_SCHEMATICS_FOUND, convert(noSchematicsFound));
        this.localeConfig.setProperty(LocaleConfig.INTERNAL_ERROR, convert(internalError));

        this.localeConfig.setProperty(LocaleConfig.PLAYER_REQUIREMENTS_MUST_BE_PLAYER, convert(mustBePlayer));
        this.localeConfig.setProperty(LocaleConfig.PLAYER_REQUIREMENTS_MUST_BE_CONSOLE_SENDER, convert(mustBeConsole));
        this.localeConfig.setProperty(LocaleConfig.PLAYER_REQUIREMENTS_MUST_BE_LOOKING_AT_BLOCK, convert(mustBeLookingAtBlock));

        this.localeConfig.setProperty(LocaleConfig.PLAYER_REQUIREMENTS_TARGET_NOT_ONLINE, convert(playerNotOnline));
        this.localeConfig.setProperty(LocaleConfig.PLAYER_REQUIREMENTS_SAME_PLAYER, convert(samePlayer));
        this.localeConfig.setProperty(LocaleConfig.PLAYER_REQUIREMENTS_NO_PERMISSION, convert(noPermission));
        this.localeConfig.setProperty(LocaleConfig.PLAYER_REQUIREMENTS_INVENTORY_NOT_EMPTY, convert(inventoryFull));
        this.localeConfig.setProperty(LocaleConfig.PLAYER_REQUIREMENTS_OBTAINING_KEYS, convert(obtainingKeys));
        this.localeConfig.setProperty(LocaleConfig.PLAYER_REQUIREMENTS_TOO_CLOSE_TO_ANOTHER_PLAYER, convert(closeAnotherPlayer));

        this.localeConfig.setProperty(LocaleConfig.CRATE_REQUIREMENTS_NOT_A_CRATE, convert(notACrate));
        this.localeConfig.setProperty(LocaleConfig.CRATE_REQUIREMENTS_NOT_A_NUMBER, convert(notANumber));

        this.localeConfig.setProperty(LocaleConfig.CRATE_REQUIREMENTS_NOT_ON_BLOCK, convert(notOnBlock));
        this.localeConfig.setProperty(LocaleConfig.CRATE_REQUIREMENTS_MUST_OUT_OF_TIME, convert(outOfTime));
        this.localeConfig.setProperty(LocaleConfig.CRATE_REQUIREMENTS_PREVIEW_DISABLED, convert(previewDisabled));
        this.localeConfig.setProperty(LocaleConfig.CRATE_REQUIREMENTS_ALREADY_OPEN, convert(crateAlreadyOpened));
        this.localeConfig.setProperty(LocaleConfig.CRATE_REQUIREMENTS_IN_USE, convert(quickCrateInUse));
        this.localeConfig.setProperty(LocaleConfig.CRATE_REQUIREMENTS_CANNOT_BE_A_VIRTUAL_CRATE, convert(cannotBeVirtualCrate));
        this.localeConfig.setProperty(LocaleConfig.CRATE_REQUIREMENTS_NEED_MORE_ROOM, convert(needsRoom));
        this.localeConfig.setProperty(LocaleConfig.CRATE_REQUIREMENTS_WORLD_DISABLED, convert(worldDisabled));
        this.localeConfig.setProperty(LocaleConfig.CRATES_PHYSICAL_CRATE_REMOVED, convert(removedPhysCrate));

        this.localeConfig.setProperty(LocaleConfig.COMMAND_OPEN_CRATE, convert(openedCrate));

        this.localeConfig.setProperty(LocaleConfig.COMMAND_GIVE_PLAYER_KEYS, convert(givenPlayerKeys));
        this.localeConfig.setProperty(LocaleConfig.PLAYER_REQUIREMENTS_INVENTORY_NOT_EMPTY, convert(cannotGivePlayerKeys));
        this.localeConfig.setProperty(LocaleConfig.COMMAND_GIVE_EVERYONE_KEYS, convert(givenEveryoneKeys));
        this.localeConfig.setProperty(LocaleConfig.COMMAND_GIVE_OFFLINE_PLAYER_KEYS, convert(givenOfflinePlayerKeys));

        this.localeConfig.setProperty(LocaleConfig.COMMAND_TAKE_PLAYER_KEYS, convert(takePlayerKeys));
        this.localeConfig.setProperty(LocaleConfig.COMMAND_TAKE_OFFLINE_PLAYER_KEYS, convert(takeOfflineKeys));

        this.localeConfig.setProperty(LocaleConfig.COMMAND_ADDITEM_NO_ITEM_IN_HAND, convert(noItemInHand));
        this.localeConfig.setProperty(LocaleConfig.COMMAND_ADD_ITEM_FROM_HAND, convert(addedItem));

        this.localeConfig.setProperty(LocaleConfig.COMMAND_CONVERT_NO_FILES_TO_CONVERT, convert(filesConvertedNone));
        this.localeConfig.setProperty(LocaleConfig.COMMAND_CONVERT_ERROR_CONVERTING_FILES, convert(filesConvertedError));

        this.localeConfig.setProperty(LocaleConfig.COMMAND_RELOAD_COMPLETED, convert(reload));

        this.localeConfig.setProperty(LocaleConfig.COMMAND_TRANSFER_NOT_ENOUGH_KEYS, convert(transferKeys));
        this.localeConfig.setProperty(LocaleConfig.COMMAND_TRANSFER_KEYS, convert(transferredKeys));
        this.localeConfig.setProperty(LocaleConfig.COMMAND_TRANSFER_KEYS_RECEIVED, convert(gotTransferKeys));

        this.localeConfig.setProperty(LocaleConfig.COMMAND_KEYS_PERSONAL_NO_VIRTUAL_KEYS, convert(personalNoVirtualKeys));
        this.localeConfig.setProperty(LocaleConfig.COMMAND_KEYS_PERSONAL_NO_VIRTUAL_KEYS_HEADER, convert(personalHeader));

        this.localeConfig.setProperty(LocaleConfig.COMMAND_KEYS_OTHER_PLAYER_NO_VIRTUAL_KEYS, convert(otherPlayer));
        this.localeConfig.setProperty(LocaleConfig.COMMAND_KEYS_OTHER_PLAYER_NO_VIRTUAL_KEYS_HEADER, convert(otherHeader));

        this.localeConfig.setProperty(LocaleConfig.COMMAND_KEYS_CRATE_FORMAT, convert(perCrate));

        this.localeConfig.setProperty(LocaleConfig.PLAYER_HELP, convert(playerHelp));
        this.localeConfig.setProperty(LocaleConfig.ADMIN_HELP, convert(adminHelp));

        this.localeConfig.setProperty(LocaleConfig.CRATES_PHYSICAL_CRATE_CREATED, convert(physicalCrate));

        this.localeConfig.setProperty(LocaleConfig.PLAYER_REQUIREMENTS_REQUIRED_KEYS, convert(requiredKeys));

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