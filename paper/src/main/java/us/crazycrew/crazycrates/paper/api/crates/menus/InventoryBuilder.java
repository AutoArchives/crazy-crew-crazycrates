package us.crazycrew.crazycrates.paper.api.crates.menus;

import com.badbones69.crazycrates.paper.api.objects.Crate;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.utils.MsgUtils;

@SuppressWarnings("ALL")
public abstract class InventoryBuilder implements InventoryHolder {

    @NotNull
    public final CrazyCrates plugin = CrazyCrates.get();

    private final Inventory inventory;
    private final Player player;
    private String title;
    private Crate crate;
    private int size;
    private int page;

    public InventoryBuilder(Player player, int size, String title) {
        this.title = title;
        this.player = player;
        this.size = size;

        this.inventory = this.plugin.getServer().createInventory(this, this.size, MsgUtils.color(this.title));
    }

    public InventoryBuilder(Crate crate, Player player, int size, String title) {
        this.title = title;
        this.player = player;
        this.size = size;

        this.crate = crate;

        this.inventory = this.plugin.getServer().createInventory(this, this.size, MsgUtils.color(this.title));
    }

    public InventoryBuilder(Crate crate, Player player, int size, int page, String title) {
        this.title = title;
        this.player = player;
        this.size = size;
        this.page = page;

        this.crate = crate;

        this.inventory = this.plugin.getServer().createInventory(this, this.size, MsgUtils.color(this.title));
    }

    public abstract InventoryBuilder build();

    public void size(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return this.page;
    }

    public Crate getCrate() {
        return this.crate;
    }

    public void title(String title) {
        this.title = title;
    }

    public boolean contains(String message) {
        return this.title.contains(message);
    }

    public Player getPlayer() {
        return this.player;
    }

    public void update() {
        this.player.closeInventory(InventoryCloseEvent.Reason.OPEN_NEW);
        this.player.openInventory(getInventory());
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return this.inventory;
    }
}