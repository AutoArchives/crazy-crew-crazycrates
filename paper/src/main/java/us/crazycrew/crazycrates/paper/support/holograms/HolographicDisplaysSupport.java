package us.crazycrew.crazycrates.paper.support.holograms;

import us.crazycrew.crazycrates.common.crates.CrateHologram;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.api.interfaces.HologramController;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;
import java.util.HashMap;

public class HolographicDisplaysSupport implements HologramController {
    
    private final HashMap<Block, Hologram> holograms = new HashMap<>();

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesLoader cratesLoader = this.plugin.getCratesLoader();

    private final @NotNull HolographicDisplaysAPI api = HolographicDisplaysAPI.get(plugin);

    @Override
    public void createHologram(Block block, Crate crate) {
        CrateHologram crateHologram = crate.getHologram();

        if (!crateHologram.isEnabled()) return;

        double height = crateHologram.getHeight();

        Hologram hologram = api.createHologram(block.getLocation().add(.5, height, .5));

        crateHologram.getMessages().forEach(line -> hologram.getLines().appendText(this.cratesLoader.getMethods().color(line)));

        holograms.put(block, hologram);
    }

    @Override
    public void removeHologram(Block block) {
        if (!holograms.containsKey(block)) return;

        Hologram hologram = holograms.get(block);

        holograms.remove(block);
        hologram.delete();
    }

    @Override
    public void removeAllHolograms() {
        holograms.forEach((key, value) -> value.delete());
        holograms.clear();
    }
}