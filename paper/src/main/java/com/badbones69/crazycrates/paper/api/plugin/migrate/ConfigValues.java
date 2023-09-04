package com.badbones69.crazycrates.paper.api.plugin.migrate;

import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.FileManager;
import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;

public enum ConfigValues {

    TOGGLE_METRICS(FileManager.Files.CONFIG.getFile(), "plugin-config.yml", "Settings.Toggle-Metrics", "toggle_metrics");

    private final FileConfiguration oldFileConfiguration;
    private final FileConfiguration newFileCOnfiguration;
    private final File newFile;
    private final File oldFile;
    private final String oldPath;
    private final String newPath;

    ConfigValues(FileConfiguration oldFile, String newFile, String oldPath, String newPath) {
        this.oldFileConfiguration = oldFile;

        @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

        this.oldFile = new File(plugin.getDataFolder(), FileManager.Files.CONFIG.getFileName());

        this.newFile = new File(plugin.getDataFolder(), newFile);

        this.newFileCOnfiguration = YamlConfiguration.loadConfiguration(this.newFile);

        this.oldPath = oldPath;
        this.newPath = newPath;
    }

    public static void convert() {
        for (ConfigValues values : ConfigValues.values()) {
            if (values.oldFileConfiguration.contains(values.oldPath)) {
                FancyLogger.warn("Attempting to migrate " + values.oldFileConfiguration.get(values.oldPath) + " to " + values.newPath);

                values.newFileCOnfiguration.set(values.newPath, values.oldFileConfiguration.get(values.oldPath));

                try {
                    values.newFileCOnfiguration.save(values.newFile);

                    values.oldFileConfiguration.set(values.oldPath, null);
                    values.oldFileConfiguration.save(values.oldFile);
                } catch (IOException e) {
                    FancyLogger.debug("Failed to save file.");
                    FancyLogger.warn(e.getMessage());
                }

                FancyLogger.debug("Migration successful!");

                return;
            }
        }
    }
}