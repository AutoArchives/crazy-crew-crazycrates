package us.crazycrew.crazycrates.paper.cratetypes;

import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import us.crazycrew.crazycrates.paper.api.events.PlayerPrizeEvent;
import us.crazycrew.crazycrates.paper.api.frame.BukkitUserManager;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.objects.ItemBuilder;
import us.crazycrew.crazycrates.paper.api.objects.Prize;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;
import java.util.ArrayList;
import java.util.UUID;

public class Wonder implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesLoader cratesLoader = this.plugin.getCratesLoader();
    private final @NotNull CrazyManager crazyManager = this.cratesLoader.getCrazyManager();
    private final @NotNull BukkitUserManager userManager = this.cratesLoader.getUserManager();
    private final @NotNull Methods methods = this.cratesLoader.getMethods();
    
    public void startWonder(Player player, Crate crate, KeyType keyType, boolean checkHand) {
        UUID uuid = player.getUniqueId();

        if (!this.userManager.takeKeys(1, player.getUniqueId(), crate.getName(), keyType, checkHand)) {
            this.crazyManager.removePlayerFromOpeningList(uuid);
            return;
        }

        final Inventory inv = this.plugin.getServer().createInventory(null, 45, crate.getCrateInventoryName());
        final ArrayList<String> slots = new ArrayList<>();

        for (int i = 0; i < 45; i++) {
            Prize prize = crate.pickPrize(player);
            slots.add(i + "");
            inv.setItem(i, prize.getDisplayItem());
        }

        player.openInventory(inv);

        this.crazyManager.addCrateTask(uuid, new BukkitRunnable() {
            int fullTime = 0;
            int timer = 0;
            int slot1 = 0;
            int slot2 = 44;
            final ArrayList<Integer> Slots = new ArrayList<>();
            Prize prize = null;
            
            @Override
            public void run() {
                if (timer >= 2 && fullTime <= 65) {
                    slots.remove(slot1 + "");
                    slots.remove(slot2 + "");
                    Slots.add(slot1);
                    Slots.add(slot2);
                    inv.setItem(slot1, new ItemBuilder().setMaterial(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
                    inv.setItem(slot2, new ItemBuilder().setMaterial(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());

                    for (String slot : slots) {
                        prize = crate.pickPrize(player);
                        inv.setItem(Integer.parseInt(slot), prize.getDisplayItem());
                    }

                    slot1++;
                    slot2--;
                }

                if (fullTime > 67) {
                    ItemStack item = methods.getRandomPaneColor().setName(" ").build();

                    for (int slot : Slots) {
                        inv.setItem(slot, item);
                    }
                }

                player.openInventory(inv);

                if (fullTime > 100) {
                    crazyManager.endCrate(uuid);

                    // Only run this code if the player isn't null.
                    player.closeInventory();
                    crazyManager.givePrize(player, prize, crate);

                    if (prize.useFireworks()) methods.firework(player.getLocation().add(0, 1, 0));

                    plugin.getServer().getPluginManager().callEvent(new PlayerPrizeEvent(uuid, crate, crate.getName(), prize));

                    crazyManager.removePlayerFromOpeningList(uuid);

                    return;
                }

                fullTime++;
                timer++;

                if (timer > 2) timer = 0;
            }
        }.runTaskTimer(this.plugin, 0, 2));
    }
}