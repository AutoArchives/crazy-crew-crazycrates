package com.badbones69.crazycrates.paper.cratetypes;

import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.api.crates.CrateManager;
import com.badbones69.crazycrates.paper.api.objects.Crate;
import com.badbones69.crazycrates.paper.api.objects.Prize;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import us.crazycrew.crazycrates.paper.api.crates.menus.types.CratePrizeMenu;
import us.crazycrew.crazycrates.paper.utils.MiscUtils;
import us.crazycrew.crazycrates.paper.utils.MsgUtils;

public class Roulette implements Listener {

    @NotNull
    private static final CrazyCrates plugin = CrazyCrates.get();

    @NotNull
    private static final CrateManager crateManager = plugin.getCrateManager();
    
    private static void setGlass(Inventory inv) {
        for (int i = 0; i < 27; i++) {
            if (i != 13) {
                ItemStack item = MiscUtils.getRandomPaneColor().setName(" ").build();
                inv.setItem(i, item);
            }
        }
    }
    
    public static void openRoulette(Player player, Crate crate, KeyType keyType, boolean checkHand) {
        Inventory inventory = new CratePrizeMenu(crate, player, 27, MsgUtils.sanitizeColor(crate.getFile().getString("Crate.CrateName"))).build().getInventory();
        setGlass(inventory);
        inventory.setItem(13, crate.pickPrize(player).getDisplayItem());
        player.openInventory(inventory);

        if (!plugin.getCrazyHandler().getUserManager().takeKeys(1, player.getUniqueId(), crate.getName(), keyType, checkHand)) {
            MiscUtils.failedToTakeKey(player, crate);
            crateManager.removePlayerFromOpeningList(player);
            return;
        }

        startRoulette(player, inventory, crate);
    }
    
    private static void startRoulette(final Player player, final Inventory inv, final Crate crate) {
        crateManager.addCrateTask(player, new BukkitRunnable() {
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

                    if (MiscUtils.slowSpin().contains(time)) {
                        setGlass(inv);
                        inv.setItem(13, crate.pickPrize(player).getDisplayItem());
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    }

                    time++;

                    if (time >= 23) {
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        crateManager.endCrate(player);
                        Prize prize = crate.getPrize(inv.getItem(13));

                        plugin.getCrazyHandler().getPrizeManager().checkPrize(prize, player, crate);

                        crateManager.removePlayerFromOpeningList(player);

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