package com.badbones69.crazycrates.paper.cratetypes;

import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.Methods;
import com.badbones69.crazycrates.paper.api.v1.CrazyManager;
import com.badbones69.crazycrates.paper.support.holograms.interfaces.HologramManager;
import com.badbones69.crazycrates.core.enums.KeyType;
import com.badbones69.crazycrates.paper.api.v1.oldobjects.Crate;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.Random;

public class FireCracker {

    private static final CrazyCrates plugin = CrazyCrates.getPlugin(CrazyCrates.class);

    private static final CrazyManager crazyManager = plugin.getCrazyManager();
    
    public static void startFireCracker(final Player player, final Crate crate, KeyType keyType, final Location loc, HologramManager hologramManager) {
        if (!crazyManager.takeKeys(1, player, crate, keyType, true)) {
            Methods.failedToTakeKey(player, crate);
            crazyManager.removePlayerFromOpeningList(player);
            return;
        }

        if (hologramManager != null) hologramManager.remove(loc);

        final ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.BLACK);
        colors.add(Color.AQUA);
        colors.add(Color.MAROON);
        colors.add(Color.PURPLE);

        crazyManager.addCrateTask(player, new BukkitRunnable() {
            final Random r = new Random();
            final int color = r.nextInt(colors.size());
            int l = 0;
            final Location L = loc.clone().add(.5, 25, .5);
            
            @Override
            public void run() {
                L.subtract(0, 1, 0);
                Methods.firework(L, colors.get(color));
                l++;

                if (l == 25) {
                    crazyManager.endCrate(player);
                    // The key type is set to free because the key has already been taken above.
                    QuickCrate.openCrate(player, loc, crate, KeyType.FREE_KEY, hologramManager);
                }
            }
        }.runTaskTimer(plugin, 0, 2));
    }
}