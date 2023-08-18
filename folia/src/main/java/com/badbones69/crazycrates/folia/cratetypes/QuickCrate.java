package com.badbones69.crazycrates.folia.cratetypes;

import com.badbones69.crazycrates.api.enums.keys.KeyType;
import com.badbones69.crazycrates.folia.CrazyCrates;
import com.badbones69.crazycrates.folia.api.CrazyManager;
import com.badbones69.crazycrates.folia.api.events.PlayerPrizeEvent;
import com.badbones69.crazycrates.folia.api.interfaces.HologramController;
import com.badbones69.crazycrates.folia.api.objects.Crate;
import com.badbones69.crazycrates.folia.api.objects.Prize;
import com.badbones69.crazycrates.folia.listeners.CrateControlListener;
import com.badbones69.crazycrates.folia.support.structures.blocks.ChestStateHandler;
import com.badbones69.crazycrates.folia.utils.Methods;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class QuickCrate implements Listener {
    
    public static ArrayList<Entity> allRewards = new ArrayList<>();
    public static HashMap<Player, Entity> rewards = new HashMap<>();
    private static final com.badbones69.crazycrates.folia.CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private static final CrazyManager crazyManager = plugin.getCrazyManager();
    private static final HashMap<Player, BukkitTask> tasks = new HashMap<>();

    private static final ChestStateHandler chestStateHandler = plugin.getChestHandler();

    public static void openCrate(final Player player, final Location loc, com.badbones69.crazycrates.folia.api.objects.Crate crate, KeyType keyType, com.badbones69.crazycrates.folia.api.interfaces.HologramController hologramController) {
        int keys = switch (keyType) {
            case VIRTUAL_KEY -> crazyManager.getVirtualKeys(player, crate);
            case PHYSICAL_KEY -> crazyManager.getPhysicalKeys(player, crate);
            default -> 1;
        }; // If the key is free it is set to one.
        
        if (player.isSneaking() && keys > 1) {
            int keysUsed = 0;
            
            // give the player the prizes
            for (;keys > 0; keys--) {
                if (com.badbones69.crazycrates.folia.utils.Methods.isInventoryFull(player)) break;
                if (keysUsed >= crate.getMaxMassOpen()) break;

                com.badbones69.crazycrates.folia.api.objects.Prize prize = crate.pickPrize(player);
                crazyManager.givePrize(player, prize, crate);
                plugin.getServer().getPluginManager().callEvent(new com.badbones69.crazycrates.folia.api.events.PlayerPrizeEvent(player, crate, crate.getName(), prize));

                if (prize.useFireworks()) com.badbones69.crazycrates.folia.utils.Methods.firework(loc.clone().add(.5, 1, .5));
                
                keysUsed++;
            }
            
            if (!crazyManager.takeKeys(keysUsed, player, crate, keyType, false)) {
                com.badbones69.crazycrates.folia.utils.Methods.failedToTakeKey(player, crate);
                com.badbones69.crazycrates.folia.listeners.CrateControlListener.inUse.remove(player);
                crazyManager.removePlayerFromOpeningList(player);
                return;
            }

            endQuickCrate(player, loc, crate, hologramController, true);
        } else {
            if (!crazyManager.takeKeys(1, player, crate, keyType, true)) {
                com.badbones69.crazycrates.folia.utils.Methods.failedToTakeKey(player, crate);
                com.badbones69.crazycrates.folia.listeners.CrateControlListener.inUse.remove(player);
                crazyManager.removePlayerFromOpeningList(player);
                return;
            }

            Prize prize = crate.pickPrize(player, loc.clone().add(.5, 1.3, .5));
            crazyManager.givePrize(player, prize, crate);
            plugin.getServer().getPluginManager().callEvent(new PlayerPrizeEvent(player, crate, crate.getName(), prize));
            ItemStack displayItem = prize.getDisplayItem();
            NBTItem nbtItem = new NBTItem(displayItem);
            nbtItem.setBoolean("crazycrates-item", true);
            displayItem = nbtItem.getItem();
            Item reward;

            if (hologramController != null) hologramController.removeHologram(loc.getBlock());

            try {
                reward = player.getWorld().dropItem(loc.clone().add(.5, 1, .5), displayItem);
            } catch (IllegalArgumentException e) {
                plugin.getServer().getLogger().warning("A prize could not be given due to an invalid display item for this prize. ");
                plugin.getServer().getLogger().warning("Crate: " + prize.getCrate() + " Prize: " + prize.getName());

                e.printStackTrace();
                return;
            }

            reward.setMetadata("betterdrops_ignore", new FixedMetadataValue(plugin, true));
            reward.setVelocity(new Vector(0, .2, 0));
            reward.setCustomName(displayItem.getItemMeta().getDisplayName());
            reward.setCustomNameVisible(true);
            reward.setPickupDelay(Integer.MAX_VALUE);
            rewards.put(player, reward);
            allRewards.add(reward);
            chestStateHandler.openChest(loc.getBlock(), true);

            if (prize.useFireworks()) Methods.firework(loc.clone().add(.5, 1, .5));

            tasks.put(player, new BukkitRunnable() {
                @Override
                public void run() {
                    endQuickCrate(player, loc, crate, hologramController, false);
                }
            }.runTaskLater(plugin, 5 * 20));
        }
    }
    
    public static void endQuickCrate(Player player, Location loc, Crate crate, HologramController hologramController, boolean useQuickCrate) {
        if (tasks.containsKey(player)) {
            tasks.get(player).cancel();
            tasks.remove(player);
        }

        if (rewards.get(player) != null) {
            allRewards.remove(rewards.get(player));
            rewards.get(player).remove();
            rewards.remove(player);
        }

        chestStateHandler.closeChest(loc.getBlock(), false);
        CrateControlListener.inUse.remove(player);
        crazyManager.removePlayerFromOpeningList(player);

        if (!useQuickCrate) {
            if (hologramController != null) hologramController.createHologram(loc.getBlock(), crate);
        }
    }
    
    public static void removeAllRewards() {
        allRewards.stream().filter(Objects :: nonNull).forEach(Entity :: remove);
    }
    
    @EventHandler
    public void onHopperPickUp(InventoryPickupItemEvent e) {
        if (crazyManager.isDisplayReward(e.getItem())) e.setCancelled(true);
    }
}