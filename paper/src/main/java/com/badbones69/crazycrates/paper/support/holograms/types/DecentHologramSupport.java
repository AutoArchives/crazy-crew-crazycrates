package com.badbones69.crazycrates.paper.support.holograms.types;

import com.badbones69.crazycrates.paper.support.holograms.interfaces.HologramManager;
import com.badbones69.crazycrates.paper.api.objects.CrateHologram;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import java.util.HashMap;
import java.util.UUID;

public class DecentHologramSupport implements HologramManager {

    private final HashMap<Location, Hologram> holograms = new HashMap<>();

    @Override
    public void create(Location location, CrateHologram crateHologram) {
        if (!crateHologram.isEnabled()) return;

        double height = crateHologram.getHeight();

        Hologram hologram = DHAPI.createHologram("CrazyCrates-" + UUID.randomUUID(), location.add(.5, height, .5));

        crateHologram.getMessages().forEach(line -> DHAPI.addHologramLine(hologram, line));

        holograms.put(location, hologram);
    }

    @Override
    public void remove(Location location) {
        if (!holograms.containsKey(location)) return;

        Hologram hologram = holograms.get(location);

        hologram.delete();
        holograms.remove(location);
    }

    @Override
    public void purge() {
        if (holograms.isEmpty()) return;

        holograms.forEach((key, value) -> value.delete());
        holograms.clear();
    }
}