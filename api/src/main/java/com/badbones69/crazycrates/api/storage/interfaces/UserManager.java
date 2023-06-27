package com.badbones69.crazycrates.api.storage.interfaces;

import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.api.storage.objects.UserData;
import com.ryderbelserion.stick.core.storage.enums.StorageType;
import java.io.File;
import java.util.Map;
import java.util.UUID;

public interface UserManager {

    void load();

    void save();

    void saveSingular(UUID uuid);

    void convert(File file, UUID uuid, StorageType storageType, Crate crate);

    void convertLegacy(File file, UUID uuid, StorageType storageType, Crate crate);

    void addUser(UUID uuid, Crate crate);

    UserData getUser(UUID uuid, Crate crate);

    void addKey(UUID uuid, int amount, Crate crate);

    void removeKey(UUID uuid, int amount, Crate crate);

    Map<String, Integer> getKeys(UUID uuid, Crate crate);

    Map<UUID, UserData> getUsers(UUID uuid);

}