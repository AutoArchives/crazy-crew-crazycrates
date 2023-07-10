package com.badbones69.crazycrates.api.support.holograms.interfaces;

import com.badbones69.crazycrates.api.objects.CrateHologram;
import org.bukkit.Location;

public interface HologramManager {

    void create(Location location, CrateHologram crateHologram);

    void remove(Location location);

    void purge();

}