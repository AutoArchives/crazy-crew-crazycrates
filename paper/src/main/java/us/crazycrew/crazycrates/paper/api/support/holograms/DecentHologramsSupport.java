package us.crazycrew.crazycrates.paper.api.support.holograms;

import us.crazycrew.crazycrates.paper.api.interfaces.HologramController;
import com.badbones69.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.common.crates.CrateHologram;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.block.Block;
import us.crazycrew.crazycrates.paper.utils.MsgUtils;
import java.util.HashMap;
import java.util.UUID;

public class DecentHologramsSupport implements HologramController {

    private final HashMap<Block, Hologram> holograms = new HashMap<>();
    
    public void createHologram(Block block, Crate crate) {
        CrateHologram crateHologram = crate.getHologram();

        if (!crateHologram.isEnabled()) return;

        double height = crateHologram.getHeight();

        Hologram hologram = DHAPI.createHologram("CrazyCrates-" + UUID.randomUUID(), block.getLocation().add(.5, height, .5));

        crateHologram.getMessages().forEach(line -> DHAPI.addHologramLine(hologram, MsgUtils.color(line)));

        this.holograms.put(block, hologram);
    }
    
    public void removeHologram(Block block) {
        if (!this.holograms.containsKey(block)) return;

        Hologram hologram = this.holograms.get(block);

        this.holograms.remove(block);
        hologram.delete();
    }

    public void removeAllHolograms() {
        this.holograms.forEach((key, value) -> value.delete());
        this.holograms.clear();
    }
}