package us.crazycrew.crazycrates.paper.api;

import ch.jalu.configme.SettingsManager;
import com.Zrips.CMI.Modules.ModuleHandling.CMIModule;
import com.ryderbelserion.cluster.api.utils.ColorUtils;
import com.ryderbelserion.cluster.bukkit.utils.LegacyUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import us.crazycrew.crazycrates.common.config.ConfigManager;
import us.crazycrew.crazycrates.common.config.types.Config;
import us.crazycrew.crazycrates.common.config.types.PluginConfig;
import us.crazycrew.crazycrates.common.config.types.menus.CrateMainMenu;
import us.crazycrew.crazycrates.paper.api.enums.Translation;
import us.crazycrew.crazycrates.paper.api.plugin.frame.BukkitUserManager;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.objects.CrateLocation;
import us.crazycrew.crazycrates.paper.api.objects.ItemBuilder;
import us.crazycrew.crazycrates.paper.api.objects.Prize;
import us.crazycrew.crazycrates.paper.api.objects.Tier;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyHandler;
import us.crazycrew.crazycrates.paper.support.MetricsHandler;
import us.crazycrew.crazycrates.paper.support.holograms.CMIHologramsSupport;
import us.crazycrew.crazycrates.paper.support.holograms.HolographicDisplaysSupport;
import us.crazycrew.crazycrates.paper.support.libraries.PluginSupport;
import us.crazycrew.crazycrates.paper.api.FileManager.Files;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.enums.BrokeLocation;
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
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import static java.util.regex.Matcher.quoteReplacement;

public class CrazyManager {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    
    private final @NotNull CrazyHandler crazyHandler = this.plugin.getCrazyHandler();
    private final @NotNull FileManager fileManager = this.crazyHandler.getFileManager();
    private final @NotNull BukkitUserManager userManager = this.crazyHandler.getUserManager();
    private final @NotNull Methods methods = this.crazyHandler.getMethods();

    private final @NotNull ConfigManager configManager = this.crazyHandler.getConfigManager();
    private final @NotNull SettingsManager config = this.configManager.getConfig();
    private final @NotNull SettingsManager menuConfig = this.configManager.getMainMenuConfig();
    private final @NotNull SettingsManager pluginConfig = this.configManager.getPluginConfig();

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

    // A list of current crate schematics for Quad Crate.
    private final List<CrateSchematic> crateSchematics = new ArrayList<>();

    // True if at least one crate gives new players keys and false if none give new players keys.
    private boolean giveNewPlayersKeys;

    // The hologram api that is being hooked into.
    private HologramController hologramController;

    // Schematic locations for 1.13+.
    private final HashMap<UUID, Location[]> schemLocations = new HashMap<>();

    private final boolean isLogging = this.pluginConfig.getProperty(PluginConfig.verbose_logging);

    public void load(boolean serverStart) {
        if (serverStart) {

        }

        //TODO() Re-work how this functions.
        loadCrates();
    }

    public void reload(boolean serverStop) {
        MetricsHandler metricsHandler = this.crazyHandler.getMetrics();

        if (serverStop) {
            metricsHandler.stop();
            return;
        }

        this.configManager.reload();

        boolean metrics = this.pluginConfig.getProperty(PluginConfig.toggle_metrics);

        if (metrics) {
            metricsHandler.start();
        } else {
            metricsHandler.stop();
        }

        loadCrates();
    }

    // Loads all the information the plugin needs to run.
    private void loadCrates() {
        this.giveNewPlayersKeys = false;
        this.crates.clear();
        this.brokecrates.clear();
        this.crateLocations.clear();
        this.crateSchematics.clear();

        // Removes all holograms so that they can be replaced.
        if (this.hologramController != null) this.hologramController.removeAllHolograms();

        if (PluginSupport.DECENT_HOLOGRAMS.isPluginEnabled()) {
            this.hologramController = new DecentHologramsSupport();
            if (this.plugin.isLogging()) FancyLogger.info("DecentHolograms support has been enabled.");
        } else if (PluginSupport.CMI.isPluginEnabled() && CMIModule.holograms.isEnabled()) {
            this.hologramController = new CMIHologramsSupport();
            if (this.plugin.isLogging()) FancyLogger.info("CMI Hologram support has been enabled.");
        } else if (PluginSupport.HOLOGRAPHIC_DISPLAYS.isPluginEnabled()) {
            this.hologramController = new HolographicDisplaysSupport();
            if (this.plugin.isLogging()) FancyLogger.info("Holographic Displays support has been enabled.");
        } else {
            FancyLogger.warn("No holograms plugin were found. If using CMI, make sure holograms module is enabled.");
        }

        if (this.plugin.isLogging()) FancyLogger.info("Loading all crate information...");

        for (String crateName : this.fileManager.getAllCratesNames(this.plugin)) {
            try {
                FileConfiguration file = this.fileManager.getFile(crateName).getFile();
                CrateType crateType = CrateType.getFromName(file.getString("Crate.CrateType"));
                ArrayList<Prize> prizes = new ArrayList<>();
                String previewName = file.contains("Crate.Preview-Name") ? file.getString("Crate.Preview-Name") : file.getString("Crate.Name");
                ArrayList<Tier> tiers = new ArrayList<>();
                int maxMassOpen = file.contains("Crate.Max-Mass-Open") ? file.getInt("Crate.Max-Mass-Open") : 10;
                int requiredKeys = file.contains("Crate.RequiredKeys") ? file.getInt("Crate.RequiredKeys") : 0;

                if (file.contains("Crate.Tiers") && file.getConfigurationSection("Crate.Tiers") != null) {
                    for (String tier : file.getConfigurationSection("Crate.Tiers").getKeys(false)) {
                        String path = "Crate.Tiers." + tier;
                        tiers.add(new Tier(tier, file.getString(path + ".Name"), file.getString(path + ".Color"), file.getInt(path + ".Chance"), file.getInt(path + ".MaxRange")));
                    }
                }

                if (crateType == CrateType.COSMIC && tiers.isEmpty()) {
                    this.brokecrates.add(crateName);
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

                if (!this.giveNewPlayersKeys) {
                    if (newPlayersKeys > 0) this.giveNewPlayersKeys = true;
                }

                List<String> prizeMessage = file.contains("Crate.Prize-Message") ? file.getStringList("Crate.Prize-Message") : Collections.emptyList();

                CrateHologram holo = new CrateHologram(file.getBoolean("Crate.Hologram.Toggle"), file.getDouble("Crate.Hologram.Height", 0.0), file.getStringList("Crate.Hologram.Message"));
                this.crates.add(new Crate(crateName, previewName, crateType, getKey(file), prizes, file, newPlayersKeys, tiers, maxMassOpen, requiredKeys, prizeMessage, holo));
            } catch (Exception exception) {
                this.brokecrates.add(crateName);

                FancyLogger.error("There was an error while loading the " + crateName + ".yml file.", exception);
            }
        }

        this.crates.add(new Crate("Menu", "Menu", CrateType.MENU, new ItemStack(Material.AIR), new ArrayList<>(), null, 0, null, 0, 0, Collections.emptyList(), null));

        if (this.plugin.isLogging()) {
            FancyLogger.info("All crate information has been loaded.");
            FancyLogger.info("Loading all the physical crate locations.");
        }

        FileConfiguration locations = Files.LOCATIONS.getFile();
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
                    Crate crate = getCrateFromName(locations.getString("Locations." + locationName + ".Crate"));

                    if (world != null && crate != null) {
                        this.crateLocations.add(new CrateLocation(locationName, crate, location));

                        if (this.hologramController != null) {
                            this.hologramController.createHologram(location.getBlock(), crate);
                        }

                        loadedAmount++;
                    } else {
                        this.brokeLocations.add(new BrokeLocation(locationName, crate, x, y, z, worldName));
                        brokeAmount++;
                    }
                } catch (Exception exception) {
                    FancyLogger.error("Failed to create crate locations.", exception);
                }
            }
        }

        // Checking if all physical locations loaded
        if (loadedAmount > 0 || brokeAmount > 0) {
            if (brokeAmount <= 0) {
                if (this.plugin.isLogging()) FancyLogger.success("All physical crate locations have been loaded.");
            } else {
                FancyLogger.info("Loaded " + loadedAmount + " physical crate locations.");
                FancyLogger.error("Failed to load " + brokeAmount + " physical crate locations.");
            }
        }

        // Loading schematic files
        if (this.plugin.isLogging()) FancyLogger.info("Searching for schematics to load.");

        String[] schems = new File(this.plugin.getDataFolder() + "/schematics/").list();

        assert schems != null;
        for (String schematicName : schems) {
            if (schematicName.endsWith(".nbt")) {
                this.crateSchematics.add(new CrateSchematic(schematicName.replace(".nbt", ""), new File(this.plugin.getDataFolder() + "/schematics/" + schematicName)));

                if (this.plugin.isLogging()) FancyLogger.success(schematicName + " was successfully found and loaded.");
            }
        }

        if (this.plugin.isLogging()) FancyLogger.success("All schematics were found and loaded.");

        cleanDataFile();
    }

    // This method is deigned to help clean the data.yml file of any useless info that it may have.
    public void cleanDataFile() {
        FileConfiguration data = Files.DATA.getFile();

        if (data.contains("Players")) {
            if (this.plugin.isLogging()) FancyLogger.info("Cleaning up the data.yml file.");

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
                if (this.plugin.isLogging()) FancyLogger.warn(removePlayers.size() + " player's data has been marked to be removed.");

                removePlayers.forEach(uuid -> data.set("Players." + uuid, null));

                if (this.plugin.isLogging()) FancyLogger.success("All empty player data has been removed.");
            }

            if (this.plugin.isLogging()) FancyLogger.success("The data.yml file has been cleaned.");
            
            Files.DATA.saveFile();
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
                this.methods.sendMessage(player, Translation.no_prizes_found.getComponent(), Translation.no_prizes_found.getString());
                removePlayerFromOpeningList(uuid);
                removePlayerKeyType(uuid);
                return;
            }
        }

        if (!(player.hasPermission("crazycrates.open." + crate.getName()) || !player.hasPermission("crazycrates.open.*"))) {
            this.methods.sendMessage(player, Translation.crate_no_permission.getComponent(), Translation.crate_no_permission.getString());
            removePlayerFromOpeningList(uuid);
            CrateControlListener.inUse.remove(uuid);
            return;
        }

        addPlayerToOpeningList(uuid, crate);

        if (crate.getFile() != null) this.methods.broadCastMessage(crate.getFile(), player);

        switch (crate.getCrateType()) {
            case MENU -> {
                boolean openMenu = this.menuConfig.getProperty(CrateMainMenu.crate_menu_toggle);

                if (openMenu) this.crazyHandler.getMenuManager().openMainMenu(player);
                else this.methods.sendMessage(player, Translation.feature_disabled.getComponent(), Translation.feature_disabled.getString());
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
                StructureHandler handler = new StructureHandler(crateSchematic.getSchematicFile());
                CrateLocation crateLocation = getCrateLocation(location);
                QuadCrateManager session = new QuadCrateManager(player, crate, keyType, crateLocation.getLocation(), lastLocation, checkHand, handler);

                session.startCrate();
            }
            case FIRE_CRACKER -> {
                if (CrateControlListener.inUse.containsValue(location)) {
                    this.methods.sendMessage(player, Translation.in_use.getComponent(), Translation.in_use.getString());
                    removePlayerFromOpeningList(uuid);
                    return;
                } else {
                    if (virtualCrate) {
                        this.methods.sendMessage(player, Translation.cannot_be_a_virtual_crate.getComponent(), Translation.cannot_be_a_virtual_crate.getString());
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
                    this.methods.sendMessage(player, Translation.in_use.getComponent(), Translation.in_use.getString());
                    removePlayerFromOpeningList(uuid);
                    return;
                } else {
                    if (virtualCrate && location.equals(player.getLocation())) {
                        this.methods.sendMessage(player, Translation.cannot_be_a_virtual_crate.getComponent(), Translation.cannot_be_a_virtual_crate.getString());
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
                    this.methods.sendMessage(player, Translation.cannot_be_a_virtual_crate.getComponent(), Translation.cannot_be_a_virtual_crate.getString());
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

        boolean logFile = this.config.getProperty(Config.log_to_file);
        boolean logConsole = this.config.getProperty(Config.log_to_console);

        this.crazyHandler.getEventLogger().logCrateEvent(player, crate, keyType, logFile, logConsole);
    }

    /**
     * This forces a crate to end and will not give out a prize. This is meant for people who leave the server to stop any errors or lag from happening.
     *
     * @param uuid The uuid of the player that the crate is being ended for.
     */
    public void endCrate(UUID uuid) {
        if (this.currentTasks.containsKey(uuid)) {
            this.currentTasks.get(uuid).cancel();
            removeCrateTask(uuid);
        }
    }

    /**
     * Ends the tasks running by a player.
     *
     * @param uuid The uuid of the player using the crate.
     */
    public void endQuadCrate(UUID uuid) {
        if (this.currentQuadTasks.containsKey(uuid)) {
            for (BukkitTask task : this.currentQuadTasks.get(uuid)) {
                task.cancel();
            }

            this.currentQuadTasks.remove(uuid);
        }
    }

    /**
     * Add a quad crate task that is going on for a player.
     *
     * @param uuid The uuid of the player opening the crate.
     * @param task The task of the quad crate.
     */
    public void addQuadCrateTask(UUID uuid, BukkitTask task) {
        if (!this.currentQuadTasks.containsKey(uuid)) {
            this.currentQuadTasks.put(uuid, new ArrayList<>());
        }

        this.currentQuadTasks.get(uuid).add(task);
    }

    /**
     * Checks to see if the player has a quad crate task going on.
     *
     * @param uuid The uuid of the player that is being checked.
     * @return True if they do have a task and false if not.
     */
    public boolean hasQuadCrateTask(UUID uuid) {
        return this.currentQuadTasks.containsKey(uuid);
    }

    /**
     * Add a crate task that is going on for a player.
     *
     * @param uuid The uuid of the player opening the crate.
     * @param task The task of the crate.
     */
    public void addCrateTask(UUID uuid, BukkitTask task) {
        this.currentTasks.put(uuid, task);
    }

    /**
     * Remove a task from the list of current tasks.
     *
     * @param uuid The uuid of the player using the crate.
     */
    public void removeCrateTask(UUID uuid) {
        this.currentTasks.remove(uuid);
    }

    /**
     * Checks to see if the player has a crate task going on.
     *
     * @param uuid The uuid of the player that is being checked.
     * @return True if they do have a task and false if not.
     */
    public boolean hasCrateTask(UUID uuid) {
        return this.currentTasks.containsKey(uuid);
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

        this.crateLocations.add(new CrateLocation(id, crate, location));

        if (this.hologramController != null) this.hologramController.createHologram(location.getBlock(), crate);
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
            this.crateLocations.remove(location);

            if (this.hologramController != null) this.hologramController.removeHologram(location.getLocation().getBlock());
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
                    placeholders.put("{crate}", prize.getCrate());
                    placeholders.put("{prize}", prize.getName());
                    this.methods.sendMessage(player, Translation.prize_error.getMessage(placeholders).toListComponent(), Translation.prize_error.getMessage(placeholders).toListString());
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
                if (command.contains("{random}:")) {
                    String cmd = command;
                    StringBuilder commandBuilder = new StringBuilder();

                    for (String word : cmd.split(" ")) {
                        if (word.startsWith("{random}:")) {
                            word = word.replace("{random}:", "");

                            try {
                                long min = Long.parseLong(word.split("-")[0]);
                                long max = Long.parseLong(word.split("-")[1]);
                                commandBuilder.append(pickNumber(min, max)).append(" ");
                            } catch (Exception exception) {
                                commandBuilder.append("1 ");
                                FancyLogger.error("The prize " + prize.getName() + " in the " + prize.getCrate() + " crate has caused an error when trying to run " + cmd, exception);
                            }
                        } else {
                            commandBuilder.append(word).append(" ");
                        }
                    }

                    command = commandBuilder.toString();
                    command = command.substring(0, command.length() - 1);
                }

                if (PluginSupport.PLACEHOLDERAPI.isPluginEnabled()) command = PlaceholderAPI.setPlaceholders(player, command);

                this.methods.sendCommand(command.replaceAll("\\{player}", player.getName()).replaceAll("\\{reward}", quoteReplacement(prize.getDisplayItemBuilder().getUpdatedName())).replaceAll("\\{crate}", crate.getCrateInventoryName()));
            }

            if (!crate.getPrizeMessage().isEmpty() && prize.getMessages().isEmpty()) {
                for (String message : crate.getPrizeMessage()) {
                    if (PluginSupport.PLACEHOLDERAPI.isPluginEnabled()) {
                        message = PlaceholderAPI.setPlaceholders(player, message);
                    }

                    String newString = message.replaceAll("\\{player}", player.getName()).replaceAll("\\{reward}", prize.getDisplayItemBuilder().getName()).replaceAll("\\{crate}", crate.getCrateInventoryName());

                    this.methods.sendMessage(player, ColorUtils.parse(newString), LegacyUtils.color(newString));
                }

                return;
            }

            for (String message : prize.getMessages()) {
                if (PluginSupport.PLACEHOLDERAPI.isPluginEnabled()) {
                    message = PlaceholderAPI.setPlaceholders(player, message);
                }

                String newString = message.replaceAll("\\{player}", player.getName()).replaceAll("\\{reward}", prize.getDisplayItemBuilder().getName()).replaceAll("\\{crate}", crate.getCrateInventoryName());

                this.methods.sendMessage(player, ColorUtils.parse(newString), LegacyUtils.color(newString));
            }
        } else {
            FancyLogger.warn("No prize was found when giving " + player.getName() + " a prize.");
        }
    }

    /**
     * Add a player to the list of players that are currently opening crates.
     *
     * @param uuid The uuid of the player that is opening a crate.
     * @param crate The crate the player is opening.
     */
    public void addPlayerToOpeningList(UUID uuid, Crate crate) {
        this.playerOpeningCrates.put(uuid, crate);
    }

    /**
     * Remove a player from the list of players that are opening crates.
     *
     * @param uuid The uuid of the player that has finished opening a crate.
     */
    public void removePlayerFromOpeningList(UUID uuid) {
        this.playerOpeningCrates.remove(uuid);
    }

    /**
     * Check if a player is opening a crate.
     *
     * @param uuid The uuid of the player you are checking.
     * @return True if they are opening a crate and false if they are not.
     */
    public boolean isInOpeningList(UUID uuid) {
        return this.playerOpeningCrates.containsKey(uuid);
    }

    /**
     * Get the crate the player is currently opening.
     *
     * @param uuid The uuid of the player you want to check.
     * @return The Crate of which the player is opening. May return null if no crate found.
     */
    public Crate getOpeningCrate(UUID uuid) {
        return this.playerOpeningCrates.get(uuid);
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
        this.playerKeys.put(uuid, keyType);
    }

    /**
     * Remove the player from the list as they have finished the crate.
     * Currently, only used in the Cosmic CrateType.
     *
     * @param uuid The uuid of the player you are removing.
     */
    public void removePlayerKeyType(UUID uuid) {
        this.playerKeys.remove(uuid);
    }

    /**
     * Check if the player is in the list.
     *
     * @param uuid The uuid of the player you are checking.
     * @return True if they are in the list and false if not.
     */
    public boolean hasPlayerKeyType(UUID uuid) {
        return this.playerKeys.containsKey(uuid);
    }

    /**
     * The key type the player's current crate is using.
     *
     * @param uuid The uuid of the player that is using the crate.
     * @return The key type of the crate the player is using.
     */
    public KeyType getPlayerKeyType(UUID uuid) {
        return this.playerKeys.get(uuid);
    }

    /**
     * Get the locations a player sets for when creating a new schematic.
     *
     * @return The list of locations set by players.
     */
    public HashMap<UUID, Location[]> getSchematicLocations() {
        return this.schemLocations;
    }

    /**
     * Set a new player's default amount of keys.
     *
     * @param player the player that has just joined.
     */
    public void setNewPlayerKeys(Player player) {
        if (this.giveNewPlayersKeys) { // Checks if any crate gives new players keys and if not then no need to do all this stuff.
            UUID uuid = player.getUniqueId();

            if (!player.hasPlayedBefore()) {
                this.crates.stream()
                .filter(Crate::doNewPlayersGetKeys)
                .forEach(crate -> {
                    Files.DATA.getFile().set("Players." + uuid + "." + crate.getName(), crate.getNewPlayerKeys());
                    Files.DATA.saveFile();
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
        return this.hologramController;
    }

    /**
     * Load all the schematics inside the Schematics folder.
     */
    public void loadSchematics() {
        this.crateSchematics.clear();
        String[] schems = new File(this.plugin.getDataFolder() + "/schematics/").list();

        assert schems != null;
        for (String schematicName : schems) {
            if (schematicName.endsWith(".nbt")) {
                this.crateSchematics.add(new CrateSchematic(schematicName.replace(".nbt", ""), new File(this.plugin.getDataFolder() + "/schematics/" + schematicName)));
            }
        }
    }

    /**
     * Get the list of all the schematics currently loaded onto the server.
     *
     * @return The list of all loaded schematics.
     */
    public List<CrateSchematic> getCrateSchematics() {
        return this.crateSchematics;
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