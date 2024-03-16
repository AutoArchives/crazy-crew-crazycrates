package us.crazycrew.crazycrates.platform.crates.types;

import org.simpleyaml.configuration.ConfigurationSection;
import us.crazycrew.crazycrates.platform.crates.CrateConfig;

public class CasinoManager extends AbstractCrateManager {

    private final CrateConfig config;

    public CasinoManager(CrateConfig config) {
        this.config = config;
    }

    public ConfigurationSection getTierSection() {
        return this.config.getCrateSection().getConfigurationSection("Tiers");
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

    public boolean isTiersRandom() {
        return this.config.getCrateSection().getBoolean("random.toggle", false);
    }

    public String getTierOne() {
        return this.config.getCrateSection().getString("random.types.row-1", "Basic");
    }

    public String getTierTwo() {
        return this.config.getCrateSection().getString("random.types.row-2", "UnCommon");
    }

    public String getTierThree() {
        return this.config.getCrateSection().getString("random.types.row-3", "Rare");
    }
}