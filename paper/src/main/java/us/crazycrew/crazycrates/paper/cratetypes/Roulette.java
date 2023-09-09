package us.crazycrew.crazycrates.paper.cratetypes;

import org.bukkit.SoundCategory;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.frame.BukkitUserManager;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.objects.Prize;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;
import java.util.UUID;

public class Roulette implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesLoader cratesLoader = this.plugin.getCratesLoader();
    private final @NotNull BukkitUserManager userManager = this.cratesLoader.getUserManager();
    private final @NotNull CrazyManager crazyManager = this.cratesLoader.getCrazyManager();
    private final @NotNull Methods methods = this.cratesLoader.getMethods();
    
    private void setGlass(Inventory inv) {
        for (int i = 0; i < 27; i++) {
            if (i != 13) {
                ItemStack item = this.methods.getRandomPaneColor().setName(" ").build();
                inv.setItem(i, item);
            }
        }
    }
    
    public void openRoulette(Player player, Crate crate, KeyType keyType, boolean checkHand) {
        Inventory inv = this.plugin.getServer().createInventory(null, 27, this.methods.sanitizeColor(crate.getFile().getString("Crate.CrateName")));
        setGlass(inv);

        UUID uuid = player.getUniqueId();

        inv.setItem(13, crate.pickPrize(player).getDisplayItem());

        player.openInventory(inv);

        if (!this.userManager.takeKeys(1, player.getUniqueId(), crate.getName(), keyType, checkHand)) {
            this.crazyManager.removePlayerFromOpeningList(uuid);
            return;
        }

        startRoulette(player, inv, crate);
    }
    
    private void startRoulette(Player player, final Inventory inv, final Crate crate) {
        UUID uuid = player.getUniqueId();

        this.crazyManager.addCrateTask(uuid, new BukkitRunnable() {
            int time = 1;
            int even = 0;
            int full = 0;
            int open = 0;

            @Override
            public void run() {
                if (full <= 15) {
                    inv.setItem(13, crate.pickPrize(player).getDisplayItem());
                    setGlass(inv);

                    //TODO() make volume/pitch configurable and sound type configurable.
                    //TODO() Adopt the new sound system including custom sounds.
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1f, 1f);

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
                    if (methods.slowSpin().contains(time)) {
                        setGlass(inv);
                        inv.setItem(13, crate.pickPrize(player).getDisplayItem());

                        //TODO() make volume/pitch configurable and sound type configurable.
                        //TODO() Adopt the new sound system including custom sounds.
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1f, 1f);
                    }

                    time++;

                    if (time >= 23) {
                        //TODO() make volume/pitch configurable and sound type configurable.
                        //TODO() Adopt the new sound system including custom sounds.
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS,1f, 1f);

                        crazyManager.endCrate(uuid);
                        Prize prize = crate.getPrize(inv.getItem(13));

                        methods.pickPrize(player, crate, prize);

                        crazyManager.removePlayerFromOpeningList(uuid);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (player.getOpenInventory().getTopInventory().equals(inv)) player.closeInventory();
                            }
                        }.runTaskLater(plugin, 40);
                    }
                }
            }
        }.runTaskTimer(this.plugin, 2, 2));
    }
}