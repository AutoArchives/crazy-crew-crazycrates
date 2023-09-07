package us.crazycrew.crazycrates.paper.api;

import com.Zrips.CMI.Modules.ModuleHandling.CMIModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import us.crazycrew.crazycrates.common.config.PluginConfig;
import us.crazycrew.crazycrates.paper.api.enums.settings.Messages;
import us.crazycrew.crazycrates.paper.api.frame.BukkitUserManager;
import us.crazycrew.crazycrates.paper.api.objects.*;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;
import us.crazycrew.crazycrates.paper.support.holograms.CMIHologramsSupport;
import us.crazycrew.crazycrates.paper.support.holograms.HolographicDisplaysSupport;
import us.crazycrew.crazycrates.paper.support.libraries.PluginSupport;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.enums.BrokeLocation;
import us.crazycrew.crazycrates.paper.api.events.PlayerReceiveKeyEvent;
import us.crazycrew.crazycrates.paper.api.events.PlayerReceiveKeyEvent.KeyReceiveReason;
import us.crazycrew.crazycrates.paper.api.interfaces.HologramController;
import us.crazycrew.crazycrates.paper.api.managers.QuadCrateManager;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import us.crazycrew.crazycrates.paper.listeners.CrateControlListener;
import us.crazycrew.crazycrates.common.crates.CrateHologram;
import us.crazycrew.crazycrates.common.crates.quadcrates.CrateSchematic;
import us.crazycrew.crazycrates.paper.support.holograms.DecentHologramsSupport;
import us.crazycrew.crazycrates.paper.support.structures.StructureHandler;
import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import de.tr7zw.changeme.nbtapi.NBTItem;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import static java.util.regex.Matcher.quoteReplacement;

public class CrazyManager {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    
    private final @NotNull CrazyCratesLoader cratesLoader = this.plugin.getCratesLoader();
    private final @NotNull FileManager fileManager = this.cratesLoader.getFileManager();
    private final @NotNull BukkitUserManager userManager = this.cratesLoader.getUserManager();
    private final @NotNull Methods methods = this.cratesLoader.getMethods();

    // All the crates that have been loaded.
    private final ArrayList<Crate> crates = new ArrayList<>();

    // A list of all the physical crate locations.
    private final ArrayList<CrateLocation> crateLocations = new ArrayList<>();

    // List of all the broken crates.
    private final ArrayList<String> brokecrates = new ArrayList<>();

    // List of broken physical crate locations.
    private final List<BrokeLocation> brokeLocations = new ArrayList<>();

    // The crate that the player is opening.
    private final HashMap<UUID, Crate> playerOpeningCrates = new HashMap<>();

    // Keys that are being used in crates. Only needed in cosmic due to it taking the key after the player picks a prize and not in a start method.
    private final HashMap<UUID, KeyType> playerKeys = new HashMap<>();

    // A list of all current crate tasks that are running that a time. Used to force stop any crates it needs to.
    private final HashMap<UUID, BukkitTask> currentTasks = new HashMap<>();

    // A list of tasks being run by the QuadCrate type.
    private final HashMap<UUID, ArrayList<BukkitTask>> currentQuadTasks = new HashMap<>();

    // The time in seconds a quadcrate can go until afk kicks them from it.
    private int quadCrateTimer;

    // A list of current crate schematics for Quad Crate.
    private final List<CrateSchematic> crateSchematics = new ArrayList<>();

    // True if at least one crate gives new players keys and false if none give new players keys.
    private boolean giveNewPlayersKeys;

    // The hologram api that is being hooked into.
    private HologramController hologramController;

    // Schematic locations for 1.13+.
    private final HashMap<UUID, Location[]> schemLocations = new HashMap<>();

    public void load(boolean serverStart) {
        if (serverStart) {

        }

        //TODO() Re-work how this functions.
        loadCrates();
    }

    public void reload(boolean serverStop) {
        if (serverStop) {
            this.cratesLoader.getMetrics().stop();
            return;
        }

        this.cratesLoader.getConfigManager().reload();

        boolean metrics = this.cratesLoader.getConfigManager().getPluginConfig().getProperty(PluginConfig.TOGGLE_METRICS);

        if (metrics) {
            this.cratesLoader.getMetrics().start();
        } else {
            this.cratesLoader.getMetrics().stop();
        }

        loadCrates();
    }

    // Loads all the information the plugin needs to run.
    private void loadCrates() {
        giveNewPlayersKeys = false;
        crates.clear();
        brokecrates.clear();
        crateLocations.clear();
        crateSchematics.clear();

        quadCrateTimer = FileManager.Files.CONFIG.getFile().getInt("Settings.QuadCrate.Timer") * 20;

        // Removes all holograms so that they can be replaced.
        if (hologramController != null) hologramController.removeAllHolograms();

        if (PluginSupport.DECENT_HOLOGRAMS.isPluginEnabled()) {
            hologramController = new DecentHologramsSupport();
            FancyLogger.info("DecentHolograms support has been enabled.");
        } else if (PluginSupport.CMI.isPluginEnabled() && CMIModule.holograms.isEnabled()) {
            hologramController = new CMIHologramsSupport();
            FancyLogger.info("CMI Hologram support has been enabled.");
        } else if (PluginSupport.HOLOGRAPHIC_DISPLAYS.isPluginEnabled()) {
            hologramController = new HolographicDisplaysSupport();
            FancyLogger.info("Holographic Displays support has been enabled.");
        } else FancyLogger.warn("No holograms plugin were found. If using CMI, make sure holograms module is enabled.");

        if (fileManager.isLogging()) FancyLogger.info("Loading all crate information...");

        for (String crateName : fileManager.getAllCratesNames(plugin)) {
            try {
                FileConfiguration file = fileManager.getFile(crateName).getFile();
                CrateType crateType = CrateType.getFromName(file.getString("Crate.CrateType"));
                ArrayList<Prize> prizes = new ArrayList<>();
                String previewName = file.contains("Crate.Preview-Name") ? file.getString("Crate.Preview-Name") : file.getString("Crate.Name");
                ArrayList<Tier> tiers = new ArrayList<>();
                int maxMassOpen =  file.contains("Crate.Max-Mass-Open") ? file.getInt("Crate.Max-Mass-Open") : 10;
                int requiredKeys = file.contains("Crate.RequiredKeys") ? file.getInt("Crate.RequiredKeys") : 0;

                if (file.contains("Crate.Tiers") && file.getConfigurationSection("Crate.Tiers") != null) {
                    for (String tier : file.getConfigurationSection("Crate.Tiers").getKeys(false)) {
                        String path = "Crate.Tiers." + tier;
                        tiers.add(new Tier(tier, file.getString(path + ".Name"), file.getString(path + ".Color"), file.getInt(path + ".Chance"), file.getInt(path + ".MaxRange")));
                    }
                }

                if (crateType == CrateType.COSMIC && tiers.isEmpty()) {
                    brokecrates.add(crateName);
                    FancyLogger.warn("No tiers were found for this cosmic crate " + crateName + ".yml file.");
                    continue;
                }

                for (String prize : file.getConfigurationSection("Crate.Prizes").getKeys(false)) {
                    Prize altPrize = null;
                    String path = "Crate.Prizes." + prize;
                    ArrayList<Tier> prizeTiers = new ArrayList<>();

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

                if (!giveNewPlayersKeys) {
                    if (newPlayersKeys > 0) giveNewPlayersKeys = true;
                }

                List<String> prizeMessage = file.contains("Crate.Prize-Message") ? file.getStringList("Crate.Prize-Message") : Collections.emptyList();

                CrateHologram holo = new CrateHologram(file.getBoolean("Crate.Hologram.Toggle"), file.getDouble("Crate.Hologram.Height", 0.0), file.getStringList("Crate.Hologram.Message"));
                crates.add(new Crate(crateName, previewName, crateType, getKey(file), prizes, file, newPlayersKeys, tiers, maxMassOpen, requiredKeys, prizeMessage, holo));
            } catch (Exception e) {
                brokecrates.add(crateName);

                FancyLogger.error("There was an error while loading the " + crateName + ".yml file.");
                FancyLogger.debug(e.getMessage());
            }
        }

        crates.add(new Crate("Menu", "Menu", CrateType.MENU, new ItemStack(Material.AIR), new ArrayList<>(), null, 0, null, 0, 0, Collections.emptyList(), null));

        if (fileManager.isLogging()) {
            FancyLogger.info("All crate information has been loaded.");
            FancyLogger.info("Loading all the physical crate locations.");
        }

        FileConfiguration locations = FileManager.Files.LOCATIONS.getFile();
        int loadedAmount = 0;
        int brokeAmount = 0;

        if (locations.getConfigurationSection("Locations") != null) {
            for (String locationName : locations.getConfigurationSection("Locations").getKeys(false)) {
                try {
                    String worldName = locations.getString("Locations." + locationName + ".World");
                    World world = plugin.getServer().getWorld(worldName);
                    int x = locations.getInt("Locations." + locationName + ".X");
                    int y = locations.getInt("Locations." + locationName + ".Y");
                    int z = locations.getInt("Locations." + locationName + ".Z");
                    Location location = new Location(world, x, y, z);
                    Crate crate = getCrateFromName(locations.getString("Locations." + locationName + ".Crate"));

                    if (world != null && crate != null) {
                        crateLocations.add(new CrateLocation(locationName, crate, location));

                        if (hologramController != null) {
                            hologramController.createHologram(location.getBlock(), crate);
                        }

                        loadedAmount++;
                    } else {
                        brokeLocations.add(new BrokeLocation(locationName, crate, x, y, z, worldName));
                        brokeAmount++;
                    }
                } catch (Exception e) {
                    FancyLogger.error("Failed to create crate locations.");
                    FancyLogger.debug(e.getMessage());
                }
            }
        }

        // Checking if all physical locations loaded
        if (fileManager.isLogging()) {
            if (loadedAmount > 0 || brokeAmount > 0) {
                if (brokeAmount <= 0) {
                    FancyLogger.success("All physical crate locations have been loaded.");
                } else {
                    FancyLogger.info("Loaded " + loadedAmount + " physical crate locations.");
                    FancyLogger.error("Failed to load " + brokeAmount + " physical crate locations.");
                }
            }
        }

        // Loading schematic files
        if (fileManager.isLogging()) FancyLogger.info("Searching for schematics to load.");

        String[] schems = new File(plugin.getDataFolder() + "/schematics/").list();

        assert schems != null;
        for (String schematicName : schems) {
            if (schematicName.endsWith(".nbt")) {
                crateSchematics.add(new CrateSchematic(schematicName.replace(".nbt", ""), new File(plugin.getDataFolder() + "/schematics/" + schematicName)));

                if (fileManager.isLogging()) FancyLogger.success(schematicName + " was successfully found and loaded.");
            }
        }

        if (fileManager.isLogging()) FancyLogger.success("All schematics were found and loaded.");

        cleanDataFile();
    }

    // This method is deigned to help clean the data.yml file of any useless info that it may have.
    public void cleanDataFile() {
        FileConfiguration data = FileManager.Files.DATA.getFile();

        if (data.contains("Players")) {
            boolean logging = fileManager.isLogging();

            if (logging) FancyLogger.info("Cleaning up the data.yml file.");

            List<String> removePlayers = new ArrayList<>();

            for (String uuid : data.getConfigurationSection("Players").getKeys(false)) {
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
                if (logging) FancyLogger.warn(removePlayers.size() + " player's data has been marked to be removed.");

                removePlayers.forEach(uuid -> data.set("Players." + uuid, null));

                if (logging) FancyLogger.success("All empty player data has been removed.");
            }

            if (logging) FancyLogger.success("The data.yml file has been cleaned.");
            
            FileManager.Files.DATA.saveFile();
        }
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
        UUID uuid = player.getUniqueId();

        if (crate.getCrateType() != CrateType.MENU) {
            if (!crate.canWinPrizes(player)) {
                player.sendMessage(Messages.NO_PRIZES_FOUND.getMessage());
                removePlayerFromOpeningList(uuid);
                removePlayerKeyType(uuid);
                return;
            }
        }

        addPlayerToOpeningList(uuid, crate);

        if (crate.getFile() != null) this.methods.broadCastMessage(crate.getFile(), player);

        FileConfiguration config = FileManager.Files.CONFIG.getFile();

        switch (crate.getCrateType()) {
            case MENU -> {
                boolean openMenu = config.getBoolean("Settings.Enable-Crate-Menu");

                if (openMenu) this.cratesLoader.getMenuManager().openMainMenu(player);
                else player.sendMessage(Messages.FEATURE_DISABLED.getMessage());
            }
            case COSMIC -> this.plugin.getCosmic().openCosmic(player, crate, keyType, checkHand);
            case CSGO -> this.plugin.getCsgo().openCSGO(player, crate, keyType, checkHand);
            case ROULETTE -> this.plugin.getRoulette().openRoulette(player, crate, keyType, checkHand);
            case WHEEL -> this.plugin.getWheel().startWheel(player, crate, keyType, checkHand);
            case WONDER -> this.plugin.getWonder().startWonder(player, crate, keyType, checkHand);
            case WAR -> this.plugin.getWar().openWarCrate(player, crate, keyType, checkHand);
            case QUAD_CRATE -> {
                Location lastLocation = player.getLocation();
                lastLocation.setPitch(0F);
                CrateSchematic crateSchematic = getCrateSchematics().get(new Random().nextInt(getCrateSchematics().size()));
                StructureHandler handler = new StructureHandler(crateSchematic.schematicFile());
                CrateLocation crateLocation = getCrateLocation(location);
                QuadCrateManager session = new QuadCrateManager(player, crate, keyType, crateLocation.getLocation(), lastLocation, checkHand, handler);

                session.startCrate();
            }
            case FIRE_CRACKER -> {
                if (CrateControlListener.inUse.containsValue(location)) {
                    player.sendMessage(Messages.QUICK_CRATE_IN_USE.getMessage());
                    removePlayerFromOpeningList(uuid);
                    return;
                } else {
                    if (virtualCrate) {
                        player.sendMessage(Messages.CANT_BE_A_VIRTUAL_CRATE.getMessage());
                        removePlayerFromOpeningList(uuid);
                        return;
                    } else {
                        CrateControlListener.inUse.put(uuid, location);
                        this.plugin.getFireCracker().startFireCracker(player, crate, keyType, location, hologramController);
                    }
                }
            }
            case QUICK_CRATE -> {
                if (CrateControlListener.inUse.containsValue(location)) {
                    player.sendMessage(Messages.QUICK_CRATE_IN_USE.getMessage());
                    removePlayerFromOpeningList(uuid);
                    return;
                } else {
                    if (virtualCrate && location.equals(player.getLocation())) {
                        player.sendMessage(Messages.CANT_BE_A_VIRTUAL_CRATE.getMessage());
                        removePlayerFromOpeningList(uuid);
                        return;
                    } else {
                        CrateControlListener.inUse.put(uuid, location);
                        this.plugin.getQuickCrate().openCrate(player, location, crate, keyType, hologramController);
                    }
                }
            }
            case CRATE_ON_THE_GO -> {
                if (virtualCrate) {
                    player.sendMessage(Messages.CANT_BE_A_VIRTUAL_CRATE.getMessage());
                    removePlayerFromOpeningList(uuid);
                    return;
                } else {
                    if (this.userManager.takeKeys(1, player.getUniqueId(), crate.getName(), keyType, true)) {
                        Prize prize = crate.pickPrize(player);
                        givePrize(player, prize, crate);

                        if (prize.useFireworks()) this.methods.firework(player.getLocation().add(0, 1, 0));

                        removePlayerFromOpeningList(uuid);
                    } else {
                        this.methods.failedToTakeKey(player.getName(), crate);
                    }
                }
            }
        }

        boolean logFile = FileManager.Files.CONFIG.getFile().getBoolean("Settings.Crate-Actions.Log-File");
        boolean logConsole = FileManager.Files.CONFIG.getFile().getBoolean("Settings.Crate-Actions.Log-Console");

        this.cratesLoader.getEventLogger().logCrateEvent(player, crate, keyType, logFile, logConsole);
    }

    /**
     * This forces a crate to end and will not give out a prize. This is meant for people who leave the server to stop any errors or lag from happening.
     *
     * @param uuid The uuid of the player that the crate is being ended for.
     */
    public void endCrate(UUID uuid) {
        if (currentTasks.containsKey(uuid)) {
            currentTasks.get(uuid).cancel();
            removeCrateTask(uuid);
        }
    }

    /**
     * Ends the tasks running by a player.
     *
     * @param uuid The uuid of the player using the crate.
     */
    public void endQuadCrate(UUID uuid) {
        if (currentQuadTasks.containsKey(uuid)) {
            for (BukkitTask task : currentQuadTasks.get(uuid)) {
                task.cancel();
            }

            currentQuadTasks.remove(uuid);
        }
    }

    /**
     * Add a quad crate task that is going on for a player.
     *
     * @param uuid The uuid of the player opening the crate.
     * @param task The task of the quad crate.
     */
    public void addQuadCrateTask(UUID uuid, BukkitTask task) {
        if (!currentQuadTasks.containsKey(uuid)) {
            currentQuadTasks.put(uuid, new ArrayList<>());
        }

        currentQuadTasks.get(uuid).add(task);
    }

    /**
     * Checks to see if the player has a quad crate task going on.
     *
     * @param uuid The uuid of the player that is being checked.
     * @return True if they do have a task and false if not.
     */
    public boolean hasQuadCrateTask(UUID uuid) {
        return currentQuadTasks.containsKey(uuid);
    }

    /**
     * Add a crate task that is going on for a player.
     *
     * @param uuid The uuid of the player opening the crate.
     * @param task The task of the crate.
     */
    public void addCrateTask(UUID uuid, BukkitTask task) {
        currentTasks.put(uuid, task);
    }

    /**
     * Remove a task from the list of current tasks.
     *
     * @param uuid The uuid of the player using the crate.
     */
    public void removeCrateTask(UUID uuid) {
        currentTasks.remove(uuid);
    }

    /**
     * Checks to see if the player has a crate task going on.
     *
     * @param uuid The uuid of the player that is being checked.
     * @return True if they do have a task and false if not.
     */
    public boolean hasCrateTask(UUID uuid) {
        return currentTasks.containsKey(uuid);
    }

    /**
     * A list of all the physical crate locations.
     *
     * @return List of locations.
     */
    public List<CrateLocation> getCrateLocations() {
        return Collections.unmodifiableList(this.crateLocations);
    }

    public void addCrateLocation(CrateLocation crateLocation) {
        if (this.crateLocations.contains(crateLocation)) return;

        this.crateLocations.add(crateLocation);
    }

    /**
     * Checks to see if the location is a physical crate.
     *
     * @param loc The location you are checking.
     * @return True if it is a physical crate and false if not.
     */
    public boolean isCrateLocation(Location loc) {
        for (CrateLocation crateLocation : getCrateLocations()) {
            if (crateLocation.getLocation().equals(loc)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the physical crate of the location.
     *
     * @param loc The location you are checking.
     * @return A CrateLocation if the location is a physical crate otherwise null if not.
     */
    public CrateLocation getCrateLocation(Location loc) {
        for (CrateLocation crateLocation : getCrateLocations()) {
            if (crateLocation.getLocation().equals(loc)) {
                return crateLocation;
            }
        }

        return null;
    }

    /**
     * Get a list of all the broke physical crate locations.
     *
     * @return List of broken crate locations.
     */
    public List<BrokeLocation> getBrokeCrateLocations() {
        return Collections.unmodifiableList(this.brokeLocations);
    }

    public void removeBrokeCrateLocation(BrokeLocation location) {
        if (!this.brokeLocations.contains(location)) return;

        this.brokeLocations.remove(location);
    }

    /**
     * Add a new physical crate location.
     *
     * @param location The location you wish to add.
     * @param crate The crate which you would like to set it to.
     */
    public void addCrateLocation(Location location, Crate crate) {
        FileConfiguration locations = FileManager.Files.LOCATIONS.getFile();
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
        FileManager.Files.LOCATIONS.saveFile();

        crateLocations.add(new CrateLocation(id, crate, location));

        if (hologramController != null) hologramController.createHologram(location.getBlock(), crate);
    }

    /**
     * Remove a physical crate location.
     *
     * @param id The id of the location.
     */
    public void removeCrateLocation(String id) {
        FileManager.Files.LOCATIONS.getFile().set("Locations." + id, null);
        FileManager.Files.LOCATIONS.saveFile();
        CrateLocation location = null;

        for (CrateLocation crateLocation : getCrateLocations()) {
            if (crateLocation.getID().equalsIgnoreCase(id)) {
                location = crateLocation;
                break;
            }
        }

        if (location != null) {
            crateLocations.remove(location);

            if (hologramController != null) hologramController.removeHologram(location.getLocation().getBlock());
        }
    }

    /**
     * Get a list of broken crates.
     *
     * @return An ArrayList of all the broken crates.
     */
    public List<String> getBrokeCrates() {
        return Collections.unmodifiableList(this.brokecrates);
    }

    /**
     * Get a list of all the crates loaded into the plugin.
     *
     * @return An ArrayList of all the loaded crates.
     */
    public List<Crate> getCrates() {
        return Collections.unmodifiableList(this.crates);
    }

    /**
     * Get a crate by its name.
     *
     * @param name The name of the crate you wish to grab.
     * @return Returns a Crate object of the crate it found and if none are found it returns null.
     */
    public Crate getCrateFromName(String name) {
        for (Crate crate : getCrates()) {
            if (crate.getName().equalsIgnoreCase(name)) {
                return crate;
            }
        }

        return null;
    }

    /**
     * The time in seconds a quadcrate will last before kicking the player.
     *
     * @return The time in seconds till kick.
     */
    public int getQuadCrateTimer() {
        return quadCrateTimer;
    }

    /**
     * Give a player a prize they have won.
     *
     * @param player The player you wish to give the prize to.
     * @param prize The prize the player has won.
     */
    public void givePrize(Player player, Prize prize, Crate crate) {
        if (prize != null) {
            prize = prize.hasBlacklistPermission(player) ? prize.getAltPrize() : prize;

            for (ItemStack item : prize.getItems()) {

                if (item == null) {
                    HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("%Crate%", prize.getCrate());
                    placeholders.put("%Prize%", prize.getName());
                    player.sendMessage(Messages.PRIZE_ERROR.getMessage(placeholders));
                    continue;
                }

                if (!this.methods.isInventoryFull(player)) {
                    player.getInventory().addItem(item);
                } else {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                }
            }

            for (ItemBuilder item : prize.getItemBuilders()) {
                ItemBuilder clone = new ItemBuilder(item);

                if (PluginSupport.PLACEHOLDERAPI.isPluginEnabled()) {
                    clone.setName(PlaceholderAPI.setPlaceholders(player, clone.getName()));
                    clone.setLore(PlaceholderAPI.setPlaceholders(player, clone.getLore()));
                }

                if (!this.methods.isInventoryFull(player)) {
                    player.getInventory().addItem(clone.build());
                } else {
                    player.getWorld().dropItemNaturally(player.getLocation(), clone.build());
                }
            }

            for (String command : prize.getCommands()) { // /give %player% iron %random%:1-64
                if (command.contains("%random%:")) {
                    String cmd = command;
                    StringBuilder commandBuilder = new StringBuilder();

                    for (String word : cmd.split(" ")) {
                        if (word.startsWith("%random%:")) {
                            word = word.replace("%random%:", "");

                            try {
                                long min = Long.parseLong(word.split("-")[0]);
                                long max = Long.parseLong(word.split("-")[1]);
                                commandBuilder.append(pickNumber(min, max)).append(" ");
                            } catch (Exception e) {
                                commandBuilder.append("1 ");
                                FancyLogger.error("The prize " + prize.getName() + " in the " + prize.getCrate() + " crate has caused an error when trying to run a command.");
                                FancyLogger.debug("Command: " + cmd);
                            }
                        } else {
                            commandBuilder.append(word).append(" ");
                        }
                    }

                    command = commandBuilder.toString();
                    command = command.substring(0, command.length() - 1);
                }

                if (PluginSupport.PLACEHOLDERAPI.isPluginEnabled()) command = PlaceholderAPI.setPlaceholders(player, command);

                this.methods.sendCommand(command.replaceAll("%player%", player.getName()).replaceAll("%Player%", player.getName()).replaceAll("%reward%", quoteReplacement(prize.getDisplayItemBuilder().getUpdatedName())).replaceAll("%crate%", crate.getCrateInventoryName()));
            }

            if (!crate.getPrizeMessage().isEmpty() && prize.getMessages().isEmpty()) {
                for (String message : crate.getPrizeMessage()) {
                    if (PluginSupport.PLACEHOLDERAPI.isPluginEnabled()) {
                        message = PlaceholderAPI.setPlaceholders(player, message);
                    }

                    this.methods.sendMessage(player, message.replaceAll("%player%", player.getName()).replaceAll("%Player%", player.getName()).replaceAll("%reward%", quoteReplacement(prize.getDisplayItemBuilder().getName())).replaceAll("%crate%", crate.getCrateInventoryName()), false);
                }

                return;
            }

            for (String message : prize.getMessages()) {
                if (PluginSupport.PLACEHOLDERAPI.isPluginEnabled()) {
                    message = PlaceholderAPI.setPlaceholders(player, message);
                }

                this.methods.sendMessage(player, message.replaceAll("%player%", player.getName()).replaceAll("%Player%", player.getName()).replaceAll("%reward%", quoteReplacement(prize.getDisplayItemBuilder().getName())).replaceAll("%crate%", crate.getCrateInventoryName()), false);
            }
        } else {
            FancyLogger.warn("No prize was found when giving " + player.getName() + " a prize.");
        }
    }

    /**
     * Load the offline keys of a player who has come online.
     *
     * @param player The player which you would like to load the offline keys for.
     */
    public void loadOfflinePlayersKeys(Player player) {
        FileConfiguration data = FileManager.Files.DATA.getFile();

        UUID uuid = player.getUniqueId();

        String name = player.getName().toLowerCase();

        if (data.contains("Offline-Players." + name)) {
            for (Crate crate : getCrates()) {
                if (data.contains("Offline-Players." + name + "." + crate.getName())) {
                    PlayerReceiveKeyEvent event = new PlayerReceiveKeyEvent(uuid, crate, KeyReceiveReason.OFFLINE_PLAYER, 1);
                    plugin.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        this.userManager.addVirtualKeys(data.getInt("Offline-Players." + name + "." + crate.getName()), uuid, crate.getName());
                    }
                }
            }

            data.set("Offline-Players." + name, null);
            FileManager.Files.DATA.saveFile();
        }
    }

    /**
     * Add a player to the list of players that are currently opening crates.
     *
     * @param uuid The uuid of the player that is opening a crate.
     * @param crate The crate the player is opening.
     */
    public void addPlayerToOpeningList(UUID uuid, Crate crate) {
        playerOpeningCrates.put(uuid, crate);
    }

    /**
     * Remove a player from the list of players that are opening crates.
     *
     * @param uuid The uuid of the player that has finished opening a crate.
     */
    public void removePlayerFromOpeningList(UUID uuid) {
        playerOpeningCrates.remove(uuid);
    }

    /**
     * Check if a player is opening a crate.
     *
     * @param uuid The uuid of the player you are checking.
     * @return True if they are opening a crate and false if they are not.
     */
    public boolean isInOpeningList(UUID uuid) {
        return playerOpeningCrates.containsKey(uuid);
    }

    /**
     * Get the crate the player is currently opening.
     *
     * @param uuid The uuid of the player you want to check.
     * @return The Crate of which the player is opening. May return null if no crate found.
     */
    public Crate getOpeningCrate(UUID uuid) {
        return playerOpeningCrates.get(uuid);
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
                if (crate.getCrateType() != CrateType.MENU) {
                    if (isKeyFromCrate(item, crate)) {
                        return crate;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Check if a key is from a specific Crate.
     *
     * @param item The key ItemStack you are checking.
     * @param crate The Crate you are checking.
     * @return Returns true if it belongs to that Crate and false if it does not.
     */
    public boolean isKeyFromCrate(ItemStack item, Crate crate) {
        if (crate.getCrateType() != CrateType.MENU) {
            if (item != null && item.getType() != Material.AIR) {
                return this.methods.isSimilar(item, crate);
            }
        }

        return false;
    }

    /**
     * Set the type of key the player is opening a crate for.
     * This is only used in the Cosmic CrateType currently.
     *
     * @param uuid The uuid of the player that is opening the crate.
     * @param keyType The KeyType that they are using.
     */
    public void addPlayerKeyType(UUID uuid, KeyType keyType) {
        playerKeys.put(uuid, keyType);
    }

    /**
     * Remove the player from the list as they have finished the crate.
     * Currently, only used in the Cosmic CrateType.
     *
     * @param uuid The uuid of the player you are removing.
     */
    public void removePlayerKeyType(UUID uuid) {
        playerKeys.remove(uuid);
    }

    /**
     * Check if the player is in the list.
     *
     * @param uuid The uuid of the player you are checking.
     * @return True if they are in the list and false if not.
     */
    public boolean hasPlayerKeyType(UUID uuid) {
        return playerKeys.containsKey(uuid);
    }

    /**
     * The key type the player's current crate is using.
     *
     * @param uuid The uuid of the player that is using the crate.
     * @return The key type of the crate the player is using.
     */
    public KeyType getPlayerKeyType(UUID uuid) {
        return playerKeys.get(uuid);
    }

    /**
     * Get the amount of virtual keys a player has.
     *
     * @param uuid The uuid of the player you are checking.
     * @return The amount of virtual keys they own.
     */
    public Map<Crate, Integer> getVirtualKeys(UUID uuid) {
        HashMap<Crate, Integer> keys = new HashMap<>();

        for (Crate crate : getCrates()) {
            keys.put(crate, this.userManager.getVirtualKeys(uuid, crate.getName()));
        }

        return Collections.unmodifiableMap(keys);
    }

    /**
     * Get the locations a player sets for when creating a new schematic.
     *
     * @return The list of locations set by players.
     */
    public HashMap<UUID, Location[]> getSchematicLocations() {
        return schemLocations;
    }

    /**
     * Set a new player's default amount of keys.
     *
     * @param player the player that has just joined.
     */
    public void setNewPlayerKeys(Player player) {
        if (giveNewPlayersKeys) { // Checks if any crate gives new players keys and if not then no need to do all this stuff.
            UUID uuid = player.getUniqueId();

            if (!player.hasPlayedBefore()) {
                crates.stream()
                .filter(Crate::doNewPlayersGetKeys)
                .forEach(crate -> {
                    FileManager.Files.DATA.getFile().set("Players." + uuid + "." + crate.getName(), crate.getNewPlayerKeys());
                    FileManager.Files.DATA.saveFile();
                });
            }
        }
    }

    /**
     * Get the hologram plugin settings that is being used.
     *
     * @return The hologram controller for the holograms.
     */
    public HologramController getHologramController() {
        return hologramController;
    }

    /**
     * Load all the schematics inside the Schematics folder.
     */
    public void loadSchematics() {
        crateSchematics.clear();
        String[] schems = new File(plugin.getDataFolder() + "/schematics/").list();

        assert schems != null;
        for (String schematicName : schems) {
            if (schematicName.endsWith(".nbt")) {
                crateSchematics.add(new CrateSchematic(schematicName.replace(".nbt", ""), new File(plugin.getDataFolder() + "/schematics/" + schematicName)));
            }
        }
    }

    /**
     * Get the list of all the schematics currently loaded onto the server.
     *
     * @return The list of all loaded schematics.
     */
    public List<CrateSchematic> getCrateSchematics() {
        return crateSchematics;
    }

    /**
     * Get a schematic based on its name.
     *
     * @param name The name of the schematic.
     * @return Returns the CrateSchematic otherwise returns null if not found.
     */
    public CrateSchematic getCrateSchematic(String name) {
        for (CrateSchematic schematic : crateSchematics) {
            if (schematic.schematicName().equalsIgnoreCase(name)) {
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
                return new NBTItem(item).hasKey("crazycrates-item");
            }
        }

        return false;
    }

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
                    Enchantment enchantment = this.methods.getEnchantment(enchantmentName.split(":")[0]);

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

    private List<ItemBuilder> getItems(FileConfiguration file, String prize) {
        return ItemBuilder.convertStringList(file.getStringList("Crate.Prizes." + prize + ".Items"), prize);
    }

    public long pickNumber(long min, long max) {
        max++;

        try {
            // new Random() does not have a nextLong(long bound) method.
            return min + ThreadLocalRandom.current().nextLong(max - min);
        } catch (IllegalArgumentException e) {
            return min;
        }
    }
}