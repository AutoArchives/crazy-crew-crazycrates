package us.crazycrew.crazycrates.platform.config;

import com.ryderbelserion.cluster.utils.FileUtils;
import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;
import us.crazycrew.crazycrates.platform.Server;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyManager {

    private final Server server;

    private final Logger logger;

    public KeyManager(Server server) {
        this.server = server;

        this.logger = this.server.getLogger();

        List.of(
                "CasinoKey.yml",
                "DiamondKey.yml"
        ).forEach(key -> FileUtils.copyFile(server.getKeyFolder().toPath(), "keys", key));
    }

    private final Map<String, FileConfiguration> keys = new HashMap<>();

    public void loadKeys() {
        File[] files = this.server.getKeyFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {
            String fileName = file.getName().replace(".yml", "");

            try {
                this.keys.put(fileName, YamlConfiguration.loadConfiguration(file));
            } catch (IOException exception) {
                this.logger.log(Level.SEVERE, "Failed to load: " + fileName + ".", exception);
            }
        }
    }

    public void reloadKeys() {
        File[] files = this.server.getKeyFiles();

        if (files == null) {
            return;
        }

        if (this.keys.isEmpty()) {
            return;
        }

        for (Map.Entry<String, FileConfiguration> key : this.keys.entrySet()) {
            String fileName = key.getKey().replace(".yml", "");

            File file = new File(this.server.getKeyFolder(), fileName);

            if (file.exists()) {
                try {
                    key.getValue().save(file);
                    key.getValue().load(file);
                } catch (IOException exception) {
                    this.logger.log(Level.SEVERE, "Failed to save/load: " + fileName + ".", exception);
                }

                return;
            }

            this.keys.remove(fileName);
        }
    }

    public FileConfiguration getConfig(String keyName) {
        if (!hasKey(keyName)) {
            this.logger.warning(keyName + " does not exist so we cannot get a configuration.");

            return null;
        }

        return this.keys.get(keyName);
    }

    public boolean hasKey(String keyName) {
        return this.keys.containsKey(keyName);
    }

    public void removeKey(String keyName) {
        if (!hasKey(keyName)) {
            this.logger.warning(keyName + " does not exist.");

            return;
        }

        this.keys.remove(keyName);
    }

    public void addKey(String keyName, FileConfiguration config) {
        if (hasKey(keyName)) {
            this.logger.warning(keyName + " already exists.");
            return;
        }

        this.keys.put(keyName, config);
    }

    public Map<String, FileConfiguration> getKeys() {
        return Collections.unmodifiableMap(this.keys);
    }
}