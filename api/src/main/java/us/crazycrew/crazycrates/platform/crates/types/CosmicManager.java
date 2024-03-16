package us.crazycrew.crazycrates.platform.crates.types;

import org.simpleyaml.configuration.ConfigurationSection;
import us.crazycrew.crazycrates.platform.crates.CrateConfig;
import java.util.Collections;
import java.util.List;

public class CosmicManager extends AbstractCrateManager {

    private final CrateConfig config;

    public CosmicManager(CrateConfig config) {
        this.config = config;
    }

    public ConfigurationSection getTierSection() {
        return this.config.getCrateSection().getConfigurationSection("Tiers");
    }

    public int getTotalPrizeAmount() {
        return this.config.getCrateSection().getInt("Crate-Type-Settings.Total-Prize-Amount", 4);
    }

    public String getMysteryCrateItem() {
        return this.config.getCrateSection().getString("Crate-Type-Settings.Mystery-Crate.Item", "CHEST");
    }

    public String getMysteryCrateName() {
        return this.config.getCrateSection().getString("Crate-Type-Settings.Mystery-Crate.Name", "&f&l???");
    }

    public List<String> getMysteryCrateLore() {
        List<String> keys = this.config.getCrateSection().getStringList("Crate-Type-Settings.Mystery-Crate.Lore");

        return keys == null ? Collections.emptyList() : keys;
    }

    public String getPickedCrateItem() {
        return this.config.getCrateSection().getString("Crate-Type-Settings.Picked-Crate.Item", "CHEST");
    }

    public String getPickedCrateName() {
        return this.config.getCrateSection().getString("Crate-Type-Settings.Picked-Crate.Name", "&f&l???");
    }

    public List<String> getPickedCrateLore() {
        List<String> keys = this.config.getCrateSection().getStringList("Crate-Type-Settings.Picked-Crate.Lore");

        return keys == null ? Collections.emptyList() : keys;
    }

    public boolean isTierPreviewEnabled() {
        return this.config.getCrateSection().getBoolean("tier-preview.toggle", true);
    }

    public int getTierPreviewRows() {
        return this.config.getCrateSection().getInt("tier-preview.rows", 5);
    }

    public boolean isTierPreviewFillerEnabled() {
        return this.config.getCrateSection().getBoolean("tier-preview.glass.toggle", true);
    }

    public String getTierPreviewFillerName() {
        return this.config.getCrateSection().getString("tier-preview.glass.name", " ");
    }

    public String getTierPreviewFillerItem() {
        return this.config.getCrateSection().getString("tier-preview.glass.item", "red_stained_glass_pane");
    }
}