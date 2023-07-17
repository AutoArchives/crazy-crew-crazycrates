package com.badbones69.crazycrates.paper.api.v2.crates;

import com.badbones69.crazycrates.core.enums.CrateType;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CrateConfig extends YamlConfiguration {

    private final File file;

    private final String path = "Crate.";

    public CrateConfig(File file) {
        this.file = file;
    }

    public void load() {
        try {
            load(file);

            if (getString("Crate") != null) {
                set("crate-settings.type", getString(path + "CrateType"));
                set("crate-settings.name", getString(path + "CrateName"));

                set("crate-settings.key.name", getString(path + "PhysicalKey.Name"));
                set("crate-settings.key.lore", getStringList(path + "PhysicalKey.Lore"));
                set("crate-settings.key.display.item", getString(path + "PhysicalKey.Item"));
                set("crate-settings.key.display.glowing", getBoolean(path + "PhysicalKey.Glowing"));

                set("holograms.toggle", getString(path + "Hologram.Toggle"));
                set("holograms.height", getString(path + "Hologram.Height"));
                set("holograms.display.lines", getString(path + "Hologram.Message"));

                set("crate-preview.toggle", getBoolean(path + "Preview.Toggle"));
                set("crate-preview.name", getString(path + "Preview-Name"));
                set("crate-preview.rows", getInt(path + "Preview-ChestLines"));

                set("crate-preview.glass.toggle", getBoolean(path + "Preview.Glass.Toggle"));
                set("crate-preview.glass.name", getString(path + "Preview.Glass.Name"));
                set("crate-preview.glass.item", getString(path + "Preview.Glass.Item"));

                set("starting-keys.toggle", false);
                set("starting-keys.amount", getInt(path + "StartingKeys"));

                set("misc.mass-open.min", 3);
                set("misc.mass-open.max", getInt(path + "Max-Mass-Open"));

                set("misc.opening-broadcast.toggle", getBoolean(path + "OpeningBroadCast"));
                set("misc.opening-broadcast.message", getString(path + "BroadCast"));

                set("gui.toggle", getBoolean(path + "InGUI"));
                set("gui.slot", getInt(path + "Slot"));

                set("gui.display.name", getString(path + "Name"));
                set("gui.display.lore", getStringList(path + "Lore"));

                set("gui.display.item", getStringList(path + "Item"));

                set("gui.glowing", getBoolean(path + "Glowing"));

                ConfigurationSection section = getConfigurationSection("Prizes");

                if (section != null) {
                    section.getKeys(false).forEach(value -> {
                        String prizePath = path + "Prizes." + value;
                        String newPath = "prizes." + value;

                        String id = getString(prizePath + ".DisplayItem", "Stone");
                        String name = getString(prizePath + ".DisplayName", "");
                        List<String> lore = getStringList(prizePath + ".Lore");

                        String player = getString(prizePath + ".Player", "");
                        set(newPath + ".player", player);

                        boolean glowing = getBoolean(prizePath + ".Glowing", false);
                        set(newPath + ".glowing", glowing);

                        int amount = getInt(prizePath + ".DisplayAmount", 1);
                        set(newPath + ".display.amount", amount);

                        boolean unbreakable = getBoolean(prizePath + ".Unbreakable", false);
                        set(newPath + ".unbreakable", unbreakable);

                        boolean hideItemFlags = getBoolean(prizePath + ".HideItemsFlags", false);
                        set(newPath + ".hide-item-flags", hideItemFlags);

                        List<String> enchantments = getStringList(prizePath + ".DisplayEnchantments");
                        set(newPath + ".display.enchantments", enchantments);

                        // Misc
                        boolean fireworks = getBoolean(prizePath + ".Firework");
                        set(newPath + ".fireworks.toggle", fireworks);

                        int chance = getInt(prizePath + ".Chance");
                        set(newPath + ".chance", chance);

                        int range = getInt(prizePath + ".MaxRange");
                        set(newPath + ".max-range", range);

                        List<String> blacklisted = getStringList(prizePath + ".BlackListed-Permissions");
                        set(newPath + ".permissions.blacklisted.toggle", false);
                        set(newPath + ".permissions.blacklisted.values", blacklisted);

                        List<String> commands = getStringList(prizePath + ".Commands");
                        set(newPath + ".commands.toggle", false);
                        set(newPath + ".commands.values", commands);

                        List<String> messages = getStringList(prizePath + ".Messages");
                        set(newPath + ".messages.toggle", false);
                        set(newPath + ".messages.values", messages);

                        // Alternative Prizes
                        boolean altPrizeToggle = getBoolean(prizePath + ".Alternative-Prize.Toggle");
                        List<String> altPrizeMessageList = getStringList(prizePath + ".Alternative-Prize.Messages");
                        List<String> altPrizeCommandList = getStringList(prizePath + ".Alternative-Prize.Commands");

                        // If it contains it, set the old values to new.
                        if (contains(prizePath + ".Alternative-Prize")) {
                            set(newPath + ".permissions.alternative-prize.toggle", altPrizeToggle);

                            set(newPath + ".permissions.alternative-prize.messages.values", altPrizeMessageList);
                            set(newPath + ".permissions.alternative-prize.messages.toggle", false);

                            set(newPath + ".permissions.alternative-prize.commands.values", altPrizeCommandList);
                            set(newPath + ".permissions.alternative-prize.commands.toggle", false);
                        }

                        // If it contains it, set the old values to new.
                        if (contains(prizePath + ".Editor-Items")) {
                            List<?> items = getList(prizePath + ".Editor-Items");

                            ArrayList<ItemStack> editorItems = new ArrayList<>();

                            if (items != null) {
                                items.forEach(item -> editorItems.add((ItemStack) item));

                                set(newPath + ".editor-items", editorItems);
                            }
                        }

                        set(newPath + ".display.name", name);
                        set(newPath + ".display.item", id);

                        set(newPath + ".display.lore.toggle", false);
                        set(newPath + ".display.lore.lines", lore);

                        if (contains(prizePath + ".Firework")) set(newPath + ".fireworks.toggle", false);

                        // Cosmic Crate
                        if (contains(prizePath + ".Tiers") && Objects.requireNonNull(getString(path + "Type")).equalsIgnoreCase(CrateType.cosmic.getCrateType())) {
                            List<String> tiers = getStringList(prizePath + ".Tiers");

                            set(newPath + ".cosmic-settings.tiers", tiers);
                        }

                        set(newPath + ".cosmic-settings.total-prize-amount", getInt(path + "Crate-Type-Settings.Total-Prize-Amount"));
                        set(newPath + ".cosmic-settings.mystery-crate.item",  getString(path + "Crate-Type-Settings.Mystery-Crate.Item"));
                        set(newPath + ".cosmic-settings.mystery-crate.name",  getString(path + "Crate-Type-Settings.Mystery-Crate.Name"));
                        set(newPath + ".cosmic-settings.mystery-crate.lore", getStringList(path + "Crate-Type-Settings.Mystery-Crate.Lore"));

                        set(newPath + ".cosmic-settings.picked-crate.item",  getString(path + "Crate-Type-Settings.Picked-Crate.Item"));
                        set(newPath + ".cosmic-settings.picked-crate.name",  getString(path + "Crate-Type-Settings.Picked-Crate.Name"));
                        set(newPath + ".cosmic-settings.picked-crate.lore", getStringList(path + "Crate-Type-Settings.Picked-Crate.Lore"));

                        set(path + "Crate-Type-Settings", null);
                    });
                }

                set("Crate", null);

                save(file);
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the file
     */
    public File getFile() {
        return this.file;
    }

    public String getCrateName() {
        return getFile().getName().replace(".yml", "");
    }

    public Configuration getConfiguration() {
        return this;
    }

    /**
     * Save the file.
     */
    public void save() {
        try {
            save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return crate type
     */
    public String getType() {
        return getString("crate-settings.type", "CSGO");
    }

    /**
     * Get the display name of the crate and check if it isn't null.
     *
     * @return display name
     */
    public String getDisplayName() {
        return getString("crate-settings.name", "&cYou should change this in " + this.file.getName());
    }

    /**
     * Get the display name of the key and check if it isn't null.
     *
     * @return key name
     */
    public String getKeyName() {
        return getString("crate-settings.key.name", "&cYou should change this in " + this.file.getName());
    }

    /**
     * Check if the lore is empty and print out a debug message if verbose logging is enabled.
     *
     * @return lore
     */
    public List<String> getKeyLore() {
        List<String> lore = getStringList("crate-settings.key.lore");

        if (lore.isEmpty() || getString("crate-settings.key.lore") == null) return Collections.emptyList();

        return lore;
    }

    /**
     * @return get the key's material
     */
    public ItemStack getKeyItem() {
        String material = getString("crate-settings.key.display.item", "TRIPWIRE_HOOK");

        return new ItemStack(Material.valueOf(material));
    }

    /**
     * @return whether the key glows
     */
    public boolean isKeyGlowing() {
        return getBoolean("crate-settings.key.display.glowing", false);
    }

    /**
     * Check if holograms are enabled.
     *
     * @return true or false
     */
    public boolean isHologramEnabled() {
        return getBoolean("holograms.toggle", false);
    }

    /**
     * How far above the crate should the hologram be?
     *
     * @return the distance
     */
    public double getHologramHeight() {
        return getDouble("holograms.height", 1.5);
    }

    /**
     * @return list of text to display
     */
    public List<String> getHologramMessages() {
        List<String> lines = getStringList("holograms.display.lines");

        if (lines.isEmpty() || getString("holograms.display.lines") == null) return Collections.emptyList();

        return lines;
    }

    public boolean isCratePreviewEnabled() {
        return getBoolean("crate-preview.toggle", false);
    }

    public String getCratePreviewName() {
        return getString("crate-preview.name", "&cError: Preview name is missing from &e" + this.file.getName());
    }

    public int getCratePreviewRows() {
        return getInt("crate-preview.rows", 6);
    }

    public boolean isCratePreviewGlassEnabled() {
        return getBoolean("crate-preview.glass.toggle", false);
    }

    public String getCratePreviewGlassName() {
        return getString("crate-preview.glass.name", " ");
    }

    public String getCratePreviewGlassItem() {
        return getString("crate-preview.glass.item", "GRAY_STAINED_GLASS_PANE");
    }

    public boolean isStartingKeysEnabled() {
        return getBoolean("starting-keys.toggle", false);
    }

    public int getStartingKeysAmount() {
        return getInt("starting-keys.amount", 1);
    }

    public boolean isMassOpenEnabled() {
        return getBoolean("misc.mass-open.toggle", false);
    }

    public int getMassOpenMin() {
        return getInt("misc.mass-open.min", 3);
    }

    public int getMassOpenMax() {
        return getInt("misc.mass-open.max", 10);
    }

    public boolean isOpeningBroadcastEnabled() {
        return getBoolean("misc.opening-broadcast.toggle", false);
    }

    public String getOpeningBroadcastMessage() {
        return getString("misc.opening-broadcast.message", "&cError: Opening broadcast message is missing from &e" + this.file.getName());
    }

    public boolean isGuiEnabled() {
        return getBoolean("gui.toggle", true);
    }

    public int getGuiSlot() {
        return getInt("gui.slot", 22);
    }

    public String getGuiDisplayName() {
        return getString("gui.display.name", "Change this in " + this.file.getName());
    }

    public List<String> getGuiDisplayLore() {
        List<String> lines = getStringList("gui.display.lore");

        if (lines.isEmpty() || getString("gui.display.lore") == null) return Collections.emptyList();

        return lines;
    }

    public String getGuiDisplayItem() {
        return getString("gui.display.name", "CHEST");
    }

    public boolean isGuiItemGlowing() {
        return getBoolean("gui.glowing", false);
    }

    public List<PrizeConfig> getPrizes() {
        ArrayList<PrizeConfig> prizes = new ArrayList<>();

        ConfigurationSection prizeSection = getConfigurationSection("prizes");

        if (prizeSection == null) return Collections.unmodifiableList(prizes);

        Set<String> keys = prizeSection.getKeys(false);

        for (String reward : keys) {
            ConfigurationSection section = prizeSection.getConfigurationSection(reward);

            if (section == null) continue;

            PrizeConfig prizeConfig = new PrizeConfig(
                    CrateType.getFromName(getType()),
                    getCrateName(),
                    section.getString("display.name", "&cError"),
                    section.getString("display.item", "STONE"),
                    section.getString("display.trim-pattern", ""),
                    section.getString("display.trim-material", ""),
                    section.getString("display.player", ""),
                    section.getInt("display.amount", 1),
                    section.getStringList("display.enchantments"),
                    section.getBoolean("display.lore.toggle", false),
                    section.getStringList("display.lore.lines"));

            prizes.add(prizeConfig);
        }

        return Collections.unmodifiableList(prizes);
    }
}