package us.crazycrew.crazycrates.paper.api.plugin.migrate;

import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.api.plugin.migrate.enums.ConfigOptions;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MigrationService {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final String prefix = "Settings.";

    public void migrate() {
        for (ConfigOptions configOptions : ConfigOptions.values()) {
            if (configOptions.getFile() == null || configOptions.getNewFile() == null) return;

            if (!configOptions.getPreviousConfiguration().contains(configOptions.getOldPath())) return;

            FancyLogger.warn("Attempting to migrate " + configOptions.getOldPath() + " from " + configOptions.getFile().getName() + " to " + configOptions.getNewPath() + " in " + configOptions.getNewFile().getName());

            configOptions.setMultiplePath(configOptions.getNewConfiguration(), configOptions.getPreviousConfiguration());
            configOptions.saveMultiple(configOptions.getNewConfiguration(), configOptions.getPreviousConfiguration());
        }

        copyConfigSettings();
    }

    private void copyConfigSettings() {
        File output = new File(this.plugin.getDataFolder(), "config-v1.yml");

        File input = new File(this.plugin.getDataFolder(), "config.yml");

        // The old configuration of config.yml.
        YamlConfiguration config;
        YamlConfiguration config_v1 = null;

        try {
            config = YamlConfiguration.loadConfiguration(input);

            if (config.getString(prefix + "Enable-Crate-Menu") == null && !output.exists()) return;

            input.renameTo(output);

            if (!input.exists()) input.createNewFile();

            config_v1 = YamlConfiguration.loadConfiguration(output);
        } catch (IOException exception) {
            FancyLogger.error("Failed to convert/create " + input.getName());
            FancyLogger.debug(exception.getMessage());
        }

        if (config_v1 == null) return;

        boolean enableCrateMenu = config_v1.getBoolean(prefix + "Enable-Crate-Menu");
        boolean crateLogFile = config_v1.getBoolean(prefix + "Crate-Actions.Log-File");
        boolean crateLogConsole = config_v1.getBoolean(prefix + "Crate-Actions.Log-Console");

        String invName = config_v1.getString(prefix + "InventoryName");
        int invSize = config_v1.getInt(prefix + "InventorySize");

        boolean knockBack = config_v1.getBoolean(prefix + "KnockBack");

        boolean physAcceptsVirtual = config_v1.getBoolean(prefix + "Physical-Accepts-Virtual-Keys");
        boolean physAcceptsPhys = config_v1.getBoolean(prefix + "Physical-Accepts-Physical-Keys");
        boolean virtualAcceptsPhys = config_v1.getBoolean(prefix + "Virtual-Accepts-Physical-Keys");
        boolean giveVirtualKeysInventoryMessage = config_v1.getBoolean(prefix + "Give-Virtual-Keys-When-Inventory-Full-Message");
        boolean giveVirtualKeysInventory = config_v1.getBoolean(prefix + "Give-Virtual-Keys-When-Inventory-Full");

        // TODO() Move this to per crate.
        String needKeySound = config_v1.getString(prefix + "Need-Key-Sound");

        // TODO() Move this to QuadCrate type.
        int quadCrateTimer = config_v1.getInt(prefix + "QuadCrate.Timer");

        List<String> disabledWorlds = config_v1.getStringList(prefix + "DisabledWorlds");

        // TODO() Move this to its own configuration file.
        String menuName = config_v1.getString(prefix + "Preview.Buttons.Menu.Name");
        String menuItem = config_v1.getString(prefix + "Preview.Buttons.Menu.Item");
        List<String> menuLore = config_v1.getStringList(prefix + "Preview.Buttons.Menu.Lore");

        String nextName = config_v1.getString(prefix + "Preview.Buttons.Next.Name");
        String nextItem = config_v1.getString(prefix + "Preview.Buttons.Next.Item");
        List<String> nextLore = config_v1.getStringList(prefix + "Preview.Buttons.Next.Lore");

        String backName = config_v1.getString(prefix + "Preview.Buttons.Back.Name");
        String backItem = config_v1.getString(prefix + "Preview.Buttons.Back.Item");
        List<String> backLore = config_v1.getStringList(prefix + "Preview.Buttons.Back.Lore");

        boolean fillerToggle = config_v1.getBoolean(prefix + "Filler.Toggle");
        String fillerItem = config_v1.getString(prefix + "Filler.Item");
        String fillerName = config_v1.getString(prefix + "Filler.Name");
        List<String> fillerLore = config_v1.getStringList(prefix + "Filler.Lore");

        List<String> guiCustomizer = config_v1.getStringList(prefix + "GUI-Customizer");

        YamlConfiguration other = YamlConfiguration.loadConfiguration(input);

        other.set("crate-settings.crate-actions.log-to-file", crateLogFile);
        other.set("crate-settings.crate-actions.log-to-console", crateLogConsole);

        other.set("crate-settings.preview-menu.toggle", enableCrateMenu);
        other.set("crate-settings.preview-menu.name", invName);
        other.set("crate-settings.preview-menu.size", invSize);

        other.set("crate-settings.knock-back", knockBack);
        other.set("crate-settings.keys.physical-accepts-virtual-keys", physAcceptsVirtual);
        other.set("crate-settings.keys.physical-accepts-physical-keys", physAcceptsPhys);
        other.set("crate-settings.keys.virtual-accepts-physical-keys", virtualAcceptsPhys);

        other.set("crate-settings.keys.inventory-not-empty.give-virtual-keys-message", giveVirtualKeysInventoryMessage);
        other.set("crate-settings.keys.inventory-not-empty.give-virtual-keys", giveVirtualKeysInventory);

        other.set("crate-settings.keys.key-sound.name", needKeySound);

        other.set("crate-settings.quad-crate.timer", quadCrateTimer);

        other.set("crate-settings.disabled-worlds.worlds", disabledWorlds);

        other.set("gui-settings.filler-items.toggle", fillerToggle);
        other.set("gui-settings.filler-items.item", fillerItem);
        other.set("gui-settings.filler-items.name", fillerName);

        other.set("gui-settings.filler-items.lore", fillerLore);

        other.set("gui-settings.buttons.menu.item", menuItem);
        other.set("gui-settings.buttons.menu.name", menuName);
        other.set("gui-settings.buttons.menu.lore", menuLore);

        other.set("gui-settings.buttons.next.item", nextItem);
        other.set("gui-settings.buttons.next.name", nextName);
        other.set("gui-settings.buttons.next.lore", nextLore);

        other.set("gui-settings.buttons.back.item", backItem);
        other.set("gui-settings.buttons.back.name", backName);
        other.set("gui-settings.buttons.back.lore", backLore);

        other.set("gui-settings.customizer.items", guiCustomizer);

        try {
            other.save(input);
            output.delete();
        } catch (IOException exception) {
            FancyLogger.error("Failed to finish the conversion");
            FancyLogger.debug(exception.getMessage());
        }
    }
}