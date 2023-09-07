package us.crazycrew.crazycrates.paper.listeners;

import org.bukkit.plugin.java.JavaPlugin;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.FileManager;
import us.crazycrew.crazycrates.paper.api.enums.settings.Messages;
import us.crazycrew.crazycrates.paper.api.frame.BukkitUserManager;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
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

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        UUID uuid = player.getUniqueId();

        Inventory inv = e.getClickedInventory();
        FileConfiguration config = FileManager.Files.CONFIG.getFile();

        if (inv == null) return;

        for (Crate crate : crazyManager.getCrates()) {
            if (crate.getCrateType() != CrateType.MENU && crate.isCrateMenu(e.getView())) return;
        }

        if (e.getView().getTitle().equals(methods.sanitizeColor(config.getString("Settings.InventoryName")))) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                ItemStack item = e.getCurrentItem();

                if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                    NBTItem nbtItem = new NBTItem(item);

                    if (nbtItem.hasNBTData() && nbtItem.hasKey("CrazyCrates-Crate")) {
                        Crate crate = crazyManager.getCrateFromName(nbtItem.getString("CrazyCrates-Crate"));

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

                            if (crazyManager.isInOpeningList(uuid)) {
                                player.sendMessage(Messages.CRATE_ALREADY_OPENED.getMessage());
                                return;
                            }

                            boolean hasKey = false;
                            KeyType keyType = KeyType.VIRTUAL_KEY;

                            if (this.userManager.getVirtualKeys(uuid, crate.getName()) >= 1) {
                                hasKey = true;
                            } else {
                                if (FileManager.Files.CONFIG.getFile().getBoolean("Settings.Virtual-Accepts-Physical-Keys") && this.userManager.hasPhysicalKey(uuid, crate.getName(), false)) {
                                    hasKey = true;
                                    keyType = KeyType.PHYSICAL_KEY;
                                }
                            }

                            if (!hasKey) {
                                if (config.contains("Settings.Need-Key-Sound")) {
                                    Sound sound = Sound.valueOf(config.getString("Settings.Need-Key-Sound"));

                                    if (sound != null) player.playSound(player.getLocation(), sound, 1f, 1f);
                                }

                                player.sendMessage(Messages.NO_VIRTUAL_KEY.getMessage());
                                return;
                            }

                            for (String world : getDisabledWorlds()) {
                                if (world.equalsIgnoreCase(player.getWorld().getName())) {
                                    player.sendMessage(Messages.WORLD_DISABLED.getMessage("%World%", player.getWorld().getName()));
                                    return;
                                }
                            }

                            if (methods.isInventoryFull(player)) {
                                player.sendMessage(Messages.INVENTORY_FULL.getMessage());
                                return;
                            }

                            crazyManager.openCrate(player, crate, keyType, player.getLocation(), true, false);
                        }
                    }
                }
            }
        }
    }
    
    private ArrayList<String> getDisabledWorlds() {
        return new ArrayList<>(FileManager.Files.CONFIG.getFile().getStringList("Settings.DisabledWorlds"));
    }
}