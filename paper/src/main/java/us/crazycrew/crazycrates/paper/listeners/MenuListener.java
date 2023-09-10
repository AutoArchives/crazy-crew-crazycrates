package us.crazycrew.crazycrates.paper.listeners;

import ch.jalu.configme.SettingsManager;
import org.bukkit.SoundCategory;
import org.bukkit.plugin.java.JavaPlugin;
import us.crazycrew.crazycrates.common.config.MainConfig;
import us.crazycrew.crazycrates.common.config.menus.CrateMainMenu;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.enums.settings.Messages;
import us.crazycrew.crazycrates.paper.api.plugin.frame.BukkitUserManager;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;
import java.util.ArrayList;
import java.util.UUID;

public class MenuListener implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesLoader cratesLoader = this.plugin.getCratesLoader();
    private final @NotNull CrazyManager crazyManager = this.cratesLoader.getCrazyManager();
    private final @NotNull BukkitUserManager userManager = this.cratesLoader.getUserManager();
    private final @NotNull Methods methods = this.cratesLoader.getMethods();
    private final @NotNull SettingsManager config = this.cratesLoader.getConfigManager().getConfig();
    private final @NotNull SettingsManager menuConfig = this.cratesLoader.getConfigManager().getMainMenuConfig();

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        UUID uuid = player.getUniqueId();

        Inventory inv = e.getClickedInventory();

        if (inv == null) return;

        for (Crate crate : this.crazyManager.getCrates()) {
            if (crate.getCrateType() != CrateType.MENU && crate.isCrateMenu(e.getView())) return;
        }

        //TODO() Re-do how this works.
        if (e.getView().getTitle().equals(this.menuConfig.getProperty(CrateMainMenu.crate_menu_title))) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                ItemStack item = e.getCurrentItem();

                if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                    NBTItem nbtItem = new NBTItem(item);

                    if (nbtItem.hasNBTData() && nbtItem.hasKey("CrazyCrates-Crate")) {
                        Crate crate = this.crazyManager.getCrateFromName(nbtItem.getString("CrazyCrates-Crate"));

                        if (crate != null) {
                            if (e.getAction() == InventoryAction.PICKUP_HALF) { // Right-clicked the item
                                if (crate.isPreviewEnabled()) {
                                    player.closeInventory();

                                    this.cratesLoader.getMenuManager().setPlayerInMenu(player, true);
                                    this.cratesLoader.getMenuManager().openNewPreview(player, crate);
                                } else {
                                    player.sendMessage(Messages.PREVIEW_DISABLED.getMessage());
                                }

                                return;
                            }

                            if (this.crazyManager.isInOpeningList(uuid)) {
                                player.sendMessage(Messages.CRATE_ALREADY_OPENED.getMessage());
                                return;
                            }

                            boolean hasKey = false;
                            KeyType keyType = KeyType.VIRTUAL_KEY;

                            if (this.userManager.getVirtualKeys(uuid, crate.getName()) >= 1) {
                                hasKey = true;
                            } else {
                                if (this.config.getProperty(Config.virtual_accepts_physical_keys) && this.userManager.hasPhysicalKey(uuid, crate.getName(), false)) {
                                    hasKey = true;
                                    keyType = KeyType.PHYSICAL_KEY;
                                }
                            }

                            if (!hasKey) {
                                if (this.config.getProperty(Config.key_sound_toggle)) {
                                    Sound sound = Sound.valueOf(this.config.getProperty(Config.key_sound_name));

                                    //TODO() make volume/pitch configurable and sound type configurable.
                                    //TODO() Adopt the new sound system including custom sounds.
                                    player.playSound(player.getLocation(), sound, SoundCategory.PLAYERS, 1f, 1f);
                                }

                                player.sendMessage(Messages.NO_VIRTUAL_KEY.getMessage());
                                return;
                            }

                            if (this.config.getProperty(Config.disabled_worlds_toggle)) {
                                for (String world : getDisabledWorlds()) {
                                    if (world.equalsIgnoreCase(player.getWorld().getName())) {
                                        player.sendMessage(Messages.WORLD_DISABLED.getMessage("%World%", player.getWorld().getName()));
                                        return;
                                    }
                                }
                            }

                            if (this.methods.isInventoryFull(player)) {
                                player.sendMessage(Messages.INVENTORY_FULL.getMessage());
                                return;
                            }

                            this.crazyManager.openCrate(player, crate, keyType, player.getLocation(), true, false);
                        }
                    }
                }
            }
        }
    }
    
    private ArrayList<String> getDisabledWorlds() {
        return new ArrayList<>(this.config.getProperty(Config.disabled_worlds));
    }
}