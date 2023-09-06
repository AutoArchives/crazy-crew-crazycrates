package us.crazycrew.crazycrates.paper.cratetypes;

import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.events.PlayerPrizeEvent;
import us.crazycrew.crazycrates.paper.api.interfaces.HologramController;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.objects.Prize;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesPlugin;
import us.crazycrew.crazycrates.paper.api.plugin.registry.CrazyCratesProvider;
import us.crazycrew.crazycrates.paper.listeners.CrateControlListener;
import us.crazycrew.crazycrates.paper.support.structures.blocks.ChestStateHandler;
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

import java.util.*;

public class QuickCrate implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesPlugin cratesPlugin = CrazyCratesProvider.get();
    private final @NotNull CrazyManager crazyManager = this.cratesPlugin.getCrazyManager();
    private final @NotNull ChestStateHandler chestStateHandler = this.cratesPlugin.getChestManager();
    private final @NotNull Methods methods = this.cratesPlugin.getMethods();

    private final ArrayList<Entity> allRewards = new ArrayList<>();
    private final HashMap<UUID, Entity> rewards = new HashMap<>();

    private final HashMap<UUID, BukkitTask> tasks = new HashMap<>();

    public void openCrate(final UUID uuid, final Location loc, Crate crate, KeyType keyType, HologramController hologramController) {
        int keys = switch (keyType) {
            case VIRTUAL_KEY -> crazyManager.getVirtualKeys(uuid, crate);
            case PHYSICAL_KEY -> crazyManager.getPhysicalKeys(uuid, crate);
            default -> 1;
        }; // If the key is free it is set to one.

        Player player = this.plugin.getServer().getPlayer(uuid);

        if (player != null && player.isSneaking() && keys > 1) {
            int keysUsed = 0;

            // give the player the prizes
            for (; keys > 0; keys--) {
                if (methods.isInventoryFull(uuid)) break;
                if (keysUsed >= crate.getMaxMassOpen()) break;

                Prize prize = crate.pickPrize(uuid);
                crazyManager.givePrize(uuid, prize, crate);
                plugin.getServer().getPluginManager().callEvent(new PlayerPrizeEvent(uuid, crate, crate.getName(), prize));

                if (prize.useFireworks()) methods.firework(loc.clone().add(.5, 1, .5));

                keysUsed++;
            }

            if (!crazyManager.takeKeys(keysUsed, uuid, crate, keyType, false)) {
                methods.failedToTakeKey(player.getName(), crate);
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

        chestStateHandler.closeChest(loc.getBlock(), false);
        CrateControlListener.inUse.remove(uuid);
        crazyManager.removePlayerFromOpeningList(uuid);

        if (!useQuickCrate) {
            if (hologramController != null) hologramController.createHologram(loc.getBlock(), crate);
        }
    }
    
    public void removeAllRewards() {
        allRewards.stream().filter(Objects :: nonNull).forEach(Entity :: remove);
    }
    
    @EventHandler
    public void onHopperPickUp(InventoryPickupItemEvent e) {
        if (crazyManager.isDisplayReward(e.getItem())) e.setCancelled(true);
    }
}