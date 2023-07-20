package com.badbones69.crazycrates.paper.api.storage.objects;

import com.badbones69.crazycrates.paper.api.objects.Crate;
import com.google.gson.annotations.Expose;
import com.badbones69.crazycrates.core.frame.CrazyLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserData {

    private final UUID uuid;

    @Expose
    private ConcurrentHashMap<String, Integer> keys = new ConcurrentHashMap<>();

    public UserData(UUID uuid) {
        this.uuid = uuid;
    }

    public void addKey(Crate crate, int amount) {
        if (this.keys.containsKey(crate.getCrateName())) {
            this.keys.put(crate.getCrateName(), this.keys.get(crate.getCrateName()) + amount);
            return;
        }

        this.keys.put(crate.getCrateName(), amount);
    }

    public void removeKey(Crate crate, int amount) {
        if (!this.keys.containsKey(crate.getCrateName())) {
            CrazyLogger.warn("They don't have any keys to remove.");
            return;
        }

        this.keys.remove(crate.getCrateName(), this.keys.get(crate.getCrateName()) - amount);
    }

    public int getKey(Crate crate) {
        if (!this.keys.containsKey(crate.getCrateName())) {
            CrazyLogger.warn("They don't have any keys to fetch.");
            return 0;
        }

        return this.keys.get(crate.getCrateName());
    }

    public Map<String, Integer> getKeys() {
        return Collections.unmodifiableMap(this.keys);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
}