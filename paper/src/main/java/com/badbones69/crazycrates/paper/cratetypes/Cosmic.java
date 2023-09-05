package com.badbones69.crazycrates.paper.cratetypes;

import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.Methods;
import com.badbones69.crazycrates.paper.api.CrazyManager;
import com.badbones69.crazycrates.paper.api.enums.settings.Messages;
import com.badbones69.crazycrates.paper.api.events.PlayerPrizeEvent;
import com.badbones69.crazycrates.paper.api.events.PlayerReceiveKeyEvent;
import com.badbones69.crazycrates.paper.api.managers.CosmicCrateManager;
import com.badbones69.crazycrates.paper.api.objects.Crate;
import com.badbones69.crazycrates.paper.api.objects.Prize;
import com.badbones69.crazycrates.paper.api.objects.Tier;
import com.badbones69.crazycrates.api.enums.types.KeyType;
import com.badbones69.crazycrates.paper.api.plugin.CrazyCratesPlugin;
import com.badbones69.crazycrates.paper.api.plugin.registry.CrazyCratesProvider;
import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class Cosmic implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesPlugin cratesPlugin = CrazyCratesProvider.get();
    private final @NotNull CrazyManager crazyManager = this.cratesPlugin.getCrazyManager();
    private final @NotNull Methods methods = this.cratesPlugin.getMethods();

    private final HashMap<UUID, ArrayList<Integer>> glass = new HashMap<>();
    private final HashMap<UUID, ArrayList<Integer>> picks = new HashMap<>();
    private final HashMap<UUID, Boolean> checkHands = new HashMap<>();
    
    private void showRewards(UUID uuid, Crate crate) {
        Inventory inv = plugin.getServer().createInventory(null, 27, methods.sanitizeColor(crate.getFile().getString("Crate.CrateName") + " - Prizes"));

        Player player = this.plugin.getServer().getPlayer(uuid);

        if (player != null) {
            picks.get(uuid).forEach(i -> inv.setItem(i, pickTier(uuid).getTierPane()));
            player.openInventory(inv);
        }
    }
    
    private void startRoll(UUID uuid, Crate crate) {
        Inventory inv = plugin.getServer().createInventory(null, 27, methods.sanitizeColor(crate.getFile().getString("Crate.CrateName") + " - Shuffling"));

        Player player = this.plugin.getServer().getPlayer(uuid);

        if (player != null) {
            for (int i = 0; i < 27; i++) {
                inv.setItem(i, pickTier(uuid).getTierPane());
            }

            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            player.openInventory(inv);
        }
    }
    
    private void setChests(Inventory inv, Crate crate) {
        CosmicCrateManager manager = (CosmicCrateManager) crate.getManager();
        int slot = 1;

        for (int i = 0; i < 27; i++) {
            inv.setItem(i, manager.getMysteryCrate().setAmount(slot).addNamePlaceholder("%Slot%", slot + "").addLorePlaceholder("%Slot%", slot + "").build());
            slot++;
        }
    }
    
    public void openCosmic(UUID uuid, Crate crate, KeyType keyType, boolean checkHand) {
        Inventory inv = plugin.getServer().createInventory(null, 27, methods.sanitizeColor(crate.getFile().getString("Crate.CrateName") + " - Choose"));
        setChests(inv, crate);
        Player player = this.plugin.getServer().getPlayer(uuid);

        if (player != null) {
            crazyManager.addPlayerKeyType(uuid, keyType);
            checkHands.put(uuid, checkHand);
            player.openInventory(inv);
        }
    }
    
    private Tier pickTier(UUID uuid) {
        Crate crate = crazyManager.getOpeningCrate(uuid);

        if (crate.getTiers() != null && !crate.getTiers().isEmpty()) {
            for (int stopLoop = 0; stopLoop <= 100; stopLoop++) {
                for (Tier tier : crate.getTiers()) {
                    int chance = tier.getChance();
                    int num = new Random().nextInt(tier.getMaxRange());

                    if (num >= 1 && num <= chance) return tier;
                }
            }
        }

        return null;
    }
    
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        final Inventory inv = e.getInventory();
        final Player player = (Player) e.getWhoClicked();
        final UUID uuid = player.getUniqueId();

        final Crate crate = crazyManager.getOpeningCrate(uuid);

        if (crazyManager.isInOpeningList(uuid)) {
            if (!crate.getFile().getString("Crate.CrateType").equalsIgnoreCase("Cosmic")) return;
        } else {
            return;
        }

        final FileConfiguration file = crate.getFile();

        if (e.getView().getTitle().equals(methods.sanitizeColor(file.getString("Crate.CrateName") + " - Shuffling"))) e.setCancelled(true);

        if (e.getView().getTitle().equals(methods.sanitizeColor(file.getString("Crate.CrateName") + " - Prizes"))) {
            e.setCancelled(true);
            int slot = e.getRawSlot();

            if (inCosmic(slot)) {
                for (int i : picks.get(uuid)) {
                    if (slot == i) {
                        ItemStack item = e.getCurrentItem();
                        Tier tier = getTier(crate, item);

                        if (item != null && tier != null) {
                            Prize prize = crate.pickPrize(uuid, tier);

                            for (int stop = 0; prize == null && stop <= 2000; stop++) {
                                prize = crate.pickPrize(uuid, tier);
                            }

                            if (prize != null) {
                                crazyManager.givePrize(uuid, prize, crate);
                                plugin.getServer().getPluginManager().callEvent(new PlayerPrizeEvent(uuid, crate, crazyManager.getOpeningCrate(uuid).getName(), prize));
                                e.setCurrentItem(prize.getDisplayItem());
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

                                if (prize.useFireworks()) methods.firework(player.getLocation().add(0, 1, 0));
                            }

                            return;
                        }
                    }
                }
            }
        }

        if (e.getView().getTitle().equals(methods.sanitizeColor(file.getString("Crate.CrateName") + " - Choose"))) {
            e.setCancelled(true);
            int slot = e.getRawSlot();

            if (inCosmic(slot)) {
                ItemStack item = e.getCurrentItem();

                if (item != null) {
                    CosmicCrateManager manager = (CosmicCrateManager) crate.getManager();
                    int totalPrizes = manager.getTotalPrizes();
                    int pickedSlot = slot + 1;
                    NBTItem nbtItem = new NBTItem(item);

                    if (nbtItem.hasNBTData()) {
                        if (nbtItem.hasKey("Cosmic-Mystery-Crate")) {
                            if (!glass.containsKey(uuid)) glass.put(uuid, new ArrayList<>());

                            if (glass.get(uuid).size() < totalPrizes) {
                                e.setCurrentItem(manager.getPickedCrate().setAmount(pickedSlot).addNamePlaceholder("%Slot%", pickedSlot + "").addLorePlaceholder("%Slot%", pickedSlot + "").build());
                                glass.get(uuid).add(slot);
                            }

                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                        } else if (nbtItem.hasKey("Cosmic-Picked-Crate")) {
                            if (!glass.containsKey(uuid)) glass.put(uuid, new ArrayList<>());

                            e.setCurrentItem(manager.getMysteryCrate().setAmount(pickedSlot).addNamePlaceholder("%Slot%", pickedSlot + "").addLorePlaceholder("%Slot%", pickedSlot + "").build());
                            ArrayList<Integer> l = new ArrayList<>();

                            for (int i : glass.get(uuid)) if (i != slot) l.add(i);

                            glass.put(uuid, l);
                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                        }
                    }

                    if (glass.get(uuid).size() >= totalPrizes) {
                        KeyType keyType = crazyManager.getPlayerKeyType(uuid);

                        if (keyType == KeyType.PHYSICAL_KEY && !crazyManager.hasPhysicalKey(uuid, crate, checkHands.get(uuid))) {
                            player.closeInventory();
                            player.sendMessage(Messages.NO_KEY.getMessage());

                            if (crazyManager.isInOpeningList(uuid)) {
                                crazyManager.removePlayerFromOpeningList(uuid);
                                crazyManager.removePlayerKeyType(uuid);
                            }

                            checkHands.remove(uuid);
                            glass.remove(uuid);
                            return;
                        }

                        if (crazyManager.hasPlayerKeyType(uuid) && !crazyManager.takeKeys(1, uuid, crate, keyType, checkHands.get(uuid))) {
                            methods.failedToTakeKey(player.getName(), crate);
                            crazyManager.removePlayerFromOpeningList(uuid);
                            crazyManager.removePlayerKeyType(uuid);
                            checkHands.remove(uuid);
                            glass.remove(uuid);
                            return;
                        }

                        crazyManager.addCrateTask(uuid, new BukkitRunnable() {
                            int time = 0;

                            @Override
                            public void run() {
                                try {
                                    startRoll(uuid, crate);
                                } catch (Exception e) {
                                    PlayerReceiveKeyEvent event = new PlayerReceiveKeyEvent(uuid, crate, PlayerReceiveKeyEvent.KeyReceiveReason.REFUND, 1);
                                    plugin.getServer().getPluginManager().callEvent(event);

                                    if (!event.isCancelled()) {
                                        crazyManager.addKeys(1, uuid, crate, keyType);
                                        crazyManager.endCrate(uuid);
                                        cancel();

                                        player.sendMessage(methods.getPrefix("&cAn issue has occurred and so a key refund was given."));

                                        FancyLogger.error("An issue occurred when the user " + player.getName() + " was using the " + crate.getName() + " crate and so they were issued a key refund.");
                                        FancyLogger.debug(e.getMessage());
                                    }

                                    return;
                                }

                                time++;

                                if (time == 40) {
                                    crazyManager.endCrate(uuid);
                                    showRewards(uuid, crate);
                                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);

                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            if (player.getOpenInventory().getTopInventory().equals(inv)) player.closeInventory();
                                        }
                                    }.runTaskLater(plugin, 40);
                                }
                            }
                        }.runTaskTimer(plugin, 0, 2));
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        final Inventory inv = e.getInventory();
        final Player player = (Player) e.getPlayer();
        final UUID uuid = player.getUniqueId();

        Crate crate = crazyManager.getOpeningCrate(uuid);

        if (crazyManager.isInOpeningList(uuid)) {
            if (crate.getFile() == null) {
                return;
            } else {
                if (!crate.getFile().getString("Crate.CrateType").equalsIgnoreCase("Cosmic")) return;
            }
        } else {
            return;
        }

        if (e.getView().getTitle().equals(methods.sanitizeColor(crate.getFile().getString("Crate.CrateName") + " - Prizes"))) {
            boolean playSound = false;

            for (int i : picks.get(uuid)) {
                if (inv.getItem(i) != null) {
                    Tier tier = getTier(crate, inv.getItem(i));

                    if (tier != null) {
                        Prize prize = crate.pickPrize(uuid, tier);

                        for (int stop = 0; prize == null && stop <= 2000; stop++) {
                            prize = crate.pickPrize(uuid, tier);
                        }

                        crazyManager.givePrize(uuid, prize, crate);
                        playSound = true;
                    }
                }
            }

            if (playSound) player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);

            crazyManager.removePlayerFromOpeningList(uuid);
            crazyManager.removePlayerKeyType(uuid);

            if (glass.containsKey(uuid)) {
                picks.put(uuid, glass.get(uuid));
                glass.remove(uuid);
            }

            checkHands.remove(uuid);
        }

        if (crazyManager.isInOpeningList(uuid) && e.getView().getTitle().equals(methods.sanitizeColor(crate.getFile().getString("Crate.CrateName") + " - Choose"))) {

            if (!glass.containsKey(uuid) || glass.get(uuid).size() < 4) {
                crazyManager.removePlayerFromOpeningList(uuid);
                crazyManager.removePlayerKeyType(uuid);
            }

            if (glass.containsKey(uuid)) {
                picks.put(uuid, glass.get(uuid));
                glass.remove(uuid);
            }

            checkHands.remove(uuid);
        }
    }
    
    private Tier getTier(Crate crate, ItemStack item) {
        for (Tier tier : crate.getTiers()) {
            if (tier.getTierPane().isSimilar(item)) return tier;
        }

        return null;
    }
    
    private boolean inCosmic(int slot) {
        // The last slot in cosmic crate is 27
        return slot < 27;
    }
}