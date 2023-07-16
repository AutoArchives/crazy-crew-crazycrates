package com.badbones69.crazycrates.paper.api.v2.storage.interfaces;

import com.badbones69.crazycrates.paper.api.v2.objects.Crate;
import com.badbones69.crazycrates.paper.api.v2.storage.objects.UserData;
import com.badbones69.crazycrates.core.frame.storage.enums.StorageType;
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

    Map<UUID, UserData> getUsers();

}