package com.badbones69.crazycrates.api.storage.types.file.yaml.users;

import com.badbones69.crazycrates.api.crates.CrateManager;
import com.badbones69.crazycrates.api.storage.interfaces.UserManager;
import com.badbones69.crazycrates.api.storage.objects.UserData;
import com.badbones69.crazycrates.api.objects.Crate;
import com.ryderbelserion.stick.core.storage.enums.StorageType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class YamlUserManager extends YamlConfiguration implements UserManager {

    private final File file;
    private final CrateManager crateManager;
    private final JavaPlugin plugin;
    private final boolean verbose;

    private final ConcurrentHashMap<UUID, UserData> userData = new ConcurrentHashMap<>();

    public YamlUserManager(File file, CrateManager crateManager, JavaPlugin plugin, boolean verbose) {
        this.file = file;

        this.crateManager = crateManager;

        this.plugin = plugin;

        this.verbose = verbose;
    }

    @Override
    public void load() {
        try {
            if (!this.file.exists()) this.file.createNewFile();

            load(this.file);

            ConfigurationSection section = getConfigurationSection("users");
            if (section == null) {
                set("users", Collections.emptySet());

                save(this.file);
                load(this.file);
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void reload() {
        try {
            save(this.file);
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        // If user data empty return.
        if (this.userData.isEmpty()) return;

        // If the player is not leaving, continue here as we are stopping the server or doing periodic save.
        this.userData.forEach((id, user) -> {
            user.getKeys().forEach((crateMap, keys) -> set("users." + id + "." + crateMap, keys));

            // Save the file then load the changes back in.
            reload();
        });
    }

    @Override
    public void saveSingular(UUID uuid) {
        // If user data empty return.
        if (this.userData.isEmpty()) return;

        // Check if user data contains keys.
        if (this.userData.containsKey(uuid)) {
            // Add keys to file.
            this.userData.get(uuid).getKeys().forEach((crateMap, keys) -> set("users." + uuid + "." + crateMap, keys));
            // Remove user when done.
            this.userData.remove(uuid);

            // Save the file then load the changes back in.
            reload();
        }
    }

    @Override
    public void convert(File file, UUID uuid, StorageType storageType, Crate crate) {
        //TODO() Add a second data type.
        //TODO() Convert from it.
    }

    @Override
    public void convertLegacy(File file, UUID uuid, StorageType storageType, Crate crate) {
        YamlConfiguration legacy = YamlConfiguration.loadConfiguration(file);

        ConfigurationSection section = legacy.getConfigurationSection("Players");

        addUser(uuid, crate);

        if (section != null && section.contains(String.valueOf(uuid))) {
            section.getKeys(false).forEach(value -> {
                int amount = legacy.getInt("Players." + uuid + "." + value);

                if (this.verbose) this.plugin.getLogger().warning("Keys: " + amount + " UUID:" + uuid + " Crate: " + value);

                addKey(uuid, amount, crate);

                save();
            });
        }
    }

    @Override
    public void addUser(UUID uuid, Crate crate) {
        Player player = this.plugin.getServer().getPlayer(uuid);

        // Get the configuration section. This is used to check for existing users.
        ConfigurationSection section = getConfigurationSection("users");

        if (section != null) {
            // Check if player is null i.e has never played.
            if (player == null && !section.contains(String.valueOf(uuid))) {
                // If the crate manager actually contains the crate.
                if (this.crateManager.getCrates().contains(crate)) {
                    if (this.verbose) this.plugin.getLogger().warning("Added " + uuid + " who has never played to the data file.");

                    // Add uuid since it doesn't exist.
                    set("users." + uuid, Collections.emptySet());

                    // Save file.
                    reload();
                }

                // Return since we're done.
                return;
            }

            // Check if the section is not null, and if it contains their uuid.
            if (section.contains(String.valueOf(uuid))) {
                // Loop through all values.
                section.getKeys(false).forEach(value -> {
                    // Check if the crate is actually active and equals the crate name used in the method.
                    if (this.crateManager.getCrates().contains(crate) && crate.getCrateName().equals(value)) {
                        // Get the current amount of keys for their crate name.
                        int amount = getInt("users." + uuid + "." + crate.getCrateName());

                        // Add the keys to the hashmap since they are online.
                        addKey(uuid, amount, crate);
                    }
                });

                // Return since we're done.
                return;
            }
        }

        // Only add if they are a completely new user or made a purchase on something like Tebex.
        if (!this.userData.containsKey(uuid)) this.userData.put(uuid, new UserData(uuid));
    }

    @Override
    public UserData getUser(UUID uuid, Crate crate) {
        Player player = this.plugin.getServer().getPlayer(uuid);

        // Check if player is not null and is online before checking the files.
        if (player != null && player.isOnline()) {
            ConfigurationSection section = getConfigurationSection("users");

            // Make sure the configuration section is not null.
            if (section != null && section.contains(String.valueOf(uuid))) {
                // Add user if it doesn't exist.
                addUser(uuid, crate);

                // Loop through the section and get the amount of each crate.
                section.getKeys(false).forEach(value -> {
                    // Get their current key amount
                    int currentAmount = getInt("users." + uuid + "." + value);

                    // Populate the hashmap.
                    addKey(uuid, currentAmount, crate);
                });
            }
        }

        // Return with their user data.
        return this.userData.get(uuid);
    }

    @Override
    public void addKey(UUID uuid, int amount, Crate crate) {
        Player player = this.plugin.getServer().getPlayer(uuid);

        // Check if the player is null or if the player is not online but has played before.
        if (player == null || !player.isOnline() && player.hasPlayedBefore()) {
            ConfigurationSection section = getConfigurationSection("users");

            // Make sure the section is not null and check if it contains a uuid.
            if (section != null && section.contains(String.valueOf(uuid))) {
                int currentAmount = getInt("users." + uuid + "." + crate.getCrateName());

                // Set the uuid and new key amount.
                set("users." + uuid + "." + crate.getCrateName(), currentAmount+amount);
                // Save the file.
                reload();

                // We're done.
                return;
            }

            // Send a message if uuid is not found i.e for us or improper use of our api!
            if (this.verbose) this.plugin.getLogger().warning(uuid + " does not exist in " + this.file.getName());

            // We're done.
            return;
        }

        // If the above checks all pass. Continue to here!
        if (this.userData.containsKey(uuid)) this.userData.get(uuid).addKey(crate, amount);
    }

    @Override
    public void removeKey(UUID uuid, int amount, Crate crate) {
        Player player = this.plugin.getServer().getPlayer(uuid);

        // Check if the player is null or if the player is not online but has played before.
        if (player == null || !player.isOnline() && player.hasPlayedBefore()) {
            ConfigurationSection section = getConfigurationSection("users");

            // Make sure the section is not null and check if it contains a uuid.
            if (section != null && section.contains(String.valueOf(uuid))) {
                int currentAmount = getInt("users." + uuid + "." + crate.getCrateName());

                // New amount cannot be less than 0
                if (currentAmount-amount < 0) {
                    if (this.verbose) this.plugin.getLogger().warning("Amount cannot be less then 0.");
                    return;
                }

                // Set the uuid and new key amount.
                set("users." + uuid + "." + crate.getCrateName(), currentAmount-amount);
                // Save the file.
                reload();

                // We're done.
                return;
            }

            // Send a message if uuid is not found i.e for us or improper use of our api!
            if (this.verbose) this.plugin.getLogger().warning(uuid + " does not exist in " + this.file.getName());

            // We're done.
            return;
        }

        // If the above checks all pass. Continue to here!
        if (this.userData.containsKey(uuid)) this.userData.get(uuid).removeKey(crate, amount);
    }

    @Override
    public Map<String, Integer> getKeys(UUID uuid, Crate crate) {
        return getUser(uuid, crate).getKeys();
    }

    @Override
    public Map<UUID, UserData> getUsers() {
        return Collections.unmodifiableMap(this.userData);
    }
}