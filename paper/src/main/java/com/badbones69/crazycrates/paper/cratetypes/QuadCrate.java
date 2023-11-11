package com.badbones69.crazycrates.paper.cratetypes;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.managers.QuadCrateManager;
import com.badbones69.crazycrates.paper.api.managers.quadcrates.SessionManager;
import com.badbones69.crazycrates.paper.api.objects.Crate;
import com.badbones69.crazycrates.paper.api.objects.ItemBuilder;
import com.badbones69.crazycrates.paper.api.objects.Prize;
import us.crazycrew.crazycrates.paper.api.enums.Translation;
import us.crazycrew.crazycrates.paper.api.support.structures.blocks.ChestManager;
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
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import java.util.Random;

/**
 * Controller class for the quad-crate crate type.
 * Display items are controlled from the quick crate due to them using nbt tags.
 */
public class QuadCrate implements Listener {

    @NotNull
    private final CrazyCrates plugin = CrazyCrates.get();

    @NotNull
    private final ChestManager chestManager = this.plugin.getCrazyHandler().getChestManager();

    private final SessionManager sessionManager = new SessionManager();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (this.sessionManager.inSession(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onChestClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (!this.sessionManager.inSession(player)) return;

        QuadCrateManager session = sessionManager.getSession(player);

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block block = e.getClickedBlock();

            if (session.getCrateLocations().contains(block.getLocation())) {
                e.setCancelled(true);

                if (session.getCratesOpened().get(block.getLocation())) return;

                this.chestManager.openChest(block, true);

                Crate crate = session.getCrate();
                Prize prize = crate.pickPrize(player, block.getLocation().add(.5, 1.3, .5));
                this.plugin.getCrazyHandler().getPrizeManager().givePrize(player, prize, crate);

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

                session.getDisplayedRewards().add(reward);

                if (session.allCratesOpened()) { // All 4 crates have been opened
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            session.endCrate();
                            player.playSound(player.getLocation(), Sound.BLOCK_STONE_STEP, 1, 1);
                        }
                    }.runTaskLater(this.plugin, 60);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (this.sessionManager.inSession(player)) { // Player tries to walk away from the crate area
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
                if (this.sessionManager.inSession(p)) {
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
        if (this.sessionManager.inSession(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onCommandProcess(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        if (this.sessionManager.inSession(player) && !player.hasPermission("crazycrates.admin")) {
            e.setCancelled(true);
            player.sendMessage(Translation.no_commands_while_in_crate.getMessage("%player%", player.getName()).toString());
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();

        if (this.sessionManager.inSession(player) && e.getCause() == TeleportCause.ENDER_PEARL) {
            e.setCancelled(true);
            player.sendMessage(Translation.no_teleporting.getMessage("%Player%", player.getName()).toString());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        if (this.sessionManager.inSession(player)) this.sessionManager.getSession(player).endCrate();
    }
}