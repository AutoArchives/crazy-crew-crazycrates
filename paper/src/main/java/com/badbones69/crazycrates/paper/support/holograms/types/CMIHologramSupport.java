package com.badbones69.crazycrates.paper.support.holograms.types;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Holograms.CMIHologram;
import com.badbones69.crazycrates.paper.support.holograms.interfaces.HologramManager;
import com.badbones69.crazycrates.paper.api.v2.objects.CrateHologram;
import net.Zrips.CMILib.Container.CMILocation;
import org.bukkit.Location;
import java.util.HashMap;
import java.util.UUID;

public class CMIHologramSupport implements HologramManager {

    private final HashMap<Location, CMIHologram> holograms = new HashMap<>();

    @Override
    public void create(Location location, CrateHologram crateHologram) {
        if (!crateHologram.isEnabled()) return;

        double height = crateHologram.getHeight();

        CMILocation cmiLocation = new CMILocation(location.add(0.5, height, 0.5));

        CMIHologram hologram = new CMIHologram("CrazyCrates-" + UUID.randomUUID(), cmiLocation);

        hologram.setLines(crateHologram.getMessages());

        CMI.getInstance().getHologramManager().addHologram(hologram);

        hologram.update();

        holograms.put(location, hologram);
    }

    @Override
    public void remove(Location location) {
        if (!holograms.containsKey(location)) return;

        CMIHologram hologram = holograms.get(location);

        hologram.remove();
        holograms.remove(location);
    }

    @Override
    public void purge() {
        if (holograms.isEmpty()) return;

        holograms.forEach((Key, value) -> value.remove());
        holograms.clear();
    }
}