package us.crazycrew.crazycrates.platform.config;

import org.simpleyaml.configuration.file.FileConfiguration;
import us.crazycrew.crazycrates.platform.Server;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class KeyManager {

    private final Server server;

    public KeyManager(Server server) {
        this.server = server;
    }

    private final Map<String, FileConfiguration> keys = new HashMap<>();

    public void loadKeys() {
        File[] files = this.server.getKeyFiles();

        // if files is null, return.
        if (files == null) {
            return;
        }

        for (File file : files) {
            String fileName = file.getName();

            this.server.getLogger().warning("File: " + fileName);
        }
    }

    public void reloadKeys() {
        File[] files = this.server.getKeyFiles();

        // if files is null, return.
        if (files == null) {
            return;
        }

        // If the map is empty, return.
        if (this.keys.isEmpty()) {
            return;
        }

        for (Map.Entry<String, FileConfiguration> key : this.keys.entrySet()) {
            String fileName = key.getKey();

            File file = new File(this.server.getKeyFolder(), fileName);

            // Check if file exists.
            if (file.exists()) {
                // Reload it
                //key.getValue().reload();

                return;
            }

            this.keys.remove(fileName);
        }
    }

    public FileConfiguration getConfig(String keyName) {
        if (!hasKey(keyName)) {
            //todo() add logger support.
            return null;
        }

        return this.keys.get(keyName);
    }

    public boolean hasKey(String keyName) {
        return this.keys.containsKey(keyName);
    }

    public void removeKey(String keyName) {
        if (!hasKey(keyName)) {
            //todo() add logger support.
            return;
        }

        this.keys.remove(keyName);
    }

    public void addKey(String keyName, FileConfiguration config) {
        if (hasKey(keyName)) {
            //todo() add logger support.
            return;
        }

        this.keys.put(keyName, config);
    }

    public Map<String, FileConfiguration> getKeys() {
        return Collections.unmodifiableMap(this.keys);
    }
}