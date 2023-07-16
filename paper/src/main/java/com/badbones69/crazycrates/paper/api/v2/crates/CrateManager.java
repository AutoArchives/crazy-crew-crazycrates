package com.badbones69.crazycrates.paper.api.v2.crates;

import com.badbones69.crazycrates.paper.api.v2.objects.Crate;
import com.ryderbelserion.crazycrates.core.CrazyLogger;
import com.ryderbelserion.crazycrates.core.utils.FileUtils;

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
                CrazyLogger.severe("Could not create crates directory! " + cratesDir.getAbsolutePath());
                return;
            }

            FileUtils.extract("/crates/", this.dataFolder.toPath(), false);
        }

        File[] crateList = cratesDir.listFiles((dir, name) -> name.endsWith(".yml"));

        if (crateList == null) {
            CrazyLogger.severe("Could not read crates directory! " + cratesDir.getAbsolutePath());
            return;
        }

        for (File file : crateList) {
            CrazyLogger.info("Loading crate: " + file.getName());

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