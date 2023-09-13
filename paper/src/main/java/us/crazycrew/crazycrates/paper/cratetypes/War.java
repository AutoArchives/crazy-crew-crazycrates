package us.crazycrew.crazycrates.paper.cratetypes;

import org.bukkit.SoundCategory;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.events.PlayerPrizeEvent;
import us.crazycrew.crazycrates.paper.api.plugin.frame.BukkitUserManager;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.objects.Prize;
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
import us.crazycrew.crazycrates.paper.api.plugin.CrazyHandler;
import java.util.HashMap;
import java.util.UUID;

public class War implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyHandler crazyHandler = this.plugin.getCrazyHandler();
    private final @NotNull BukkitUserManager userManager = this.crazyHandler.getUserManager();
    private final @NotNull CrazyManager crazyManager = this.crazyHandler.getCrazyManager();
    private final @NotNull Methods methods = this.crazyHandler.getMethods();
    
    private final String crateNameString = "Crate.CrateName";
    private HashMap<ItemStack, String> colorCodes;
    private final HashMap<UUID, Boolean> canPick = new HashMap<>();
    private final HashMap<UUID, Boolean> canClose = new HashMap<>();
    
    public void openWarCrate(Player player, Crate crate, KeyType keyType, boolean checkHand) {
        String crateName = this.methods.sanitizeColor(crate.getFile().getString(this.crateNameString));
        Inventory inv = this.plugin.getServer().createInventory(null, 9, crateName);

        UUID uuid = player.getUniqueId();

        setRandomPrizes(player, inv, crate, crateName);
        InventoryView inventoryView;
        inventoryView = player.openInventory(inv);
        this.canPick.put(uuid, false);
        this.canClose.put(uuid, false);

        if (!this.userManager.takeKeys(1, player.getUniqueId(), crate.getName(), keyType, checkHand)) {
            this.crazyManager.removePlayerFromOpeningList(uuid);
            this.canClose.remove(uuid);
            this.canPick.remove(uuid);
            return;
        }

        if (inventoryView != null) {
            startWar(player, inv, crate, inventoryView.getTitle());
        }
    }
    
    private void startWar(Player player, final Inventory inv, final Crate crate, final String inventoryTitle) {
        UUID uuid = player.getUniqueId();

        this.crazyManager.addCrateTask(uuid, new BukkitRunnable() {
            int full = 0;
            int open = 0;

            @Override
            public void run() {
                if (full < 25) {
                    setRandomPrizes(player, inv, crate, inventoryTitle);
                    //TODO() make volume/pitch configurable and sound type configurable.
                    //TODO() Adopt the new sound system including custom sounds.
                    player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 1f, 1f);
                }

                open++;

                if (open >= 3) {
                    player.openInventory(inv);
                    open = 0;
                }

                full++;

                if (full == 26) { // Finished Rolling
                    //TODO() make volume/pitch configurable and sound type configurable.
                    //TODO() Adopt the new sound system including custom sounds.
                    player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 1f, 1f);
                    setRandomGlass(uuid, inv, inventoryTitle);
                    canPick.put(uuid, true);
                }
            }
        }.runTaskTimer(this.plugin, 1, 3));
    }
    
    private void setRandomPrizes(Player player, Inventory inv, Crate crate, String inventoryTitle) {
        UUID uuid = player.getUniqueId();

        if (this.crazyManager.isInOpeningList(uuid) && inventoryTitle.equalsIgnoreCase(this.methods.sanitizeColor(this.crazyManager.getOpeningCrate(uuid).getFile().getString(this.crateNameString)))) {
            for (int i = 0; i < 9; i++) {
                inv.setItem(i, crate.pickPrize(player).getDisplayItem());
            }
        }
    }
    
    private void setRandomGlass(UUID uuid, Inventory inv, String inventoryTitle) {
        if (this.crazyManager.isInOpeningList(uuid) && inventoryTitle.equalsIgnoreCase(this.methods.sanitizeColor(this.crazyManager.getOpeningCrate(uuid).getFile().getString(this.crateNameString)))) {
            if (this.colorCodes == null) this.colorCodes = getColorCode();

            ItemBuilder itemBuilder = this.methods.getRandomPaneColor();
            itemBuilder.setName("&" + this.colorCodes.get(itemBuilder.build()) + "&l???");
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

        if (inv != null && this.canPick.containsKey(uuid) && this.crazyManager.isInOpeningList(uuid)) {
            Crate crate = this.crazyManager.getOpeningCrate(uuid);

            if (crate.getCrateType() == CrateType.WAR && this.canPick.get(uuid)) {
                ItemStack item = e.getCurrentItem();

                if (item != null && item.getType().toString().contains(Material.GLASS_PANE.toString())) {
                    final int slot = e.getRawSlot();
                    Prize prize = crate.pickPrize(player);
                    inv.setItem(slot, prize.getDisplayItem());

                    if (this.crazyManager.hasCrateTask(uuid)) this.crazyManager.endCrate(uuid);

                    this.canPick.remove(uuid);
                    this.canClose.put(uuid, true);
                    this.crazyManager.givePrize(player, prize, crate);

                    if (prize.useFireworks()) this.methods.firework(player.getLocation().add(0, 1, 0));

                    this.plugin.getServer().getPluginManager().callEvent(new PlayerPrizeEvent(uuid, crate, crate.getName(), prize));
                    this.crazyManager.removePlayerFromOpeningList(uuid);
                    //TODO() make volume/pitch configurable and sound type configurable.
                    //TODO() Adopt the new sound system including custom sounds.
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1f, 1f);
                    // Sets all other non-picked prizes to show what they could have been.

                    this.crazyManager.addCrateTask(uuid, new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 9; i++) {
                                if (i != slot) inv.setItem(i, crate.pickPrize(player).getDisplayItem());
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
                    }.runTaskLater(this.plugin, 30));
                }
            }
        }
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        final Player player = (Player) e.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (this.canClose.containsKey(uuid) && this.canClose.get(uuid)) {
            for (Crate crate : this.crazyManager.getCrates()) {
                if (crate.getCrateType() == CrateType.WAR && e.getView().getTitle().equalsIgnoreCase(this.methods.sanitizeColor(crate.getFile().getString(this.crateNameString)))) {
                    this.canClose.remove(uuid);

                    if (this.crazyManager.hasCrateTask(uuid)) this.crazyManager.endCrate(uuid);
                }
            }
        }
    }
}