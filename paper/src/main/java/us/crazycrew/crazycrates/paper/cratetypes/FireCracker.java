package us.crazycrew.crazycrates.paper.cratetypes;

import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import us.crazycrew.crazycrates.paper.api.interfaces.HologramController;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class FireCracker {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesLoader cratesLoader = this.plugin.getCratesLoader();
    private final @NotNull CrazyManager crazyManager = this.cratesLoader.getCrazyManager();
    private final @NotNull Methods methods = this.cratesLoader.getMethods();
    
    public void startFireCracker(Player player, final Crate crate, KeyType keyType, final Location loc, HologramController hologramController) {
        UUID uuid = player.getUniqueId();

        if (!crazyManager.takeKeys(1, player, crate, keyType, true)) {
            methods.failedToTakeKey(player.getName(), crate);

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
                    plugin.getQuickCrate().openCrate(player, loc, crate, KeyType.FREE_KEY, hologramController);
                }
            }
        }.runTaskTimer(plugin, 0, 2));
    }
}