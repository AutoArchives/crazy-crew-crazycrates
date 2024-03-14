package us.crazycrew.crazycrates.platform.crates;

import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlFile;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.platform.crates.types.AbstractCrateManager;
import us.crazycrew.crazycrates.platform.crates.types.CasinoManager;
import us.crazycrew.crazycrates.platform.crates.types.CosmicManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CrateConfig extends YamlFile {

    private AbstractCrateManager crateManager;
    private final YamlFile file;

    public CrateConfig(File file) {
        this.file = new YamlFile(file.getPath());
    }

    @Override
    public ConfigurationSection getConfigurationSection(String path) {
        return this.file.getConfigurationSection(path);
    }

    @Override
    public void load() throws IOException {
        this.file.createOrLoadWithComments();

        Set<String> keys = getConfigurationSection("Crate.Prizes").getKeys(false);

        for (String key : keys) {
            ConfigurationSection prizeSection = getConfigurationSection(key);

            if (prizeSection != null) {
                if (prizeSection.contains("DisplayEnchantments")) {
                    List<String> finalEnchantments = new ArrayList<>();

                    prizeSection.getStringList("DisplayEnchantments").forEach(line -> finalEnchantments.add(line.replaceAll("PROTECTION_ENVIRONMENTAL", "protection")
                            .replaceAll("PROTECTION_FIRE", "fire_protection")
                            .replaceAll("PROTECTION_FALL", "feather_falling")
                            .replaceAll("PROTECTION_EXPLOSIONS", "blast_protection")
                            .replaceAll("PROTECTION_PROJECTILE", "projectile_protection")
                            .replaceAll("OXYGEN", "respiration")
                            .replaceAll("WATER_WORKER", "aqua_affinity")
                            .replaceAll("DAMAGE_ALL", "sharpness")
                            .replaceAll("DAMAGE_UNDEAD", "smite")
                            .replaceAll("DAMAGE_ARTHROPODS", "bane_of_arthropods")
                            .replaceAll("LOOT_BONUS_MOBS", "looting")
                            .replaceAll("SWEEPING_EDGE", "sweeping")
                            .replaceAll("DIG_SPEED", "efficiency")
                            .replaceAll("DURABILITY", "unbreaking")
                            .replaceAll("LOOT_BONUS_BLOCKS", "fortune")
                            .replaceAll("ARROW_DAMAGE", "power")
                            .replaceAll("ARROW_KNOCKBACK", "punch")
                            .replaceAll("ARROW_FIRE", "flame")
                            .replaceAll("ARROW_INFINITE", "infinity")
                            .replaceAll("LUCK", "luck_of_the_sea")));

                    prizeSection.set("DisplayEnchantments", finalEnchantments);
                }
            }

            this.file.save();
            this.file.loadWithComments();
        }

        switch (getCrateType()) {
            case cosmic -> this.crateManager = new CosmicManager(this);

            case casino -> this.crateManager = new CasinoManager(this);
        }
    }

    /**
     * @return the file
     */
    public YamlFile getFile() {
        return this.file;
    }

    /**
     * @return crate type specific options.
     */
    public AbstractCrateManager getCrateManager() {
        return this.crateManager;
    }

    public ConfigurationSection getCrateSection() {
        return getConfigurationSection("Crate");
    }

    public ConfigurationSection getSoundSection() {
        return getCrateSection().getConfigurationSection("sound");
    }

    public ConfigurationSection getPreviewSection() {
        return getCrateSection().getConfigurationSection("Preview");
    }

    public ConfigurationSection getHologramSection() {
        return getCrateSection().getConfigurationSection("Hologram");
    }

    public ConfigurationSection getPrizeSection() {
        return getCrateSection().getConfigurationSection("Prizes");
    }

    public CrateType getCrateType() {
        return CrateType.getFromName(getCrateSection().getString("CrateType", "CSGO"));
    }

    public String getCrateName() {
        return getCrateSection().getString("CrateName", " ");
    }
}