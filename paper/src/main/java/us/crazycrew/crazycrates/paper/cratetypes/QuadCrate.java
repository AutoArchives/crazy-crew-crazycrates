package us.crazycrew.crazycrates.paper.cratetypes;

import org.bukkit.SoundCategory;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.enums.settings.Messages;
import us.crazycrew.crazycrates.paper.api.managers.QuadCrateManager;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.objects.ItemBuilder;
import us.crazycrew.crazycrates.paper.api.objects.Prize;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;
import us.crazycrew.crazycrates.paper.support.structures.blocks.ChestManager;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import java.util.Random;
import java.util.UUID;

/**
 * Controller class for the quad-crate crate type.
 * Display items are controlled from the quick crate due to them using nbt tags.
 */
public class QuadCrate implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesLoader cratesLoader = this.plugin.getCratesLoader();
    private final @NotNull CrazyManager crazyManager = this.cratesLoader.getCrazyManager();
    private final @NotNull ChestManager chestManager = this.cratesLoader.getChestManager();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (inSession(uuid)) e.setCancelled(true);
    }

    @EventHandler
    public void onChestClick(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (inSession(uuid)) {
            QuadCrateManager session = getSession(uuid);

            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                Block block = e.getClickedBlock();

                if (block != null && session != null && session.getCrateLocations().contains(block.getLocation())) {
                    e.setCancelled(true);

                    if (!session.getCratesOpened().get(block.getLocation())) {

                        this.chestManager.openChest(block, true);

                        Crate crate = session.getCrate();
                        Prize prize = crate.pickPrize(player, block.getLocation().add(.5, 1.3, .5));
                        this.crazyManager.givePrize(player, prize, crate);

                        ItemBuilder itemBuilder = ItemBuilder.convertItemStack(prize.getDisplayItem());
                        itemBuilder.addLore(new Random().nextInt(Integer.MAX_VALUE) + ""); // Makes sure items don't merge

                        ItemStack item = itemBuilder.build();
                        NBTItem nbtItem = new NBTItem(item);
                        nbtItem.setBoolean("crazycrates-item", true);
                        item = nbtItem.getItem();
                        Item reward = player.getWorld().dropItem(block.getLocation().add(.5, 1, .5), item);

                        reward.setMetadata("betterdrops_ignore", new FixedMetadataValue(plugin, true));
                        reward.setVelocity(new Vector(0, .2, 0));

                        reward.setCustomName(prize.getDisplayItem().getItemMeta().getDisplayName());
                        reward.setCustomNameVisible(true);
                        reward.setPickupDelay(Integer.MAX_VALUE);

                        session.getCratesOpened().put(block.getLocation(), true);

                        session.addReward(reward);

                        if (session.allCratesOpened()) { // All 4 crates have been opened
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    session.endCrate();
                                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_STEP, 1, 1);
                                }
                            }.runTaskLater(plugin, 60);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (inSession(uuid)) { // Player tries to walk away from the crate area
            Location from = e.getFrom();
            Location to = e.getTo();

            if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
                e.setCancelled(true);
                player.teleport(from);
                return;
            }
        }

        for (Entity en : player.getNearbyEntities(2, 2, 2)) { // Someone tries to enter the crate area
            if (en instanceof Player p) {
                if (inSession(p.getUniqueId())) {
                    Vector v = player.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().setY(1);

                    if (player.isInsideVehicle() && player.getVehicle() != null) {
                        player.getVehicle().setVelocity(v);
                    } else {
                        player.setVelocity(v);
                    }

                    break;
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (inSession(uuid)) e.setCancelled(true);
    }

    @EventHandler
    public void omCommand(PlayerCommandPreprocessEvent e) {
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (inSession(uuid) && !player.hasPermission("crazycrates.admin")) {
            player.sendMessage(Messages.NO_COMMANDS_WHILE_CRATE_OPENED.getMessage("%Player%", player.getName()));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (inSession(uuid) && e.getCause() == TeleportCause.ENDER_PEARL) {
            player.sendMessage(Messages.NO_TELEPORTING.getMessage("%Player%", player.getName()));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();

        QuadCrateManager session = getSession(uuid);

        if (inSession(uuid) && session != null) session.endCrate();
    }

    private boolean inSession(UUID uuid) {
        for (QuadCrateManager quadCrateManager : QuadCrateManager.getCrateSessions()) {
            if (quadCrateManager.getPlayer().getUniqueId() == uuid) return true;
        }

        return false;
    }

    private QuadCrateManager getSession(UUID uuid) {
        for (QuadCrateManager quadCrateManager : QuadCrateManager.getCrateSessions()) {
            if (quadCrateManager.getPlayer().getUniqueId() == uuid) return quadCrateManager;
        }

        return null;
    }
}