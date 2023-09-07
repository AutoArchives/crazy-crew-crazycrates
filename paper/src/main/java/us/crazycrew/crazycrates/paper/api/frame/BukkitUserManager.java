package us.crazycrew.crazycrates.paper.api.frame;

import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import us.crazycrew.crazycrates.api.frame.UserManager;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.FileManager.Files;
import us.crazycrew.crazycrates.paper.api.enums.settings.Messages;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BukkitUserManager extends UserManager {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesLoader cratesLoader = this.plugin.getCratesLoader();
    private final @NotNull CrazyManager crazyManager = this.cratesLoader.getCrazyManager();
    private final @NotNull Methods methods = this.cratesLoader.getMethods();

    private final @NotNull FileConfiguration config = Files.CONFIG.getFile();
    private final @NotNull FileConfiguration data = Files.DATA.getFile();

    @Override
    public Player getUser(UUID uuid) {
        return this.plugin.getServer().getPlayer(uuid);
    }

    @Override
    public int getVirtualKeys(UUID uuid, String crateName) {
        return this.data.getInt("Players." + uuid + "." + crateName);
    }

    @Override
    public void addVirtualKeys(int amount, UUID uuid, String crateName) {
        if (isPlayerNull(uuid)) {
            FancyLogger.warn("Player with the uuid: " + uuid + " is null.");
            return;
        }

        if (crateName.isBlank()) {
            FancyLogger.warn("Crate name cannot be empty or null.");
            return;
        }

        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (crate == null) {
            FancyLogger.warn("Crate " + crateName + " doesn't exist.");
            return;
        }

        Player player = getUser(uuid);

        int keys = getVirtualKeys(uuid, crate.getName());

        this.data.set("Players." + uuid + ".Name", player.getName());
        this.data.set("Players." + uuid + "." + crate.getName(), (Math.max((keys + amount), 0)));

        Files.DATA.saveFile();
    }

    @Override
    public void setKeys(int amount, UUID uuid, String crateName) {
        boolean isValid = true;

        if (isPlayerNull(uuid)) {
            FancyLogger.warn("Player with the uuid: " + uuid + " is null.");
            return;
        }

        Player player = getUser(uuid);

        if (crateName.isBlank()) {
            FancyLogger.warn("Crate name cannot be empty or null.");
            isValid = false;
        }

        if (!isValid) {
            FancyLogger.warn("Could not set keys for whatever reason to this player.");
            return;
        }

        Crate crate = this.crazyManager.getCrateFromName(crateName);

        this.data.set("Players." + player.getUniqueId() + ".Name", player.getName());
        this.data.set("Players." + player.getUniqueId() + "." + crate.getName(), amount);
        Files.DATA.saveFile();
    }

    private boolean isPlayerNull(UUID uuid) {
        return getUser(uuid) == null;
    }

    @Override
    public void addKeys(int amount, UUID uuid, String crateName, KeyType keyType) {
        boolean isValid = true;

        if (isPlayerNull(uuid)) {
            FancyLogger.warn("Player with the uuid: " + uuid + " is null.");
            return;
        }

        Player player = getUser(uuid);

        if (crateName.isBlank()) {
            FancyLogger.warn("Crate name cannot be empty or null.");
            isValid = false;
        }

        if (!isValid) {
            player.sendMessage("Could not add a key to you: " + isValid);
            return;
        }

        Crate crate = this.crazyManager.getCrateFromName(crateName);

        switch (keyType) {
            case PHYSICAL_KEY -> {
                if (!this.methods.isInventoryFull(player)) {
                    player.getInventory().addItem(crate.getKey(amount));
                    return;
                }

                if (this.config.getBoolean("Settings.Give-Virtual-Keys-When-Inventory-Full")) {
                    addVirtualKeys(amount, player.getUniqueId(), crate.getName());

                    boolean sendMessage = this.config.getBoolean("Settings.Give-Virtual-Keys-When-Inventory-Full-Message");

                    if (sendMessage) {
                        player.sendMessage(Messages.CANNOT_GIVE_PLAYER_KEYS.getMessage()
                                .replaceAll("%amount%", String.valueOf(amount))
                                .replaceAll("%key%", crate.getName()));
                    }

                    return;
                }

                player.getWorld().dropItem(player.getLocation(), crate.getKey(amount));
            }

            case VIRTUAL_KEY -> addVirtualKeys(amount, player.getUniqueId(), crate.getName());
        }
    }

    @Override
    public int getTotalKeys(UUID uuid, String crateName) {
        return getVirtualKeys(uuid, crateName) + getPhysicalKeys(uuid, crateName);
    }

    @Override
    public int getPhysicalKeys(UUID uuid, String crateName) {
        boolean isValid = true;

        int keys = 0;

        if (isPlayerNull(uuid)) {
            FancyLogger.warn("Player with the uuid: " + uuid + " is null.");
            return keys;
        }

        Player player = getUser(uuid);

        if (crateName.isBlank()) {
            FancyLogger.warn("Crate name cannot be empty or null.");
            isValid = false;
        }

        if (!isValid) {
            FancyLogger.warn("Could not get keys because player is invalid.");
            return keys;
        }

        Crate crate = this.crazyManager.getCrateFromName(crateName);

        for (ItemStack item : player.getOpenInventory().getBottomInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;

            if (this.methods.isSimilar(item, crate)) keys += item.getAmount();
        }

        return keys;
    }

    @Override
    public boolean takeKeys(int amount, UUID uuid, String crateName, KeyType keyType, boolean checkHand) {
        boolean isValid = true;

        if (isPlayerNull(uuid)) {
            FancyLogger.warn("Player with the uuid: " + uuid + " is null.");
            return false;
        }

        Player player = getUser(uuid);

        if (crateName.isBlank()) {
            FancyLogger.warn("Crate name cannot be empty or null.");
            isValid = false;
        }

        if (!isValid) {
            FancyLogger.warn("Could not take a key from: " + player.getName());
            return false;
        }

        Crate crate = this.crazyManager.getCrateFromName(crateName);

        switch (keyType) {
            case PHYSICAL_KEY -> {
                int takeAmount = amount;

                try {
                    List<ItemStack> items = new ArrayList<>();

                    if (checkHand) {
                        items.add(player.getEquipment().getItemInMainHand());
                        items.add(player.getEquipment().getItemInOffHand());
                    } else {
                        items.addAll(Arrays.asList(player.getInventory().getContents()));
                        items.remove(player.getEquipment().getItemInOffHand());
                    }

                    for (ItemStack item : items) {
                        if (item != null) {
                            if (isKeyFromCrate(item, crate)) {
                                int keyAmount = item.getAmount();

                                if ((takeAmount - keyAmount) >= 0) {
                                    this.methods.removeItemAnySlot(player.getInventory(), item);
                                    takeAmount -= keyAmount;
                                } else {
                                    item.setAmount(keyAmount - takeAmount);
                                    takeAmount = 0;
                                }

                                if (takeAmount <= 0) return true;
                            }
                        }
                    }

                    // This needs to be done as player.getInventory().removeItem(ItemStack); does NOT remove from the offhand.
                    if (takeAmount > 0) {
                        ItemStack item = player.getEquipment().getItemInOffHand();

                        if (isKeyFromCrate(item, crate)) {
                            int keyAmount = item.getAmount();

                            if ((takeAmount - keyAmount) >= 0) {
                                player.getEquipment().setItemInOffHand(new ItemStack(Material.AIR, 1));
                                takeAmount -= keyAmount;
                            } else {
                                item.setAmount(keyAmount - takeAmount);
                                takeAmount = 0;
                            }

                            if (takeAmount <= 0) return true;
                        }
                    }
                } catch (Exception exception) {
                    this.methods.failedToTakeKey(player.getName(), crate);
                    return false;
                }
            }

            case VIRTUAL_KEY -> {
                int keys = getVirtualKeys(uuid, crate.getName());

                this.data.set("Players." + uuid + ".Name", player.getName());

                int newAmount = Math.max((keys - amount), 0);

                if (newAmount == 0) {
                    this.data.set("Players." + uuid + "." + crate.getName(), null);
                } else {
                    this.data.set("Players." + uuid + "." + crate.getName(), newAmount);
                }

                Files.DATA.saveFile();
                return true;
            }

            case FREE_KEY -> {
                return true;
            }
        }

        this.methods.failedToTakeKey(player.getName(), crate);
        return false;
    }

    @Override
    public boolean hasPhysicalKey(UUID uuid, String crateName, boolean checkHand) {
        boolean isValid = true;

        if (isPlayerNull(uuid)) {
            FancyLogger.warn("Player with the uuid: " + uuid + " is null.");
            return false;
        }

        Player player = getUser(uuid);

        if (crateName.isBlank()) {
            FancyLogger.warn("Crate name cannot be empty or null.");
            isValid = false;
        }

        if (!isValid) {
            FancyLogger.warn("Could not check if player has a physical key: " + player.getName());
            return false;
        }

        Crate crate = this.crazyManager.getCrateFromName(crateName);

        List<ItemStack> items = new ArrayList<>();

        if (checkHand) {
            items.add(player.getEquipment().getItemInMainHand());
            items.add(player.getEquipment().getItemInOffHand());
        } else {
            items.addAll(Arrays.asList(player.getInventory().getContents()));
            items.removeAll(Arrays.asList(player.getInventory().getArmorContents()));
        }

        for (ItemStack item : items) {
            if (item != null) {
                if (isKeyFromCrate(item, crate)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Check if a key is from a specific Crate.
     *
     * @param item The key ItemStack you are checking.
     * @param crate The Crate you are checking.
     * @return Returns true if it belongs to that Crate and false if it does not.
     */
    private boolean isKeyFromCrate(ItemStack item, Crate crate) {
        if (crate.getCrateType() != CrateType.MENU) {
            if (item != null && item.getType() != Material.AIR) {
                return this.methods.isSimilar(item, crate);
            }
        }

        return false;
    }

    @Override
    public boolean addOfflineKeys(UUID uuid, String crateName, int keys) {
        if (crateName.isBlank()) {
            FancyLogger.warn("Crate name cannot be empty or null.");
            return false;
        }

        Crate crate = this.crazyManager.getCrateFromName(crateName);

        OfflinePlayer player = this.plugin.getServer().getOfflinePlayer(uuid);

        try {
            if (this.data.contains("Offline-Players." + player.getName() + "." + crate.getName())) keys += this.data.getInt("Offline-Players." + player.getName() + "." + crate.getName());

            this.data.set("Offline-Players." + player.getName() + "." + crate.getName(), keys);
            Files.DATA.saveFile();

            return true;
        } catch (Exception exception) {
            FancyLogger.error("Could not add keys to offline player with uuid: " + uuid);
            FancyLogger.debug(exception.getMessage());
            return false;
        }
    }
}