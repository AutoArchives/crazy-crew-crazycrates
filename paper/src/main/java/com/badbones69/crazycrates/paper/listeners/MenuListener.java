package com.badbones69.crazycrates.paper.listeners;

import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.Methods;
import com.badbones69.crazycrates.core.config.types.Config;
import com.badbones69.crazycrates.paper.api.objects.builder.ItemBuilder;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class MenuListener implements Listener {

    private static final CrazyCrates plugin = CrazyCrates.getPlugin(CrazyCrates.class);

    //private static final CrazyManager crazyManager = plugin.getCrazyManager();
    
    public static void openGUI(Player player) {
        int size = plugin.getApiManager().getConfig().getProperty(Config.INVENTORY_SIZE);
        Inventory inv = player.getServer().createInventory(null, size, Methods.sanitizeColor(plugin.getApiManager().getConfig().getProperty(Config.INVENTORY_NAME)));

        if (plugin.getApiManager().getConfig().getProperty(Config.FILLER_TOGGLE)) {
            String id = plugin.getApiManager().getConfig().getProperty(Config.FILLER_ITEM);
            String name = plugin.getApiManager().getConfig().getProperty(Config.FILLER_NAME);
            List<String> lore = plugin.getApiManager().getConfig().getProperty(Config.FILLER_LORE);

            ItemStack item = new ItemBuilder().setMaterial(id).setName(name).setLore(lore).build();

            for (int i = 0; i < size; i++) {
                inv.setItem(i, item.clone());
            }
        }

        if (plugin.getApiManager().getConfig().getProperty(Config.CUSTOMIZER_TOGGLE)) {
            for (String custom : plugin.getApiManager().getConfig().getProperty(Config.CUSTOMIZER)) {
                int slot = 0;
                ItemBuilder item = new ItemBuilder();
                String[] split = custom.split(", ");

                for (String option : split) {

                    if (option.contains("Item:")) item.setMaterial(option.replace("Item:", ""));

                    if (option.contains("Name:")) {
                        option = option.replace("Name:", "");

                        option = getCrates(player, option);

                        item.setName(option.replaceAll("%player%", player.getName()));
                    }

                    if (option.contains("Lore:")) {
                        option = option.replace("Lore:", "");
                        String[] d = option.split(",");

                        for (String l : d) {
                            option = getCrates(player, option);

                            item.addLore(option.replaceAll("%player%", player.getName()));
                        }
                    }

                    if (option.contains("Glowing:")) item.setGlow(Boolean.parseBoolean(option.replace("Glowing:", "")));

                    if (option.contains("Player:")) item.setPlayerName(option.replaceAll("%player%", player.getName()));

                    if (option.contains("Slot:")) slot = Integer.parseInt(option.replace("Slot:", ""));

                    if (option.contains("Unbreakable-Item")) item.setUnbreakable(Boolean.parseBoolean(option.replace("Unbreakable-Item:", "")));

                    if (option.contains("Hide-Item-Flags")) item.hideItemFlags(Boolean.parseBoolean(option.replace("Hide-Item-Flags:", "")));
                }

                if (slot > size) continue;

                slot--;
                inv.setItem(slot, item.build());
            }
        }

        /*for (Crate crate : crazyManager.getCrates()) {
            FileConfiguration file = crate.getFile();

            if (file != null) {
                if (file.getBoolean("Crate.InGUI")) {
                    String path = "Crate.";
                    int slot = file.getInt(path + "Slot");

                    if (slot > size) continue;

                    slot--;
                    inv.setItem(slot, new ItemBuilder()
                    .setMaterial(file.getString(path + "Item"))
                    .setName(file.getString(path + "Name"))
                    .setLore(file.getStringList(path + "Lore"))
                    .setCrateName(crate.getName())
                    .setPlayerName(file.getString(path + "Player"))
                    .setGlow(file.getBoolean(path + "Glowing"))
                    .addLorePlaceholder("%Keys%", NumberFormat.getNumberInstance().format(crazyManager.getVirtualKeys(player, crate)))
                    .addLorePlaceholder("%Keys_Physical%", NumberFormat.getNumberInstance().format(crazyManager.getPhysicalKeys(player, crate)))
                    .addLorePlaceholder("%Keys_Total%", NumberFormat.getNumberInstance().format(crazyManager.getTotalKeys(player, crate)))
                    .addLorePlaceholder("%Player%", player.getName())
                    .build());
                }
            }
        }*/

        player.openInventory(inv);
    }

    private static String getCrates(Player player, String option) {
        //for (Crate crate : crazyManager.getCrates()) {
            //if (crate.getCrateType() != CrateType.MENU) {
            //    option = option.replaceAll("%" + crate.getName().toLowerCase() + "%", crazyManager.getVirtualKeys(player, crate) + "")
            //    .replaceAll("%" + crate.getName().toLowerCase() + "_physical%", crazyManager.getPhysicalKeys(player, crate) + "")
            //    .replaceAll("%" + crate.getName().toLowerCase() + "_total%", crazyManager.getTotalKeys(player, crate) + "");
            //}
        //}

        return option;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();

        if (inv != null) {
            //for (Crate crate : crazyManager.getCrates()) {
                //if (crate.getCrateType() != CrateType.MENU && crate.isCrateMenu(e.getView())) return;
            //}

            if (e.getView().getTitle().equals(Methods.sanitizeColor(plugin.getApiManager().getConfig().getProperty(Config.INVENTORY_NAME)))) {
                e.setCancelled(true);

                if (e.getCurrentItem() != null) {
                    ItemStack item = e.getCurrentItem();

                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                        NBTItem nbtItem = new NBTItem(item);

                        /*if (nbtItem.hasNBTData() && nbtItem.hasKey("CrazyCrates-Crate")) {
                            Crate crate = crazyManager.getCrateFromName(nbtItem.getString("CrazyCrates-Crate"));

                            if (crate != null) {
                                if (e.getAction() == InventoryAction.PICKUP_HALF) { // Right-clicked the item

                                    if (crate.isPreviewEnabled()) {
                                        player.closeInventory();
                                        PreviewListener.setPlayerInMenu(player, true);
                                        PreviewListener.openNewPreview(player, crate);
                                    } else {
                                        player.sendMessage(Messages.PREVIEW_DISABLED.getMessage());
                                    }

                                    return;
                                }

                                if (crazyManager.isInOpeningList(player)) {
                                    player.sendMessage(Messages.CRATE_ALREADY_OPENED.getMessage());
                                    return;
                                }

                                boolean hasKey = false;
                                KeyType keyType = KeyType.VIRTUAL_KEY;

                                if (crazyManager.getVirtualKeys(player, crate) >= 1) {
                                    hasKey = true;
                                } else {
                                    if (plugin.getApiManager().getConfig().getProperty(Config.VIRTUAL_ACCEPTS_PHYSICAL_KEYS) && crazyManager.hasPhysicalKey(player, crate, false)) {
                                        hasKey = true;
                                        keyType = KeyType.PHYSICAL_KEY;
                                    }
                                }

                                if (!hasKey) {
                                    if (plugin.getApiManager().getConfig().getProperty(Config.NEED_KEY_SOUND_TOGGLE)) {
                                        Sound sound = Sound.valueOf(plugin.getApiManager().getConfig().getProperty(Config.NEED_KEY_SOUND));

                                        player.playSound(player.getLocation(), sound, 1f, 1f);
                                    }

                                    player.sendMessage(Messages.NO_VIRTUAL_KEY.getMessage());
                                    return;
                                }

                                for (String world : plugin.getApiManager().getConfig().getProperty(Config.DISABLED_WORLDS)) {
                                    if (world.equalsIgnoreCase(player.getWorld().getName())) {
                                        player.sendMessage(Messages.WORLD_DISABLED.getMessage("%World%", player.getWorld().getName()));
                                        return;
                                    }
                                }

                                if (Methods.isInventoryFull(player)) {
                                    player.sendMessage(Messages.INVENTORY_FULL.getMessage());
                                    return;
                                }

                                crazyManager.openCrate(player, crate, keyType, player.getLocation(), true, false);
                            }
                        }*/
                    }
                }
            }
        }
    }
}