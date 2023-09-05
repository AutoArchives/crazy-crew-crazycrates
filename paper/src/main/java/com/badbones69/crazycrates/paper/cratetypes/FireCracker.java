package com.badbones69.crazycrates.paper.cratetypes;

import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.Methods;
import com.badbones69.crazycrates.paper.api.CrazyManager;
import com.badbones69.crazycrates.api.enums.types.KeyType;
import com.badbones69.crazycrates.paper.api.interfaces.HologramController;
import com.badbones69.crazycrates.paper.api.objects.Crate;
import com.badbones69.crazycrates.paper.api.plugin.CrazyCratesPlugin;
import com.badbones69.crazycrates.paper.api.plugin.registry.CrazyCratesProvider;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class FireCracker {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesPlugin cratesPlugin = CrazyCratesProvider.get();
    private final @NotNull CrazyManager crazyManager = this.cratesPlugin.getCrazyManager();
    private final @NotNull Methods methods = this.cratesPlugin.getMethods();
    
    public void startFireCracker(UUID uuid, final Crate crate, KeyType keyType, final Location loc, HologramController hologramController) {
        if (!crazyManager.takeKeys(1, uuid, crate, keyType, true)) {
            Player player = this.plugin.getServer().getPlayer(uuid);

            if (player != null) {
                methods.failedToTakeKey(player.getName(), crate);
            }

            crazyManager.removePlayerFromOpeningList(uuid);
            return;
        }

        if (hologramController != null) hologramController.removeHologram(loc.getBlock());

        final ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.BLACK);
        colors.add(Color.AQUA);
        colors.add(Color.MAROON);
        colors.add(Color.PURPLE);

        crazyManager.addCrateTask(uuid, new BukkitRunnable() {
            final Random r = new Random();
            final int color = r.nextInt(colors.size());
            int l = 0;
            final Location L = loc.clone().add(.5, 25, .5);
            
            @Override
            public void run() {
                L.subtract(0, 1, 0);
                methods.firework(L, colors.get(color));
                l++;

                if (l == 25) {
                    crazyManager.endCrate(uuid);
                    // The key type is set to free because the key has already been taken above.
                    plugin.getQuickCrate().openCrate(uuid, loc, crate, KeyType.FREE_KEY, hologramController);
                }
            }
        }.runTaskTimer(plugin, 0, 2));
    }
}