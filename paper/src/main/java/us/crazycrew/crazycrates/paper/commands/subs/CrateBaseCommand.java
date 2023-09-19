package us.crazycrew.crazycrates.paper.commands.subs;

import ch.jalu.configme.SettingsManager;
import dev.triumphteam.cmd.core.annotation.ArgName;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import us.crazycrew.crazycrates.common.config.ConfigManager;
import us.crazycrew.crazycrates.common.config.types.Config;
import us.crazycrew.crazycrates.common.config.types.menus.CrateMainMenu;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.EventLogger;
import us.crazycrew.crazycrates.paper.api.FileManager;
import us.crazycrew.crazycrates.paper.api.FileManager.Files;
import us.crazycrew.crazycrates.paper.api.enums.Translation;
import us.crazycrew.crazycrates.paper.api.enums.settings.Messages;
import us.crazycrew.crazycrates.paper.api.events.PlayerPrizeEvent;
import us.crazycrew.crazycrates.paper.api.events.PlayerReceiveKeyEvent;
import us.crazycrew.crazycrates.paper.api.plugin.frame.BukkitUserManager;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.objects.CrateLocation;
import us.crazycrew.crazycrates.paper.api.objects.Prize;
import us.crazycrew.crazycrates.common.enums.Permissions;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyHandler;
import us.crazycrew.crazycrates.paper.listeners.CrateControlListener;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Command(value = "crates", alias = {"crazycrates", "cc", "crate", "crazycrate"})
public class CrateBaseCommand extends BaseCommand {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyHandler crazyHandler = this.plugin.getCrazyHandler();
    private final @NotNull FileManager fileManager = this.crazyHandler.getFileManager();
    private final @NotNull BukkitUserManager userManager = this.crazyHandler.getUserManager();
    private final @NotNull CrazyManager crazyManager = this.crazyHandler.getCrazyManager();
    private final @NotNull EventLogger eventLogger = this.crazyHandler.getEventLogger();
    private final @NotNull Methods methods = this.crazyHandler.getMethods();
    private final @NotNull ConfigManager configManager = this.crazyHandler.getConfigManager();

    private final @NotNull SettingsManager config = this.configManager.getConfig();
    private final @NotNull SettingsManager menuConfig = this.configManager.getMainMenuConfig();

    @Default
    @Permission(value = "crazycrates.crate-menu", def = PermissionDefault.TRUE)
    public void onDefaultMenu(Player player) {
        boolean openMenu = this.menuConfig.getProperty(CrateMainMenu.crate_menu_toggle);

        if (openMenu) this.crazyHandler.getMenuManager().openMainMenu(player); else this.methods.sendMessage(player, Translation.feature_disabled);
    }

    @SubCommand("help")
    @Permission(value = "crazycrates.help", def = PermissionDefault.TRUE)
    public void onHelp(CommandSender sender) {
        //TODO() Paginated help list with permission filtering.
        if (sender.hasPermission(new org.bukkit.permissions.Permission("crazycrates.admin-access", PermissionDefault.NOT_OP))) {
            this.methods.sendMessage(sender, Translation.admin_help);
            return;
        }

        this.methods.sendMessage(sender, Translation.player_help);
    }

    @SubCommand("transfer")
    @Permission(value = "crazycrates.transfer", def = PermissionDefault.TRUE)
    public void onPlayerTransferKeys(Player sender, @Suggestion("crates") String crateName, @Suggestion("online-players") Player player, @Suggestion("numbers") int amount) {
        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            this.methods.sendMessage(sender, Translation.not_a_crate.getMessage("{crate}", crateName));
            return;
        }

        if (player.getName().equalsIgnoreCase(sender.getName())) {
            this.methods.sendMessage(sender, Translation.same_player);
            return;
        }

        if (this.userManager.getVirtualKeys(sender.getUniqueId(), crate.getName()) < amount) {
            this.methods.sendMessage(sender, Translation.command_transfer_not_enough_keys.getMessage("{crate}", crateName));
            return;
        }

        PlayerReceiveKeyEvent event = new PlayerReceiveKeyEvent(player.getUniqueId(), crate, PlayerReceiveKeyEvent.KeyReceiveReason.TRANSFER, amount);
        this.plugin.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) return;

        this.userManager.takeKeys(amount, sender.getUniqueId(), crate.getName(), KeyType.VIRTUAL_KEY, false);
        this.userManager.addVirtualKeys(amount, player.getUniqueId(), crate.getName());

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("{crate}", crate.getName());
        placeholders.put("{amount}", amount + "");
        placeholders.put("{player}", player.getName());

        this.methods.sendMessage(sender, Translation.command_transfer_keys.getMessage(placeholders));

        placeholders.put("{player}", sender.getName());

        this.methods.sendMessage(player, Translation.command_transfer_keys_received.getMessage(placeholders));

        boolean logFile = this.config.getProperty(Config.log_to_file);
        boolean logConsole = this.config.getProperty(Config.log_to_console);

        this.eventLogger.logKeyEvent(player, sender, crate, KeyType.VIRTUAL_KEY, EventLogger.KeyEventType.key_received_event, logFile, logConsole);
    }

    @SubCommand("reload")
    @Permission(value = "crazycrates.reload", def = PermissionDefault.TRUE)
    public void onReload(CommandSender sender) {
        this.fileManager.reloadAllFiles();
        this.fileManager.setup();

        CrazyHandler.janitor();

        this.crazyManager.reload(false);

        this.crazyHandler.getMenuManager().loadButtons();

        this.methods.sendMessage(sender, Translation.command_reload_completed);
    }

    @SubCommand("debug")
    @Permission(value = "crazycrates.debug", def = PermissionDefault.TRUE)
    public void onDebug(Player player, @Suggestion("crates") String crateName) {
        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            this.methods.sendMessage(player, Translation.not_a_crate.getMessage("{crate}", crateName));
            return;
        }

        crate.getPrizes().forEach(prize -> this.crazyManager.givePrize(player, prize, crate));
    }

    @SubCommand("schem-save")
    @Permission(value = "crazycrates.schematic-save", def = PermissionDefault.TRUE)
    public void onAdminSave(Player player) {
        this.methods.sendMessage(player, Translation.feature_disabled);
    }

    @SubCommand("schem-set")
    @Permission(value = "crazycrates.schematic-set", def = PermissionDefault.TRUE)
    public void onAdminSet(Player player) {
        this.methods.sendMessage(player, Translation.feature_disabled);
    }

    @SubCommand("admin")
    @Permission(value = "crazycrates.admin-access", def = PermissionDefault.TRUE)
    public void onAdminMenu(Player player) {
        int size = this.crazyManager.getCrates().size();
        int slots = 9;

        for (; size > 9; size -= 9) slots += 9;

        Inventory inv = this.plugin.getServer().createInventory(null, slots, methods.color("&4&lAdmin Keys"));

        for (Crate crate : this.crazyManager.getCrates()) {
            if (crate.getCrateType() != CrateType.MENU) {
                if (inv.firstEmpty() >= 0) inv.setItem(inv.firstEmpty(), crate.getAdminKey());
            }
        }

        player.openInventory(inv);
    }

    @SubCommand("list")
    @Permission(value = "crazycrates.list", def = PermissionDefault.TRUE)
    public void onAdminList(CommandSender sender) {
        StringBuilder crates = new StringBuilder();
        String brokecrates;

        this.crazyManager.getCrates().forEach(crate -> crates.append("&a").append(crate.getName()).append("&8, "));

        StringBuilder brokecratesBuilder = new StringBuilder();

        this.crazyManager.getBrokeCrates().forEach(crate -> brokecratesBuilder.append("&c").append(crate).append(".yml&8,"));

        brokecrates = brokecratesBuilder.toString();

        sender.sendMessage(this.methods.color("&e&lCrates:&f " + crates));

        if (!brokecrates.isEmpty()) sender.sendMessage(this.methods.color("&6&lBroken Crates:&f " + brokecrates.substring(0, brokecrates.length() - 2)));

        sender.sendMessage(this.methods.color("&e&lAll Crate Locations:"));
        sender.sendMessage(this.methods.color("&c[ID]&8, &c[Crate]&8, &c[World]&8, &c[X]&8, &c[Y]&8, &c[Z]"));
        int line = 1;

        for (CrateLocation loc : this.crazyManager.getCrateLocations()) {
            Crate crate = loc.getCrate();
            String world = loc.getLocation().getWorld().getName();

            int x = loc.getLocation().getBlockX();
            int y = loc.getLocation().getBlockY();
            int z = loc.getLocation().getBlockZ();

            sender.sendMessage(this.methods.color("&8[&b" + line + "&8]: " + "&c" + loc.getID() + "&8, &c" + crate.getName() + "&8, &c" + world + "&8, &c" + x + "&8, &c" + y + "&8, &c" + z));
            line++;
        }
    }

    @SubCommand("tp")
    @Permission(value = "crazycrates.teleport", def = PermissionDefault.TRUE)
    public void onAdminTeleport(Player player, @Suggestion("locations") String id) {
        FileConfiguration locations = Files.LOCATIONS.getFile();

        if (!locations.contains("Locations")) {
            locations.set("Locations.Clear", null);
            Files.LOCATIONS.saveFile();
        }

        for (String name : locations.getConfigurationSection("Locations").getKeys(false)) {
            if (name.equalsIgnoreCase(id)) {
                World world = this.plugin.getServer().getWorld(locations.getString("Locations." + name + ".World"));

                int x = locations.getInt("Locations." + name + ".X");
                int y = locations.getInt("Locations." + name + ".Y");
                int z = locations.getInt("Locations." + name + ".Z");

                Location loc = new Location(world, x, y, z);

                player.teleport(loc.add(.5, 0, .5));
                player.sendMessage(this.methods.getPrefix("&7You have been teleported to &6" + name + "&7."));

                return;
            }
        }

        player.sendMessage(this.methods.getPrefix("&cThere is no location called &6" + id + "&c."));
    }

    @SubCommand("additem")
    @Permission(value = "crazycrates.additem", def = PermissionDefault.TRUE)
    public void onAdminCrateAddItem(Player player, @Suggestion("crates") String crateName, @Suggestion("prizes") String prize) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.AIR) {
            this.methods.sendMessage(player, Translation.command_add_item_no_item_in_hand);
            return;
        }

        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            this.methods.sendMessage(player, Translation.not_a_crate.getMessage("{crate}", crateName));
            return;
        }

        try {
            crate.addEditorItem(prize, item);
        } catch (Exception exception) {
            FancyLogger.error("Failed to add a new prize to the " + crate.getName() + " crate.", exception);
        }

        this.crazyManager.load(false);

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("{crate}", crate.getName());
        placeholders.put("{prize}", prize);

        this.methods.sendMessage(player, Translation.command_add_item_from_hand.getMessage(placeholders));
    }

    @SubCommand("preview")
    @Permission(value = "crazycrates.preview", def = PermissionDefault.TRUE)
    public void onAdminCratePreview(CommandSender sender, @Suggestion("crates") String crateName, @Suggestion("online-players") Player player) {
        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            this.methods.sendMessage(sender, Translation.not_a_crate.getMessage("{crate}", crateName));
            return;
        }

        if (!crate.isPreviewEnabled()) {
            this.methods.sendMessage(sender, Translation.feature_disabled);
            return;
        }

        this.crazyHandler.getMenuManager().setPlayerInMenu(player, false);
        this.crazyHandler.getMenuManager().openNewPreview(player, crate);
    }

    @SubCommand("open-others")
    @Permission(value = "crazycrates.open-others", def = PermissionDefault.TRUE)
    public void onAdminCrateOpenOthers(CommandSender sender, @Suggestion("crates") String crateName, @Suggestion("online-players") Player player) {
        openCrate(sender, player, crateName);
    }

    @SubCommand("open")
    @Permission(value = "crazycrates.open", def = PermissionDefault.TRUE)
    public void onAdminCrateOpen(Player player, @Suggestion("crates") String crateName) {
        openCrate(player, player, crateName);
    }

    private void openCrate(CommandSender sender, Player player, String crateName) {
        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            this.methods.sendMessage(sender, Translation.not_a_crate.getMessage("{crate}", crateName));
            return;
        }

        if (this.crazyManager.isInOpeningList(player.getUniqueId())) {
            this.methods.sendMessage(sender, Translation.already_open);
            return;
        }

        CrateType type = crate.getCrateType();

        if (type == CrateType.CRATE_ON_THE_GO || type == CrateType.QUICK_CRATE || type == CrateType.FIRE_CRACKER || type == CrateType.QUAD_CRATE) {
            this.methods.sendMessage(sender, Translation.cannot_be_a_virtual_crate);
            return;
        }

        boolean hasKey = false;
        KeyType keyType = KeyType.VIRTUAL_KEY;

        if (this.userManager.getVirtualKeys(player.getUniqueId(), crate.getName()) >= 1) {
            hasKey = true;
        } else {
            if (this.config.getProperty(Config.virtual_accepts_physical_keys)) {
                if (this.userManager.hasPhysicalKey(player.getUniqueId(), crate.getName(), false)) {
                    hasKey = true;
                    keyType = KeyType.PHYSICAL_KEY;
                }
            }
        }

        if (!hasKey) {
            //TODO() make volume/pitch configurable
            //TODO() Adopt the new sound system including custom sounds.
            if (this.config.getProperty(Config.key_sound_toggle)) {
                Sound sound = Sound.valueOf(this.config.getProperty(Config.key_sound_name));

                player.playSound(player.getLocation(), sound, SoundCategory.PLAYERS, 1f, 1f);
            }

            this.methods.sendMessage(player, Translation.no_virtual_keys);
            return;
        }

        if (this.methods.isInventoryFull(player)) {
            this.methods.sendMessage(player, Translation.inventory_not_empty);
            return;
        }

        this.crazyManager.openCrate(player, crate, keyType, player.getLocation(), true, false);

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("{crate}", crate.getName());
        placeholders.put("{player}", player.getName());

        this.methods.sendMessage(sender, Translation.command_open_crate.getMessage(placeholders));

        boolean logFile = this.config.getProperty(Config.log_to_file);
        boolean logConsole = this.config.getProperty(Config.log_to_console);

        this.eventLogger.logKeyEvent(player, sender, crate, keyType, EventLogger.KeyEventType.key_removed_event, logFile, logConsole);
    }

    @SubCommand("mass-open")
    @Permission(value = "crazycrates.mass-open", def = PermissionDefault.TRUE)
    public void onAdminCrateMassOpen(Player player, @Suggestion("crates") String crateName, @Suggestion("numbers") int amount) {
        UUID uuid = player.getUniqueId();

        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            this.methods.sendMessage(player, Translation.not_a_crate.getMessage("{crate}", crateName));
            return;
        }

        this.crazyManager.addPlayerToOpeningList(uuid, crate);

        int keys = this.userManager.getVirtualKeys(uuid, crate.getName());
        int keysUsed = 0;

        if (keys == 0) {
            this.methods.sendMessage(player, Translation.no_virtual_keys);
            return;
        }

        for (;keys > 0; keys--) {
            if (this.methods.isInventoryFull(player)) break;
            if (keysUsed > amount) break;
            if (keysUsed >= crate.getMaxMassOpen()) break;

            Prize prize = crate.pickPrize(player);
            this.crazyManager.givePrize(player, prize, crate);
            this.plugin.getServer().getPluginManager().callEvent(new PlayerPrizeEvent(uuid, crate, crate.getName(), prize));

            if (prize.useFireworks()) this.methods.firework(player.getLocation().clone().add(.5, 1, .5));

            keysUsed++;
        }

        if (!this.userManager.takeKeys(keysUsed, player.getUniqueId(), crate.getName(), KeyType.VIRTUAL_KEY, false)) {
            CrateControlListener.inUse.remove(uuid);
            this.crazyManager.removePlayerFromOpeningList(uuid);
            return;
        }

        this.crazyManager.removePlayerFromOpeningList(uuid);
    }

    @SubCommand("forceopen")
    @Permission(value = "crazycrates.force-open", def = PermissionDefault.TRUE)
    public void onAdminForceOpen(CommandSender sender, @Suggestion("crates") String crateName, @Suggestion("online-players") Player player) {
        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            this.methods.sendMessage(sender, Translation.not_a_crate.getMessage("{crate}", crateName));
            return;
        }

        if (this.crazyManager.isInOpeningList(player.getUniqueId())) {
            this.methods.sendMessage(sender, Translation.already_open);
            return;
        }

        CrateType type = crate.getCrateType();

        if (type == CrateType.CRATE_ON_THE_GO || type == CrateType.QUICK_CRATE || type == CrateType.FIRE_CRACKER) {
            this.methods.sendMessage(sender, Translation.cannot_be_a_virtual_crate);
            return;
        }

        this.crazyManager.openCrate(player, crate, KeyType.FREE_KEY, player.getLocation(), true, false);

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("{crate}", crate.getName());
        placeholders.put("{player}", player.getName());

        this.methods.sendMessage(sender, Translation.command_open_crate.getMessage(placeholders));

        boolean logFile = this.config.getProperty(Config.log_to_file);
        boolean logConsole = this.config.getProperty(Config.log_to_console);

        this.eventLogger.logKeyEvent(player, sender, crate, KeyType.FREE_KEY, EventLogger.KeyEventType.key_removed_event, logFile, logConsole);
    }

    @SubCommand("set")
    @Permission(value = "crazycrates.set-crate", def = PermissionDefault.TRUE)
    public void onAdminCrateSet(Player player, @Suggestion("crates") String crateName) {
        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            this.methods.sendMessage(player, Translation.not_a_crate.getMessage("{crate}", crateName));
            return;
        }

        Block block = player.getTargetBlock(null, 5);

        if (block.isEmpty()) {
            this.methods.sendMessage(player, Translation.must_be_looking_at_block);
            return;
        }

        if (this.crazyManager.isCrateLocation(block.getLocation())) {
            player.sendMessage(this.methods.getPrefix("&cThis location already has a crate!"));
            return;
        }

        this.crazyManager.addCrateLocation(block.getLocation(), crate);

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("{crate}", crate.getName());
        placeholders.put("{prefix}", this.methods.getPrefix());

        this.methods.sendMessage(player, Translation.physical_crate_created.getMessage(placeholders));
    }

    @SubCommand("give-random")
    @Permission(value = "crazycrates.give-random-key", def = PermissionDefault.TRUE)
    public void onAdminCrateGiveRandom(CommandSender sender, @Suggestion("key-types") String keyType, @Suggestion("numbers") int amount, @Suggestion("online-players") CustomPlayer player) {
        Crate crate = this.crazyManager.getCrates().get((int) this.crazyManager.pickNumber(0, (this.crazyManager.getCrates().size() - 2)));

        onAdminCrateGive(sender, keyType, crate.getName(), amount, player);
    }

    public record CustomPlayer(String name, CrazyCrates plugin) {
        public @NotNull OfflinePlayer getOfflinePlayer() {
            CompletableFuture<UUID> future = CompletableFuture.supplyAsync(() -> this.plugin.getServer().getOfflinePlayer(this.name)).thenApply(OfflinePlayer::getUniqueId);

            return this.plugin.getServer().getOfflinePlayer(future.join());
        }

        public Player getPlayer() {
            return this.plugin.getServer().getPlayer(name);
        }
    }

    private void addKey(CommandSender sender, Player player, OfflinePlayer offlinePlayer, UUID uuid, Crate crate, KeyType type, int amount) {
        PlayerReceiveKeyEvent event = new PlayerReceiveKeyEvent(uuid, crate, PlayerReceiveKeyEvent.KeyReceiveReason.GIVE_COMMAND, amount);

        boolean logFile = this.config.getProperty(Config.log_to_file);
        boolean logConsole = this.config.getProperty(Config.log_to_console);

        this.plugin.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) return;

        if (player != null) {
            if (crate.getCrateType() == CrateType.CRATE_ON_THE_GO) {
                player.getInventory().addItem(crate.getKey(amount));
            } else {
                this.userManager.addKeys(amount, uuid, crate.getName(), type);
            }
        } else {
            if (!this.userManager.addOfflineKeys(uuid, crate.getName(), amount, type)) {
                this.methods.sendMessage(sender, Translation.internal_error);
            } else {
                HashMap<String, String> placeholders = new HashMap<>();

                placeholders.put("{amount}", String.valueOf(amount));
                placeholders.put("{player}", offlinePlayer.getName());

                this.methods.sendMessage(sender, Translation.command_give_offline_player_keys.getMessage(placeholders));

                this.eventLogger.logKeyEvent(offlinePlayer, sender, crate, type, EventLogger.KeyEventType.key_removed_event, logFile, logConsole);

                return;
            }
        }

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("{amount}", String.valueOf(amount));
        placeholders.put("{player}", player.getName());
        placeholders.put("{key}", crate.getKey().getItemMeta().getDisplayName());

        boolean fullMessage = this.config.getProperty(Config.give_virtual_keys_message);
        boolean inventoryCheck = this.config.getProperty(Config.give_virtual_keys);

        this.methods.sendMessage(sender, Translation.command_give_player_keys.getMessage(placeholders));
        if (!inventoryCheck || !fullMessage && !methods.isInventoryFull(player) && player.isOnline()) this.methods.sendMessage(player, Translation.obtaining_keys.getMessage(placeholders));

        this.eventLogger.logKeyEvent(player, sender, crate, type, EventLogger.KeyEventType.key_give_event, logFile, logConsole);
    }

    @SubCommand("give")
    @Permission(value = "crazycrates.give-key", def = PermissionDefault.TRUE)
    public void onAdminCrateGive(CommandSender sender, @Suggestion("key-types") String keyType, @Suggestion("crates") String crateName, @Suggestion("numbers") int amount, @Suggestion("online-players") CustomPlayer target) {
        KeyType type = KeyType.getFromName(keyType);
        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (type == null || type == KeyType.FREE_KEY) {
            sender.sendMessage(this.methods.color(this.methods.getPrefix() + "&cPlease use Virtual/V or Physical/P for a Key type."));
            return;
        }

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            this.methods.sendMessage(sender, Translation.not_a_crate.getMessage("{crate}", crateName));
            return;
        }

        if (target.getPlayer() != null) {
            Player player = target.getPlayer();

            addKey(sender, player, null, player.getUniqueId(), crate, type, amount);

            return;
        }

        OfflinePlayer offlinePlayer = target.getOfflinePlayer();

        addKey(sender, null, offlinePlayer, offlinePlayer.getUniqueId(), crate, type, amount);
    }

    private void takeKey(CommandSender sender, Player player, OfflinePlayer offlinePlayer, UUID uuid, Crate crate, KeyType type, int amount) {
        boolean logFile = this.config.getProperty(Config.log_to_file);
        boolean logConsole = this.config.getProperty(Config.log_to_console);

        if (player != null) {
            this.userManager.takeKeys(amount, uuid, crate.getName(), type, false);
        } else {
            if (!this.userManager.takeKeys(amount, uuid, crate.getName(), type, false)) {
                sender.sendMessage(Messages.INTERNAL_ERROR.getMessage());
            } else {
                this.userManager.takeKeys(amount, uuid, crate.getName(), type, false);

                HashMap<String, String> placeholders = new HashMap<>();

                placeholders.put("{amount}", String.valueOf(amount));
                placeholders.put("{player}", offlinePlayer.getName());

                this.methods.sendMessage(sender, Translation.command_take_offline_player_keys.getMessage(placeholders));

                this.eventLogger.logKeyEvent(offlinePlayer, sender, crate, type, EventLogger.KeyEventType.key_removed_event, logFile, logConsole);

                return;
            }
        }

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("{amount}", String.valueOf(amount));
        placeholders.put("{player}", player.getName());

        this.methods.sendMessage(sender, Translation.command_take_player_keys.getMessage(placeholders));

        this.eventLogger.logKeyEvent(player, sender, crate, type, EventLogger.KeyEventType.key_removed_event, logFile, logConsole);
    }

    @SubCommand("take")
    @Permission(value = "crazycrates.take-key", def = PermissionDefault.TRUE)
    public void onAdminCrateTake(CommandSender sender, @Suggestion("key-types") String keyType, @Suggestion("crates") String crateName, @Suggestion("numbers") int amount, @Suggestion("online-players") CustomPlayer target) {
        KeyType type = KeyType.getFromName(keyType);

        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (type == null || type == KeyType.FREE_KEY) {
            sender.sendMessage(this.methods.color(this.methods.getPrefix() + "&cPlease use Virtual/V or Physical/P for a Key type."));
            return;
        }

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            this.methods.sendMessage(sender, Translation.not_a_crate.getMessage("{crate}", crateName));
            return;
        }

        if (target.getPlayer() != null) {
            Player player = target.getPlayer();

            takeKey(sender, player, null, player.getUniqueId(), crate, type, amount);

            return;
        }

        OfflinePlayer offlinePlayer = target.getOfflinePlayer();

        takeKey(sender, null, offlinePlayer, offlinePlayer.getUniqueId(), crate, type, amount);
    }

    @SubCommand("giveall")
    @Permission(value = "crazycrates.give-all", def = PermissionDefault.TRUE)
    public void onAdminCrateGiveAllKeys(CommandSender sender, @Suggestion("key-types") @ArgName("key-type") String keyType, @Suggestion("crates") @ArgName("crate-name") String crateName, @Suggestion("numbers") int amount) {
        KeyType type = KeyType.getFromName(keyType);

        if (type == null || type == KeyType.FREE_KEY) {
            sender.sendMessage(this.methods.color(this.methods.getPrefix() + "&cPlease use Virtual/V or Physical/P for a Key type."));
            return;
        }

        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            this.methods.sendMessage(sender, Translation.not_a_crate.getMessage("{crate}", crateName));
            return;
        }

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("{amount}", String.valueOf(amount));
        placeholders.put("{key}", crate.getKey().getItemMeta().getDisplayName());

        this.methods.sendMessage(sender, Translation.command_give_everyone_keys.getMessage(placeholders));

        for (Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
            if (this.methods.permCheck(onlinePlayer, Permissions.crazy_crates_player_exclude_give_all, true)) continue;

            PlayerReceiveKeyEvent event = new PlayerReceiveKeyEvent(onlinePlayer.getUniqueId(), crate, PlayerReceiveKeyEvent.KeyReceiveReason.GIVE_ALL_COMMAND, amount);
            onlinePlayer.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.methods.sendMessage(onlinePlayer, Translation.obtaining_keys.getMessage(placeholders));

                if (crate.getCrateType() == CrateType.CRATE_ON_THE_GO) {
                    onlinePlayer.getInventory().addItem(crate.getKey(amount));
                    return;
                }

                this.userManager.addKeys(amount, onlinePlayer.getUniqueId(), crate.getName(), type);

                boolean logFile = this.config.getProperty(Config.log_to_file);
                boolean logConsole = this.config.getProperty(Config.log_to_console);

                this.eventLogger.logKeyEvent(onlinePlayer, sender, crate, type, EventLogger.KeyEventType.key_give_event, logFile, logConsole);
            }
        }
    }
}