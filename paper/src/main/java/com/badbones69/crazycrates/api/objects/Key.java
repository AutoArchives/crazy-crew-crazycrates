package com.badbones69.crazycrates.api.objects;

import com.badbones69.crazycrates.api.builders.ItemBuilder;
import com.ryderbelserion.cluster.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import us.crazycrew.crazycrates.platform.keys.KeyConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Key {

    private final Material material;
    private final String name;
    private final List<String> lore;

    private final List<ItemFlag> flags;

    private final boolean isUnbreakable;

    private final boolean isGlowing;

    public Key(KeyConfig config) {
        this.material = ItemUtils.getMaterial(config.getMaterial());

        this.name = config.getName();

        this.lore = config.getLore() == null ? Collections.emptyList() : config.getLore();

        if (config.getItemFlags() == null) {
            this.flags = Collections.emptyList();
        } else {
            this.flags = new ArrayList<>();

            config.getItemFlags().forEach(line -> this.flags.add(ItemFlag.valueOf(line)));
        }

        this.isUnbreakable = config.isUnbreakable();
        this.isGlowing = config.isGlowing();
    }

    public Material getMaterial() {
        return this.material;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public List<ItemFlag> getFlags() {
        return this.flags;
    }

    public boolean isUnbreakable() {
        return this.isUnbreakable;
    }

    public boolean isGlowing() {
        return this.isGlowing;
    }

    public ItemStack getKey() {
        return new ItemBuilder().setMaterial(getMaterial()).setName(getName()).setLore(getLore()).setGlow(isGlowing()).setUnbreakable(isUnbreakable()).setItemFlags(getFlags()).build();
    }
}