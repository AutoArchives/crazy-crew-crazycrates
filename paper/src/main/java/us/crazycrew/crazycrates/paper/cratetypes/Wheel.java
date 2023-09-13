package us.crazycrew.crazycrates.paper.cratetypes;

import org.bukkit.SoundCategory;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.plugin.frame.BukkitUserManager;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.objects.ItemBuilder;
import us.crazycrew.crazycrates.paper.api.objects.Prize;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Wheel implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyHandler crazyHandler = this.plugin.getCrazyHandler();
    private final @NotNull BukkitUserManager userManager = this.crazyHandler.getUserManager();
    private final @NotNull CrazyManager crazyManager = this.crazyHandler.getCrazyManager();
    private final @NotNull Methods methods = this.crazyHandler.getMethods();
    
    private final Map<UUID, HashMap<Integer, ItemStack>> rewards = new HashMap<>();
    
    public void startWheel(Player player, Crate crate, KeyType keyType, boolean checkHand) {
        UUID uuid = player.getUniqueId();

        if (!this.userManager.takeKeys(1, player.getUniqueId(), crate.getName(), keyType, checkHand)) {
            this.crazyManager.removePlayerFromOpeningList(uuid);
            return;
        }

        final Inventory inv = this.plugin.getServer().createInventory(null, 54, this.methods.sanitizeColor(crate.getFile().getString("Crate.CrateName")));

        for (int i = 0; i < 54; i++) {
            inv.setItem(i, new ItemBuilder().setMaterial(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
        }

        HashMap<Integer, ItemStack> items = new HashMap<>();

        for (int i : getBorder()) {
            Prize prize = crate.pickPrize(player);
            inv.setItem(i, prize.getDisplayItem());
            items.put(i, prize.getDisplayItem());
        }

        this.rewards.put(uuid, items);

        player.openInventory(inv);

        this.crazyManager.addCrateTask(uuid, new BukkitRunnable() {
            final ArrayList<Integer> slots = getBorder();
            int i = 0;
            int f = 17;
            int full = 0;
            final int timer = methods.randomNumber(42, 68);
            int slower = 0;
            int open = 0;
            int slow = 0;
            
            @Override
            public void run() {

                if (i >= 18) i = 0;

                if (f >= 18) f = 0;

                if (full < timer) checkLore();

                if (full >= timer) {
                    if (methods.slowSpin().contains(slower)) checkLore();

                    if (full == timer + 47) {
                        //TODO() make volume/pitch configurable and sound type configurable.
                        //TODO() Adopt the new sound system including custom sounds.
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 1f);
                    }

                    if (full >= timer + 47) {
                        slow++;

                        if (slow >= 2) {
                            ItemStack item = methods.getRandomPaneColor().setName(" ").build();

                            for (int slot = 0; slot < 54; slot++) {
                                if (!getBorder().contains(slot)) inv.setItem(slot, item);
                            }

                            slow = 0;
                        }
                    }

                    if (full >= (timer + 55 + 47)) {
                        Prize prize = null;

                        if (crazyManager.isInOpeningList(uuid)) prize = crate.getPrize(rewards.get(uuid).get(slots.get(f)));

                        methods.pickPrize(player, crate, prize);

                        player.closeInventory();

                        crazyManager.removePlayerFromOpeningList(uuid);
                        crazyManager.endCrate(uuid);
                    }

                    slower++;
                }

                full++;
                open++;

                if (open > 5) {
                    player.openInventory(inv);
                    open = 0;
                }
            }

            private void checkLore() {
                if (rewards.get(uuid).get(slots.get(i)).getItemMeta().hasLore()) {
                    inv.setItem(slots.get(i), new ItemBuilder().setMaterial(Material.LIME_STAINED_GLASS_PANE).setName(rewards.get(uuid).get(slots.get(i)).getItemMeta().getDisplayName()).setLore(rewards.get(uuid).get(slots.get(i)).getItemMeta().getLore()).build());
                } else {
                    inv.setItem(slots.get(i), new ItemBuilder().setMaterial(Material.LIME_STAINED_GLASS_PANE).setName(rewards.get(uuid).get(slots.get(i)).getItemMeta().getDisplayName()).build());
                }

                inv.setItem(slots.get(f), rewards.get(uuid).get(slots.get(f)));

                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);

                i++;
                f++;
            }
        }.runTaskTimer(this.plugin, 1, 1));
    }

    private ArrayList<Integer> getBorder() {
        ArrayList<Integer> slots = new ArrayList<>();

        slots.add(13);
        slots.add(14);
        slots.add(15);
        slots.add(16);
        slots.add(25);
        slots.add(34);
        slots.add(43);
        slots.add(42);
        slots.add(41);
        slots.add(40);
        slots.add(39);
        slots.add(38);
        slots.add(37);
        slots.add(28);
        slots.add(19);
        slots.add(10);
        slots.add(11);
        slots.add(12);

        return slots;
    }
}