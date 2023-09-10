package us.crazycrew.crazycrates.paper.listeners;

import org.bukkit.plugin.java.JavaPlugin;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.managers.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;
import java.util.UUID;

public class PreviewListener implements Listener {


    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesLoader cratesLoader = this.plugin.getCratesLoader();
    private final @NotNull MenuManager menuManager = this.cratesLoader.getMenuManager();
    
    @EventHandler
    public void onPlayerClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        UUID uuid = player.getUniqueId();

        if (e.getClickedInventory() == null) return;

        if (this.menuManager.getPlayerCrate().get(uuid) != null) {
            Crate crate = this.menuManager.getPlayerCrate().get(player.getUniqueId());

            if (crate.isPreview(e.getView())) {
                e.setCancelled(true);

                if (e.getCurrentItem() != null) {
                    if (e.getRawSlot() == crate.getAbsoluteItemPosition(4)) { // Clicked the menu button.
                        if (this.menuManager.playerInMenu(player)) this.cratesLoader.getMenuManager().openMainMenu(player);
                    } else if (e.getRawSlot() == crate.getAbsoluteItemPosition(5)) { // Clicked the next button.
                        if (this.menuManager.getPage(uuid) < crate.getMaxPage()) {
                            nextPage(uuid);
                            this.menuManager.openPreview(player, crate);
                        }
                    } else if (e.getRawSlot() == crate.getAbsoluteItemPosition(3)) { // Clicked the back button.
                        if (this.menuManager.getPage(uuid) > 1 && this.menuManager.getPage(uuid) <= crate.getMaxPage()) {
                            backPage(uuid);
                            this.menuManager.openPreview(player, crate);
                        }
                    }
                }
            }
        }
    }

    private void nextPage(UUID uuid) {
        this.menuManager.setPage(uuid, this.menuManager.getPage(uuid) + 1);
    }

    private void backPage(UUID uuid) {
        this.menuManager.setPage(uuid, this.menuManager.getPage(UUID.randomUUID()) - 1);
    }
}