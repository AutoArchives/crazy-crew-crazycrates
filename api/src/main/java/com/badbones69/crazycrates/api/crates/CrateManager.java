package com.badbones69.crazycrates.api.crates;

import com.badbones69.crazycrates.api.objects.Crate;
import com.ryderbelserion.stick.core.StickLogger;
import com.ryderbelserion.stick.core.utils.FileUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrateManager {
    
    private final File dataFolder;

    private final ArrayList<Crate> crates = new ArrayList<>();

    public CrateManager(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    public void loadCrates() {
        this.crates.clear();

        File cratesDir = new File(this.dataFolder, "crates");

        if (!cratesDir.exists()) {
            if (!cratesDir.mkdirs()) {
                StickLogger.severe("Could not create crates directory! " + cratesDir.getAbsolutePath());
                return;
            }

            FileUtils.extract("/crates/", this.dataFolder.toPath(), false);
        }

        File[] crateList = cratesDir.listFiles((dir, name) -> name.endsWith(".yml"));

        if (crateList == null) {
            StickLogger.severe("Could not read crates directory! " + cratesDir.getAbsolutePath());
            return;
        }

        for (File file : crateList) {
            StickLogger.info("Loading crate: " + file.getName());

            CrateConfig crateConfig = new CrateConfig(file);

            crateConfig.load();

            Crate crate = new Crate(crateConfig);

            //TODO() Get locations of crate if it exists then add them.

            this.crates.add(crate);
        }
    }

    private void unloadCrates() {
        this.crates.clear();

        //TODO() unload crates, close inventories, remove holograms, clear hashmap.
    }

    public List<Crate> getCrates() {
        return Collections.unmodifiableList(crates);
    }
}