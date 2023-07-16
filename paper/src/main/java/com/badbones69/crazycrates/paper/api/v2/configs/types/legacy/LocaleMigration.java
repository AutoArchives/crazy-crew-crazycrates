package com.badbones69.crazycrates.paper.api.v2.configs.types.legacy;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class LocaleMigration extends YamlConfiguration {

    private final File file;

    public LocaleMigration(File file) {
        this.file = file;
    }

    public void load() {
        try {
            load(this.file);

            ConfigurationSection legacySection = getConfigurationSection("Messages");

            if (legacySection != null) {
                for (LocaleEnum value : LocaleEnum.values()) {
                    value.setMessage(this);

                    save();
                }

                set("Messages", null);
                save();
            }
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }
    }

    public void save() {
        try {
            save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}