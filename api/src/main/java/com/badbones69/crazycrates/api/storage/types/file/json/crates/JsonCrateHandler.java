package com.badbones69.crazycrates.api.storage.types.file.json.crates;

import com.badbones69.crazycrates.api.ApiManager;
import com.badbones69.crazycrates.api.storage.CustomLocation;
import com.badbones69.crazycrates.api.storage.interfaces.LocationManager;
import com.badbones69.crazycrates.api.storage.CrateLocation;
import com.ryderbelserion.stick.paper.storage.enums.StorageType;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

public non-sealed class JsonCrateHandler extends JsonCrateData implements LocationManager {

    private final Path path;
    private final Server server;

    public JsonCrateHandler(Path path, Server server) {
        super(path);

        this.path = path;
        this.server = server;
    }

    @Override
    public void load() {
        ApiManager.getStickCore().getFileHandler().addFile(new JsonCrateData(this.path));
    }

    @Override
    public void save() {
        ApiManager.getStickCore().getFileHandler().saveFile(new JsonCrateData(this.path));
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
        CrateLocation crateLocation = new CrateLocation();

        // Check if the crate name already exists.
        if (!hasLocation(crateName)) {
            // Add the first location.

            crates.put(crateName, crateLocation);

            crateLocation.addLocation(new CustomLocation(location.x(), location.y(), location.x()));

            return;
        }

        crateLocation.addLocation(new CustomLocation(location.x(), location.y(), location.x()));
    }

    @Override
    public boolean hasLocation(String crateName) {
        return getCrates().containsKey(crateName);
    }

    @Override
    public CrateLocation getLocation(String crateName) {
        if (hasLocation(crateName)) return getCrates().get(crateName);

        return null;
    }

    @Override
    public void removeLocation(String crateName) {
        if (hasLocation(crateName)) crates.remove(crateName);
    }

    @Override
    public Map<String, CrateLocation> getCrates() {
        return Collections.unmodifiableMap(crates);
    }
}