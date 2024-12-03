package com.badbones69.crazycrates.api.enums.misc;

import com.badbones69.crazycrates.CrazyCrates;
import com.ryderbelserion.vital.files.FileManager;
import com.ryderbelserion.vital.files.enums.FileType;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.io.File;

public enum Files {

    respin_gui(FileType.YAML, "respin-gui.yml", "guis"),

    crate_log(FileType.NONE, "crates.log", "logs"),
    key_log(FileType.NONE, "keys.log", "logs"),

    locations(FileType.YAML, "locations.yml"),
    data(FileType.YAML, "data.yml");


    private final FileType fileType;
    private final String fileName;
    private final String folder;

    private final CrazyCrates plugin = CrazyCrates.getPlugin();

    private final FileManager fileManager = this.plugin.getVital().getFileManager();

    /**
     * A constructor to build a file
     *
     * @param fileName the name of the file
     */
    Files(final FileType fileType, final String fileName, final String folder) {
        this.fileType = fileType;
        this.fileName = fileName;
        this.folder = folder;
    }

    /**
     * A constructor to build a file
     *
     * @param fileName the name of the file
     */
    Files(final FileType fileType, final String fileName) {
        this.fileType = fileType;
        this.fileName = fileName;
        this.folder = null;
    }

    public final CommentedConfigurationNode getConfiguration() {
        return this.fileManager.getFile(this.fileName, this.fileType).getConfigurationNode();
    }

    public void reload() {
        this.fileManager.addFile(this.fileName, this.folder, false, this.fileType);
    }

    public void save() {
        this.fileManager.saveFile(this.fileName, this.fileType);
    }

    public final File getFile() {
        return new File(this.folder == null ? this.plugin.getDataFolder() : new File(this.plugin.getDataFolder(), this.folder), this.fileName);
    }
}