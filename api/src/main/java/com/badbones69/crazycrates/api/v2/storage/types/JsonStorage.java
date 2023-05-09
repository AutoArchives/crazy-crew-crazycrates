package com.badbones69.crazycrates.api.v2.storage.types;

import com.badbones69.crazycrates.api.v2.storage.objects.UserData;
import com.google.gson.annotations.Expose;
import us.crazycrew.crazycore.paper.files.FileExtension;
import us.crazycrew.crazycore.paper.files.enums.FileType;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JsonStorage extends FileExtension {

    public JsonStorage(Path path) {
        super("users.json", path, FileType.JSON);
    }

    @Expose
    protected static ConcurrentHashMap<UUID, UserData> userData = new ConcurrentHashMap<>();
}