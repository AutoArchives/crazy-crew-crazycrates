package us.crazycrew.crazycrates.paper.listeners;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.cluster.bukkit.utils.LegacyUtils;
import org.bukkit.SoundCategory;
import us.crazycrew.crazycrates.common.config.ConfigManager;
import us.crazycrew.crazycrates.common.config.types.Config;
import us.crazycrew.crazycrates.common.config.types.menus.CrateMainMenu;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.common.enums.Permissions;
import us.crazycrew.crazycrates.paper.api.enums.Translation;
import us.crazycrew.crazycrates.paper.api.events.PhysicalCrateKeyCheckEvent;
import us.crazycrew.crazycrates.paper.api.plugin.frame.BukkitUserManager;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import us.crazycrew.crazycrates.paper.api.objects.CrateLocation;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyHandler;
import java.util.HashMap;
import java.util.UUID;

public class CrateControlListener implements Listener { // Crate Control
    
    // A list of crate locations that are in use.
    public static HashMap<UUID, Location> inUse = new HashMap<>();

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyHandler crazyHandler = this.plugin.getCrazyHandler();
    private final @NotNull BukkitUserManager userManager = this.crazyHandler.getUserManager();
    private final @NotNull CrazyManager crazyManager = this.crazyHandler.getCrazyManager();
    private final @NotNull Methods methods = this.crazyHandler.getMethods();

    private final @NotNull ConfigManager configManager = this.crazyHandler.getConfigManager();
    private final @NotNull SettingsManager config = this.configManager.getConfig();
    private final @NotNull SettingsManager mainMenuConfig = this.configManager.getMainMenuConfig();
    
    // This event controls when a player tries to click in a GUI based crate type. This will stop them from taking items out of their inventories.
    @EventHandler
    public void onCrateInventoryClick(InventoryClickEvent event) {
        for (Crate crate : this.crazyManager.getCrates()) {
            if (crate.getCrateType() != CrateType.MENU && crate.isCrateMenu(event.getView())) event.setCancelled(true);
        }
    }
    
    // This must run as highest, so it doesn't cause other plugins to check
    // the items that were added to the players inventory and replaced the item in the player's hand.
    // This is only an issue with QuickCrate
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCrateOpen(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (e.getHand() == EquipmentSlot.OFF_HAND) {
            if (this.crazyManager.isKey(player.getInventory().getItemInOffHand())) {
                e.setCancelled(true);
                player.updateInventory();
            }

            return;
        }

        Block clickedBlock = e.getClickedBlock();

        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            // Loops through all loaded physical locations.
            for (CrateLocation loc : this.crazyManager.getCrateLocations()) {
                // Checks to see if the clicked block is the same as a physical crate.
                if (loc.getLocation().equals(clickedBlock.getLocation())) {
                    // Checks to see if the player is removing a crate location.
                    if (player.getGameMode() == GameMode.CREATIVE && player.isSneaking() && player.hasPermission("crazycrates.admin")) {
                        e.setCancelled(true);
                        this.crazyManager.removeCrateLocation(loc.getID());
                        player.sendMessage(Messages.REMOVED_PHYSICAL_CRATE.getMessage("%ID%", loc.getID()));
                        return;
                    }

                    e.setCancelled(true);

                    if (loc.getCrateType() != CrateType.MENU) {
                        if (loc.getCrate().isPreviewEnabled()) {
                             this.crazyHandler.getMenuManager().setPlayerInMenu(player, false);
                             this.crazyHandler.getMenuManager().openNewPreview(player, loc.getCrate());
                        } else {
                            player.sendMessage(Messages.PREVIEW_DISABLED.getMessage());
                        }
                    }
                }
            }
        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // Checks if the item in their hand is a key and if so it stops them from right-clicking with it.
            ItemStack key = player.getInventory().getItemInMainHand();
            boolean keyInHand = this.crazyManager.isKey(key);

            if (!keyInHand) keyInHand = this.crazyManager.isKey(player.getEquipment().getItemInOffHand());

            if (keyInHand) {
                e.setCancelled(true);
                player.updateInventory();
            }

            //Checks to see if the clicked block is a physical crate.
            CrateLocation crateLocation = this.crazyManager.getCrateLocation(clickedBlock.getLocation());

            if (crateLocation != null && crateLocation.getCrate() != null) {
                Crate crate = crateLocation.getCrate();
                e.setCancelled(true);

                if (crate.getCrateType() == CrateType.MENU) {
                    boolean openMenu = this.mainMenuConfig.getProperty(CrateMainMenu.crate_menu_toggle);

                    //This is to stop players in QuadCrate to not be able to try and open a crate set to menu.
                    if (!this.crazyManager.isInOpeningList(uuid) && openMenu) this.crazyHandler.getMenuManager().openMainMenu(player);

                    return;
                }

                PhysicalCrateKeyCheckEvent event = new PhysicalCrateKeyCheckEvent(uuid, crateLocation);
                this.plugin.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    boolean hasKey = false;
                    boolean isPhysical = false;
                    boolean useQuickCrateAgain = false;
                    String keyName = crate.getKey().getItemMeta().getDisplayName();
                    keyName = keyName != null ? keyName : crate.getKey().getType().toString();

                    int requiredKeys = this.crazyManager.getCrateFromName(crate.getName()).getRequiredKeys();

                    int totalKeys = this.userManager.getTotalKeys(uuid, crate.getName());

                    if (requiredKeys > 0 && totalKeys < requiredKeys) {
                        player.sendMessage(Messages.REQUIRED_KEYS.getMessage().replaceAll("\\{key-amount}", String.valueOf(requiredKeys)).replaceAll("\\{crate}", crate.getPreviewName()).replaceAll("\\{amount}", String.valueOf(totalKeys)));
                        return;
                    }

                    if (crate.getCrateType() != CrateType.CRATE_ON_THE_GO && keyInHand && this.crazyManager.isKeyFromCrate(key, crate) && this.config.getProperty(Config.physical_accepts_physical)) {
                        hasKey = true;
                        isPhysical = true;
                    }

                    if (this.config.getProperty(Config.physical_accepts_virtual) && this.userManager.getVirtualKeys(uuid, crate.getName()) >= 1) hasKey = true;

                    if (hasKey) {
                        // Checks if the player uses the quick crate again.
                        if (this.crazyManager.isInOpeningList(uuid) && this.crazyManager.getOpeningCrate(uuid).getCrateType() == CrateType.QUICK_CRATE && inUse.containsKey(uuid) && inUse.get(uuid).equals(crateLocation.getLocation())) {
                            useQuickCrateAgain = true;
                        }

                        if (!useQuickCrateAgain) {
                            if (this.crazyManager.isInOpeningList(uuid)) {
                                player.sendMessage(Messages.ALREADY_OPENING_CRATE.getMessage("%Key%", keyName));
                                return;
                            }

                            if (inUse.containsValue(crateLocation.getLocation())) {
                                player.sendMessage(Messages.QUICK_CRATE_IN_USE.getMessage());
                                return;
                            }
                        }

                        if (this.methods.isInventoryFull(player)) {
                            player.sendMessage(Messages.INVENTORY_FULL.getMessage());
                            return;
                        }

                        if (useQuickCrateAgain) this.plugin.getQuickCrate().endQuickCrate(uuid, crateLocation.getLocation(), crate, this.crazyManager.getHologramController(), true);

                        KeyType keyType = isPhysical ? KeyType.PHYSICAL_KEY : KeyType.VIRTUAL_KEY;

                        // Only cosmic crate type uses this method.
                        if (crate.getCrateType() == CrateType.COSMIC) this.crazyManager.addPlayerKeyType(uuid, keyType);

                        this.crazyManager.addPlayerToOpeningList(uuid, crate);
                        this.crazyManager.openCrate(player, crate, keyType, crateLocation.getLocation(), false, true);
                    } else {
                        if (crate.getCrateType() != CrateType.CRATE_ON_THE_GO) {
                            if (this.config.getProperty(Config.crate_knock_back)) knockBack(player, clickedBlock.getLocation());

                            if (this.config.getProperty(Config.key_sound_toggle)) {
                                Sound sound = Sound.valueOf(this.config.getProperty(Config.key_sound_name));

                                //TODO() make volume/pitch configurable and sound type configurable.
                                //TODO() Adopt the new sound system including custom sounds.
                                player.playSound(player.getLocation(), sound, SoundCategory.PLAYERS, 1f, 1f);
                            }

                            player.sendMessage(Messages.NO_KEY.getMessage("%Key%", keyName));
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onAdminMenuClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        Player player = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equals(this.methods.sanitizeColor("&4&lAdmin Keys"))) {
            e.setCancelled(true);

            if (!this.methods.permCheck(player, Permissions.crazy_crates_admin_access, false)) {
                player.closeInventory();
                return;
            }

            // Added the >= due to an error about a raw slot set at -999.
            if (e.getRawSlot() < inv.getSize() && e.getRawSlot() >= 0) { // Clicked in the admin menu.
                ItemStack item = inv.getItem(e.getRawSlot());
                if (this.crazyManager.isKey(item)) {
                    Crate crate = this.crazyManager.getCrateFromKey(item);

                    if (e.getAction() == InventoryAction.PICKUP_ALL) {
                        player.getInventory().addItem(crate.getKey());
                    } else if (e.getAction() == InventoryAction.PICKUP_HALF) {
                        this.userManager.addVirtualKeys(1, player.getUniqueId(), crate.getName());

                        String name = null;
                        ItemStack key = crate.getKey();

                        if (key.hasItemMeta() && key.getItemMeta().hasDisplayName()) name = key.getItemMeta().getDisplayName();

                        player.sendMessage(this.methods.getPrefix() + LegacyUtils.color("&a&l+1 " + (name != null ? name : crate.getName())));
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (this.crazyManager.hasCrateTask(uuid)) this.crazyManager.endCrate(uuid);

        if (this.crazyManager.hasQuadCrateTask(uuid)) this.crazyManager.endQuadCrate(uuid);

        if (this.crazyManager.isInOpeningList(uuid)) this.crazyManager.removePlayerFromOpeningList(uuid);
    }
    
    private void knockBack(Player player, Location location) {
        Vector vector = player.getLocation().toVector().subtract(location.toVector()).normalize().multiply(1).setY(.1);

        if (player.isInsideVehicle() && player.getVehicle() != null) {
            player.getVehicle().setVelocity(vector);
            return;
        }

        player.setVelocity(vector);
    }
}