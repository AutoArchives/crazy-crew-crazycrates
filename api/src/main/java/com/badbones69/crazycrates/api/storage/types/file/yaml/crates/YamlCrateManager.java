package com.badbones69.crazycrates.api.storage.types.file.yaml.crates;

import com.badbones69.crazycrates.api.ApiManager;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.api.storage.CrateData;
import com.badbones69.crazycrates.api.storage.CustomLocation;
import com.badbones69.crazycrates.api.storage.interfaces.LocationManager;
import com.ryderbelserion.stick.core.storage.enums.StorageType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class YamlCrateManager extends YamlConfiguration implements LocationManager {

    private final File file;
    private final ApiManager apiManager;

    private final ConcurrentHashMap<String, CrateData> crates = new ConcurrentHashMap<>();

    public YamlCrateManager(File file, ApiManager apiManager) {
        this.file = file;

        this.apiManager = apiManager;
    }

    @Override
    public void load() {
        try {
            if (!this.file.exists()) this.file.createNewFile();

            load(this.file);

            ConfigurationSection section = getConfigurationSection("locations");

            if (section == null) {
                set("locations", Collections.emptySet());

                save(this.file);
                load(this.file);
            }
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void save() {
        try {
            save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reload() {
        save();
        load();
    }

    @Override
    public void convert(File file, StorageType storageType) {
        //TODO() Add file conversion
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
            World world = this.apiManager.getServer().getWorld(name);

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
        AtomicInteger id = new AtomicInteger(1); // Starter ID

        for (int i = 1; contains("locations." + crateName + "." + location.getWorld().getName() + "." + id); i++) {
            id.set(i + 1);
        }

        CrateData crateData = new CrateData();

        CustomLocation customLocation = new CustomLocation(
                id.get(),
                location.getWorld().getName(),
                location.x(),
                location.y(),
                location.z()
        );

        if (!crateData.getLocations().isEmpty()) {
            for (CustomLocation locations : crateData.getLocations()) {
                if (locations.equals(customLocation)) {
                    id.set(locations.id());
                    break;
                }
            }
        }

        set("locations." + crateName + "." + location.getWorld().getName() + "." + id + ".x", location.x());
        set("locations." + crateName + "." + location.getWorld().getName() + "." + id + ".y", location.y());
        set("locations." + crateName + "." + location.getWorld().getName() + "." + id + ".z", location.z());

        reload();

        crateData.addLocation(customLocation);

        if (this.apiManager.getHolograms() != null) {
            this.apiManager.getCrateManager().getCrates().forEach(crate -> {
                if (!crate.getCrateHologram().isEnabled()) return;

                this.apiManager.getHolograms().create(
                        location,
                        crate.getCrateHologram()
                );
            });
        }

        this.apiManager.getHolograms();
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
        CrateData crateData = getCrateLocation(crateName);

        CustomLocation customLocation = null;

        if (!crateData.getLocations().isEmpty()) {
            for (CustomLocation locations : crateData.getLocations()) {
                if (locations.id() == id) {
                    customLocation = locations;
                    break;
                }
            }
        }

        if (customLocation != null) {
            if (this.apiManager.getHolograms() != null) {
                for (Crate crate : this.apiManager.getCrateManager().getCrates()) {
                    if (crate.getCrateName().equals(crateName)) {
                        this.apiManager.getHolograms().remove(new Location(this.apiManager.getServer().getWorld(customLocation.world()), customLocation.x(), customLocation.y(), customLocation.z()));

                        break;
                    }
                }
            }

            crateData.removeLocation(customLocation);
        }
    }

    @Override
    public Map<String, CrateData> getCrates() {
        return Collections.unmodifiableMap(crates);
    }
}