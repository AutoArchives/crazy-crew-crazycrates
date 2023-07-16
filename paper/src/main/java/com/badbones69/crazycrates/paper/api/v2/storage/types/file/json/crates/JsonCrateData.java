package com.badbones69.crazycrates.paper.api.v2.storage.types.file.json.crates;

import com.badbones69.crazycrates.paper.api.v2.storage.CrateData;
import com.google.gson.annotations.Expose;
import com.ryderbelserion.crazycrates.core.frame.storage.FileExtension;
import com.ryderbelserion.crazycrates.core.frame.storage.enums.StorageType;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;

public sealed class JsonCrateData extends FileExtension permits JsonCrateManager {

    public JsonCrateData(Path path) {
        super("locations.json", path, StorageType.JSON);
    }

    @Expose
    protected static ConcurrentHashMap<String, CrateData> crates = new ConcurrentHashMap<>();
}