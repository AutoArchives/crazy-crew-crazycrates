package com.badbones69.crazycrates.api;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.builders.ItemBuilder;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.api.objects.Prize;
import com.badbones69.crazycrates.api.objects.Tier;
import org.apache.commons.lang.WordUtils;
import com.badbones69.crazycrates.api.events.PlayerPrizeEvent;
import com.badbones69.crazycrates.api.enums.Messages;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import com.badbones69.crazycrates.api.utils.MiscUtils;
import com.badbones69.crazycrates.api.utils.MsgUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import static java.util.regex.Matcher.quoteReplacement;

public class PrizeManager {
    
    private static final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    /**
     * Gets the prize for the player.
     *
     * @param player who the prize is for.
     * @param crate the player is opening.
     * @param prize the player is being given.
     */
    public static void givePrize(Player player, Prize prize, Crate crate) {
        /*
        prize = prize.hasPermission(player) ? prize.getAlternativePrize() : prize;

        for (ItemStack item : prize.getItems()) {
            if (item == null) {
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("{crate}", prize.getCrateName());
                placeholders.put("{prize}", prize.getPrizeName());
                player.sendMessage(Messages.prize_error.getMessage(placeholders, player));

                continue;
            }

            if (!MiscUtils.isInventoryFull(player)) {
                player.getInventory().addItem(item);
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
        }

        for (ItemBuilder item : prize.getItemBuilders()) {
            ItemBuilder clone = new ItemBuilder(item).setTarget(player);

            if (!MiscUtils.isInventoryFull(player)) {
                player.getInventory().addItem(clone.build());
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), clone.build());
            }
        }*/
    }

    /**
     * Gets the prize for the player.
     *
     * @param player who the prize is for.
     * @param crate the player is opening.
     * @param prize the player is being given.
     */
    public static void givePrize(Player player, Crate crate, Prize prize) {
        /*if (prize != null) {
            givePrize(player, prize, crate);

            if (prize.useFireworks()) MiscUtils.spawnFirework(player.getLocation().add(0, 1, 0), null);

            plugin.getServer().getPluginManager().callEvent(new PlayerPrizeEvent(player, crate, crate.getName(), prize));
        } else {
            player.sendMessage(MsgUtils.getPrefix("&cNo prize was found, please report this issue if you think this is an error."));
        }*/
    }

    public static void getPrize(Crate crate, Inventory inventory, int slot, Player player) {
        /*ItemStack item = inventory.getItem(slot);

        if (item == null) return;

        Prize prize = crate.getPrize(item);

        givePrize(player, prize, crate);*/
    }

    public static Tier getTier(Crate crate) {
        /*if (crate.getTiers() != null && !crate.getTiers().isEmpty()) {
            for (int stopLoop = 0; stopLoop <= 100; stopLoop++) {
                for (Tier tier : crate.getTiers()) {
                    int chance = tier.getChance();

                    int num = MiscUtils.useOtherRandom() ? ThreadLocalRandom.current().nextInt(tier.getMaxRange()) : new Random().nextInt(tier.getMaxRange());

                    if (num >= 1 && num <= chance) {
                        return tier;
                    }
                }
            }
        }*/

        return null;
    }
}