package us.crazycrew.crazycrates.platform.crates;

import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlConfiguration;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.platform.crates.types.AbstractCrateManager;
import us.crazycrew.crazycrates.platform.crates.types.CasinoManager;
import us.crazycrew.crazycrates.platform.crates.types.CosmicManager;
import java.io.File;
import java.io.IOException;

public class CrateConfig extends YamlConfiguration {

    private AbstractCrateManager crateManager;
    private final File file;

    public CrateConfig(File file) {
        this.file = file;
    }

    public void load() throws IOException {
        load(this.file);

        switch (getCrateType()) {
            case cosmic -> this.crateManager = new CosmicManager(this);

            case casino -> this.crateManager = new CasinoManager(this);
        }
    }

    /**
     * @return the file
     */
    public File getFile() {
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