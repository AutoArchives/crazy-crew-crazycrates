package us.crazycrew.crazycrates.paper.api.crates;

import com.Zrips.CMI.Modules.ModuleHandling.CMIModule;
import com.badbones69.crazycrates.paper.api.FileManager;
import com.badbones69.crazycrates.paper.api.FileManager.Files;
import com.badbones69.crazycrates.paper.api.enums.BrokeLocation;
import com.badbones69.crazycrates.paper.api.managers.QuadCrateManager;
import com.badbones69.crazycrates.paper.cratetypes.CSGO;
import com.badbones69.crazycrates.paper.cratetypes.FireCracker;
import com.badbones69.crazycrates.paper.cratetypes.QuickCrate;
import com.badbones69.crazycrates.paper.cratetypes.Roulette;
import com.badbones69.crazycrates.paper.cratetypes.War;
import com.badbones69.crazycrates.paper.cratetypes.Wheel;
import com.badbones69.crazycrates.paper.cratetypes.Wonder;
import us.crazycrew.crazycrates.paper.listeners.CrateControlListener;
import org.bukkit.scheduler.BukkitTask;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import us.crazycrew.crazycrates.common.config.types.Config;
import us.crazycrew.crazycrates.paper.api.crates.menus.types.CrateMainMenu;
import us.crazycrew.crazycrates.paper.api.enums.Translation;
import us.crazycrew.crazycrates.paper.api.events.crates.CrateOpenEvent;
import us.crazycrew.crazycrates.paper.api.support.holograms.HologramHandler;
import com.badbones69.crazycrates.paper.api.objects.Crate;
import com.badbones69.crazycrates.paper.api.objects.CrateLocation;
import com.badbones69.crazycrates.paper.api.objects.ItemBuilder;
import com.badbones69.crazycrates.paper.api.objects.Prize;
import com.badbones69.crazycrates.paper.api.objects.Tier;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.common.crates.CrateHologram;
import us.crazycrew.crazycrates.common.crates.quadcrates.CrateSchematic;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.api.support.holograms.types.CMIHologramsSupport;
import us.crazycrew.crazycrates.paper.api.support.holograms.types.DecentHologramsSupport;
import us.crazycrew.crazycrates.paper.api.support.holograms.types.HolographicDisplaysSupport;
import us.crazycrew.crazycrates.paper.api.support.libraries.PluginSupport;
import us.crazycrew.crazycrates.paper.api.support.structures.StructureHandler;
import us.crazycrew.crazycrates.paper.utils.ItemUtils;
import us.crazycrew.crazycrates.paper.utils.MiscUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

public class CrateManager {

    @NotNull
    private final CrazyCrates plugin = CrazyCrates.get();

    @NotNull
    private final FileManager fileManager = this.plugin.getFileManager();

    private final List<CrateLocation> crateLocations = new ArrayList<>();
    private final List<CrateSchematic> crateSchematics = new ArrayList<>();
    private final List<BrokeLocation> brokeLocations = new ArrayList<>();
    private final List<String> brokeCrates = new ArrayList<>();
    private final List<Crate> crates = new ArrayList<>();

    private HologramHandler holograms;

    private boolean giveNewPlayersKeys;

    public void reloadCrate(Crate crate) {
        try {
            // Close previews
            this.plugin.getServer().getOnlinePlayers().forEach(player -> this.plugin.getCrazyHandler().getInventoryManager().closeCratePreview(player));

            // Grab the new file.
            FileConfiguration file = crate.getFile();

            crate.purge();

            // Profit?
            List<Prize> prizes = new ArrayList<>();

            for (String prize : file.getConfigurationSection("Crate.Prizes").getKeys(false)) {
                String path = "Crate.Prizes." + prize;

                List<ItemStack> editorItems = new ArrayList<>();

                if (file.contains(path + ".Editor-Items")) {
                    for (Object list : file.getList(path + ".Editor-Items")) {
                        editorItems.add((ItemStack) list);
                    }
                }

                prizes.add(new Prize(prize, getDisplayItem(file, prize),
                        file.getStringList(path + ".Messages"),
                        file.getStringList(path + ".Commands"),
                        editorItems,
                        getItems(file, prize),
                        crate.getName(),
                        file.getInt(path + ".Chance", 100),
                        file.getInt(path + ".MaxRange", 100),
                        file.getBoolean(path + ".Firework"),
                        file.getStringList(path + ".BlackListed-Permissions"),
                        null,
                        null));
            }

            crate.setPrize(prizes);
            crate.setPreviewItems(crate.getPreviewItems());

            for (UUID uuid : this.plugin.getCrazyHandler().getInventoryManager().getViewers()) {
                Player player = this.plugin.getServer().getPlayer(uuid);

                if (player != null) {
                    this.plugin.getCrazyHandler().getInventoryManager().openNewCratePreview(player, crate);
                }
            }

            this.plugin.getCrazyHandler().getInventoryManager().purge();
        } catch (Exception exception) {
            this.brokeCrates.add(crate.getName());
            this.plugin.getLogger().log(Level.WARNING, "There was an error while loading the " + crate.getName() + ".yml file.", exception);
        }
    }

    // Loads the crates.
    public void loadCrates() {
        this.giveNewPlayersKeys = false;

        purge();

        // Removes all holograms so that they can be replaced.
        if (this.holograms != null) this.holograms.removeAllHolograms();

        if (PluginSupport.DECENT_HOLOGRAMS.isPluginEnabled()) {
            this.holograms = new DecentHologramsSupport();
            if (this.plugin.isLogging()) this.plugin.getLogger().info("DecentHolograms support has been enabled.");
        } else if (PluginSupport.CMI.isPluginEnabled() && CMIModule.holograms.isEnabled()) {
            this.holograms = new CMIHologramsSupport();
            if (this.plugin.isLogging()) this.plugin.getLogger().info("CMI Hologram support has been enabled.");
        } else if (PluginSupport.HOLOGRAPHIC_DISPLAYS.isPluginEnabled()) {
            this.holograms = new HolographicDisplaysSupport();
            if (this.plugin.isLogging()) this.plugin.getLogger().info("Holographic Displays support has been enabled.");
        } else if (this.plugin.isLogging())
            this.plugin.getLogger().warning("No holograms plugin were found. If using CMI, make sure holograms module is enabled.");

        if (this.plugin.isLogging()) this.plugin.getLogger().info("Loading all crate information...");

        for (String crateName : this.fileManager.getAllCratesNames()) {
            try {
                FileConfiguration file = this.fileManager.getFile(crateName).getFile();
                CrateType crateType = CrateType.getFromName(file.getString("Crate.CrateType"));
                List<Prize> prizes = new ArrayList<>();
                String previewName = file.contains("Crate.Preview-Name") ? file.getString("Crate.Preview-Name") : file.getString("Crate.Name");
                List<Tier> tiers = new ArrayList<>();
                int maxMassOpen = file.contains("Crate.Max-Mass-Open") ? file.getInt("Crate.Max-Mass-Open") : 10;
                int requiredKeys = file.contains("Crate.RequiredKeys") ? file.getInt("Crate.RequiredKeys") : 0;

                if (file.contains("Crate.Tiers") && file.getConfigurationSection("Crate.Tiers") != null) {
                    for (String tier : file.getConfigurationSection("Crate.Tiers").getKeys(false)) {
                        String path = "Crate.Tiers." + tier;
                        tiers.add(new Tier(tier, file.getString(path + ".Name"), file.getString(path + ".Color"), file.getInt(path + ".Chance"), file.getInt(path + ".MaxRange")));
                    }
                }

                if (crateType == CrateType.cosmic && tiers.isEmpty()) {
                    this.brokeCrates.add(crateName);
                    if (this.plugin.isLogging()) this.plugin.getLogger().warning("No tiers were found for this cosmic crate " + crateName + ".yml file.");
                    continue;
                }

                for (String prize : file.getConfigurationSection("Crate.Prizes").getKeys(false)) {
                    Prize altPrize = null;
                    String path = "Crate.Prizes." + prize;
                    List<Tier> prizeTiers = new ArrayList<>();

                    for (String tier : file.getStringList(path + ".Tiers")) {
                        for (Tier loadedTier : tiers) {
                            if (loadedTier.getName().equalsIgnoreCase(tier)) prizeTiers.add(loadedTier);
                        }
                    }

                    if (file.contains(path + ".Alternative-Prize")) {
                        if (file.getBoolean(path + ".Alternative-Prize.Toggle")) {
                            altPrize = new Prize("Alternative-Prize",
                                    file.getStringList(path + ".Alternative-Prize.Messages"),
                                    file.getStringList(path + ".Alternative-Prize.Commands"),
                                    null, // No editor items
                                    getItems(file, prize + ".Alternative-Prize"));
                        }
                    }

                    ArrayList<ItemStack> editorItems = new ArrayList<>();

                    if (file.contains(path + ".Editor-Items")) {
                        for (Object list : file.getList(path + ".Editor-Items")) {
                            editorItems.add((ItemStack) list);
                        }
                    }

                    prizes.add(new Prize(prize, getDisplayItem(file, prize),
                            file.getStringList(path + ".Messages"),
                            file.getStringList(path + ".Commands"),
                            editorItems,
                            getItems(file, prize),
                            crateName,
                            file.getInt(path + ".Chance", 100),
                            file.getInt(path + ".MaxRange", 100),
                            file.getBoolean(path + ".Firework"),
                            file.getStringList(path + ".BlackListed-Permissions"),
                            prizeTiers,
                            altPrize));
                }

                int newPlayersKeys = file.getInt("Crate.StartingKeys");

                if (!this.giveNewPlayersKeys) {
                    if (newPlayersKeys > 0) this.giveNewPlayersKeys = true;
                }

                List<String> prizeMessage = file.contains("Crate.Prize-Message") ? file.getStringList("Crate.Prize-Message") : Collections.emptyList();

                CrateHologram holo = new CrateHologram(file.getBoolean("Crate.Hologram.Toggle"), file.getDouble("Crate.Hologram.Height", 0.0), file.getStringList("Crate.Hologram.Message"));
                addCrate(new Crate(crateName, previewName, crateType, getKey(file), prizes, file, newPlayersKeys, tiers, maxMassOpen, requiredKeys, prizeMessage, holo));
            } catch (Exception exception) {
                this.brokeCrates.add(crateName);
                this.plugin.getLogger().log(Level.WARNING, "There was an error while loading the " + crateName + ".yml file.", exception);
            }
        }

        addCrate(new Crate("Menu", "Menu", CrateType.menu, new ItemStack(Material.AIR), new ArrayList<>(), null, 0, null, 0, 0, Collections.emptyList(), null));

        if (this.plugin.isLogging()) {
            List.of(
                    "All crate information has been loaded.",
                    "Loading all the physical crate locations."
            ).forEach(line -> this.plugin.getLogger().info(line));
        }

        FileConfiguration locations = FileManager.Files.LOCATIONS.getFile();
        int loadedAmount = 0;
        int brokeAmount = 0;

        if (locations.getConfigurationSection("Locations") != null) {
            for (String locationName : locations.getConfigurationSection("Locations").getKeys(false)) {
                try {
                    String worldName = locations.getString("Locations." + locationName + ".World");
                    World world = this.plugin.getServer().getWorld(worldName);
                    int x = locations.getInt("Locations." + locationName + ".X");
                    int y = locations.getInt("Locations." + locationName + ".Y");
                    int z = locations.getInt("Locations." + locationName + ".Z");
                    Location location = new Location(world, x, y, z);
                    Crate crate = this.plugin.getCrateManager().getCrateFromName(locations.getString("Locations." + locationName + ".Crate"));

                    if (world != null && crate != null) {
                        this.crateLocations.add(new CrateLocation(locationName, crate, location));

                        if (this.holograms != null) {
                            this.holograms.createHologram(location.getBlock(), crate);
                        }

                        loadedAmount++;
                    } else {
                        this.brokeLocations.add(new BrokeLocation(locationName, crate, x, y, z, worldName));
                        brokeAmount++;
                    }

                } catch (Exception ignored) {}
            }
        }

        // Checking if all physical locations loaded
        if (this.plugin.isLogging()) {
            if (loadedAmount > 0 || brokeAmount > 0) {
                if (brokeAmount <= 0) {
                    this.plugin.getLogger().info("All physical crate locations have been loaded.");
                } else {
                    this.plugin.getLogger().info("Loaded " + loadedAmount + " physical crate locations.");
                    this.plugin.getLogger().info("Failed to load " + brokeAmount + " physical crate locations.");
                }
            }

            this.plugin.getLogger().info("Searching for schematics to load.");
        }

        // Loading schematic files
        String[] schems = new File(this.plugin.getDataFolder() + "/schematics/").list();

        if (schems != null) {
            for (String schematicName : schems) {
                if (schematicName.endsWith(".nbt")) {
                    this.crateSchematics.add(new CrateSchematic(schematicName, new File(plugin.getDataFolder() + "/schematics/" + schematicName)));

                    if (this.plugin.isLogging()) this.plugin.getLogger().info(schematicName + " was successfully found and loaded.");
                }
            }
        }

        if (this.plugin.isLogging()) this.plugin.getLogger().info("All schematics were found and loaded.");

        cleanDataFile();

        this.plugin.getCrazyHandler().getInventoryManager().loadButtons();
    }

    // The crate that the player is opening.
    private final HashMap<UUID, Crate> playerOpeningCrates = new HashMap<>();

    // Keys that are being used in crates. Only needed in cosmic due to it taking the key after the player picks a prize and not in a start method.
    private final HashMap<UUID, KeyType> playerKeys = new HashMap<>();

    // A list of all current crate tasks that are running that a time. Used to force stop any crates it needs to.
    private final HashMap<UUID, BukkitTask> currentTasks = new HashMap<>();

    // A list of tasks being run by the QuadCrate type.
    private final HashMap<UUID, List<BukkitTask>> currentQuadTasks = new HashMap<>();

    private boolean callCrateEvent(Player player, Crate crate, KeyType keyType, boolean checkHand) {
        CrateOpenEvent crateOpenEvent = new CrateOpenEvent(this.plugin, player, crate, keyType, checkHand, crate.getFile());
        crateOpenEvent.callEvent();

        if (crateOpenEvent.isCancelled()) {
            List.of(
                    "Crate " + crate.getName() + " event has been cancelled.",
                    "A few reasons for why this happened can be found below",
                    "",
                    " 1) No valid prizes can be found, Likely a yaml issue.",
                    " 2) The player does not have the permission to open the crate."
            ).forEach(this.plugin.getLogger()::warning);

            return false;
        }

        return true;
    }

    /**
     * Opens a crate for a player.
     *
     * @param player The player that is having the crate opened for them.
     * @param crate The crate that is being used.
     * @param location The location that may be needed for some crate types.
     * @param checkHand If it just checks the players hand or if it checks their inventory.
     */
    public void openCrate(Player player, Crate crate, KeyType keyType, Location location, boolean virtualCrate, boolean checkHand) {
        switch (crate.getCrateType()) {
            case menu -> {
                if (this.plugin.getConfigManager().getConfig().getProperty(Config.enable_crate_menu)) {
                    CrateMainMenu crateMainMenu = new CrateMainMenu(player, this.plugin.getConfigManager().getConfig().getProperty(Config.inventory_size), this.plugin.getConfigManager().getConfig().getProperty(Config.inventory_name));

                    player.openInventory(crateMainMenu.build().getInventory());
                } else player.sendMessage(Translation.feature_disabled.getString());
            }
            case csgo -> {
                if (callCrateEvent(player, crate, keyType, checkHand)) CSGO.openCSGO(player, crate, keyType, checkHand);
            }
            case roulette -> {
                if (callCrateEvent(player, crate, keyType, checkHand)) Roulette.openRoulette(player, crate, keyType, checkHand);
            }
            case wheel -> {
                if (callCrateEvent(player, crate, keyType, checkHand)) Wheel.startWheel(player, crate, keyType, checkHand);
            }
            case wonder -> {
                if (callCrateEvent(player, crate, keyType, checkHand)) Wonder.startWonder(player, crate, keyType, checkHand);
            }
            case war -> {
                if (callCrateEvent(player, crate, keyType, checkHand)) War.openWarCrate(player, crate, keyType, checkHand);
            }
            case quad_crate -> {
                if (virtualCrate) {
                    player.sendMessage(Translation.cant_be_a_virtual_crate.getString());
                    removePlayerFromOpeningList(player);
                    return;
                }

                if (callCrateEvent(player, crate, keyType, checkHand)) {
                    boolean isRandom = crate.getFile().contains("Crate.structure.file") && !crate.getFile().getBoolean("Crate.structure.random", true);

                    CrateSchematic schematic = isRandom ? getCrateSchematic(crate.getFile().getString("Crate.structure.file")) : getCrateSchematics().get(new Random().nextInt(getCrateSchematics().size()));

                    StructureHandler handler = new StructureHandler(schematic.getSchematicFile());
                    // if crate location is not null, get physical location otherwise get player location.
                    Location crateLocation = getCrateLocation(location) != null ? getCrateLocation(location).getLocation() : location;

                    QuadCrateManager session = new QuadCrateManager(player, crate, keyType, crateLocation, checkHand, handler);

                    session.startCrate();
                }
            }
            case fire_cracker -> {
                if (CrateControlListener.inUse.containsValue(location)) {
                    player.sendMessage(Translation.quick_crate_in_use.getString());
                    removePlayerFromOpeningList(player);
                    return;
                } else {
                    if (virtualCrate) {
                        player.sendMessage(Translation.cant_be_a_virtual_crate.getString());
                        removePlayerFromOpeningList(player);
                        return;
                    } else {
                        if (callCrateEvent(player, crate, keyType, checkHand)) {
                            CrateControlListener.inUse.put(player, location);
                            FireCracker.startFireCracker(player, crate, keyType, location, holograms);
                        }
                    }
                }
            }
            case quick_crate -> {
                if (CrateControlListener.inUse.containsValue(location)) {
                    player.sendMessage(Translation.quick_crate_in_use.getString());
                    removePlayerFromOpeningList(player);
                    return;
                } else {
                    if (virtualCrate && location.equals(player.getLocation())) {
                        player.sendMessage(Translation.cant_be_a_virtual_crate.getString());
                        removePlayerFromOpeningList(player);
                        return;
                    } else {
                        if (callCrateEvent(player, crate, keyType, checkHand)) {
                            CrateControlListener.inUse.put(player, location);
                            QuickCrate.openCrate(player, location, crate, keyType, holograms);
                        }
                    }
                }
            }
            case crate_on_the_go -> {
                if (virtualCrate) {
                    player.sendMessage(Translation.cant_be_a_virtual_crate.getString());
                    removePlayerFromOpeningList(player);
                    return;
                } else {
                    if (callCrateEvent(player, crate, keyType, checkHand)) {
                        if (this.plugin.getCrazyHandler().getUserManager().takeKeys(1, player.getUniqueId(), crate.getName(), keyType, true)) {
                            Prize prize = crate.pickPrize(player);
                            this.plugin.getCrazyHandler().getPrizeManager().givePrize(player, prize, crate);

                            if (prize.useFireworks()) MiscUtils.spawnFirework(player.getLocation().add(0, 1, 0), null);

                            removePlayerFromOpeningList(player);
                        } else {
                            MiscUtils.failedToTakeKey(player, crate);
                        }
                    }
                }
            }
        }

        this.plugin.getCrazyHandler().getEventLogger().logCrateEvent(player, crate, keyType, this.plugin.getConfigManager().getConfig().getProperty(Config.log_to_file), this.plugin.getConfigManager().getConfig().getProperty(Config.log_to_console));
    }

    /**
     * This forces a crate to end and will not give out a prize. This is meant for people who leave the server to stop any errors or lag from happening.
     *
     * @param player The player that the crate is being ended for.
     */
    public void endCrate(Player player) {
        if (this.currentTasks.containsKey(player.getUniqueId())) {
            this.currentTasks.get(player.getUniqueId()).cancel();
            removeCrateTask(player);
        }
    }

    /**
     * Ends the tasks running by a player.
     *
     * @param player The player using the crate.
     */
    public void endQuadCrate(Player player) {
        if (this.currentQuadTasks.containsKey(player.getUniqueId())) {
            for (BukkitTask task : this.currentQuadTasks.get(player.getUniqueId())) {
                task.cancel();
            }

            this.currentQuadTasks.remove(player.getUniqueId());
        }
    }

    /**
     * Add a quad crate task that is going on for a player.
     *
     * @param player The player opening the crate.
     * @param task The task of the quad crate.
     */
    public void addQuadCrateTask(Player player, BukkitTask task) {
        if (!this.currentQuadTasks.containsKey(player.getUniqueId())) {
            this.currentQuadTasks.put(player.getUniqueId(), new ArrayList<>());
        }

        this.currentQuadTasks.get(player.getUniqueId()).add(task);
    }

    /**
     * Checks to see if the player has a quad crate task going on.
     *
     * @param player The player that is being checked.
     * @return True if they do have a task and false if not.
     */
    public boolean hasQuadCrateTask(Player player) {
        return this.currentQuadTasks.containsKey(player.getUniqueId());
    }

    /**
     * Add a crate task that is going on for a player.
     *
     * @param player The player opening the crate.
     * @param task The task of the crate.
     */
    public void addCrateTask(Player player, BukkitTask task) {
        this.currentTasks.put(player.getUniqueId(), task);
    }

    /**
     * Remove a task from the list of current tasks.
     *
     * @param player The player using the crate.
     */
    public void removeCrateTask(Player player) {
        this.currentTasks.remove(player.getUniqueId());
    }

    /**
     * Checks to see if the player has a crate task going on.
     *
     * @param player The player that is being checked.
     * @return True if they do have a task and false if not.
     */
    public boolean hasCrateTask(Player player) {
        return this.currentTasks.containsKey(player.getUniqueId());
    }

    /**
     * Add a player to the list of players that are currently opening crates.
     *
     * @param player The player that is opening a crate.
     * @param crate The crate the player is opening.
     */
    public void addPlayerToOpeningList(Player player, Crate crate) {
        this.playerOpeningCrates.put(player.getUniqueId(), crate);
    }

    /**
     * Remove a player from the list of players that are opening crates.
     *
     * @param player The player that has finished opening a crate.
     */
    public void removePlayerFromOpeningList(Player player) {
        this.playerOpeningCrates.remove(player.getUniqueId());
    }

    /**
     * Check if a player is opening a crate.
     *
     * @param player The player you are checking.
     * @return True if they are opening a crate and false if they are not.
     */
    public boolean isInOpeningList(Player player) {
        return this.playerOpeningCrates.containsKey(player.getUniqueId());
    }

    /**
     * Get the crate the player is currently opening.
     *
     * @param player The player you want to check.
     * @return The Crate of which the player is opening. May return null if no crate found.
     */
    public Crate getOpeningCrate(Player player) {
        return this.playerOpeningCrates.get(player.getUniqueId());
    }

    /**
     * Set the type of key the player is opening a crate for.
     * This is only used in the Cosmic CrateType currently.
     *
     * @param player The player that is opening the crate.
     * @param keyType The KeyType that they are using.
     */
    public void addPlayerKeyType(Player player, KeyType keyType) {
        this.playerKeys.put(player.getUniqueId(), keyType);
    }

    /**
     * Remove the player from the list as they have finished the crate.
     * Currently, only used in the Cosmic CrateType.
     *
     * @param player The player you are removing.
     */
    public void removePlayerKeyType(Player player) {
        this.playerKeys.remove(player.getUniqueId());
    }

    /**
     * Check if the player is in the list.
     *
     * @param player The player you are checking.
     * @return True if they are in the list and false if not.
     */
    public boolean hasPlayerKeyType(Player player) {
        return this.playerKeys.containsKey(player.getUniqueId());
    }

    /**
     * The key type the player's current crate is using.
     *
     * @param player The player that is using the crate.
     * @return The key type of the crate the player is using.
     */
    public KeyType getPlayerKeyType(Player player) {
        return this.playerKeys.get(player.getUniqueId());
    }

    /**
     * Nukes all data.
     */
    public void purge() {
        this.crates.clear();
        this.brokeCrates.clear();
        this.crateLocations.clear();
        this.crateSchematics.clear();
    }

    /**
     * Set a new player's default amount of keys.
     *
     * @param player The player that has just joined.
     */
    public void setNewPlayerKeys(Player player) {
        if (this.giveNewPlayersKeys) { // Checks if any crate gives new players keys and if not then no need to do all this stuff.
            String uuid = player.getUniqueId().toString();

            if (!player.hasPlayedBefore()) {
                this.plugin.getCrateManager().getCrates().stream()
                        .filter(Crate :: doNewPlayersGetKeys)
                        .forEach(crate -> {
                            FileManager.Files.DATA.getFile().set("Players." + uuid + "." + crate.getName(), crate.getNewPlayerKeys());
                            FileManager.Files.DATA.saveFile();
                        });
            }
        }
    }

    /**
     * Adds a crate to the arraylist
     *
     * @param crate object
     */
    public void addCrate(Crate crate) {
        this.crates.add(crate);
    }

    public void addLocation(CrateLocation crateLocation) {
        this.crateLocations.add(crateLocation);
    }

    /**
     * Removes a crate from the arraylist
     *
     * @param crate object
     */
    public void removeCrate(Crate crate) {
        this.crates.remove(crate);
    }

    /**
     * @return true if the arraylist has a crate object otherwise false
     */
    public boolean hasCrate(Crate crate) {
        return this.crates.contains(crate);
    }

    /**
     * Add a new physical crate location.
     *
     * @param location The location you wish to add.
     * @param crate The crate which you would like to set it to.
     */
    public void addCrateLocation(Location location, Crate crate) {
        FileConfiguration locations = Files.LOCATIONS.getFile();
        String id = "1"; // Location ID

        for (int i = 1; locations.contains("Locations." + i); i++) {
            id = (i + 1) + "";
        }

        for (CrateLocation crateLocation : getCrateLocations()) {
            if (crateLocation.getLocation().equals(location)) {
                id = crateLocation.getID();
                break;
            }
        }

        locations.set("Locations." + id + ".Crate", crate.getName());
        locations.set("Locations." + id + ".World", location.getWorld().getName());
        locations.set("Locations." + id + ".X", location.getBlockX());
        locations.set("Locations." + id + ".Y", location.getBlockY());
        locations.set("Locations." + id + ".Z", location.getBlockZ());
        Files.LOCATIONS.saveFile();

        addLocation(new CrateLocation(id, crate, location));

        if (this.holograms != null) this.holograms.createHologram(location.getBlock(), crate);
    }

    /**
     * Remove a physical crate location.
     *
     * @param id The id of the location.
     */
    public void removeCrateLocation(String id) {
        Files.LOCATIONS.getFile().set("Locations." + id, null);
        Files.LOCATIONS.saveFile();
        CrateLocation location = null;

        for (CrateLocation crateLocation : getCrateLocations()) {
            if (crateLocation.getID().equalsIgnoreCase(id)) {
                location = crateLocation;
                break;
            }
        }

        if (location != null) {
            removeLocation(location);

            if (this.holograms != null) this.holograms.removeHologram(location.getLocation().getBlock());
        }
    }

    /**
     * @return An unmodifiable list of crate objects.
     */
    public List<Crate> getCrates() {
        return Collections.unmodifiableList(this.crates);
    }

    /**
     * Gets a crate object using the crate name.
     *
     * @param name of the crate
     * @return crate object
     */
    public Crate getCrateFromName(String name) {
        for (Crate crate : this.crates) {
            if (crate.getName().equalsIgnoreCase(name)) {
                return crate;
            }
        }

        return null;
    }

    /**
     * Checks to see if the location is a physical crate.
     *
     * @param location you are checking.
     * @return true if it is a physical crate and false if not.
     */
    public boolean isCrateLocation(Location location) {
        for (CrateLocation crateLocation : getCrateLocations()) {
            if (crateLocation.getLocation().equals(location)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if an item is a key for a crate.
     *
     * @param item The item you are checking.
     * @return True if the item is a key and false if it is not.
     */
    public boolean isKey(ItemStack item) {
        return getCrateFromKey(item) != null;
    }

    /**
     * Get a Crate from a key ItemStack the player.
     *
     * @param item The key ItemStack you are checking.
     * @return Returns a Crate if is a key from a crate otherwise null if it is not.
     */
    public Crate getCrateFromKey(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            for (Crate crate : getCrates()) {
                if (crate.getCrateType() != CrateType.menu) {
                    if (isKeyFromCrate(item, crate)) {
                        return crate;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Gets the physical crate of the location.
     *
     * @param location you are checking.
     * @return a crate location if the location is a physical crate otherwise null if not.
     */
    public CrateLocation getCrateLocation(Location location) {
        for (CrateLocation crateLocation : this.crateLocations) {
            if (crateLocation.getLocation().equals(location)) {
                return crateLocation;
            }
        }

        return null;
    }

    /**
     * Get a schematic based on its name.
     *
     * @param name The name of the schematic.
     * @return Returns the CrateSchematic otherwise returns null if not found.
     */
    public CrateSchematic getCrateSchematic(String name) {
        for (CrateSchematic schematic : this.crateSchematics) {
            if (schematic.getSchematicName().equalsIgnoreCase(name)) {
                return schematic;
            }
        }

        return null;
    }

    /**
     * Check if an entity is a display reward for a crate.
     *
     * @param entity Entity you wish to check.
     * @return True if it is a display reward item and false if not.
     */
    public boolean isDisplayReward(Entity entity) {
        if (entity instanceof Item) {
            ItemStack item = ((Item) entity).getItemStack();

            if (item.getType() != Material.AIR) {
                return new NBTItem(item).hasTag("crazycrates-item");
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
    public boolean isKeyFromCrate(ItemStack item, Crate crate) {
        if (crate.getCrateType() != CrateType.menu) {
            if (item != null && item.getType() != Material.AIR) {
                return ItemUtils.isSimilar(item, crate);
            }
        }

        return false;
    }

    public HologramHandler getHolograms() {
        return this.holograms;
    }

    /**
     * @return An unmodifiable list of crate locations.
     */
    public List<CrateLocation> getCrateLocations() {
        return Collections.unmodifiableList(this.crateLocations);
    }

    public void removeLocation(CrateLocation crateLocation) {
        this.crateLocations.remove(crateLocation);
    }

    /**
     * @return An unmodifiable list of broke crates.
     */
    public List<String> getBrokeCrates() {
        return Collections.unmodifiableList(this.brokeCrates);
    }

    /**
     * @return An unmodifiable list of broken crate locations.
     */
    public List<BrokeLocation> getBrokeLocations() {
        return Collections.unmodifiableList(this.brokeLocations);
    }

    public void removeBrokeLocation(List<BrokeLocation> crateLocation) {
        this.brokeLocations.removeAll(crateLocation);
    }

    /**
     * @return An unmodifiable list of crate schematics.
     */
    public List<CrateSchematic> getCrateSchematics() {
        return Collections.unmodifiableList(this.crateSchematics);
    }

    // Internal methods.
    private ItemStack getKey(FileConfiguration file) {
        String name = file.getString("Crate.PhysicalKey.Name");
        List<String> lore = file.getStringList("Crate.PhysicalKey.Lore");
        String id = file.getString("Crate.PhysicalKey.Item");
        boolean glowing = false;

        if (file.contains("Crate.PhysicalKey.Glowing")) {
            glowing = file.getBoolean("Crate.PhysicalKey.Glowing");
        }

        return new ItemBuilder().setMaterial(id).setName(name).setLore(lore).setGlow(glowing).build();
    }

    private ItemBuilder getDisplayItem(FileConfiguration file, String prize) {
        String path = "Crate.Prizes." + prize + ".";
        ItemBuilder itemBuilder = new ItemBuilder();

        try {
            itemBuilder.setMaterial(file.getString(path + "DisplayItem"))
                    .setAmount(file.getInt(path + "DisplayAmount", 1))
                    .setName(file.getString(path + "DisplayName"))
                    .setLore(file.getStringList(path + "Lore"))
                    .setGlow(file.getBoolean(path + "Glowing"))
                    .setUnbreakable(file.getBoolean(path + "Unbreakable"))
                    .hideItemFlags(file.getBoolean(path + "HideItemFlags"))
                    .addItemFlags(file.getStringList(path + "Flags"))
                    .addPatterns(file.getStringList(path + "Patterns"))
                    .setPlayerName(file.getString(path + "Player"));

            if (file.contains(path + "DisplayDamage") && file.getInt(path + "DisplayDamage") >= 1) {
                itemBuilder.setDamage(file.getInt(path + "DisplayDamage"));
            }

            if (file.contains(path + "DisplayTrim.Pattern")) {
                itemBuilder.setTrimPattern(Registry.TRIM_PATTERN.get(NamespacedKey.minecraft(file.getString(path + "DisplayTrim.Pattern").toLowerCase())));
            }

            if (file.contains(path + "DisplayTrim.Material")) {
                itemBuilder.setTrimMaterial(Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft(file.getString(path + "DisplayTrim.Material").toLowerCase())));
            }

            if (file.contains(path + "DisplayEnchantments")) {
                for (String enchantmentName : file.getStringList(path + "DisplayEnchantments")) {
                    Enchantment enchantment = MiscUtils.getEnchantment(enchantmentName.split(":")[0]);

                    if (enchantment != null) {
                        itemBuilder.addEnchantments(enchantment, Integer.parseInt(enchantmentName.split(":")[1]));
                    }
                }
            }

            return itemBuilder;
        } catch (Exception e) {
            return new ItemBuilder().setMaterial(Material.RED_TERRACOTTA).setName("&c&lERROR").setLore(Arrays.asList("&cThere is an error", "&cFor the reward: &c" + prize));
        }
    }

    // Cleans the data file.
    private void cleanDataFile() {
        FileConfiguration data = FileManager.Files.DATA.getFile();

        if (!data.contains("Players")) return;

        if (this.plugin.isLogging()) this.plugin.getLogger().info("Cleaning up the data.yml file.");

        List<String> removePlayers = new ArrayList<>();

        for (String uuid : data.getConfigurationSection("Players").getKeys(false)) {

            if (data.contains("Players." + uuid + ".tracking")) return;

            boolean hasKeys = false;
            List<String> noKeys = new ArrayList<>();

            for (Crate crate : getCrates()) {
                if (data.getInt("Players." + uuid + "." + crate.getName()) <= 0) {
                    noKeys.add(crate.getName());
                } else {
                    hasKeys = true;
                }
            }

            if (hasKeys) {
                noKeys.forEach(crate -> data.set("Players." + uuid + "." + crate, null));
            } else {
                removePlayers.add(uuid);
            }
        }

        if (!removePlayers.isEmpty()) {
            if (this.plugin.isLogging()) this.plugin.getLogger().info(removePlayers.size() + " player's data has been marked to be removed.");

            removePlayers.forEach(uuid -> data.set("Players." + uuid, null));

            if (this.plugin.isLogging()) this.plugin.getLogger().info("All empty player data has been removed.");
        }

        if (this.plugin.isLogging()) this.plugin.getLogger().info("The data.yml file has been cleaned.");
        FileManager.Files.DATA.saveFile();
    }

    private List<ItemBuilder> getItems(FileConfiguration file, String prize) {
        return ItemBuilder.convertStringList(file.getStringList("Crate.Prizes." + prize + ".Items"), prize);
    }
}