package us.crazycrew.crazycrates.paper.cratetypes;

import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.events.PlayerPrizeEvent;
import us.crazycrew.crazycrates.paper.api.frame.BukkitUserManager;
import us.crazycrew.crazycrates.paper.api.interfaces.HologramController;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.objects.Prize;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;
import us.crazycrew.crazycrates.paper.listeners.CrateControlListener;
import us.crazycrew.crazycrates.paper.support.structures.blocks.ChestManager;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class QuickCrate implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesLoader cratesLoader = this.plugin.getCratesLoader();
    private final @NotNull CrazyManager crazyManager = this.cratesLoader.getCrazyManager();
    private final @NotNull BukkitUserManager userManager = this.cratesLoader.getUserManager();
    private final @NotNull ChestManager chestManager = this.cratesLoader.getChestManager();
    private final @NotNull Methods methods = this.cratesLoader.getMethods();

    private final ArrayList<Entity> allRewards = new ArrayList<>();
    private final HashMap<UUID, Entity> rewards = new HashMap<>();

    private final HashMap<UUID, BukkitTask> tasks = new HashMap<>();

    public void openCrate(Player player, final Location loc, Crate crate, KeyType keyType, HologramController hologramController) {
        UUID uuid = player.getUniqueId();

        int keys = switch (keyType) {
            case VIRTUAL_KEY -> this.userManager.getVirtualKeys(uuid, crate.getName());
            case PHYSICAL_KEY -> this.userManager.getPhysicalKeys(uuid, crate.getName());
            default -> 1;
        }; // If the key is free it is set to one.

        if (player.isSneaking() && keys > 1) {
            int keysUsed = 0;

            // give the player the prizes
            for (; keys > 0; keys--) {
                if (methods.isInventoryFull(player)) break;
                if (keysUsed >= crate.getMaxMassOpen()) break;

                Prize prize = crate.pickPrize(player);
                crazyManager.givePrize(player, prize, crate);
                plugin.getServer().getPluginManager().callEvent(new PlayerPrizeEvent(uuid, crate, crate.getName(), prize));

                if (prize.useFireworks()) methods.firework(loc.clone().add(.5, 1, .5));

                keysUsed++;
            }

            if (!this.userManager.takeKeys(keysUsed, player.getUniqueId(), crate.getName(), keyType, false)) {
                CrateControlListener.inUse.remove(uuid);
                crazyManager.removePlayerFromOpeningList(uuid);
                return;
            }

            endQuickCrate(uuid, loc, crate, hologramController, true);
        }
    }
    
    public void endQuickCrate(UUID uuid, Location loc, Crate crate, HologramController hologramController, boolean useQuickCrate) {
        if (tasks.containsKey(uuid)) {
            tasks.get(uuid).cancel();
            tasks.remove(uuid);
        }

        if (rewards.get(uuid) != null) {
            allRewards.remove(rewards.get(uuid));
            rewards.get(uuid).remove();
            rewards.remove(uuid);
        }

        chestManager.closeChest(loc.getBlock(), false);
        CrateControlListener.inUse.remove(uuid);
        crazyManager.removePlayerFromOpeningList(uuid);

        if (!useQuickCrate) {
            if (hologramController != null) hologramController.createHologram(loc.getBlock(), crate);
        }
    }
    
    public void removeAllRewards() {
        allRewards.stream().filter(Objects::nonNull).forEach(Entity :: remove);
    }
    
    @EventHandler
    public void onHopperPickUp(InventoryPickupItemEvent e) {
        if (crazyManager.isDisplayReward(e.getItem())) e.setCancelled(true);
    }
}