package us.crazycrew.crazycrates.paper.api.plugin.migrate.enums;

import us.crazycrew.crazycrates.paper.CrazyCrates;
import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;

public enum ConfigOptions {

    TOGGLE_METRICS("plugin-config.yml", "config.yml", "Settings.Toggle-Metrics", "toggle_metrics"),
    PLUGIN_PREFIX("plugin-config.yml", "config.yml", "Settings.Prefix", "prefix.command");

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private File file;
    private String oldPath;
    private String newPath;
    private boolean isList;
    private boolean isInteger;
    private boolean isDouble;
    private boolean isBoolean;

    private FileConfiguration previousConfiguration;

    public void setMultiplePath(FileConfiguration newConfiguration, FileConfiguration previousConfiguration) {
        if (this.isList) {
            newConfiguration.set(this.newPath, previousConfiguration.getStringList(this.oldPath));

            previousConfiguration.set(this.oldPath, null);

            return;
        }

        if (this.isInteger) {
            newConfiguration.set(this.newPath, previousConfiguration.getInt(this.oldPath));

            previousConfiguration.set(this.oldPath, null);

            return;
        }

        if (this.isDouble) {
            newConfiguration.set(this.newPath, previousConfiguration.getDouble(this.oldPath));

            previousConfiguration.set(this.oldPath, null);

            return;
        }

        if (this.isBoolean) {
            newConfiguration.set(this.newPath, previousConfiguration.getBoolean(this.oldPath));

            previousConfiguration.set(this.oldPath, null);

            return;
        }

        newConfiguration.set(this.newPath, previousConfiguration.getString(this.oldPath));

        previousConfiguration.set(this.oldPath, null);
    }

    public void saveMultiple(FileConfiguration newConfiguration, FileConfiguration previousConfiguration) {
        try {
            newConfiguration.save(this.newFile);

            previousConfiguration.save(this.file);
        } catch (IOException exception) {
            FancyLogger.error("Failed to save file.");
            FancyLogger.warn(exception.getMessage());
        }
    }

    /**
     * A section for config.yml -> plugin-config.yml as an example.
     */
    private YamlConfiguration newConfiguration;
    private File newFile;

    /**
     * Used for file to file conversions.
     *
     * @param newFileName the name of the file we are migrating to.
     * @param oldFileName the name of the file we are migrating from.
     * @param oldPath the old path we are trying to migrate.
     * @param newPath the new path we are migrating to.
     */
    ConfigOptions(String newFileName, String oldFileName, String oldPath, String newPath) {
        File file = new File(this.plugin.getDataFolder(), oldFileName);

        if (!file.exists()) return;

        File newFile = new File(this.plugin.getDataFolder(), newFileName);

        if (!newFile.exists()) return;

        this.file = file;

        this.newFile = newFile;

        this.newConfiguration = YamlConfiguration.loadConfiguration(newFile);

        this.previousConfiguration = YamlConfiguration.loadConfiguration(file);

        this.oldPath = oldPath;
        this.newPath = newPath;

        this.isList = this.previousConfiguration.isList(this.oldPath);
        this.isBoolean = this.previousConfiguration.isBoolean(this.oldPath);
        this.isDouble = this.previousConfiguration.isInt(this.oldPath);
        this.isInteger = this.previousConfiguration.isInt(this.oldPath);
    }

    public String getOldPath() {
        return this.oldPath;
    }

    public String getNewPath() {
        return this.newPath;
    }

    public File getFile() {
        return this.file;
    }

    public File getNewFile() {
        return this.newFile;
    }

    public FileConfiguration getPreviousConfiguration() {
        return this.previousConfiguration;
    }

    public YamlConfiguration getNewConfiguration() {
        return this.newConfiguration;
    }
}