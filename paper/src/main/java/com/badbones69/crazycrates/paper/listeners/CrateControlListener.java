package com.badbones69.crazycrates.paper.listeners;

import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.Methods;
import com.badbones69.crazycrates.paper.api.v2.enums.Permissions;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import java.util.HashMap;

public class CrateControlListener implements Listener { // Crate Control
    
    // A list of crate locations that are in use.
    public static HashMap<Player, Location> inUse = new HashMap<>();

    private static final CrazyCrates plugin = CrazyCrates.getPlugin(CrazyCrates.class);

    //private static final CrazyManager crazyManager = plugin.getCrazyManager();
    
    // This event controls when a player tries to click in a GUI based crate type. This will stop them from taking items out of their inventories.
    @EventHandler
    public void onCrateInventoryClick(InventoryClickEvent e) {
        //for (Crate crate : crazyManager.getCrates()) {
            //if (crate.getCrateType() != CrateType.MENU && crate.isCrateMenu(e.getView())) e.setCancelled(true);
        //}
    }
    
    // This must run as highest, so it doesn't cause other plugins to check
    // the items that were added to the players inventory and replaced the item in the player's hand.
    // This is only an issue with QuickCrate
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCrateOpen(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (e.getHand() == EquipmentSlot.OFF_HAND) {
            //if (crazyManager.isKey(player.getInventory().getItemInOffHand())) {
                e.setCancelled(true);
                player.updateInventory();
            //}

            return;
        }

        Block clickedBlock = e.getClickedBlock();

        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            // Loops through all loaded physical locations.
            /*for (CrateLocation loc : crazyManager.getCrateLocations()) {
                // Checks to see if the clicked block is the same as a physical crate.
                if (loc.getLocation().equals(clickedBlock.getLocation())) {
                    // Checks to see if the player is removing a crate location.
                    if (player.getGameMode() == GameMode.CREATIVE && player.isSneaking() && player.hasPermission("crazycrates.admin")) {
                        e.setCancelled(true);
                        crazyManager.removeCrateLocation(loc.getID());
                        player.sendMessage(Messages.REMOVED_PHYSICAL_CRATE.getMessage("%ID%", loc.getID()));
                        return;
                    }

                    e.setCancelled(true);

                    //if (loc.getCrateType() != CrateType.MENU) {
                        if (loc.getCrate().isPreviewEnabled()) {
                            PreviewListener.setPlayerInMenu(player, false);
                            PreviewListener.openNewPreview(player, loc.getCrate());
                        } else {
                            player.sendMessage(Messages.PREVIEW_DISABLED.getMessage());
                        }
                    //}
                }
            }*/
        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // Checks if the item in their hand is a key and if so it stops them from right-clicking with it.
            ItemStack key = player.getInventory().getItemInMainHand();
            //boolean keyInHand = crazyManager.isKey(key);

            //if (!keyInHand) keyInHand = crazyManager.isKey(player.getEquipment().getItemInOffHand());

            //if (keyInHand) {
                e.setCancelled(true);
                player.updateInventory();
            //}

            //Checks to see if the clicked block is a physical crate.
            //CrateLocation crateLocation = crazyManager.getCrateLocation(clickedBlock.getLocation());

            /*if (crateLocation != null && crateLocation.getCrate() != null) {
                //Crate crate = crateLocation.getCrate();
                e.setCancelled(true);

                //if (crate.getCrateType() == CrateType.MENU) {
                //    boolean openMenu = plugin.getApiManager().getConfigSettings().getProperty(ConfigSettings.ENABLE_CRATE_MENU);

                    //This is to stop players in QuadCrate to not be able to try and open a crate set to menu.
                //    if (!crazyManager.isInOpeningList(player) && openMenu) MenuListener.openGUI(player);

                //    return;
                //}

                PhysicalCrateKeyCheckEvent event = new PhysicalCrateKeyCheckEvent(player, crateLocation);
                player.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    boolean hasKey = false;
                    boolean isPhysical = false;
                    boolean useQuickCrateAgain = false;
                    String keyName = crate.getKey().getItemMeta().getDisplayName();
                    keyName = keyName != null ? keyName : crate.getKey().getType().toString();

                    boolean physAcceptsPhys = plugin.getApiManager().getConfig().getProperty(Config.PHYSICAL_ACCEPTS_PHYSICAL_KEYS);
                    boolean physAcceptsVirtual = plugin.getApiManager().getConfig().getProperty(Config.PHYSICAL_ACCEPTS_VIRTUAL_KEYS);

                    if (crate.getCrateType() != CrateType.crate_on_the_go && keyInHand && crazyManager.isKeyFromCrate(key, crate) && physAcceptsPhys) {
                        hasKey = true;
                        isPhysical = true;
                    }

                    if (physAcceptsVirtual && crazyManager.getVirtualKeys(player, crate) >= 1) hasKey = true;

                    if (hasKey) {
                        // Checks if the player uses the quick crate again.
                        if (crazyManager.isInOpeningList(player) && crazyManager.getOpeningCrate(player).getCrateType() == CrateType.quick_crate && inUse.containsKey(player) && inUse.get(player).equals(crateLocation.getLocation())) {
                            useQuickCrateAgain = true;
                        }

                        if (!useQuickCrateAgain) {
                            if (crazyManager.isInOpeningList(player)) {
                                player.sendMessage(Messages.ALREADY_OPENING_CRATE.getMessage("%Key%", keyName));
                                return;
                            }

                            if (inUse.containsValue(crateLocation.getLocation())) {
                                player.sendMessage(Messages.QUICK_CRATE_IN_USE.getMessage());
                                return;
                            }
                        }

                        if (Methods.isInventoryFull(player)) {
                            player.sendMessage(Messages.INVENTORY_FULL.getMessage());
                            return;
                        }

                        //if (useQuickCrateAgain) QuickCrate.endQuickCrate(player, crateLocation.getLocation(), crate, plugin.getApiManager().getHolograms(), true);

                        KeyType keyType = isPhysical ? KeyType.PHYSICAL_KEY : KeyType.VIRTUAL_KEY;

                        // Only cosmic crate type uses this method.
                        if (crate.getCrateType() == CrateType.cosmic) crazyManager.addPlayerKeyType(player, keyType);

                        crazyManager.addPlayerToOpeningList(player, crate);
                        crazyManager.openCrate(player, crate, keyType, crateLocation.getLocation(), false, true);
                    } else {
                        if (crate.getCrateType() != CrateType.crate_on_the_go) {
                            if (plugin.getApiManager().getConfig().getProperty(Config.CRATE_KNOCK_BACK)) knockBack(player, clickedBlock.getLocation());

                            if (plugin.getApiManager().getConfig().getProperty(Config.NEED_KEY_SOUND_TOGGLE)) {
                                Sound sound = Sound.valueOf(plugin.getApiManager().getConfig().getProperty(Config.NEED_KEY_SOUND));

                                player.playSound(player.getLocation(), sound, 1f, 1f);
                            }

                            player.sendMessage(Messages.NO_KEY.getMessage("%Key%", keyName));
                        }
                    }
                }
            }*/
        }
    }
    
    @EventHandler
    public void onAdminMenuClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        Player player = (Player) e.getWhoClicked();

        if (inv != null && e.getView().getTitle().equals(Methods.sanitizeColor("&4&lAdmin Keys"))) {
            e.setCancelled(true);

            if (!Methods.permCheck(player, Permissions.ADMIN_ACCESS, false)) {
                player.closeInventory();
                return;
            }

            // Added the >= due to an error about a raw slot set at -999.
            if (e.getRawSlot() < inv.getSize() && e.getRawSlot() >= 0) { // Clicked in the admin menu.
                ItemStack item = inv.getItem(e.getRawSlot());
                /*if (crazyManager.isKey(item)) {
                    Crate crate = crazyManager.getCrateFromKey(item);

                    if (e.getAction() == InventoryAction.PICKUP_ALL) {
                        player.getInventory().addItem(crate.getKey());
                    } else if (e.getAction() == InventoryAction.PICKUP_HALF) {
                        crazyManager.addKeys(1, player, crate, KeyType.VIRTUAL_KEY);
                        String name = null;
                        ItemStack key = crate.getKey();

                        if (key.hasItemMeta() && key.getItemMeta().hasDisplayName()) name = key.getItemMeta().getDisplayName();

                        player.sendMessage(Methods.getPrefix() + ColorUtils.color("&a&l+1 " + (name != null ? name : crate.getName())));
                    }
                }*/
            }
        }
    }
    
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        //if (crazyManager.hasCrateTask(player)) crazyManager.endCrate(player);

        //if (crazyManager.hasQuadCrateTask(player)) crazyManager.endQuadCrate(player);

        //if (crazyManager.isInOpeningList(player)) crazyManager.removePlayerFromOpeningList(player);
    }
    
    public static void knockBack(Player player, Location location) {
        Vector vector = player.getLocation().toVector().subtract(location.toVector()).normalize().multiply(1).setY(.1);

        if (player.isInsideVehicle() && player.getVehicle() != null) {
            player.getVehicle().setVelocity(vector);
            return;
        }

        player.setVelocity(vector);
    }
}