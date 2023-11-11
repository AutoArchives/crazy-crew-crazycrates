package us.crazycrew.crazycrates.paper.modules.events;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.cluster.paper.modules.ModuleHandler;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.common.config.types.Config;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.api.crates.CrateManager;
import com.badbones69.crazycrates.paper.api.objects.Crate;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import us.crazycrew.crazycrates.paper.CrazyHandler;
import us.crazycrew.crazycrates.paper.api.crates.menus.types.CrateMainMenu;
import us.crazycrew.crazycrates.paper.api.enums.Translation;
import us.crazycrew.crazycrates.paper.api.crates.menus.InventoryManager;
import us.crazycrew.crazycrates.paper.utils.MiscUtils;

public class CrateMenuListener extends ModuleHandler {

    @NotNull
    private final CrazyCrates plugin = CrazyCrates.get();

    @NotNull
    private final InventoryManager inventoryManager = this.plugin.getCrazyHandler().getInventoryManager();

    @NotNull
    private final SettingsManager config = this.plugin.getConfigManager().getConfig();

    @NotNull
    private final CrazyHandler crazyHandler = this.plugin.getCrazyHandler();

    @NotNull
    private final CrateManager crateManager = this.plugin.getCrateManager();

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Inventory inventory = event.getInventory();

        if (!(inventory.getHolder(false) instanceof CrateMainMenu)) {
            return;
        }

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        if (!item.hasItemMeta() && !item.getItemMeta().hasDisplayName()) {
            return;
        }

        NBTItem nbtItem = new NBTItem(item);

        if (!nbtItem.hasNBTData() && !nbtItem.hasTag("CrazyCrates-Crate")) {
            return;
        }

        Crate crate = this.plugin.getCrateManager().getCrateFromName(nbtItem.getString("CrazyCrates-Crate"));

        if (crate == null) {
            return;
        }

        if (event.getAction() == InventoryAction.PICKUP_HALF) { // Right-clicked the item
            if (crate.isPreviewEnabled()) {
                player.closeInventory();
                this.inventoryManager.addViewer(player);
                this.inventoryManager.openNewCratePreview(player, crate);
            } else {
                player.sendMessage(Translation.preview_disabled.getString());
            }

            return;
        }

        if (this.crateManager.isInOpeningList(player)) {
            player.sendMessage(Translation.already_opening_crate.getString());
            return;
        }

        boolean hasKey = false;
        KeyType keyType = KeyType.virtual_key;

        if (this.plugin.getCrazyHandler().getUserManager().getVirtualKeys(player.getUniqueId(), crate.getName()) >= 1) {
            hasKey = true;
        } else {
            if (this.config.getProperty(Config.virtual_accepts_physical_keys) && this.crazyHandler.getUserManager().hasPhysicalKey(player.getUniqueId(), crate.getName(), false)) {
                hasKey = true;
                keyType = KeyType.physical_key;
            }
        }

        if (!hasKey) {
            if (this.config.getProperty(Config.need_key_sound_toggle)) {
                player.playSound(player.getLocation(), Sound.valueOf(this.config.getProperty(Config.need_key_sound)), 1f, 1f);
            }

            player.sendMessage(Translation.no_virtual_key.getString());
            return;
        }

        for (String world : this.config.getProperty(Config.disabledWorlds)) {
            if (world.equalsIgnoreCase(player.getWorld().getName())) {
                player.sendMessage(Translation.world_disabled.getMessage("%world%", player.getWorld().getName()).toString());
                return;
            }
        }

        if (MiscUtils.isInventoryFull(player)) {
            player.sendMessage(Translation.inventory_not_empty.getString());
            return;
        }

        this.crateManager.openCrate(player, crate, keyType, player.getLocation(), true, false);
    }

    @Override
    public String getModuleName() {
        return "Crate Menu Listener";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void reload() {}
}