package com.badbones69.crazycrates.cratetypes;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.Methods;
import com.badbones69.crazycrates.api.CrazyManager;
import com.badbones69.crazycrates.api.enums.KeyType;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.utilities.CommonUtils;
import com.badbones69.crazycrates.utilities.ScheduleUtils;
import com.badbones69.crazycrates.utilities.handlers.tasks.CrateTaskHandler;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class RouletteCrate implements Listener {

    private final CrazyCrates plugin = CrazyCrates.getPlugin();

    @Inject private CrazyManager crazyManager;

    @Inject private Methods methods;
    @Inject private ScheduleUtils scheduleUtils;
    @Inject private CommonUtils commonUtils;

    // Task Handler
    @Inject private CrateTaskHandler crateTaskHandler;

    private void setGlass(Inventory inv) {
        for (int i = 0; i < 27; i++) {
            if (i != 13) inv.setItem(i, methods.getRandomPaneColor().setName(" ").build());
        }
    }

    public void openRoulette(Player player, Crate crate, KeyType keyType, boolean checkHand) {
        Inventory inv = plugin.getServer().createInventory(null, 27, crate.getFile().getString("Crate.CrateName"));
        setGlass(inv);
        inv.setItem(13, crate.pickPrize(player).getDisplayItem());
        player.openInventory(inv);

        if (!crazyManager.takeKeys(1, player, crate, keyType, checkHand)) {
            methods.failedToTakeKey(player, crate);
            crazyManager.removePlayerFromOpeningList(player);
            return;
        }

        startRoulette(player, inv, crate);
    }

    private void startRoulette(final Player player, final Inventory inv, final Crate crate) {

        CrateTaskHandler crateTaskHandler = new CrateTaskHandler();

        crateTaskHandler.addTask(player, scheduleUtils.timer(2L, 2L, () -> {

            AtomicInteger full = new AtomicInteger();
            AtomicInteger time = new AtomicInteger();
            AtomicInteger even = new AtomicInteger();
            AtomicInteger open = new AtomicInteger();

            if (full.incrementAndGet() <= 15) {
                inv.setItem(13, crate.pickPrize(player).getDisplayItem());
                setGlass(inv);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);

                if (even.incrementAndGet() >= 4) {
                    even.set(0);
                    inv.setItem(13, crate.pickPrize(player).getDisplayItem());
                }
            }

            if (open.incrementAndGet() >= 5) {
                player.openInventory(inv);
                open.set(0);
            }

            if (full.incrementAndGet() > 16) {

                if (commonUtils.slowSpin().contains(time.get())) {
                    setGlass(inv);
                    inv.setItem(13, crate.pickPrize(player).getDisplayItem());
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                }

                if (time.incrementAndGet() >= 23) {
                    crateTaskHandler.endCrate(player, crate, inv);

                    scheduleUtils.later(40L, () -> {
                        if (player.getOpenInventory().getTopInventory().equals(inv)) player.closeInventory();
                    });
                }
            }
        }));
    }
}