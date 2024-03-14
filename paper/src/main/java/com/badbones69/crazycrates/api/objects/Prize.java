package com.badbones69.crazycrates.api.objects;

import com.badbones69.crazycrates.api.builders.ItemBuilder;
import com.badbones69.crazycrates.api.enums.PersistentKeys;
import com.ryderbelserion.cluster.utils.ItemUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.persistence.PersistentDataType;
import org.simpleyaml.configuration.ConfigurationSection;
import us.crazycrew.crazycrates.platform.utils.EnchantUtils;

import java.util.Collections;
import java.util.List;

public class Prize {

    private final ConfigurationSection section;


    private final ItemBuilder displayItem;

    private final List<String> commands;
    private final List<String> messages;

    private final String prizeNumber;

    private final boolean isFirework;

    private final int maxRange;
    private final int chance;

    /**
     * Builds a prize object.
     *
     * @param section the config section of the prize.
     */
    public Prize(ConfigurationSection section) {
        this.section = section;

        this.prizeNumber = this.section.getName();

        this.commands = section.getStringList("Commands") == null ? Collections.emptyList() : section.getStringList("Commands");
        this.messages = section.getStringList("Messages") == null ? Collections.emptyList() : section.getStringList("Messages");

        this.isFirework = section.getBoolean("Firework", false);

        this.maxRange = section.getInt("MaxRange", 100);
        this.chance = section.getInt("Chance", 25);

        this.displayItem = new ItemBuilder()
                .setMaterial(this.section.getString("DisplayItem", "RED_TERRACOTTA"))
                .setName(this.section.getString("DisplayName", WordUtils.capitalizeFully(section.getString("DisplayItem", "RED_TERRACOTTA").replaceAll("_", " "))))
                .setLore(section.getStringList("Lore") == null ? Collections.emptyList() : section.getStringList("Lore"))
                .setGlow(section.getBoolean("Glowing", false))
                .setUnbreakable(section.getBoolean("Unbreakable", false))
                .hideItemFlags(section.getBoolean("HideItemFlags", false))
                .addItemFlags(section.getStringList("Flags") == null ? Collections.emptyList() : section.getStringList("Flags"))
                .addPatterns(section.getStringList("Patterns") == null ? Collections.emptyList() : section.getStringList("Patterns"))
                .setPlayerName(section.getString("Player", " "))
                .setDamage(section.getInt("DisplayDamage", 0));

        if (this.section.contains("DisplayTrim.Pattern")) {
            TrimPattern pattern = ItemUtils.getTrimPattern(this.section.getString("DisplayTrim.Pattern", "sentry").toLowerCase());

            if (pattern != null) {
                this.displayItem.setTrimPattern(pattern);
            }
        }

        if (this.section.contains("DisplayTrim.Material")) {
            TrimMaterial material = ItemUtils.getTrimMaterial(this.section.getString("DisplayTrim.Material", "quartz").toLowerCase());

            if (material != null) {
                this.displayItem.setTrimMaterial(material);
            }
        }

        if (this.section.contains("DisplayEnchantments")) {
            for (String enchant : this.section.getStringList("DisplayEnchantments")) {
                Enchantment enchantment = ItemUtils.getEnchantment(EnchantUtils.getEnchant(enchant.split(":")[0]));

                if (enchantment != null) {
                    this.displayItem.addEnchantment(enchantment, Integer.parseInt(enchant.split(":")[1]));
                }
            }
        }

        ItemMeta itemMeta = this.displayItem.getItemMeta();
        itemMeta.getPersistentDataContainer().set(PersistentKeys.crate_prize.getNamespacedKey(), PersistentDataType.STRING, this.section.getName());
        this.displayItem.setItemMeta(itemMeta);
    }

    /**
     * @return the configuration section of the prize.
     */
    public ConfigurationSection getSection() {
        return this.section;
    }

    /**
     * Gets the name of the section which can be "fuckyou" or "1"
     *
     * @return the name of the section.
     */
    public String getPrizeNumber() {
        return this.prizeNumber;
    }

    /**
     * @return A list of commands to execute.
     */
    public List<String> getCommands() {
        return commands;
    }

    /**
     * @return A list of messages to send.
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * Whether to spawn a firework
     *
     * @return true or false
     */
    public boolean isFirework() {
        return isFirework;
    }

    /**
     * @return the max range of the chance.
     */
    public int getMaxRange() {
        return maxRange;
    }

    /**
     * @return the chance the prize can be won.
     */
    public int getChance() {
        return chance;
    }

    /**
     * Build an itemstack with no PlaceholderAPI support.
     *
     * @return an itemstack.
     */
    public ItemStack getDisplayItem() {
        return this.displayItem.build();
    }

    /**
     * Build an itemstack with PlaceholderAPI Support.
     *
     * @param player the player to use as reference.
     * @return an itemstack.
     */
    public ItemStack getDisplayItem(Player player) {
        return this.displayItem.setTarget(player).build();
    }
}