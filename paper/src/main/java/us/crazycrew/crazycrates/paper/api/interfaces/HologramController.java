package us.crazycrew.crazycrates.paper.api.interfaces;

import us.crazycrew.crazycrates.paper.api.objects.Crate;
import org.bukkit.block.Block;

public interface HologramController {
    
    void createHologram(Block block, Crate crate);
    
    void removeHologram(Block block);
    
    void removeAllHolograms();
    
}