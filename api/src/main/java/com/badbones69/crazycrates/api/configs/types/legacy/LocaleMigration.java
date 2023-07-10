package com.badbones69.crazycrates.api.configs.types.legacy;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;

public class LocaleMigration extends YamlConfiguration {

    private final File file;
    private final JavaPlugin plugin;

    public LocaleMigration(File file, JavaPlugin plugin) {
        this.file = file;

        this.plugin = plugin;
    }

    public void load() {
        try {
            load(this.file);

            ConfigurationSection legacySection = getConfigurationSection("Messages");

            if (legacySection != null) {
                for (LocaleEnum value : LocaleEnum.values()) {
                    value.setMessage(this, this.plugin);

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