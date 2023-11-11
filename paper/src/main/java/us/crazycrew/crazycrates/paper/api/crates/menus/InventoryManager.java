package us.crazycrew.crazycrates.paper.api.crates.menus;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.paper.api.objects.Crate;
import com.badbones69.crazycrates.paper.api.objects.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.common.config.types.Config;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class InventoryManager {

    @NotNull
    private final CrazyCrates plugin = CrazyCrates.get();

    @NotNull
    private final SettingsManager config = this.plugin.getConfigManager().getConfig();

    private ItemStack menuButton;
    private ItemBuilder nextButton;
    private ItemBuilder backButton;

    public void loadButtons() {
        this.menuButton = new ItemBuilder()
                .setMaterial(this.config.getProperty(Config.menu_button_item))
                .setName(this.config.getProperty(Config.menu_button_name))
                .setLore(this.config.getProperty(Config.menu_button_lore))
                .build();

        this.nextButton = new ItemBuilder()
                .setMaterial(this.config.getProperty(Config.next_button_item))
                .setName(this.config.getProperty(Config.next_button_name))
                .setLore(this.config.getProperty(Config.next_button_lore));
        this.backButton = new ItemBuilder()
                .setMaterial(this.config.getProperty(Config.back_button_item))
                .setName(this.config.getProperty(Config.back_button_name))
                .setLore(this.config.getProperty(Config.back_button_lore));
    }

    public ItemStack getMenuButton() {
        return this.menuButton;
    }

    public ItemStack getNextButton(Player player) {
        ItemBuilder button = new ItemBuilder(this.nextButton);

        if (player != null) button.addLorePlaceholder("%Page%", (getPage(player) + 1) + "");

        return button.build();
    }

    public ItemStack getBackButton(Player player) {
        ItemBuilder button = new ItemBuilder(this.backButton);

        if (player != null) button.addLorePlaceholder("%Page%", (getPage(player) - 1) + "");

        return button.build();
    }

    private final HashMap<UUID, Crate> crateViewers = new HashMap<>();

    public void openNewCratePreview(Player player, Crate crate) {
        this.crateViewers.put(player.getUniqueId(), crate);

        setPage(player, 1);
        player.openInventory(crate.getPreview(player));
    }

    public void addCrateViewer(Player player, Crate crate) {
        this.crateViewers.put(player.getUniqueId(), crate);
    }

    public void openCratePreview(Player player, Crate crate) {
        this.crateViewers.put(player.getUniqueId(), crate);

        player.openInventory(crate.getPreview(player));
    }

    public void closeCratePreview(Player player) {
        this.pageViewers.remove(player.getUniqueId());
        this.viewers.remove(player.getUniqueId());
        this.crateViewers.remove(player.getUniqueId());
        player.closeInventory();
    }

    public Crate getCratePreview(Player player) {
        return this.crateViewers.get(player.getUniqueId());
    }

    public void removeCrateViewer(Player player) {
        this.crateViewers.remove(player.getUniqueId());
    }

    public void removePageViewer(Player player) {
        this.pageViewers.remove(player.getUniqueId());
    }

    public boolean inCratePreview(Player player) {
        return this.crateViewers.containsKey(player.getUniqueId());
    }

    private final HashMap<UUID, Integer> pageViewers = new HashMap<>();

    public void nextPage(Player player) {
        setPage(player, getPage(player) + 1);
    }

    public void backPage(Player player) {
        setPage(player, getPage(player) - 1);
    }

    public int getPage(Player player) {
        return this.pageViewers.getOrDefault(player.getUniqueId(), 1);
    }

    public void setPage(Player player, int page) {
        int max = this.crateViewers.get(player.getUniqueId()).getMaxPage();

        if (page < 1) {
            page = 1;
        } else if (page >= max) {
            page = max;
        }

        this.pageViewers.put(player.getUniqueId(), page);
    }

    private final List<UUID> viewers = new ArrayList<>();

    public void addViewer(Player player) {
        this.viewers.add(player.getUniqueId());
    }

    public void removeViewer(Player player) {
        this.viewers.remove(player.getUniqueId());
    }

    public void purge() {
        this.viewers.clear();
    }

    public List<UUID> getViewers() {
        return Collections.unmodifiableList(this.viewers);
    }
}