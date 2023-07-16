package com.badbones69.crazycrates.paper.api.v2.crates;

import com.badbones69.crazycrates.paper.api.v2.crates.types.CrateType;
import java.util.Collections;
import java.util.List;

public class PrizeConfig {

    private final CrateType crateType;
    private final String crateName;
    private final String displayName;
    private final String item;
    private final String trimPattern;
    private final String trimMaterial;
    private final String player;
    private final int amount;
    private final List<String> enchantments;
    private final boolean hasLore;
    private final List<String> lore;

    public PrizeConfig(CrateType crateType, String crateName, String displayName, String item, String trimPattern, String trimMaterial, String player, int amount, List<String> enchantments, boolean hasLore, List<String> lore) {
        this.crateType = crateType;
        this.crateName = crateName;
        this.displayName = displayName;

        this.item = item;
        this.trimPattern = trimPattern;
        this.trimMaterial = trimMaterial;
        this.player = player;
        this.amount = amount;
        this.enchantments = enchantments != null ? enchantments : Collections.emptyList();

        this.hasLore = hasLore;

        this.lore = lore != null ? lore : Collections.emptyList();
    }

    // Crate Settings.
    public CrateType getCrateType() {
        return this.crateType;
    }

    public String getCrateName() {
        return this.crateName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    // Item Preview.
    public String getItem() {
        return this.item;
    }

    public String getTrimMaterial() {
        return trimMaterial;
    }

    public String getTrimPattern() {
        return trimPattern;
    }

    public String getPlayer() {
        return this.player;
    }

    public int getAmount() {
        return this.amount;
    }

    public boolean hasLore() {
        return this.hasLore;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public List<String> getEnchantments() {
        return this.enchantments;
    }
}