package com.badbones69.crazycrates.tasks.crates;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.api.objects.Key;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import us.crazycrew.crazycrates.platform.Server;
import us.crazycrew.crazycrates.platform.crates.CrateConfig;
import us.crazycrew.crazycrates.platform.keys.KeyConfig;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class CrateManager {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final @NotNull Server instance = this.plugin.getInstance();

    private final Set<Crate> crates = new HashSet<>();
    private final Set<Key> keys = new HashSet<>();

    private final Map<UUID, Location> locations = new HashMap<>();

    public void load() {
        File[] keyFilesList = this.instance.getKeyFiles();

        if (keyFilesList == null) {
            this.plugin.getLogger().severe("Could not read from the keys directory! " + this.instance.getKeyFolder().getAbsolutePath());
        } else {
            for (File file : keyFilesList) {
                this.plugin.getLogger().info("Loading key: " + file.getName());

                KeyConfig keyConfig = new KeyConfig(file);

                try {
                    keyConfig.load();
                } catch (InvalidConfigurationException exception) {
                    this.plugin.getLogger().log(Level.WARNING, file.getName() + " contains invalid YAML structure.", exception);
                    continue;
                } catch (IOException exception) {
                    this.plugin.getLogger().log(Level.WARNING, "Could not load key file: " + file.getName(), exception);
                    continue;
                }

                Key key = new Key(keyConfig);

                this.keys.add(key);
            }
        }

        File[] crateFilesList = this.instance.getCrateFiles();

        if (crateFilesList == null) {
            this.plugin.getLogger().severe("Could not read from crates directory! " + this.instance.getCrateFolder().getAbsolutePath());

            return;
        }

        for (File file : crateFilesList) {
            this.plugin.getLogger().info("Loading crate: " + file.getName());

            CrateConfig crateConfig = new CrateConfig(file);

            try {
                crateConfig.load();
            } catch (InvalidConfigurationException exception) {
                this.plugin.getLogger().log(Level.WARNING, file.getName() + " contains invalid YAML structure.", exception);
                continue;
            } catch (IOException exception) {
                this.plugin.getLogger().log(Level.WARNING, "Could not load crate file: " + file.getName(), exception);
                continue;
            }

            Crate crate = new Crate(crateConfig);

            this.crates.add(crate);
        }
    }

    public Crate getCrate(String fileName) {
        Crate crate = null;

        for (Crate key : this.crates) {
            if (!key.getFileName().equalsIgnoreCase(fileName)) continue;

            crate = key;
            break;
        }

        return crate;
    }

    public Key getKey(String fileName) {
        Key key = null;

        for (Key pair : this.keys) {
            if (!pair.getFileName().equalsIgnoreCase(fileName)) continue;

            key = pair;
            break;
        }

        return key;
    }

    public Set<Key> getKeys() {
        return Collections.unmodifiableSet(this.keys);
    }

    public Set<Crate> getCrates() {
        return Collections.unmodifiableSet(this.crates);
    }

    public void addActiveCrate(UUID uuid, Location location) {
        this.locations.put(uuid, location);
    }

    public boolean isCrateActive(UUID uuid) {
        return this.locations.containsKey(uuid);
    }

    public void removeActiveCrate(UUID uuid) {
        this.locations.remove(uuid);
    }

    public Map<UUID, Location> getActiveCrates() {
        return Collections.unmodifiableMap(this.locations);
    }
}