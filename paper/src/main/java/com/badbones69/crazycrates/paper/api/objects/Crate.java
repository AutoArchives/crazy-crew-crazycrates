package com.badbones69.crazycrates.paper.api.objects;

import us.crazycrew.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.FileManager;
import com.badbones69.crazycrates.paper.api.managers.CosmicCrateManager;
import com.badbones69.crazycrates.paper.api.managers.CrateManager;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.common.crates.CrateHologram;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import us.crazycrew.crazycrates.paper.api.crates.menus.InventoryManager;
import us.crazycrew.crazycrates.paper.api.crates.menus.types.CratePreviewMenu;
import us.crazycrew.crazycrates.paper.utils.MiscUtils;
import us.crazycrew.crazycrates.paper.utils.MsgUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class Crate {
    
    private CrateManager manager;
    private final String name;
    private final ItemStack key;
    private final ItemStack keyNoNBT;
    private final ItemStack adminKey;
    private int maxPage = 1;
    private final int maxSlots;
    private final String previewName;
    private final boolean previewToggle;
    private final boolean borderToggle;
    private final ItemBuilder borderItem;

    private final CrateType crateType;
    private final FileConfiguration file;
    private List<Prize> prizes;
    private final String crateInventoryName;
    private final boolean giveNewPlayerKeys;
    private int previewChestLines;
    private final int newPlayerKeys;
    private List<ItemStack> preview;
    private final List<Tier> tiers;
    private final CrateHologram hologram;

    @NotNull
    private final CrazyCrates plugin = CrazyCrates.get();

    @NotNull
    private final InventoryManager inventoryManager = this.plugin.getCrazyHandler().getInventoryManager();

    @NotNull
    private final FileManager fileManager = this.plugin.getFileManager();

    private final int maxMassOpen;
    private final int requiredKeys;
    private final List<String> prizeMessage;

    /**
     * @param name The name of the crate.
     * @param crateType The crate type of the crate.
     * @param key The key as an item stack.
     * @param prizes The prizes that can be won.
     * @param file The crate file.
     */
    public Crate(String name, String previewName, CrateType crateType, ItemStack key, List<Prize> prizes, FileConfiguration file, int newPlayerKeys, List<Tier> tiers, int maxMassOpen, int requiredKeys, List<String> prizeMessage, CrateHologram hologram) {
        ItemBuilder itemBuilder = ItemBuilder.convertItemStack(key);
        this.keyNoNBT = itemBuilder.build();
        this.key = itemBuilder.setCrateName(name).build();
        this.adminKey = itemBuilder
        .addLore("")
        .addLore("&7&l(&6&l!&7&l) Left click for Physical Key")
        .addLore("&7&l(&6&l!&7&l) Right click for Virtual Key")
        .setCrateName(name).build();
        this.file = file;
        this.name = name;
        this.tiers = tiers != null ? tiers : new ArrayList<>();
        this.maxMassOpen = maxMassOpen;
        this.requiredKeys = requiredKeys;
        this.prizeMessage = prizeMessage;
        this.prizes = prizes;
        this.crateType = crateType;
        this.preview = getPreviewItems();
        this.previewToggle = file != null && (!file.contains("Crate.Preview.Toggle") || file.getBoolean("Crate.Preview.Toggle"));
        this.borderToggle = file != null && file.getBoolean("Crate.Preview.Glass.Toggle");
        setPreviewChestLines(file != null ? file.getInt("Crate.Preview.ChestLines", 6) : 6);
        this.previewName = MsgUtils.sanitizeColor(previewName);
        this.newPlayerKeys = newPlayerKeys;
        this.giveNewPlayerKeys = newPlayerKeys > 0;
        this.maxSlots = this.previewChestLines * 9;

        for (int amount = this.preview.size(); amount > this.maxSlots - (this.borderToggle ? 18 : this.maxSlots >= this.preview.size() ? 0 : this.maxSlots != 9 ? 9 : 0); amount -= this.maxSlots - (this.borderToggle ? 18 : this.maxSlots >= this.preview.size() ? 0 : this.maxSlots != 9 ? 9 : 0), this.maxPage++) ;

        this.crateInventoryName = file != null ? MsgUtils.sanitizeColor(file.getString("Crate.CrateName")) : "";
        String borderName = file != null && file.contains("Crate.Preview.Glass.Name") ? MsgUtils.color(file.getString("Crate.Preview.Glass.Name")) : " ";
        this.borderItem = file != null && file.contains("Crate.Preview.Glass.Item") ? new ItemBuilder().setMaterial(file.getString("Crate.Preview.Glass.Item")).setName(borderName) : new ItemBuilder().setMaterial(Material.AIR).setName(borderName);

        this.hologram = hologram != null ? hologram : new CrateHologram();

        if (crateType == CrateType.cosmic) {
            if (this.file != null) this.manager = new CosmicCrateManager(this.file);
        }
    }
    
    /**
     * Get the crate manager which contains all the settings for that crate type.
     */
    public CrateManager getManager() {
        return this.manager;
    }
    
    /**
     * Set the preview lines for a Crate.
     * @param amount The amount of lines the preview has.
     */
    public void setPreviewChestLines(int amount) {
        int finalAmount;

        if (amount < 3 && this.borderToggle) {
            finalAmount = 3;
        } else finalAmount = Math.min(amount, 6);

        this.previewChestLines = finalAmount;
    }
    
    /**
     * Get the amount of lines the preview will show.
     * @return The amount of lines it is set to show.
     */
    public int getPreviewChestLines() {
        return this.previewChestLines;
    }
    
    /**
     * Get the max amount of slots in the preview.
     * @return The max number of slots in the preview.
     */
    public int getMaxSlots() {
        return this.maxSlots;
    }
    
    /**
     * Check to see if a player can win a prize from a crate.
     * @param player The player you are checking.
     * @return True if they can win at least 1 prize and false if they can't win any.
     */
    public boolean canWinPrizes(Player player) {
        return pickPrize(player) != null;
    }

    public List<String> getPrizeMessage() {
        return this.prizeMessage;
    }

    /**
     * Picks a random prize based on BlackList Permissions and the Chance System.
     * @param player The player that will be winning the prize.
     * @return The winning prize.
     */
    public Prize pickPrize(Player player) {
        List<Prize> prizes = new ArrayList<>();
        List<Prize> usablePrizes = new ArrayList<>();

        // ================= Blacklist Check ================= //
        if (player.isOp()) {
            usablePrizes.addAll(getPrizes());
        } else {
            for (Prize prize : getPrizes()) {
                if (prize.hasBlacklistPermission(player)) {
                    if (prize.hasAltPrize()) continue;
                }

                usablePrizes.add(prize);
            }
        }

        // ================= Chance Check ================= //
        chanceCheck(prizes, usablePrizes);

        try {
            return prizes.get(new Random().nextInt(prizes.size()));
        } catch (IllegalArgumentException exception) {
            this.plugin.getLogger().log(Level.WARNING, "Failed to find prize from the " + name + " crate for player " + player.getName() + ".", exception);
            return null;
        }
    }

    private void chanceCheck(List<Prize> prizes, List<Prize> usablePrizes) {
        for (int stop = 0; prizes.isEmpty() && stop <= 2000; stop++) {
            for (Prize prize : usablePrizes) {
                int max = prize.getMaxRange();
                int chance = prize.getChance();
                int num;

                for (int counter = 1; counter <= 1; counter++) {
                    num = 1 + new Random().nextInt(max);

                    if (num <= chance) prizes.add(prize);
                }
            }
        }
    }

    /**
     * Overrides the current prize pool.
     *
     * @param prizes list
     */
    public void setPrize(List<Prize> prizes) {
        this.prizes = prizes;
    }

    public void purge() {
        this.prizes.clear();
        this.preview.clear();
    }

    /**
     * Overrides the preview items.
     *
     * @param itemStacks list
     */
    public void setPreviewItems(List<ItemStack> itemStacks) {
        this.preview = itemStacks;
    }

    /**
     * Picks a random prize based on BlackList Permissions and the Chance System. Only used in the Cosmic Crate Type since it is the only one with tiers.
     * @param player The player that will be winning the prize.
     * @param tier The tier you wish the prize to be from.
     * @return The winning prize based on the crate's tiers.
     */
    public Prize pickPrize(Player player, Tier tier) {
        List<Prize> prizes = new ArrayList<>();
        List<Prize> usablePrizes = new ArrayList<>();

        // ================= Blacklist Check ================= //
        if (player.isOp()) {
            for (Prize prize : getPrizes()) {
                if (prize.getTiers().contains(tier)) usablePrizes.add(prize);
            }
        } else {
            for (Prize prize : getPrizes()) {
                if (prize.hasBlacklistPermission(player)) {
                    if (prize.hasAltPrize()) continue;
                }

                if (prize.getTiers().contains(tier)) usablePrizes.add(prize);
            }
        }

        // ================= Chance Check ================= //
        chanceCheck(prizes, usablePrizes);

        return prizes.get(new Random().nextInt(prizes.size()));
    }
    
    /**
     * Picks a random prize based on BlackList Permissions and the Chance System. Spawns the display item at the location.
     * @param player The player that will be winning the prize.
     * @param location The location the firework will spawn at.
     * @return The winning prize.
     */
    public Prize pickPrize(Player player, Location location) {
        Prize prize = pickPrize(player);

        if (prize.useFireworks()) MiscUtils.spawnFirework(location, null);

        return prize;
    }
    
    /**
     * @return name The name of the crate.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * @return The name of the crate's preview page.
     */
    public String getPreviewName() {
        return this.previewName;
    }
    
    /**
     * Check if the inventory the player is in is the preview menu.
     * @param view The inventory view of the inventory.
     * @return True if it is the preview menu and false if not.
     */
    public boolean isPreview(InventoryView view) {
        return view != null && isPreview(view.getTitle());
    }
    
    /**
     * Check if the inventory the player is in is the preview menu.
     * @param inventoryName The name of the inventory.
     * @return True if it is the preview menu and false if not.
     */
    public boolean isPreview(String inventoryName) {
        return inventoryName != null && (isInventoryNameSimilar(inventoryName, this.previewName) || isInventoryNameSimilar(inventoryName, this.crateInventoryName));
    }
    
    /**
     * Get if the preview is toggled on.
     * @return True if preview is on and false if not.
     */
    public boolean isPreviewEnabled() {
        return this.previewToggle;
    }
    
    /**
     * Get if the preview has an item boarder.
     * @return True if it does and false if not.
     */
    public boolean isBorderToggle() {
        return this.borderToggle;
    }
    
    /**
     * Get the item that shows as the preview boarder if enabled.
     * @return The ItemBuilder for the boarder item.
     */
    public ItemBuilder getBorderItem() {
        return this.borderItem;
    }
    
    /**
     * Get the name of the inventory the crate will have.
     * @return The name of the inventory for GUI based crate types.
     */
    public String getCrateInventoryName() {
        return this.crateInventoryName;
    }
    
    /**
     * Gets the inventory of a preview of prizes for the crate.
     * @return The preview as an Inventory object.
     */
    public Inventory getPreview(Player player) {
        return getPreview(player, this.plugin.getCrazyHandler().getInventoryManager().getPage(player));
    }
    
    /**
     * Gets the inventory of a preview of prizes for the crate.
     * @return The preview as an Inventory object.
     */
    public Inventory getPreview(Player player, int page) {
        CratePreviewMenu cratePreviewMenu = new CratePreviewMenu(this, player, !this.borderToggle && (this.inventoryManager.inCratePreview(player) || this.maxPage > 1) && this.maxSlots == 9 ? this.maxSlots + 9 : this.maxSlots, page, this.previewName);

        return cratePreviewMenu.build().getInventory();
    }
    
    /**
     * @return The crate type of the crate.
     */
    public CrateType getCrateType() {
        return this.crateType;
    }
    
    /**
     * @return The key as an item stack.
     */
    public ItemStack getKey() {
        return this.key.clone();
    }
    
    /**
     * @param amount The amount of keys you want.
     * @return The key as an item stack.
     */
    public ItemStack getKey(int amount) {
        ItemStack key = this.key.clone();
        key.setAmount(amount);
        return key;
    }
    
    /**
     * @return The key as an item stack with no nbt tags.
     */
    public ItemStack getKeyNoNBT() {
        return this.keyNoNBT.clone();
    }
    
    /**
     * @param amount The amount of keys you want.
     * @return The key as an item stack with no nbt tags.
     */
    public ItemStack getKeyNoNBT(int amount) {
        ItemStack key = this.keyNoNBT.clone();
        key.setAmount(amount);
        return key;
    }
    
    /**
     * Get the key that shows in the /cc admin menu.
     * @return The itemstack of the key shown in the /cc admin menu.
     */
    public ItemStack getAdminKey() {
        return this.adminKey;
    }
    
    /**
     * @return The crates file.
     */
    public FileConfiguration getFile() {
        return this.file;
    }
    
    /**
     * @return The prizes in the crate.
     */
    public List<Prize> getPrizes() {
        return this.prizes;
    }
    
    /**
     * @param name Name of the prize you want.
     * @return The prize you asked for.
     */
    public Prize getPrize(String name) {
        for (Prize prize : this.prizes) {
            if (prize.getName().equalsIgnoreCase(name)) return prize;
        }

        return null;
    }
    
    public Prize getPrize(ItemStack item) {
        try {
            NBTItem nbt = new NBTItem(item);

            if (nbt.hasTag("crazycrate-prize")) return getPrize(nbt.getString("crazycrate-prize"));
        } catch (Exception ignored) {}
        
        for (Prize prize : this.prizes) {
            if (item.isSimilar(prize.getDisplayItem())) return prize;
        }

        return null;
    }
    
    /**
     * @return True if new players get keys and false if they do not.
     */
    public boolean doNewPlayersGetKeys() {
        return this.giveNewPlayerKeys;
    }
    
    /**
     * @return The number of keys new players get.
     */
    public int getNewPlayerKeys() {
        return this.newPlayerKeys;
    }
    
    /**
     * Add a new editor item to a prize in the Crate.
     * @param prize The prize the item is being added to.
     * @param item The ItemStack that is being added.
     */
    public void addEditorItem(String prize, ItemStack item) {
        List<ItemStack> items = new ArrayList<>();
        items.add(item);

        String path = "Crate.Prizes." + prize;

        if (!this.file.contains(path)) {
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasDisplayName()) this.file.set(path + ".DisplayName", item.getItemMeta().getDisplayName());
                if (item.getItemMeta().hasLore()) this.file.set(path + ".Lore", item.getItemMeta().getLore());
            }

            NBTItem nbtItem = new NBTItem(item);

            if (nbtItem.hasNBTData()) {
                if (nbtItem.hasTag("Unbreakable") && nbtItem.getBoolean("Unbreakable")) this.file.set(path + ".Unbreakable", true);
            }

            List<String> enchantments = new ArrayList<>();

            for (Enchantment enchantment : item.getEnchantments().keySet()) {
                enchantments.add((enchantment.getKey().getKey() + ":" + item.getEnchantmentLevel(enchantment)));
            }

            if (!enchantments.isEmpty()) this.file.set(path + ".DisplayEnchantments", enchantments);

            this.file.set(path + ".DisplayItem", item.getType().name());
            this.file.set(path + ".DisplayAmount", item.getAmount());
            this.file.set(path + ".MaxRange", 100);
            this.file.set(path + ".Chance", 50);
        } else {
            // Must be checked as getList will return null if nothing is found.
            if (this.file.contains(path + ".Editor-Items")) this.file.getList(path + ".Editor-Items").forEach(listItem -> items.add((ItemStack) listItem));
        }

        this.file.set(path + ".Editor-Items", items);

        this.fileManager.saveFile(this.fileManager.getFile(this.name));

        this.fileManager.removeFile(this.name);
        this.fileManager.addFile(this.name + ".yml", "crates");

        this.plugin.getCrateManager().reloadCrate(this.plugin.getCrateManager().getCrateFromName(this.name));
    }
    
    /**
     * @return The max page for the preview.
     */
    public int getMaxPage() {
        return this.maxPage;
    }
    
    /**
     * @return A list of the tiers for the crate. Will be empty if there are none.
     */
    public List<Tier> getTiers() {
        return this.tiers;
    }

    /**
     * @return Returns the max amount that players can specify for crate mass open.
     */
    public int getMaxMassOpen() {
        return this.maxMassOpen;
    }

    public int getRequiredKeys() {
        return this.requiredKeys;
    }

    public List<ItemStack> getPreview() {
        return this.preview;
    }

    /**
     * @return A CrateHologram which contains all the info about the hologram the crate uses.
     */
    public CrateHologram getHologram() {
        return this.hologram;
    }
    
    public int getAbsoluteItemPosition(int baseSlot) {
        return baseSlot + (this.previewChestLines > 1 ? this.previewChestLines - 1 : 1) * 9;
    }
    
    private boolean isInventoryNameSimilar(String inventory1, String inventory2) {
        return MsgUtils.removeColor(inventory1).equalsIgnoreCase(MsgUtils.removeColor(inventory2));
    }
    
    /**
     * Loads all the preview items and puts them into a list.
     * @return A list of all the preview items that were created.
     */
    public List<ItemStack> getPreviewItems() {
        List<ItemStack> items = new ArrayList<>();

        for (Prize prize : getPrizes()) {
            items.add(prize.getDisplayItem());
        }

        return items;
    }
}