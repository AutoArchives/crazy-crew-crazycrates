package us.crazycrew.crazycrates.paper.utils;

import com.badbones69.crazycrates.api.enums.Permissions;
import com.badbones69.crazycrates.paper.api.objects.Crate;
import com.badbones69.crazycrates.paper.api.objects.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.api.enums.PersistentKeys;
import us.crazycrew.crazycrates.paper.api.enums.Translation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MiscUtils {

    @NotNull
    private static final CrazyCrates plugin = CrazyCrates.get();

    public static void sendCommand(String command) {
        Server server = plugin.getServer();

        ConsoleCommandSender console = server.getConsoleSender();

        server.dispatchCommand(console, command);
    }

    public static void spawnFirework(Location location, Color color) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);

        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        FireworkEffect.@NotNull Builder effect = FireworkEffect.builder()
                .with(FireworkEffect.Type.BALL)
                .trail(false)
                .flicker(false);

        if (color != null) effect.withColor(color); else effect.withColor(Color.RED).withColor(Color.AQUA).withColor(Color.ORANGE).withColor(Color.YELLOW);

        fireworkMeta.addEffects(effect.build());
        fireworkMeta.setPower(0);

        firework.setFireworkMeta(fireworkMeta);

        setEntityData(firework, PersistentKeys.no_firework_damage);

        Server server = plugin.getServer();

        server.getScheduler().scheduleSyncDelayedTask(plugin, firework::detonate, 3);
    }

    /**
     * Add a persistent tag to an entity.
     */
    @SuppressWarnings("unchecked")
    public static void setEntityData(Entity entity, PersistentKeys key) {
        PersistentDataContainer entityData = entity.getPersistentDataContainer();

        entityData.set(key.getNamespacedKey(), key.getType(), true);
    }

    /**
     * Checks if the player's inventory is empty by checking if the first empty slot is -1
     *
     * @return true if inventory is empty otherwise false
     */
    public static boolean isInventoryFull(Player player) {
        return player.getInventory().firstEmpty() == -1;
    }

    /**
     * Remove or subtract an item from a player's inventory.
     */
    public static void removeItemStack(Player player, ItemStack item) {
        if (item.getAmount() <= 1) {
            player.getInventory().removeItem(item);
            return;
        }

        item.setAmount(item.getAmount() - 1);
    }

    // ElectronicBoy is the author.
    public static HashMap<Integer, ItemStack> removeMultipleItemStacks(Inventory inventory, ItemStack... items) {
        if (items != null) {
            HashMap<Integer, ItemStack> leftover = new HashMap<>();

            // TODO: optimization

            for (int i = 0; i < items.length; i++) {
                ItemStack item = items[i];
                int toDelete = item.getAmount();

                while (true) {
                    // Paper start - Allow searching entire contents
                    ItemStack[] toSearch = inventory.getContents();
                    int first = getFirstItem(item, false, toSearch);
                    // Paper end

                    // Drat! we don't have this type in the inventory
                    if (first == -1) {
                        item.setAmount(toDelete);
                        leftover.put(i, item);
                        break;
                    } else {
                        ItemStack itemStack = inventory.getItem(first);
                        int amount = itemStack.getAmount();

                        if (amount <= toDelete) {
                            toDelete -= amount;
                            // clear the slot, all used up
                            inventory.clear(first);
                        } else {
                            // split the stack and store
                            itemStack.setAmount(amount - toDelete);
                            inventory.setItem(first, itemStack);
                            toDelete = 0;
                        }
                    }

                    // Bail when done
                    if (toDelete <= 0) {
                        break;
                    }
                }
            }

            return leftover;
        } else {
            plugin.getLogger().info("Items cannot be null.");
        }

        return null;
    }

    /**
     * Remove multiple items from a player's inventory.
     */
    public static void removeMultipleItemStacks(Player player, ItemStack... items) {
        if (items == null) {
            plugin.getLogger().warning("Items cannot be null.");
            return;
        }

        Inventory inventory = player.getInventory();

        HashMap<Integer, ItemStack> stuckItems = new HashMap<>();
        ItemStack[] toSearch = inventory.getContents();

        for (ItemStack item : items) {
            int toDelete = item.getAmount();

            while (toDelete > 0) {
                int first = getFirstItem(item, false, toSearch);

                // Drat! we don't have this type in the inventory
                if (first == -1) {
                    ItemStack itemClone = item.clone();
                    itemClone.setAmount(toDelete);
                    // Clear the slot, all used up
                    stuckItems.put(stuckItems.size(), itemClone);
                    break;
                } else {
                    ItemStack itemStack = toSearch[first];
                    int amount = itemStack.getAmount();

                    if (amount <= toDelete) {
                        toSearch[first] = null;
                        toDelete -= amount;
                    } else {
                        // Split the stack and store
                        itemStack.setAmount(amount - toDelete);
                        toSearch[first] = itemStack;
                        toDelete = 0;
                    }
                }
            }
        }
    }

    /**
     * Gets the first item amount in an inventory.
     *
     * @return -1 or item amount.
     */
    @SuppressWarnings("SameParameterValue")
    private static int getFirstItem(ItemStack item, boolean getAmount, ItemStack[] inventory) {
        if (item == null) return -1;

        for (int i = 0; i < inventory.length; i++) {
            ItemStack inventoryItem = inventory[i];

            if (inventoryItem != null) {
                if ((getAmount && item.equals(inventoryItem)) || (!getAmount && item.isSimilar(inventoryItem))) return i;
            }
        }

        return -1;
    }

    public static void failedToTakeKey(CommandSender player, Crate crate) {
        plugin.getServer().getLogger().warning("An error has occurred while trying to take a physical key from a player");
        plugin.getServer().getLogger().warning("Player: " + player.getName());
        plugin.getServer().getLogger().warning("Crate: " + crate.getName());

        player.sendMessage(MsgUtils.getPrefix("&cAn issue has occurred when trying to take a key."));
        player.sendMessage(MsgUtils.getPrefix("&cCommon reasons includes not having enough keys."));
    }

    public static long pickNumber(long min, long max) {
        max++;

        try {
            // new Random() does not have a nextLong(long bound) method.
            return min + ThreadLocalRandom.current().nextLong(max - min);
        } catch (IllegalArgumentException e) {
            return min;
        }
    }

    public static boolean permCheck(CommandSender sender, Permissions permissions, boolean tabComplete) {
        if (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender) return true;

        Player player = (Player) sender;

        if (player.hasPermission(permissions.getPermission("command"))) {
            return true;
        } else {
            if (!tabComplete) {
                player.sendMessage(Translation.no_permission.getString());
                return false;
            }

            return false;
        }
    }

    public static int randomNumber(int min, int max) {
        return min + new Random().nextInt(max - min);
    }

    public static Enchantment getEnchantment(String enchantmentName) {
        HashMap<String, String> enchantments = getEnchantmentList();
        enchantmentName = stripEnchantmentName(enchantmentName);

        for (Enchantment enchantment : Enchantment.values()) {
            try {
                if (stripEnchantmentName(enchantment.getKey().getKey()).equalsIgnoreCase(enchantmentName)) {
                    return enchantment;
                }

                if (stripEnchantmentName(enchantment.getName()).equalsIgnoreCase(enchantmentName) || (enchantments.get(enchantment.getName()) != null &&
                        stripEnchantmentName(enchantments.get(enchantment.getName())).equalsIgnoreCase(enchantmentName))) {
                    return enchantment;
                }
            } catch (Exception ignore) {}
        }

        return null;
    }

    private static String stripEnchantmentName(String enchantmentName) {
        return enchantmentName != null ? enchantmentName.replace("-", "").replace("_", "").replace(" ", "") : null;
    }

    private static HashMap<String, String> getEnchantmentList() {
        HashMap<String, String> enchantments = new HashMap<>();
        enchantments.put("ARROW_DAMAGE", "Power");
        enchantments.put("ARROW_FIRE", "Flame");
        enchantments.put("ARROW_INFINITE", "Infinity");
        enchantments.put("ARROW_KNOCKBACK", "Punch");
        enchantments.put("DAMAGE_ALL", "Sharpness");
        enchantments.put("DAMAGE_ARTHROPODS", "Bane_Of_Arthropods");
        enchantments.put("DAMAGE_UNDEAD", "Smite");
        enchantments.put("DEPTH_STRIDER", "Depth_Strider");
        enchantments.put("DIG_SPEED", "Efficiency");
        enchantments.put("DURABILITY", "Unbreaking");
        enchantments.put("FIRE_ASPECT", "Fire_Aspect");
        enchantments.put("KNOCKBACK", "KnockBack");
        enchantments.put("LOOT_BONUS_BLOCKS", "Fortune");
        enchantments.put("LOOT_BONUS_MOBS", "Looting");
        enchantments.put("LUCK", "Luck_Of_The_Sea");
        enchantments.put("LURE", "Lure");
        enchantments.put("OXYGEN", "Respiration");
        enchantments.put("PROTECTION_ENVIRONMENTAL", "Protection");
        enchantments.put("PROTECTION_EXPLOSIONS", "Blast_Protection");
        enchantments.put("PROTECTION_FALL", "Feather_Falling");
        enchantments.put("PROTECTION_FIRE", "Fire_Protection");
        enchantments.put("PROTECTION_PROJECTILE", "Projectile_Protection");
        enchantments.put("SILK_TOUCH", "Silk_Touch");
        enchantments.put("THORNS", "Thorns");
        enchantments.put("WATER_WORKER", "Aqua_Affinity");
        enchantments.put("BINDING_CURSE", "Curse_Of_Binding");
        enchantments.put("MENDING", "Mending");
        enchantments.put("FROST_WALKER", "Frost_Walker");
        enchantments.put("VANISHING_CURSE", "Curse_Of_Vanishing");
        enchantments.put("SWEEPING_EDGE", "Sweeping_Edge");
        enchantments.put("RIPTIDE", "Riptide");
        enchantments.put("CHANNELING", "Channeling");
        enchantments.put("IMPALING", "Impaling");
        enchantments.put("LOYALTY", "Loyalty");
        return enchantments;
    }

    public static ItemBuilder getRandomPaneColor() {
        List<String> colors = Arrays.asList(
                Material.WHITE_STAINED_GLASS_PANE.toString(),
                Material.ORANGE_STAINED_GLASS_PANE.toString(),
                Material.MAGENTA_STAINED_GLASS_PANE.toString(),
                Material.LIGHT_BLUE_STAINED_GLASS_PANE.toString(),
                Material.YELLOW_STAINED_GLASS_PANE.toString(),
                Material.LIME_STAINED_GLASS_PANE.toString(),
                Material.PINK_STAINED_GLASS_PANE.toString(),
                Material.GRAY_STAINED_GLASS_PANE.toString(),
                Material.CYAN_STAINED_GLASS_PANE.toString(),
                Material.PURPLE_STAINED_GLASS_PANE.toString(),
                Material.BLUE_STAINED_GLASS_PANE.toString(),
                Material.BROWN_STAINED_GLASS_PANE.toString(),
                Material.GREEN_STAINED_GLASS_PANE.toString(),
                Material.RED_STAINED_GLASS_PANE.toString(),
                Material.BLACK_STAINED_GLASS_PANE.toString(),
                Material.LIGHT_GRAY_STAINED_GLASS_PANE.toString());
        return new ItemBuilder().setMaterial(colors.get(new Random().nextInt(colors.size())));
    }

    /**
     * Decides when the crate should start to slow down.
     */
    public static List<Integer> slowSpin() {
        List<Integer> slow = new ArrayList<>();
        int full = 46;
        int cut = 9;

        for (int i = 46; cut > 0; full--) {
            if (full <= i - cut || full >= i - cut) {
                slow.add(i);
                i -= cut;
                cut--;
            }
        }

        return slow;
    }
}