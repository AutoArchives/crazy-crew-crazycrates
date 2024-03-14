package us.crazycrew.crazycrates.platform.keys;

import com.ryderbelserion.cluster.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.platform.Server;
import us.crazycrew.crazycrates.CrazyCratesProvider;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyManager {

    private final @NotNull Server instance = CrazyCratesProvider.get();

    private final @NotNull Logger logger = this.instance.getLogger();

    public KeyManager() {
        List.of(
                "CasinoKey.yml",
                "DiamondKey.yml"
        ).forEach(key -> FileUtils.copyFile(this.instance.getKeyFolder().toPath(), "keys", key));
    }

    private final Map<String, KeyConfig> keys = new ConcurrentHashMap<>();

    public void loadKeys() {
        File[] files = this.instance.getKeyFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {
            String fileName = file.getName().replace(".yml", "");

            KeyConfig config = new KeyConfig(file);

            try {
                config.load();
            } catch (IOException e) {
                this.logger.log(Level.SEVERE, "Failed to load file: " + fileName + ".yml");
            }

            this.keys.put(fileName, config);
        }
    }

    public void reloadKeys() {
        File[] files = this.instance.getKeyFiles();

        if (files == null) {
            return;
        }

        if (this.keys.isEmpty()) {
            return;
        }

        for (Map.Entry<String, KeyConfig> key : this.keys.entrySet()) {
            String fileName = key.getKey().replace(".yml", "");

            File file = new File(this.instance.getKeyFolder(), fileName);

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

    public KeyConfig getConfig(String keyName) {
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

    public void addKey(String keyName, KeyConfig config) {
        if (hasKey(keyName)) {
            this.logger.warning(keyName + " already exists.");
            return;
        }

        this.keys.put(keyName, config);
    }

    public Map<String, KeyConfig> getKeys() {
        return Collections.unmodifiableMap(this.keys);
    }
}