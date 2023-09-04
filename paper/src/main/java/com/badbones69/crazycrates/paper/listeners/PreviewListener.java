package com.badbones69.crazycrates.paper.listeners;

import com.badbones69.crazycrates.paper.api.objects.Crate;
import com.badbones69.crazycrates.paper.api.plugin.CrazyCratesPlugin;
import com.badbones69.crazycrates.paper.api.plugin.registry.CrazyCratesProvider;
import com.badbones69.crazycrates.paper.api.managers.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class PreviewListener implements Listener {

    private final @NotNull CrazyCratesPlugin cratesPlugin = CrazyCratesProvider.get();
    private final @NotNull MenuManager menuManager = this.cratesPlugin.getMenuManager();
    
    @EventHandler
    public void onPlayerClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        e.getInventory();
        if (menuManager.getPlayerCrate().get(player.getUniqueId()) != null) {
            Crate crate = menuManager.getPlayerCrate().get(player.getUniqueId());

            if (crate.isPreview(e.getView())) {
                e.setCancelled(true);

                if (e.getCurrentItem() != null) {
                    if (e.getRawSlot() == crate.getAbsoluteItemPosition(4)) { // Clicked the menu button.
                        if (menuManager.playerInMenu(player)) this.cratesPlugin.getMenuManager().openMainMenu(player);
                    } else if (e.getRawSlot() == crate.getAbsoluteItemPosition(5)) { // Clicked the next button.
                        if (menuManager.getPage(player) < crate.getMaxPage()) {
                            nextPage(player);
                            menuManager.openPreview(player, crate);
                        }
                    } else if (e.getRawSlot() == crate.getAbsoluteItemPosition(3)) { // Clicked the back button.
                        if (menuManager.getPage(player) > 1 && menuManager.getPage(player) <= crate.getMaxPage()) {
                            backPage(player);
                            menuManager.openPreview(player, crate);
                        }
                    }
                }
            }
        }
    }

    private void nextPage(Player player) {
        menuManager.setPage(player, menuManager.getPage(player) + 1);
    }

    private void backPage(Player player) {
        menuManager.setPage(player, menuManager.getPage(player) - 1);
    }
}