package com.badbones69.crazycrates.folia.cratetypes;

import com.badbones69.crazycrates.api.enums.keys.KeyType;
import com.badbones69.crazycrates.folia.CrazyCrates;
import com.badbones69.crazycrates.folia.utils.Methods;
import com.badbones69.crazycrates.folia.api.CrazyManager;
import com.badbones69.crazycrates.folia.api.objects.Crate;
import com.badbones69.crazycrates.folia.api.objects.Prize;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Roulette implements Listener {

    private static final com.badbones69.crazycrates.folia.CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private static final CrazyManager crazyManager = plugin.getCrazyManager();
    
    private static void setGlass(Inventory inv) {
        for (int i = 0; i < 27; i++) {
            if (i != 13) {
                ItemStack item = com.badbones69.crazycrates.folia.utils.Methods.getRandomPaneColor().setName(" ").build();
                inv.setItem(i, item);
            }
        }
    }
    
    public static void openRoulette(Player player, Crate crate, KeyType keyType, boolean checkHand) {
        Inventory inv = plugin.getServer().createInventory(null, 27, com.badbones69.crazycrates.folia.utils.Methods.sanitizeColor(crate.getFile().getString("Crate.CrateName")));
        setGlass(inv);
        inv.setItem(13, crate.pickPrize(player).getDisplayItem());
        player.openInventory(inv);

        if (!crazyManager.takeKeys(1, player, crate, keyType, checkHand)) {
            com.badbones69.crazycrates.folia.utils.Methods.failedToTakeKey(player, crate);
            crazyManager.removePlayerFromOpeningList(player);
            return;
        }

        startRoulette(player, inv, crate);
    }
    
    private static void startRoulette(final Player player, final Inventory inv, final Crate crate) {
        crazyManager.addCrateTask(player, new BukkitRunnable() {
            int time = 1;
            int even = 0;
            int full = 0;
            int open = 0;

            @Override
            public void run() {
                if (full <= 15) {
                    inv.setItem(13, crate.pickPrize(player).getDisplayItem());
                    setGlass(inv);
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    even++;

                    if (even >= 4) {
                        even = 0;
                        inv.setItem(13, crate.pickPrize(player).getDisplayItem());
                    }
                }

                open++;

                if (open >= 5) {
                    player.openInventory(inv);
                    open = 0;
                }

                full++;

                if (full > 16) {

                    if (com.badbones69.crazycrates.folia.utils.Methods.slowSpin().contains(time)) {
                        setGlass(inv);
                        inv.setItem(13, crate.pickPrize(player).getDisplayItem());
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    }

                    time++;

                    if (time >= 23) {
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        crazyManager.endCrate(player);
                        Prize prize = crate.getPrize(inv.getItem(13));

                        Methods.checkPrize(prize, crazyManager, plugin, player, crate);

                        crazyManager.removePlayerFromOpeningList(player);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (player.getOpenInventory().getTopInventory().equals(inv)) player.closeInventory();
                            }
                        }.runTaskLater(plugin, 40);
                    }
                }
            }
        }.runTaskTimer(plugin, 2, 2));
    }
}