package com.badbones69.crazycrates.support.holograms;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.interfaces.HologramController;
import com.badbones69.crazycrates.api.utilities.handlers.objects.crates.Crate;
import com.badbones69.crazycrates.common.configuration.objects.CrateHologram;
import com.badbones69.crazycrates.common.utilities.AdventureUtils;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.block.Block;
import java.util.HashMap;

public class HolographicDisplaysSupport implements HologramController {
    
    private final HashMap<Block, Hologram> holograms = new HashMap<>();

    private final CrazyCrates plugin = CrazyCrates.getPlugin();

    private final AdventureUtils adventureUtils = plugin.getStarter().getAdventureUtils();
    
    public void createHologram(Block block, Crate crate) {
        CrateHologram crateHologram = crate.getHologram();

        if (!crateHologram.isEnabled()) return;

        double height = crateHologram.getHeight();
        Hologram hologram = HologramsAPI.createHologram(plugin, block.getLocation().add(.5, height, .5));

        crateHologram.getMessages().forEach(line -> hologram.appendTextLine(String.valueOf(adventureUtils.parseMessage(line))));

        holograms.put(block, hologram);
    }
    
    public void removeHologram(Block block) {
        if (!holograms.containsKey(block)) return;

        Hologram hologram = holograms.get(block);
        holograms.remove(block);
        hologram.delete();
    }
    
    public void removeAllHolograms() {
        holograms.forEach((key, value) -> value.delete());
        holograms.clear();
    }
}