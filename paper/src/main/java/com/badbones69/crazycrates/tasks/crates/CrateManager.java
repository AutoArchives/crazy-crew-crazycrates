package com.badbones69.crazycrates.tasks.crates;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.FileManager.Files;
import com.badbones69.crazycrates.api.enums.PersistentKeys;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.api.objects.Key;
import com.badbones69.crazycrates.api.objects.other.BrokeLocation;
import com.badbones69.crazycrates.api.objects.other.CrateLocation;
import com.badbones69.crazycrates.api.utils.ItemUtils;
import com.badbones69.crazycrates.api.utils.MiscUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import us.crazycrew.crazycrates.CrazyCratesProvider;
import us.crazycrew.crazycrates.api.crates.quadcrates.CrateSchematic;
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
    private final Set<String> brokenCrates = new HashSet<>();
    private final Set<BrokeLocation> brokenLocations = new HashSet<>();
    private final Set<CrateSchematic> crateSchematics = new HashSet<>();
    private final Set<CrateLocation> crateLocations = new HashSet<>();

    private final Set<Key> keys = new HashSet<>();

    private final Map<UUID, Location> locations = new HashMap<>();

    /**
     * Load all crate information.
     */
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

                ConfigurationSection section = crateConfig.getCrateSection().getConfigurationSection("PhysicalKey");

                if (section != null) {
                    File keyFile = new File(CrazyCratesProvider.get().getKeyFolder() + "/" + crateConfig.getFileName() + ".yml");

                    keyFile.createNewFile();

                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(keyFile);

                    configuration.set("item.name", section.getString("Name"));
                    configuration.set("item.lore", section.getStringList("Lore"));
                    configuration.set("item.material", section.getString("Item"));
                    configuration.set("item.glowing", section.getBoolean("Glowing"));

                    configuration.set("item.flags", Collections.emptyList());
                    configuration.set("item.unbreakable", false);

                    configuration.save(keyFile);

                    crateConfig.getFile().set("Crate.PhysicalKey", null);

                    crateConfig.getFile().set("Crate.keys", new ArrayList<>() {{
                        add(keyFile.getName().replaceAll(".yml", ""));
                    }});

                    crateConfig.getFile().save();
                    crateConfig.getFile().loadWithComments();
                }
            } catch (InvalidConfigurationException exception) {
                this.brokenCrates.add(file.getName());
                this.plugin.getLogger().log(Level.WARNING, file.getName() + " contains invalid YAML structure.", exception);
                continue;
            } catch (IOException exception) {
                this.brokenCrates.add(file.getName());
                this.plugin.getLogger().log(Level.WARNING, "Could not load crate file: " + file.getName(), exception);
                continue;
            }

            Crate crate = new Crate(crateConfig);

            this.crates.add(crate);
        }

        if (MiscUtils.isLogging()) {
            List.of(
                    "All crate information has been loaded.",
                    "Loading all the physical crate locations."
            ).forEach(line -> this.plugin.getLogger().info(line));
        }

        FileConfiguration locations = Files.LOCATIONS.getFile();
        int loadedAmount = 0;
        int brokeAmount = 0;

        org.bukkit.configuration.ConfigurationSection section = locations.getConfigurationSection("Locations");

        if (section != null) {
            for (String locationName : section.getKeys(false)) {
                try {
                    String worldName = locations.getString("Locations." + locationName + ".World");

                    // If name is null, we return.
                    if (worldName == null) return;

                    // If name is empty or blank, we return.
                    if (worldName.isEmpty() || worldName.isBlank()) return;

                    World world = this.plugin.getServer().getWorld(worldName);
                    int x = locations.getInt("Locations." + locationName + ".X");
                    int y = locations.getInt("Locations." + locationName + ".Y");
                    int z = locations.getInt("Locations." + locationName + ".Z");
                    Location location = new Location(world, x, y, z);

                    Crate crate = getCrate(locations.getString("Locations." + locationName + ".Crate"));

                    if (world != null && crate != null) {
                        this.crateLocations.add(new CrateLocation(locationName, crate, location));

                        //todo() hologram support.
                        //if (this.holograms != null) {
                        //    this.holograms.createHologram(location.getBlock(), crate);
                        //}

                        loadedAmount++;
                    } else {
                        this.brokenLocations.add(new BrokeLocation(locationName, crate, x, y, z, worldName));
                        brokeAmount++;
                    }

                } catch (Exception ignored) {}
            }
        }

        // Checking if all physical locations loaded
        if (MiscUtils.isLogging()) {
            if (loadedAmount > 0 || brokeAmount > 0) {
                if (brokeAmount <= 0) {
                    this.plugin.getLogger().info("All physical crate locations have been loaded.");
                } else {
                    this.plugin.getLogger().info("Loaded " + loadedAmount + " physical crate locations.");
                    this.plugin.getLogger().info("Failed to load " + brokeAmount + " physical crate locations.");
                }
            }

            this.plugin.getLogger().info("Searching for schematics to load.");
        }

        // Loading schematic files
        String[] schems = new File(this.plugin.getDataFolder() + "/schematics/").list();

        if (schems != null) {
            for (String schematicName : schems) {
                if (schematicName.endsWith(".nbt")) {
                    this.crateSchematics.add(new CrateSchematic(schematicName, new File(plugin.getDataFolder() + "/schematics/" + schematicName)));

                    if (MiscUtils.isLogging()) this.plugin.getLogger().info(schematicName + " was successfully found and loaded.");
                }
            }
        }

        if (MiscUtils.isLogging()) this.plugin.getLogger().info("All schematics were found and loaded.");

        cleanDataFile();

        this.plugin.getInventoryManager().loadButtons();
    }

    /**
     * Clean data files of old fields.
     */
    private void cleanDataFile() {
        FileConfiguration data = Files.DATA.getFile();

        if (!data.contains("Players")) return;

        if (MiscUtils.isLogging()) this.plugin.getLogger().info("Cleaning up the data.yml file.");

        List<String> removePlayers = new ArrayList<>();

        for (String uuid : data.getConfigurationSection("Players").getKeys(false)) {
            if (data.contains("Players." + uuid + ".tracking")) return;

            boolean hasKeys = false;
            List<String> noKeys = new ArrayList<>();

            for (Key key : getKeys()) {
                if (data.getInt("Players." + uuid + "." + key.getFileName()) <= 0) {
                    noKeys.add(key.getFileName());
                } else {
                    hasKeys = true;
                }
            }

            if (hasKeys) {
                noKeys.forEach(crate -> data.set("Players." + uuid + "." + crate, null));
            } else {
                removePlayers.add(uuid);
            }
        }

        if (!removePlayers.isEmpty()) {
            if (MiscUtils.isLogging()) this.plugin.getLogger().info(removePlayers.size() + " player's data has been marked to be removed.");

            removePlayers.forEach(uuid -> data.set("Players." + uuid, null));

            if (MiscUtils.isLogging()) this.plugin.getLogger().info("All empty player data has been removed.");
        }

        if (MiscUtils.isLogging()) this.plugin.getLogger().info("The data.yml file has been cleaned.");

        Files.DATA.saveFile();
    }

    /**
     * Get crate by name.
     *
     * @param fileName the name to check.
     * @return crate object or null if nothing found.
     */
    public Crate getCrate(String fileName) {
        Crate crate = null;

        for (Crate key : this.crates) {
            if (!key.getFileName().equalsIgnoreCase(fileName)) continue;

            crate = key;
            break;
        }

        return crate;
    }

    /**
     * Get key by name.
     *
     * @param fileName the name to check.
     * @return key object or null if nothing found.
     */
    public Key getKey(String fileName) {
        Key key = null;

        for (Key pair : this.keys) {
            if (!pair.getFileName().equalsIgnoreCase(fileName)) continue;

            key = pair;
            break;
        }

        return key;
    }

    /**
     * Get a crate that has a key valid for opening.
     *
     * @param crateName the crate to check.
     * @param itemStack the key to check.
     * @return key or null.
     */
    public Key getKeyFromCrate(String crateName, ItemStack itemStack) {
        // If it's null/empty, return.
        if (crateName == null || crateName.isEmpty()) return null;

        // Get crate.
        Crate crate = getCrate(crateName);

        // If crate is null.
        if (crate == null) return null;

        if (!itemStack.hasItemMeta()) return null;

        ItemMeta itemMeta = itemStack.getItemMeta();

        String keyName = itemMeta.getPersistentDataContainer().get(PersistentKeys.crate_key.getNamespacedKey(), PersistentDataType.STRING);

        // If it's null/empty, return.
        if (keyName == null || keyName.isEmpty()) return null;

        // If the key section doesn't contain the one we're checking.
        if (!crate.getKeys().contains(keyName)) return null;

        return getKey(keyName);
    }

    /**
     * Checks if the item used is an old key.
     *
     * @param item the item to check.
     * @return true if it is the old key otherwise false.
     */
    public boolean isKeyFromCrate(ItemStack item) {
        return getCrateFromKey(item) != null;
    }

    /**
     * Get a crate from the key. Only used for older keys.
     *
     * @param item the key ItemStack you are checking.
     * @return a crate if is a key from a crate otherwise null if it is not.
     */
    public Crate getCrateFromKey(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return null;

        if (!item.hasItemMeta()) return null;

        ItemMeta itemMeta = item.getItemMeta();

        if (!itemMeta.getPersistentDataContainer().has(PersistentKeys.crate_key.getNamespacedKey())) {
            // Get the item meta as a string
            String value = itemMeta.getAsString();

            String[] sections = value.split(",");

            String pair = null;

            for (String key : sections) {
                if (key.contains("CrazyCrates-Crate")) {
                    pair = key.trim().replaceAll("\\{", "").replaceAll("\"", "");
                    break;
                }
            }

            if (pair == null) {
                return null;
            }

            return getCrate(pair.split(":")[1]);
        }

        String crateName = ItemUtils.getKey(itemMeta);

        return getCrate(crateName);
    }

    /**
     * @return A set of loaded keys.
     */
    public Set<Key> getKeys() {
        return Collections.unmodifiableSet(this.keys);
    }

    /**
     * @return A set of loaded crates.
     */
    public Set<Crate> getCrates() {
        return Collections.unmodifiableSet(this.crates);
    }

    /**
     * @return A set of broken crates.
     */
    public Set<String> getBrokenCrates() {
        return Collections.unmodifiableSet(this.brokenCrates);
    }

    /**
     * @return A set of crate locations.
     */
    public Set<CrateLocation> getCrateLocations() {
        return Collections.unmodifiableSet(this.crateLocations);
    }

    /**
     * @return a map of crates currently in use.
     */
    public Map<UUID, Location> getActiveCrates() {
        return Collections.unmodifiableMap(this.locations);
    }

    /**
     * Adds a player opening the crate so that it will be currently in use.
     *
     * @param uuid the uuid of the player.
     * @param location the location of the crate.
     */
    public void addActiveCrate(UUID uuid, Location location) {
        this.locations.put(uuid, location);
    }

    /**
     * Checks if a player is currently opening a crate.
     *
     * @param uuid the uuid of the player opening the crate.
     * @return true or false
     */
    public boolean isCrateActive(UUID uuid) {
        return this.locations.containsKey(uuid);
    }

    /**
     * Removes a crate from the active crates.
     *
     * @param uuid the uuid of the player opening the craste.
     */
    public void removeActiveCrate(UUID uuid) {
        this.locations.remove(uuid);
    }
}