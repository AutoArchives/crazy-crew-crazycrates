package com.badbones69.crazycrates.folia.api.managers;

import com.badbones69.crazycrates.folia.api.objects.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;

public class CosmicCrateManager extends CrateManager {
    
    private final FileConfiguration file;
    private final int totalPrizes;
    private final com.badbones69.crazycrates.folia.api.objects.ItemBuilder mysteryCrate;
    private final com.badbones69.crazycrates.folia.api.objects.ItemBuilder pickedCrate;
    
    public CosmicCrateManager(FileConfiguration file) {
        this.file = file;
        String path = "Crate.Crate-Type-Settings.";
        totalPrizes = file.getInt(path + "Total-Prize-Amount", 4);
        mysteryCrate = new com.badbones69.crazycrates.folia.api.objects.ItemBuilder()
        .setMaterial(file.getString(path + "Mystery-Crate.Item", "CHEST"))
        .setName(file.getString(path + "Mystery-Crate.Name", "&f&l???"))
        .setLore(file.contains(path + "Mystery-Crate.Lore") ? file.getStringList(path + "Mystery-Crate.Lore") : Collections.singletonList("&7You may choose 4 crates."));
        mysteryCrate.getNBTItem().setString("Cosmic-Mystery-Crate", "Mystery Crate");
        pickedCrate = new com.badbones69.crazycrates.folia.api.objects.ItemBuilder()
        .setMaterial(file.getString(path + "Picked-Crate.Item", Material.GLASS_PANE.toString()))
        .setName(file.getString(path + "Picked-Crate.Name", "&f&l???"))
        .setLore(file.contains(path + "Picked-Crate.Lore") ? file.getStringList(path + "Picked-Crate.Lore") : Collections.singletonList("&7You have chosen #%slot%."));
        pickedCrate.getNBTItem().setString("Cosmic-Picked-Crate", "Picked Crate");
    }
    
    public FileConfiguration getFile() {
        return file;
    }
    
    public int getTotalPrizes() {
        return totalPrizes;
    }
    
    public com.badbones69.crazycrates.folia.api.objects.ItemBuilder getMysteryCrate() {
        return new com.badbones69.crazycrates.folia.api.objects.ItemBuilder(mysteryCrate);
    }
    
    public ItemBuilder getPickedCrate() {
        return pickedCrate;
    }
}