package us.crazycrew.crazycrates.paper.cratetypes;

import org.bukkit.SoundCategory;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import us.crazycrew.crazycrates.paper.api.frame.BukkitUserManager;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.objects.ItemBuilder;
import us.crazycrew.crazycrates.paper.api.objects.Prize;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CSGO implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesLoader cratesLoader = this.plugin.getCratesLoader();
    private final @NotNull CrazyManager crazyManager = this.cratesLoader.getCrazyManager();
    private final @NotNull BukkitUserManager userManager = this.cratesLoader.getUserManager();
    private final @NotNull Methods methods = this.cratesLoader.getMethods();
    
    private void setGlass(Inventory inv) {
        HashMap<Integer, ItemStack> glass = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            if (i < 9 && i != 3) glass.put(i, inv.getItem(i));
        }

        for (int i : glass.keySet()) {
            if (inv.getItem(i) == null) {
                ItemStack item = this.methods.getRandomPaneColor().setName(" ").build();
                inv.setItem(i, item);
                inv.setItem(i + 18, item);
            }
        }

        for (int i = 1; i < 10; i++) {
            if (i < 9 && i != 4) glass.put(i, inv.getItem(i));
        }

        ItemStack item = this.methods.getRandomPaneColor().setName(" ").build();

        inv.setItem(0, glass.get(1));
        inv.setItem(18, glass.get(1));
        inv.setItem(1, glass.get(2));
        inv.setItem(1 + 18, glass.get(2));
        inv.setItem(2, glass.get(3));
        inv.setItem(2 + 18, glass.get(3));
        inv.setItem(3, glass.get(5));
        inv.setItem(3 + 18, glass.get(5));
        inv.setItem(4, new ItemBuilder().setMaterial(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
        inv.setItem(4 + 18, new ItemBuilder().setMaterial(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
        inv.setItem(5, glass.get(6));
        inv.setItem(5 + 18, glass.get(6));
        inv.setItem(6, glass.get(7));
        inv.setItem(6 + 18, glass.get(7));
        inv.setItem(7, glass.get(8));
        inv.setItem(7 + 18, glass.get(8));
        inv.setItem(8, item);
        inv.setItem(8 + 18, item);
    }
    
    public void openCSGO(Player player, Crate crate, KeyType keyType, boolean checkHand) {
        Inventory inv = this.plugin.getServer().createInventory(null, 27, this.methods.sanitizeColor(crate.getFile().getString("Crate.CrateName")));
        setGlass(inv);

        UUID uuid = player.getUniqueId();

        for (int i = 9; i > 8 && i < 18; i++) {
            inv.setItem(i, crate.pickPrize(player).getDisplayItem());
        }

        player.openInventory(inv);

        if (this.userManager.takeKeys(1, player.getUniqueId(), crate.getName(), keyType, checkHand)) {
            startCSGO(player, inv, crate);
        } else {
            this.crazyManager.removePlayerFromOpeningList(uuid);
        }
    }
    
    private void startCSGO(Player player, final Inventory inv, Crate crate) {
        UUID uuid = player.getUniqueId();

        this.crazyManager.addCrateTask(uuid, new BukkitRunnable() {
            int time = 1;
            int full = 0;
            int open = 0;
            
            @Override
            public void run() {
                if (full <= 50) { // When Spinning
                    moveItems(inv, player, crate);
                    setGlass(inv);

                    //TODO() make volume/pitch configurable and sound type configurable.
                    //TODO() Adopt the new sound system including custom sounds.
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1f, 1f);
                }

                open++;

                if (open >= 5) {
                    player.openInventory(inv);

                    open = 0;
                }

                full++;
                if (full > 51) {
                    if (slowSpin().contains(time)) { // When Slowing Down
                        moveItems(inv, player, crate);
                        setGlass(inv);

                        //TODO() make volume/pitch configurable and sound type configurable.
                        //TODO() Adopt the new sound system including custom sounds.
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1f, 1f);
                    }

                    time++;

                    if (time == 60) { // When done
                        //TODO() make volume/pitch configurable and sound type configurable.
                        //TODO() Adopt the new sound system including custom sounds.
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 1f);

                        crazyManager.endCrate(uuid);
                        Prize prize = crate.getPrize(inv.getItem(13));

                        methods.pickPrize(player, crate, prize);

                        crazyManager.removePlayerFromOpeningList(uuid);
                        cancel();

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (player.getOpenInventory().getTopInventory().equals(inv)) player.closeInventory();
                            }
                        }.runTaskLater(plugin, 40);
                    } else if (time > 60) { // Added this due reports of the prizes spamming when low tps.
                        cancel();
                    }
                }
            }
        }.runTaskTimer(this.plugin, 1, 1));
    }

    private ArrayList<Integer> slowSpin() {
        ArrayList<Integer> slow = new ArrayList<>();
        int full = 120;
        int cut = 15;

        for (int i = 120; cut > 0; full--) {
            if (full <= i - cut || full >= i - cut) {
                slow.add(i);
                i -= cut;
                cut--;
            }
        }

        return slow;
    }
    
    private void moveItems(Inventory inv, Player player, Crate crate) {
        ArrayList<ItemStack> items = new ArrayList<>();

        for (int i = 9; i > 8 && i < 17; i++) {
            items.add(inv.getItem(i));
        }

        inv.setItem(9, crate.pickPrize(player).getDisplayItem());

        for (int i = 0; i < 8; i++) {
            inv.setItem(i + 10, items.get(i));
        }
    }
}