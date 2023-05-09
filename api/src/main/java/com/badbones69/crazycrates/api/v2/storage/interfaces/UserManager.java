package com.badbones69.crazycrates.api.v2.storage.interfaces;

import com.badbones69.crazycrates.api.v2.storage.objects.UserData;
import us.crazycrew.crazycore.paper.CrazyCore;
import java.util.Map;
import java.util.UUID;

public interface UserManager {

    void load(CrazyCore crazyCore);

    void save(CrazyCore crazyCore);

    void addUser(UUID uuid);

    UserData getUser(UUID uuid);

    void addKey(UUID uuid, String crateName, int amount);

    void removeKey(UUID uuid, String crateName, int amount);

    Map<String, Integer> getKeys(UUID uuid, String crateName);

    Map<UUID, UserData> getUsers(UUID uuid);

}