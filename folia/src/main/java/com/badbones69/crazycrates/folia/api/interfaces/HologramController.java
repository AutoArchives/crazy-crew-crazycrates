package com.badbones69.crazycrates.folia.api.interfaces;

import com.badbones69.crazycrates.folia.api.objects.Crate;
import org.bukkit.block.Block;

public interface HologramController {
    
    void createHologram(Block block, Crate crate);
    
    void removeHologram(Block block);
    
    void removeAllHolograms();
    
}