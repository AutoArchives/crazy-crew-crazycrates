package us.crazycrew.crazycrates.paper.cratetypes;

import org.bukkit.SoundCategory;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.enums.settings.Messages;
import us.crazycrew.crazycrates.paper.api.events.PlayerPrizeEvent;
import us.crazycrew.crazycrates.paper.api.events.PlayerReceiveKeyEvent;
import us.crazycrew.crazycrates.paper.api.plugin.frame.BukkitUserManager;
import us.crazycrew.crazycrates.paper.api.managers.CosmicCrateManager;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.objects.Prize;
import us.crazycrew.crazycrates.paper.api.objects.Tier;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
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
import us.crazycrew.crazycrates.paper.api.plugin.CrazyHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class Cosmic implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyHandler crazyHandler = this.plugin.getCrazyHandler();
    private final @NotNull BukkitUserManager userManager = this.crazyHandler.getUserManager();
    private final @NotNull CrazyManager crazyManager = this.crazyHandler.getCrazyManager();
    private final @NotNull Methods methods = this.crazyHandler.getMethods();

    private final HashMap<UUID, ArrayList<Integer>> glass = new HashMap<>();
    private final HashMap<UUID, ArrayList<Integer>> picks = new HashMap<>();
    private final HashMap<UUID, Boolean> checkHands = new HashMap<>();
    
    private void showRewards(Player player, Crate crate) {
        Inventory inv = this.plugin.getServer().createInventory(null, 27, this.methods.sanitizeColor(crate.getFile().getString("Crate.CrateName") + " - Prizes"));

        UUID uuid = player.getUniqueId();

        this.picks.get(uuid).forEach(i -> inv.setItem(i, pickTier(uuid).getTierPane()));
        player.openInventory(inv);
    }
    
    private void startRoll(Player player, Crate crate) {
        Inventory inv = plugin.getServer().createInventory(null, 27, this.methods.sanitizeColor(crate.getFile().getString("Crate.CrateName") + " - Shuffling"));

        UUID uuid = player.getUniqueId();
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, pickTier(uuid).getTierPane());
        }

        //TODO() make volume/pitch configurable and sound type configurable.
        //TODO() Adopt the new sound system including custom sounds.
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1f, 1f);
        player.openInventory(inv);
    }
    
    private void setChests(Inventory inv, Crate crate) {
        CosmicCrateManager manager = (CosmicCrateManager) crate.getManager();
        int slot = 1;

        for (int i = 0; i < 27; i++) {
            inv.setItem(i, manager.getMysteryCrate().setAmount(slot).addNamePlaceholder("%Slot%", slot + "").addLorePlaceholder("%Slot%", slot + "").build());
            slot++;
        }
    }
    
    public void openCosmic(Player player, Crate crate, KeyType keyType, boolean checkHand) {
        Inventory inv = this.plugin.getServer().createInventory(null, 27, this.methods.sanitizeColor(crate.getFile().getString("Crate.CrateName") + " - Choose"));
        setChests(inv, crate);

        UUID uuid = player.getUniqueId();

        this.crazyManager.addPlayerKeyType(uuid, keyType);
        this.checkHands.put(uuid, checkHand);
        player.openInventory(inv);
    }
    
    private Tier pickTier(UUID uuid) {
        Crate crate = this.crazyManager.getOpeningCrate(uuid);

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

        final Crate crate = this.crazyManager.getOpeningCrate(uuid);

        if (this.crazyManager.isInOpeningList(uuid)) {
            if (!crate.getFile().getString("Crate.CrateType").equalsIgnoreCase("Cosmic")) return;
        } else {
            return;
        }

        final FileConfiguration file = crate.getFile();

        if (e.getView().getTitle().equals(this.methods.sanitizeColor(file.getString("Crate.CrateName") + " - Shuffling"))) e.setCancelled(true);

        if (e.getView().getTitle().equals(this.methods.sanitizeColor(file.getString("Crate.CrateName") + " - Prizes"))) {
            e.setCancelled(true);
            int slot = e.getRawSlot();

            if (inCosmic(slot)) {
                for (int i : this.picks.get(uuid)) {
                    if (slot == i) {
                        ItemStack item = e.getCurrentItem();
                        Tier tier = getTier(crate, item);

                        if (item != null && tier != null) {
                            Prize prize = crate.pickPrize(player, tier);

                            for (int stop = 0; prize == null && stop <= 2000; stop++) {
                                prize = crate.pickPrize(player, tier);
                            }

                            if (prize != null) {
                                this.crazyManager.givePrize(player, prize, crate);
                                this.plugin.getServer().getPluginManager().callEvent(new PlayerPrizeEvent(uuid, crate, this.crazyManager.getOpeningCrate(uuid).getName(), prize));
                                e.setCurrentItem(prize.getDisplayItem());
                                //TODO() make volume/pitch configurable and sound type configurable.
                                //TODO() Adopt the new sound system including custom sounds.
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 1f);

                                if (prize.useFireworks()) this.methods.firework(player.getLocation().add(0, 1, 0));
                            }

                            return;
                        }
                    }
                }
            }
        }

        if (e.getView().getTitle().equals(this.methods.sanitizeColor(file.getString("Crate.CrateName") + " - Choose"))) {
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
                            if (!this.glass.containsKey(uuid)) this.glass.put(uuid, new ArrayList<>());

                            if (this.glass.get(uuid).size() < totalPrizes) {
                                e.setCurrentItem(manager.getPickedCrate().setAmount(pickedSlot).addNamePlaceholder("%Slot%", pickedSlot + "").addLorePlaceholder("%Slot%", pickedSlot + "").build());
                                this.glass.get(uuid).add(slot);
                            }

                            //TODO() make volume/pitch configurable and sound type configurable.
                            //TODO() Adopt the new sound system including custom sounds.
                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1f, 1f);
                        } else if (nbtItem.hasKey("Cosmic-Picked-Crate")) {
                            if (!this.glass.containsKey(uuid)) this.glass.put(uuid, new ArrayList<>());

                            e.setCurrentItem(manager.getMysteryCrate().setAmount(pickedSlot).addNamePlaceholder("%Slot%", pickedSlot + "").addLorePlaceholder("%Slot%", pickedSlot + "").build());
                            ArrayList<Integer> l = new ArrayList<>();

                            for (int i : this.glass.get(uuid)) if (i != slot) l.add(i);

                            this.glass.put(uuid, l);
                            //TODO() make volume/pitch configurable and sound type configurable.
                            //TODO() Adopt the new sound system including custom sounds.
                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1f, 1f);
                        }
                    }

                    if (this.glass.get(uuid).size() >= totalPrizes) {
                        KeyType keyType = this.crazyManager.getPlayerKeyType(uuid);

                        if (keyType == KeyType.PHYSICAL_KEY && !this.userManager.hasPhysicalKey(uuid, crate.getName(), this.checkHands.get(uuid))) {
                            player.closeInventory();
                            player.sendMessage(Messages.NO_KEY.getMessage());

                            if (this.crazyManager.isInOpeningList(uuid)) {
                                this.crazyManager.removePlayerFromOpeningList(uuid);
                                this.crazyManager.removePlayerKeyType(uuid);
                            }

                            this.checkHands.remove(uuid);
                            this.glass.remove(uuid);
                            return;
                        }

                        if (this.crazyManager.hasPlayerKeyType(uuid) && !this.userManager.takeKeys(1, player.getUniqueId(), crate.getName(), keyType, this.checkHands.get(uuid))) {
                            this.crazyManager.removePlayerFromOpeningList(uuid);
                            this.crazyManager.removePlayerKeyType(uuid);
                            this.checkHands.remove(uuid);
                            this.glass.remove(uuid);
                            return;
                        }

                        crazyManager.addCrateTask(uuid, new BukkitRunnable() {
                            int time = 0;

                            @Override
                            public void run() {
                                try {
                                    startRoll(player, crate);
                                } catch (Exception exception) {
                                    PlayerReceiveKeyEvent event = new PlayerReceiveKeyEvent(uuid, crate, PlayerReceiveKeyEvent.KeyReceiveReason.REFUND, 1);
                                    plugin.getServer().getPluginManager().callEvent(event);

                                    if (!event.isCancelled()) {
                                        userManager.addKeys(1, player.getUniqueId(), crate.getName(), keyType);
                                        crazyManager.endCrate(uuid);
                                        cancel();

                                        methods.sendMessage(player, Translation.key_refund.getMessage("{crate}", crate.getName()).toComponent(), Translation.key_refund.getMessage("{crate}", crate.getName()).toString());

                                        FancyLogger.error("An issue occurred when the user " + player.getName() + " was using the " + crate.getName() + " crate and so they were issued a key refund.", exception);
                                    }

                                    return;
                                }

                                time++;

                                if (time == 40) {
                                    crazyManager.endCrate(uuid);
                                    showRewards(player, crate);
                                    //TODO() make volume/pitch configurable and sound type configurable.
                                    //TODO() Adopt the new sound system including custom sounds.
                                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS,1f, 1f);

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

        Crate crate = this.crazyManager.getOpeningCrate(uuid);

        if (this.crazyManager.isInOpeningList(uuid)) {
            if (crate.getFile() == null) {
                return;
            } else {
                if (!crate.getFile().getString("Crate.CrateType").equalsIgnoreCase("Cosmic")) return;
            }
        } else {
            return;
        }

        if (e.getView().getTitle().equals(this.methods.sanitizeColor(crate.getFile().getString("Crate.CrateName") + " - Prizes"))) {
            boolean playSound = false;

            for (int i : this.picks.get(uuid)) {
                if (inv.getItem(i) != null) {
                    Tier tier = getTier(crate, inv.getItem(i));

                    if (tier != null) {
                        Prize prize = crate.pickPrize(player, tier);

                        for (int stop = 0; prize == null && stop <= 2000; stop++) {
                            prize = crate.pickPrize(player, tier);
                        }

                        this.crazyManager.givePrize(player, prize, crate);
                        playSound = true;
                    }
                }
            }

            //TODO() make volume/pitch configurable and sound type configurable.
            //TODO() Adopt the new sound system including custom sounds.
            if (playSound) player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1f, 1f);

            this.crazyManager.removePlayerFromOpeningList(uuid);
            this.crazyManager.removePlayerKeyType(uuid);

            if (this.glass.containsKey(uuid)) {
                this.picks.put(uuid, this.glass.get(uuid));
                this.glass.remove(uuid);
            }

            this.checkHands.remove(uuid);
        }

        if (this.crazyManager.isInOpeningList(uuid) && e.getView().getTitle().equals(this.methods.sanitizeColor(crate.getFile().getString("Crate.CrateName") + " - Choose"))) {
            if (!this.glass.containsKey(uuid) || this.glass.get(uuid).size() < 4) {
                this.crazyManager.removePlayerFromOpeningList(uuid);
                this.crazyManager.removePlayerKeyType(uuid);
            }

            if (this.glass.containsKey(uuid)) {
                this.picks.put(uuid, this.glass.get(uuid));
                this.glass.remove(uuid);
            }

            this.checkHands.remove(uuid);
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