package com.badbones69.crazycrates.api.storage.types.file.json.users;

import com.badbones69.crazycrates.api.storage.objects.UserData;
import com.google.gson.annotations.Expose;
import com.ryderbelserion.stick.core.storage.FileExtension;
import com.ryderbelserion.stick.core.storage.enums.StorageType;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public sealed class JsonStorage extends FileExtension permits JsonUserManager {

    public JsonStorage(Path path) {
        super("users.json", path, StorageType.JSON);
    }

    @Expose
    protected static ConcurrentHashMap<UUID, UserData> userData = new ConcurrentHashMap<>();
}