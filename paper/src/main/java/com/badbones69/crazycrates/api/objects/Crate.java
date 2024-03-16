package com.badbones69.crazycrates.api.objects;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.builders.ItemBuilder;
import com.badbones69.crazycrates.api.enums.Messages;
import com.badbones69.crazycrates.api.enums.PersistentKeys;
import com.badbones69.crazycrates.api.events.PlayerPrizeEvent;
import com.badbones69.crazycrates.api.utils.MiscUtils;
import com.badbones69.crazycrates.api.utils.MsgUtils;
import com.badbones69.crazycrates.tasks.crates.CrateManager;
import com.badbones69.crazycrates.tasks.crates.effects.SoundEffect;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.WordUtils;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;
import us.crazycrew.crazycrates.api.crates.CrateHologram;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.platform.crates.CrateConfig;

import java.util.*;
import java.util.logging.Logger;

import static java.util.regex.Matcher.quoteReplacement;

public class Crate {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);
    private final @NotNull CrateManager crateManager = this.plugin.getCrateManager();

    private final boolean executeCommands;
    private final List<String> commands;

    private final ConfigurationSection section;
    private final CrateHologram crateHologram;
    private final CrateType crateType;
    private final String previewName;
    private final String crateName;
    private final String fileName;

    private final int startingKeys;
    private final int requiredKeys;
    private final int maxMassOpen;

    private final boolean inMenu;
    private final int slot;

    private final List<String> keys;

    private final boolean broadcastMessageToggle;
    private final String broadcastMessage;

    private final boolean previewToggle;
    private final int previewRows;

    private final boolean fillerToggle;
    private final ItemStack fillerItem;

    private final List<String> prizeMessage;

    private final ItemStack displayItem;

    private final List<Prize> prizes = new ArrayList<>();

    private final CrateConfig crateConfig;

    /**
     * Builds a crate object.
     *
     * @param crateConfig the config to use
     */
    public Crate(CrateConfig crateConfig) {
        this.crateConfig = crateConfig;

        this.section = crateConfig.getCrateSection();

        this.crateType = crateConfig.getCrateType();

        this.crateName = crateConfig.getCrateName();
        this.previewName = this.section.getString("Preview-Name", crateConfig.getFileName());

        this.fileName = crateConfig.getFileName();

        this.startingKeys = this.section.getInt("StartingKeys", 0);
        this.requiredKeys = this.section.getInt("RequiredKeys", 0);
        this.maxMassOpen = this.section.getInt("Max-Mass-Open", 10);
        this.inMenu = this.section.getBoolean("InGUI", true);
        this.slot = this.section.getInt("Slot");

        this.keys = this.section.getStringList("keys");

        this.broadcastMessageToggle = this.section.getBoolean("OpeningBroadCast", false);
        this.broadcastMessage = this.section.getString("BroadCast", "");

        this.executeCommands = this.section.getBoolean("opening-command.toggle", false);
        this.commands = this.section.getStringList("opening-command.commands");

        ConfigurationSection hologram = crateConfig.getHologramSection();

        this.crateHologram = new CrateHologram(
                hologram.getBoolean("Toggle", true),
                hologram.getDouble("Height", 1.5),
                hologram.getInt("Range", 8),
                hologram.getStringList("Message")
        );

        this.prizeMessage = this.section.getStringList("Prize-Message");

        this.displayItem = new ItemBuilder()
                .setMaterial(this.section.getString("Item", "CHEST"))
                .setName(this.section.getString("Name", " "))
                .setLore(this.section.getStringList("Lore") == null ? Collections.emptyList() : this.section.getStringList("Lore"))
                .setGlow(this.section.getBoolean("Glowing", false))
                .setString(PersistentKeys.crate_name.getNamespacedKey(), this.fileName).build();

        ConfigurationSection preview = crateConfig.getPreviewSection();

        this.previewToggle = preview.getBoolean("Toggle", true);
        this.previewRows = preview.getInt("ChestLines", 6);

        this.fillerToggle = preview.getBoolean("Glass.Toggle", false);
        this.fillerItem = new ItemBuilder()
                .setMaterial(preview.getString("Glass.Item", "gray_stained_glass_pane"))
                .setName(preview.getString("Glass.Name", ""))
                .build();

        ConfigurationSection section = crateConfig.getPrizeSection();

        if (section != null) {
            Set<String> keys = section.getKeys(false);

            for (String key : keys) {
                ConfigurationSection prizeSection = section.getConfigurationSection(key);

                if (prizeSection == null) continue;

                this.prizes.add(new Prize(prizeSection));
            }
        }

        PluginManager server = this.plugin.getServer().getPluginManager();

        if (server.getPermission("crazycrates.open." + getCrateName()) == null) {
            server.addPermission(new Permission("crazycrates.open." + getCrateName(), "Allows you to open " + getCrateName(), PermissionDefault.TRUE));
        }
    }

    /**
     * @return the slot in the menu.
     */
    public int getSlot() {
        return this.slot;
    }

    /**
     * @return A list of keys that can be used on the crate.
     */
    public List<String> getKeys() {
        return this.keys;
    }

    /**
     * @return the max amount of keys that can be used with /cc mass-open
     */
    public int getMaxMassOpen() {
        return this.maxMassOpen;
    }

    /**
     * @return A list of messages to send to a player.
     */
    public List<String> getPrizeMessage() {
        return this.prizeMessage;
    }

    /**
     * @return the keys required to open the crate.
     */
    public int getRequiredKeys() {
        return this.requiredKeys;
    }

    /**
     * @return the starting keys.
     */
    public int getStartingKeys() {
        return this.startingKeys;
    }

    /**
     * @return true or false
     */
    public boolean isPreviewToggle() {
        return this.previewToggle;
    }

    /**
     * @return the size of the crate preview
     */
    public int getPreviewRows() {
        return this.previewRows;
    }

    /**
     * @return true or false
     */
    public boolean isFillerToggle() {
        return this.fillerToggle;
    }

    /**
     * @return the filler item that appears in the gui.
     */
    public ItemStack getFillerItem() {
        return this.fillerItem;
    }

    /**
     * @return the item stack that appears in the gui.
     */
    public ItemStack getDisplayItem() {
        return this.displayItem;
    }

    /**
     * Sends a message to a player.
     *
     * @param player the player to send to.
     * @param prize the prize messages to send if found.
     */
    public void sendMessage(Player player, Prize prize) {
        ItemStack displayItem = prize.getDisplayItem(player);

        String displayName = displayItem.hasItemMeta() ? displayItem.getItemMeta().getDisplayName() : displayItem.getType().getKey().getKey().replaceAll("_", "");

        if (!this.prizeMessage.isEmpty() && prize.getMessages().isEmpty()) {
            this.prizeMessage.forEach(line -> {
                String value = line.replaceAll("%reward%", displayName).replaceAll("%crate%", this.crateName);

                MsgUtils.sendMessage(player, MiscUtils.isPapiActive() ? PlaceholderAPI.setPlaceholders(player, value) : value, false);
            });

            return;
        }

        for (String message : prize.getMessages()) {
            String value = message.replaceAll("%reward%", displayName).replaceAll("%crate%", this.crateName);

            MsgUtils.sendMessage(player, MiscUtils.isPapiActive() ? PlaceholderAPI.setPlaceholders(player, value) : value, false);
        }
    }

    public void playSound(Player player, String type, String fallback, SoundCategory category) {
        new SoundEffect(this.crateConfig.getSoundSection(), type, fallback, category).play(player, player.getLocation());
    }

    /**
     * This will broadcast a message to the entire server if the option is enabled.
     */
    public void broadcast() {
        if (!this.broadcastMessageToggle) return;

        this.plugin.getServer().broadcastMessage(this.broadcastMessage);
    }

    /**
     * This will execute any commands on open.
     */
    public void execute() {
        if (!this.executeCommands) return;

        this.commands.forEach(command -> this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), command));
    }

    /**
     * @return if the item appears in the menu.
     */
    public boolean isInMenu() {
        return this.inMenu;
    }

    /**
     * @return the type of crate.
     */
    public CrateType getCrateType() {
        return this.crateType;
    }

    /**
     * @return the name of the crate.
     */
    public String getCrateName() {
        return this.crateName;
    }

    /**
     * @return the preview name used in the preview menu.
     */
    public String getPreviewName() {
        return this.previewName;
    }

    /**
     * @return the name of the crate file without .yml
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * @return the hologram object.
     */
    public CrateHologram getCrateHologram() {
        return this.crateHologram;
    }

    /**
     * @return the crate section.
     */
    public ConfigurationSection getSection() {
        return this.section;
    }

    /**
     * @return A list of prizes.
     */
    public List<Prize> getPrizes() {
        return Collections.unmodifiableList(this.prizes);
    }

    /**
     * Gives a prize to a player.
     *
     * @param player the player to get the prize.
     * @param prize the prize to give.
     */
    public void givePrize(Player player, Prize prize) {
        Logger logger = this.plugin.getLogger();

        if (prize == null) {
            if (MiscUtils.isLogging()) logger.warning("No prize was found when giving " + player.getName() + " a prize.");

            return;
        }

        for (ItemStack item : prize.getItems()) {
            if (item == null) {
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("{crate}", getFileName());
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
        }

        for (String command : prize.getCommands()) {
            if (command.contains("%random%:")) {
                String cmd = command;
                StringBuilder commandBuilder = new StringBuilder();

                for (String word : cmd.split(" ")) {
                    if (word.startsWith("%random%:")) {
                        word = word.replace("%random%:", "");

                        try {
                            long min = Long.parseLong(word.split("-")[0]);
                            long max = Long.parseLong(word.split("-")[1]);
                            commandBuilder.append(MiscUtils.pickNumber(min, max)).append(" ");
                        } catch (Exception exception) {
                            commandBuilder.append("1 ");

                            logger.warning("The prize " + prize.getPrizeName() + " in the " + getCrateName() + " crate has caused an error when trying to run a command.");
                            logger.warning("Command: " + cmd);
                        }
                    } else {
                        commandBuilder.append(word).append(" ");
                    }
                }

                command = commandBuilder.toString();
                command = command.substring(0, command.length() - 1);

                if (MiscUtils.isPapiActive()) command = PlaceholderAPI.setPlaceholders(player, command);

                String display = prize.getDisplayItem().getItemMeta().getDisplayName();

                String name = display.isEmpty() ? MsgUtils.color(WordUtils.capitalizeFully(prize.getDisplayItem().getType().getKey().getKey().replaceAll("_", " "))) : display;

                MiscUtils.sendCommand(command.replaceAll("%player%", player.getName()).replaceAll("%reward%", name).replaceAll("%crate%", getPreviewName()));
            }
        }

        if (prize.isFirework()) MiscUtils.spawnFirework(player.getLocation().add(0, 1, 0), null);

        this.plugin.getServer().getPluginManager().callEvent(new PlayerPrizeEvent(player, this, getFileName(), prize));

        sendMessage(player, prize);
    }

    public void givePrize(Inventory inventory, int slot, Player player) {
        ItemStack itemStack = inventory.getItem(slot);

        if (itemStack == null) return;

        Prize prize = getPrize(itemStack);

        givePrize(player, prize);
    }

    /**
     * @param item the item to check.
     * @return the prize you asked for.
     */
    public Prize getPrize(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        return getPrize(container.get(PersistentKeys.crate_prize.getNamespacedKey(), PersistentDataType.STRING));
    }

    /**
     * @param prizeName the prize name to check.
     * @return the prize you asked for.
     */
    public Prize getPrize(String prizeName) {
        Prize prize = null;

        for (Prize key : this.prizes) {
            if (key.getPrizeName().equalsIgnoreCase(prizeName)) {
                prize = key;
                break;
            }
        }

        return prize;
    }

    public Tier getTier() {
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