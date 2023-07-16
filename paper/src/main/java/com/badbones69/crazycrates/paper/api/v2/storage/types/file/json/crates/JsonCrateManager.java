package com.badbones69.crazycrates.paper.api.v2.storage.types.file.json.crates;

import com.badbones69.crazycrates.paper.api.v2.storage.interfaces.LocationManager;
import com.badbones69.crazycrates.paper.api.v2.storage.CrateData;
import com.badbones69.crazycrates.core.frame.storage.FileHandler;
import com.badbones69.crazycrates.core.frame.storage.enums.StorageType;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

public non-sealed class JsonCrateManager extends JsonCrateData implements LocationManager {

    private final Path path;
    private final Server server;
    private final FileHandler fileHandler;

    public JsonCrateManager(Path path, Server server, FileHandler fileHandler) {
        super(path);

        this.path = path;
        this.server = server;
        this.fileHandler = fileHandler;
    }

    @Override
    public void load() {
        this.fileHandler.addFile(new JsonCrateData(this.path));
    }

    @Override
    public void save() {
        this.fileHandler.saveFile(new JsonCrateData(this.path));
    }

    @Override
    public void reload() {
        load();
        save();
    }

    @Override
    public void convert(File file, StorageType storageType) {
        //TODO() Switch between data types
    }

    @Override
    public void convertLegacy(File file, StorageType storageType) {
        if (!file.exists()) return;

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        ConfigurationSection section = configuration.getConfigurationSection("Locations");

        if (section == null) return;

        section.getKeys(false).forEach(id -> {
            String name = configuration.getString("Locations." + id + ".World");

            if (name == null) return;

            // Converting world to a unique id.
            World world = server.getWorld(name);

            int x = configuration.getInt("Locations." + id + ".X");
            int y = configuration.getInt("Locations." + id + ".Y");
            int z = configuration.getInt("Locations." + id + ".Z");

            String crateName = configuration.getString("Locations." + id + ".Crate");

            Location loc = new Location(world, x, y, z);

            addLocation(crateName, loc);
        });
    }

    @Override
    public void addLocation(String crateName, Location location) {
        CrateData crateData = new CrateData();

        // Check if the crate name already exists.
        if (!hasLocation(crateName)) {
            // Add the first location.
            crates.put(crateName, crateData);

            //crateData.addLocation(new CustomLocation(location.x(), location.y(), location.x()));

            return;
        }

        // Add the location if no previous location found.
        //crateData.addLocation(new CustomLocation(location.x(), location.y(), location.x()));
    }

    @Override
    public boolean hasLocation(String crateName) {
        return getCrates().containsKey(crateName);
    }

    @Override
    public CrateData getCrateLocation(String crateName) {
        if (hasLocation(crateName)) return getCrates().get(crateName);

        return null;
    }

    @Override
    public void removeLocation(String crateName, int id) {
        if (hasLocation(crateName)) crates.remove(crateName);
    }

    @Override
    public Map<String, CrateData> getCrates() {
        return Collections.unmodifiableMap(crates);
    }
}