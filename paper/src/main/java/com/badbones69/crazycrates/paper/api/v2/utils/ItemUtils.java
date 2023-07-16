package com.badbones69.crazycrates.paper.api.v2.utils;

import com.badbones69.crazycrates.paper.api.v2.crates.CrateConfig;
import com.badbones69.crazycrates.paper.api.v2.objects.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;

public class ItemUtils {

    public static ItemStack getItemStack(CrateConfig crateConfig, ConfigurationSection section) {
        return getItemStack(crateConfig, section, section.getInt("display.chance"), section.getInt("display.max-range"));
    }

    /**
     * Builds a prize for the preview.
     *
     * @param crateConfig the crate config object.
     * @param section the configuration section i.e prizes
     * @param chance the chance
     * @param maxRange the max range
     * @return the completed prize
     */
    public static ItemStack getItemStack(CrateConfig crateConfig, ConfigurationSection section, int chance, int maxRange) {
        if (section == null) return null;

        String path = "display.";

        Material material = getMaterial(section.getString(path + "item"));

        ItemBuilder builder = new ItemBuilder();

        String itemName = section.getString(path + "name", "&cName missing at Number #" + crateConfig.getConfiguration().getString("prizes." + section));

        builder.setMaterial(material).setPlayerName(section.getString(path + "player", "")).setAmount(section.getInt(path + "amount", 1)).setName(itemName);

        // TODO() Re-work how enchants are displayed.
        HashMap<Enchantment, Integer> enchantments = new HashMap<>();
        for (String enchantmentName : section.getStringList(path + "enchantments")) {
            Enchantment enchantment = getEnchantment(enchantmentName.split(":")[0]);

            if (enchantment != null) {
                enchantments.put(enchantment, Integer.parseInt(enchantmentName.split(":")[1]));
            }
        }

        builder.setEnchantments(enchantments);

        boolean isLoreEnabled = section.getBoolean(path + "lore.toggle", false);

        if (isLoreEnabled) {
            builder.addLorePlaceholder("{chance}", String.valueOf(chance))
                    .addLorePlaceholder("{max-range}", String.valueOf(maxRange))
                    .addLorePlaceholder("{crate}", crateConfig.getDisplayName())
                    .setLore(section.getStringList(path + "lore.lines"));
        }

        builder.setUnbreakable(section.getBoolean(path + "unbreakable", false))
                .hideItemFlags(section.getBoolean(path + "hide-item-flags", false))
                .setGlow(section.getBoolean(path + "glowing", false));

        builder.setCrateName(crateConfig.getCrateName());

        return builder.build();
    }

    public static Material getMaterial(String materialName) {
        if (materialName == null) return null;

        return Material.matchMaterial(materialName);
    }

    public static Enchantment getEnchantment(String enchantmentName) {
        HashMap<String, String> enchantments = getEnchantmentList();
        enchantmentName = stripEnchantmentName(enchantmentName);

        for (Enchantment enchantment : Enchantment.values()) {
            try {
                if (stripEnchantmentName(enchantment.getKey().getKey()).equalsIgnoreCase(enchantmentName)) {
                    return enchantment;
                }

                if (stripEnchantmentName(enchantment.getName()).equalsIgnoreCase(enchantmentName) || (enchantments.get(enchantment.getName()) != null &&
                        stripEnchantmentName(enchantments.get(enchantment.getName())).equalsIgnoreCase(enchantmentName))) {
                    return enchantment;
                }
            } catch (Exception ignore) {}
        }

        return null;
    }

    private static String stripEnchantmentName(String enchantmentName) {
        return enchantmentName != null ? enchantmentName.replace("-", "").replace("_", "").replace(" ", "") : null;
    }

    private static HashMap<String, String> getEnchantmentList() {
        HashMap<String, String> enchantments = new HashMap<>();
        enchantments.put("ARROW_DAMAGE", "Power");
        enchantments.put("ARROW_FIRE", "Flame");
        enchantments.put("ARROW_INFINITE", "Infinity");
        enchantments.put("ARROW_KNOCKBACK", "Punch");
        enchantments.put("DAMAGE_ALL", "Sharpness");
        enchantments.put("DAMAGE_ARTHROPODS", "Bane_Of_Arthropods");
        enchantments.put("DAMAGE_UNDEAD", "Smite");
        enchantments.put("DEPTH_STRIDER", "Depth_Strider");
        enchantments.put("DIG_SPEED", "Efficiency");
        enchantments.put("DURABILITY", "Unbreaking");
        enchantments.put("FIRE_ASPECT", "Fire_Aspect");
        enchantments.put("KNOCKBACK", "KnockBack");
        enchantments.put("LOOT_BONUS_BLOCKS", "Fortune");
        enchantments.put("LOOT_BONUS_MOBS", "Looting");
        enchantments.put("LUCK", "Luck_Of_The_Sea");
        enchantments.put("LURE", "Lure");
        enchantments.put("OXYGEN", "Respiration");
        enchantments.put("PROTECTION_ENVIRONMENTAL", "Protection");
        enchantments.put("PROTECTION_EXPLOSIONS", "Blast_Protection");
        enchantments.put("PROTECTION_FALL", "Feather_Falling");
        enchantments.put("PROTECTION_FIRE", "Fire_Protection");
        enchantments.put("PROTECTION_PROJECTILE", "Projectile_Protection");
        enchantments.put("SILK_TOUCH", "Silk_Touch");
        enchantments.put("THORNS", "Thorns");
        enchantments.put("WATER_WORKER", "Aqua_Affinity");
        enchantments.put("BINDING_CURSE", "Curse_Of_Binding");
        enchantments.put("MENDING", "Mending");
        enchantments.put("FROST_WALKER", "Frost_Walker");
        enchantments.put("VANISHING_CURSE", "Curse_Of_Vanishing");
        enchantments.put("SWEEPING_EDGE", "Sweeping_Edge");
        enchantments.put("RIPTIDE", "Riptide");
        enchantments.put("CHANNELING", "Channeling");
        enchantments.put("IMPALING", "Impaling");
        enchantments.put("LOYALTY", "Loyalty");
        return enchantments;
    }
}