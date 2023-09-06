package us.crazycrew.crazycrates.paper.cratetypes;

import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.objects.Prize;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesPlugin;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public class Roulette implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesPlugin cratesPlugin = null;
    private final @NotNull CrazyManager crazyManager = this.cratesPlugin.getCrazyManager();
    private final @NotNull Methods methods = this.cratesPlugin.getMethods();
    
    private void setGlass(Inventory inv) {
        for (int i = 0; i < 27; i++) {
            if (i != 13) {
                ItemStack item = methods.getRandomPaneColor().setName(" ").build();
                inv.setItem(i, item);
            }
        }
    }
    
    public void openRoulette(UUID uuid, Crate crate, KeyType keyType, boolean checkHand) {
        Inventory inv = plugin.getServer().createInventory(null, 27, methods.sanitizeColor(crate.getFile().getString("Crate.CrateName")));
        setGlass(inv);
        inv.setItem(13, crate.pickPrize(uuid).getDisplayItem());

        Player player = this.plugin.getServer().getPlayer(uuid);

        if (player != null) {
            player.openInventory(inv);
        }

        if (!crazyManager.takeKeys(1, uuid, crate, keyType, checkHand)) {
            if (player != null) {
                methods.failedToTakeKey(player.getName(), crate);
            }

            crazyManager.removePlayerFromOpeningList(uuid);
            return;
        }

        startRoulette(uuid, inv, crate);
    }
    
    private void startRoulette(UUID uuid, final Inventory inv, final Crate crate) {
        Player player = this.plugin.getServer().getPlayer(uuid);

        crazyManager.addCrateTask(uuid, new BukkitRunnable() {
            int time = 1;
            int even = 0;
            int full = 0;
            int open = 0;

            @Override
            public void run() {
                if (full <= 15) {
                    inv.setItem(13, crate.pickPrize(uuid).getDisplayItem());
                    setGlass(inv);

                    if (player != null) {
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    }

                    even++;

                    if (even >= 4) {
                        even = 0;
                        inv.setItem(13, crate.pickPrize(uuid).getDisplayItem());
                    }
                }

                open++;

                if (open >= 5) {
                    if (player != null) {
                        player.openInventory(inv);
                    }

                    open = 0;
                }

                full++;

                if (full > 16) {
                    if (methods.slowSpin().contains(time)) {
                        setGlass(inv);
                        inv.setItem(13, crate.pickPrize(uuid).getDisplayItem());

                        if (player != null) {
                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                        }
                    }

                    time++;

                    if (time >= 23) {
                        if (player != null) {
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        }

                        crazyManager.endCrate(uuid);
                        Prize prize = crate.getPrize(inv.getItem(13));

                        methods.pickPrize(uuid, crate, prize);

                        crazyManager.removePlayerFromOpeningList(uuid);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (player != null && player.getOpenInventory().getTopInventory().equals(inv)) player.closeInventory();
                            }
                        }.runTaskLater(plugin, 40);
                    }
                }
            }
        }.runTaskTimer(plugin, 2, 2));
    }
}