package com.badbones69.crazycrates.paper.support.holograms.types;

/*
import com.badbones69.crazycrates.paper.api.holograms.interfaces.HologramManager;
import com.badbones69.crazycrates.paper.api.v2.objects.CrateHologram;
import de.oliver.fancyholograms.Hologram;
import de.oliver.fancyholograms.utils.HologramSpigotAdapter;
import net.minecraft.world.entity.Display;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.UUID;

public class FancyHologramSupport implements HologramManager {

    private final JavaPlugin plugin;

    public FancyHologramSupport(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private final HashMap<Location, Hologram> holograms = new HashMap<>();

    @Override
    public void create(Location location, CrateHologram crateHologram) {
        if (!crateHologram.isEnabled()) return;

        double height = crateHologram.getHeight();

        Hologram hologram = new Hologram("CrazyCrates-" + UUID.randomUUID(), location.add(0.5, height, 0.5), crateHologram.getMessages(), Display.BillboardConstraints.CENTER, 1f, null, 0, 1, -1, null);

        hologram.create();

        HologramSpigotAdapter spigotAdapter = HologramSpigotAdapter.fromHologram(hologram);

        this.plugin.getServer().getOnlinePlayers().forEach(spigotAdapter::spawn);

        this.holograms.put(location, hologram);
    }

    @Override
    public void remove(Location location) {
        if (!this.holograms.containsKey(location)) return;

        Hologram hologram = this.holograms.get(location);

        HologramSpigotAdapter spigotAdapter = HologramSpigotAdapter.fromHologram(hologram);

        this.plugin.getServer().getOnlinePlayers().forEach(spigotAdapter::remove);

        hologram.delete();

        this.holograms.remove(location);
    }

    @Override
    public void purge() {
        this.holograms.forEach((key, value) -> {
            HologramSpigotAdapter spigotAdapter = HologramSpigotAdapter.fromHologram(value);

            this.plugin.getServer().getOnlinePlayers().forEach(spigotAdapter::remove);

            value.delete();
        });

        this.holograms.clear();
    }
}
 */