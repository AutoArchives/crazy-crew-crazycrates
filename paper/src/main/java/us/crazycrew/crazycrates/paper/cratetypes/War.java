package us.crazycrew.crazycrates.paper.cratetypes;

import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.events.PlayerPrizeEvent;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.objects.Prize;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesPlugin;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import us.crazycrew.crazycrates.paper.api.objects.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.UUID;

public class War implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesPlugin cratesPlugin = null;
    private final @NotNull CrazyManager crazyManager = this.cratesPlugin.getCrazyManager();
    private final @NotNull Methods methods = this.cratesPlugin.getMethods();
    
    private final String crateNameString = "Crate.CrateName";
    private HashMap<ItemStack, String> colorCodes;
    private final HashMap<UUID, Boolean> canPick = new HashMap<>();
    private final HashMap<UUID, Boolean> canClose = new HashMap<>();
    
    public void openWarCrate(UUID uuid, Crate crate, KeyType keyType, boolean checkHand) {
        String crateName = methods.sanitizeColor(crate.getFile().getString(crateNameString));
        Inventory inv = plugin.getServer().createInventory(null, 9, crateName);

        Player player = this.plugin.getServer().getPlayer(uuid);

        if (player != null) {
            setRandomPrizes(uuid, inv, crate, crateName);
            InventoryView inventoryView;
            inventoryView = player.openInventory(inv);
            canPick.put(uuid, false);
            canClose.put(uuid, false);

            if (!crazyManager.takeKeys(1, uuid, crate, keyType, checkHand)) {
                methods.failedToTakeKey(player.getName(), crate);
                crazyManager.removePlayerFromOpeningList(uuid);
                canClose.remove(uuid);
                canPick.remove(uuid);
                return;
            }

            if (inventoryView != null) {
                startWar(uuid, inv, crate, inventoryView.getTitle());
            }
        }
    }
    
    private void startWar(final UUID uuid, final Inventory inv, final Crate crate, final String inventoryTitle) {
        Player player = this.plugin.getServer().getPlayer(uuid);

        if (player != null) {
            crazyManager.addCrateTask(uuid, new BukkitRunnable() {
                int full = 0;
                int open = 0;

                @Override
                public void run() {
                    if (full < 25) {
                        setRandomPrizes(uuid, inv, crate, inventoryTitle);
                        player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 1, 1);
                    }

                    open++;

                    if (open >= 3) {
                        player.openInventory(inv);
                        open = 0;
                    }

                    full++;

                    if (full == 26) { // Finished Rolling
                        player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 1, 1);
                        setRandomGlass(uuid, inv, inventoryTitle);
                        canPick.put(uuid, true);
                    }
                }
            }.runTaskTimer(plugin, 1, 3));
        }
    }
    
    private void setRandomPrizes(UUID uuid, Inventory inv, Crate crate, String inventoryTitle) {
        if (crazyManager.isInOpeningList(uuid) && inventoryTitle.equalsIgnoreCase(methods.sanitizeColor(crazyManager.getOpeningCrate(uuid).getFile().getString(crateNameString)))) {
            for (int i = 0; i < 9; i++) {
                inv.setItem(i, crate.pickPrize(uuid).getDisplayItem());
            }
        }
    }
    
    private void setRandomGlass(UUID uuid, Inventory inv, String inventoryTitle) {
        if (crazyManager.isInOpeningList(uuid) && inventoryTitle.equalsIgnoreCase(methods.sanitizeColor(crazyManager.getOpeningCrate(uuid).getFile().getString(crateNameString)))) {

            if (colorCodes == null) colorCodes = getColorCode();

            ItemBuilder itemBuilder = methods.getRandomPaneColor();
            itemBuilder.setName("&" + colorCodes.get(itemBuilder.build()) + "&l???");
            ItemStack item = itemBuilder.build();

            for (int i = 0; i < 9; i++) {
                inv.setItem(i, item);
            }
        }
    }
    
    private static HashMap<ItemStack, String> getColorCode() {
        HashMap<ItemStack, String> colorCodes = new HashMap<>();

        colorCodes.put(new ItemBuilder().setMaterial(Material.WHITE_STAINED_GLASS_PANE).build(), "f");
        colorCodes.put(new ItemBuilder().setMaterial(Material.ORANGE_STAINED_GLASS_PANE).build(), "6");
        colorCodes.put(new ItemBuilder().setMaterial(Material.MAGENTA_STAINED_GLASS_PANE).build(), "d");
        colorCodes.put(new ItemBuilder().setMaterial(Material.LIGHT_BLUE_STAINED_GLASS_PANE).build(), "3");
        colorCodes.put(new ItemBuilder().setMaterial(Material.YELLOW_STAINED_GLASS_PANE).build(), "e");
        colorCodes.put(new ItemBuilder().setMaterial(Material.LIME_STAINED_GLASS_PANE).build(), "a");
        colorCodes.put(new ItemBuilder().setMaterial(Material.PINK_STAINED_GLASS_PANE).build(), "c");
        colorCodes.put(new ItemBuilder().setMaterial(Material.GRAY_STAINED_GLASS_PANE).build(), "7");
        colorCodes.put(new ItemBuilder().setMaterial(Material.LIGHT_GRAY_STAINED_GLASS_PANE).build(), "7");
        colorCodes.put(new ItemBuilder().setMaterial(Material.CYAN_STAINED_GLASS_PANE).build(), "3");
        colorCodes.put(new ItemBuilder().setMaterial(Material.PURPLE_STAINED_GLASS_PANE).build(), "5");
        colorCodes.put(new ItemBuilder().setMaterial(Material.BLUE_STAINED_GLASS_PANE).build(), "9");
        colorCodes.put(new ItemBuilder().setMaterial(Material.BROWN_STAINED_GLASS_PANE).build(), "6");
        colorCodes.put(new ItemBuilder().setMaterial(Material.GREEN_STAINED_GLASS_PANE).build(), "2");
        colorCodes.put(new ItemBuilder().setMaterial(Material.RED_STAINED_GLASS_PANE).build(), "4");
        colorCodes.put(new ItemBuilder().setMaterial(Material.BLACK_STAINED_GLASS_PANE).build(), "8");

        return colorCodes;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        final Inventory inv = e.getClickedInventory();
        final Player player = (Player) e.getWhoClicked();
        final UUID uuid = player.getUniqueId();

        if (inv != null && canPick.containsKey(uuid) && crazyManager.isInOpeningList(uuid)) {
            Crate crate = crazyManager.getOpeningCrate(uuid);

            if (crate.getCrateType() == CrateType.WAR && canPick.get(uuid)) {
                ItemStack item = e.getCurrentItem();

                if (item != null && item.getType().toString().contains(Material.GLASS_PANE.toString())) {
                    final int slot = e.getRawSlot();
                    Prize prize = crate.pickPrize(uuid);
                    inv.setItem(slot, prize.getDisplayItem());

                    if (crazyManager.hasCrateTask(uuid)) crazyManager.endCrate(uuid);

                    canPick.remove(uuid);
                    canClose.put(uuid, true);
                    crazyManager.givePrize(uuid, prize, crate);

                    if (prize.useFireworks()) methods.firework(player.getLocation().add(0, 1, 0));

                    plugin.getServer().getPluginManager().callEvent(new PlayerPrizeEvent(uuid, crate, crate.getName(), prize));
                    crazyManager.removePlayerFromOpeningList(uuid);
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
                    // Sets all other non-picked prizes to show what they could have been.

                    crazyManager.addCrateTask(uuid, new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 9; i++) {
                                if (i != slot) inv.setItem(i, crate.pickPrize(uuid).getDisplayItem());
                            }

                            if (crazyManager.hasCrateTask(uuid)) crazyManager.endCrate(uuid);

                            // Removing other items then the prize.
                            crazyManager.addCrateTask(uuid, new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < 9; i++) {
                                        if (i != slot) inv.setItem(i, new ItemStack(Material.AIR));
                                    }

                                    if (crazyManager.hasCrateTask(uuid)) crazyManager.endCrate(uuid);

                                    // Closing the inventory when finished.
                                    crazyManager.addCrateTask(uuid, new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            if (crazyManager.hasCrateTask(uuid)) crazyManager.endCrate(uuid);

                                            player.closeInventory();
                                        }
                                    }.runTaskLater(plugin, 30));
                                }
                            }.runTaskLater(plugin, 30));
                        }
                    }.runTaskLater(plugin, 30));
                }
            }
        }
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        final Player player = (Player) e.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (canClose.containsKey(uuid) && canClose.get(uuid)) {
            for (Crate crate : crazyManager.getCrates()) {
                if (crate.getCrateType() == CrateType.WAR && e.getView().getTitle().equalsIgnoreCase(methods.sanitizeColor(crate.getFile().getString(crateNameString)))) {
                    canClose.remove(uuid);

                    if (crazyManager.hasCrateTask(uuid)) crazyManager.endCrate(uuid);
                }
            }
        }
    }
}