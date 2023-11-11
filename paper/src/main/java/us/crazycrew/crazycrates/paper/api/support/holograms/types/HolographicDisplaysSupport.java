package us.crazycrew.crazycrates.paper.api.support.holograms.types;

import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.common.crates.CrateHologram;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.objects.Crate;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import org.bukkit.block.Block;
import us.crazycrew.crazycrates.paper.api.support.holograms.HologramHandler;
import us.crazycrew.crazycrates.paper.utils.MsgUtils;
import java.util.HashMap;

public class HolographicDisplaysSupport extends HologramHandler {
    
    private final HashMap<Block, Hologram> holograms = new HashMap<>();

    @NotNull
    private final CrazyCrates plugin = CrazyCrates.get();

    @NotNull
    private final HolographicDisplaysAPI api = HolographicDisplaysAPI.get(this.plugin);

    @Override
    public void createHologram(Block block, Crate crate) {
        CrateHologram crateHologram = crate.getHologram();

        if (!crateHologram.isEnabled()) return;

        double height = crateHologram.getHeight();

        Hologram hologram = this.api.createHologram(block.getLocation().add(.5, height, .5));

        crateHologram.getMessages().forEach(line -> hologram.getLines().appendText(MsgUtils.color(line)));

        this.holograms.put(block, hologram);
    }

    @Override
    public void removeHologram(Block block) {
        if (!this.holograms.containsKey(block)) return;

        Hologram hologram = this.holograms.get(block);

        this.holograms.remove(block);
        hologram.delete();
    }

    @Override
    public void removeAllHolograms() {
        this.holograms.forEach((key, value) -> value.delete());
        this.holograms.clear();
    }
}