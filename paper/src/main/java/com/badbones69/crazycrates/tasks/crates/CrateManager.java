package com.badbones69.crazycrates.tasks.crates;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.objects.Crate;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import us.crazycrew.crazycrates.platform.Server;
import us.crazycrew.crazycrates.platform.crates.CrateConfig;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class CrateManager {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final @NotNull Server instance = this.plugin.getInstance();

    private final Set<Crate> crates = new HashSet<>();

    public void load() {
        File[] crateFilesList = this.instance.getCrateFiles();

        if (crateFilesList == null) {
            this.plugin.getLogger().severe("Could not read from crates directory! " + this.instance.getCrateFolder().getAbsolutePath());

            return;
        }

        for (File file : this.instance.getCrateFiles()) {
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

    public Set<Crate> getCrates() {
        return Collections.unmodifiableSet(this.crates);
    }
}