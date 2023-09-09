package us.crazycrew.crazycrates.paper.commands.subs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.EventLogger;
import us.crazycrew.crazycrates.paper.api.FileManager;
import us.crazycrew.crazycrates.paper.api.enums.settings.Messages;
import us.crazycrew.crazycrates.paper.api.events.PlayerPrizeEvent;
import us.crazycrew.crazycrates.paper.api.events.PlayerReceiveKeyEvent;
import us.crazycrew.crazycrates.paper.api.frame.BukkitUserManager;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.objects.CrateLocation;
import us.crazycrew.crazycrates.paper.api.objects.Prize;
import us.crazycrew.crazycrates.common.enums.Permissions;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;
import us.crazycrew.crazycrates.paper.listeners.CrateControlListener;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.*;
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
    private final @NotNull CrazyCratesLoader cratesLoader = this.plugin.getCratesLoader();
    private final @NotNull FileManager fileManager = this.cratesLoader.getFileManager();
    private final @NotNull BukkitUserManager userManager = this.cratesLoader.getUserManager();
    private final @NotNull CrazyManager crazyManager = this.cratesLoader.getCrazyManager();
    private final @NotNull EventLogger eventLogger = this.cratesLoader.getEventLogger();
    private final @NotNull Methods methods = this.cratesLoader.getMethods();

    @Default
    @Permission(value = "crazycrates.command.player.menu", def = PermissionDefault.TRUE)
    public void onDefaultMenu(Player player) {
        FileConfiguration config = FileManager.Files.CONFIG.getFile();

        boolean openMenu = config.getBoolean("Settings.Enable-Crate-Menu");

        if (openMenu) this.cratesLoader.getMenuManager().openMainMenu(player); else player.sendMessage(Messages.FEATURE_DISABLED.getMessage());
    }

    @SubCommand("help")
    @Permission(value = "crazycrates.command.player.help", def = PermissionDefault.TRUE)
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Messages.HELP.getMessage());
    }

    @SubCommand("transfer")
    @Permission(value = "crazycrates.command.player.transfer", def = PermissionDefault.OP)
    public void onPlayerTransferKeys(Player sender, @Suggestion("crates") String crateName, @Suggestion("online-players") Player player, @Suggestion("numbers") int amount) {
        Crate crate = crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            player.sendMessage(Messages.NOT_A_CRATE.getMessage("%Crate%", crateName));
            return;
        }

        if (player.getName().equalsIgnoreCase(sender.getName())) {
            sender.sendMessage(Messages.SAME_PLAYER.getMessage());
            return;
        }

        if (this.userManager.getVirtualKeys(sender.getUniqueId(), crate.getName()) < amount) {
            sender.sendMessage(Messages.NOT_ENOUGH_KEYS.getMessage("%Crate%", crate.getName()));
            return;
        }

        PlayerReceiveKeyEvent event = new PlayerReceiveKeyEvent(player.getUniqueId(), crate, PlayerReceiveKeyEvent.KeyReceiveReason.TRANSFER, amount);
        this.plugin.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) return;

        this.userManager.takeKeys(amount, sender.getUniqueId(), crate.getName(), KeyType.VIRTUAL_KEY, false);
        this.userManager.addVirtualKeys(amount, player.getUniqueId(), crate.getName());

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("%Crate%", crate.getName());
        placeholders.put("%Amount%", amount + "");
        placeholders.put("%Player%", player.getName());

        sender.sendMessage(Messages.TRANSFERRED_KEYS.getMessage(placeholders));

        placeholders.put("%Player%", sender.getName());

        player.sendMessage(Messages.RECEIVED_TRANSFERRED_KEYS.getMessage(placeholders));

        FileConfiguration config = FileManager.Files.CONFIG.getFile();

        boolean logFile = config.getBoolean("Settings.Crate-Actions.Log-File");
        boolean logConsole = config.getBoolean("Settings.Crate-Actions.Log-Console");

        this.eventLogger.logKeyEvent(player, sender, crate, KeyType.VIRTUAL_KEY, EventLogger.KeyEventType.KEY_EVENT_RECEIVED, logFile, logConsole);
    }

    @SubCommand("admin-help")
    @Permission(value = "crazycrates.command.admin.help", def = PermissionDefault.OP)
    public void onAdminHelp(CommandSender sender) {
        sender.sendMessage(Messages.ADMIN_HELP.getMessage());
    }

    @SubCommand("reload")
    @Permission(value = "crazycrates.command.admin.reload", def = PermissionDefault.OP)
    public void onReload(CommandSender sender) {
        this.fileManager.reloadAllFiles();
        this.fileManager.setup();

        CrazyCratesLoader.janitor();

        this.crazyManager.reload(false);

        this.cratesLoader.getMenuManager().loadButtons();

        sender.sendMessage(Messages.RELOAD.getMessage());
    }

    @SubCommand("debug")
    @Permission(value = "crazycrates.command.admin.debug", def = PermissionDefault.OP)
    public void onDebug(Player player, @Suggestion("crates") String crateName) {
        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            player.sendMessage(Messages.NOT_A_CRATE.getMessage("%Crate%", crateName));
            return;
        }

        crate.getPrizes().forEach(prize -> this.crazyManager.givePrize(player, prize, crate));
    }

    @SubCommand("schem-save")
    @Permission(value = "crazycrates.command.admin.schematic.save", def = PermissionDefault.OP)
    public void onAdminSave(Player player) {
        player.sendMessage(Messages.FEATURE_DISABLED.getMessage());
    }

    @SubCommand("schem-set")
    @Permission(value = "crazycrates.command.admin.schematic.set", def = PermissionDefault.OP)
    public void onAdminSet(Player player) {
        player.sendMessage(Messages.FEATURE_DISABLED.getMessage());
    }

    @SubCommand("admin")
    @Permission(value = "crazycrates.command.admin.access", def = PermissionDefault.OP)
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
    @Permission(value = "crazycrates.command.admin.list", def = PermissionDefault.OP)
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
    @Permission(value = "crazycrates.command.admin.teleport", def = PermissionDefault.OP)
    public void onAdminTeleport(Player player, @Suggestion("locations") String id) {
        FileConfiguration locations = FileManager.Files.LOCATIONS.getFile();

        if (!locations.contains("Locations")) {
            locations.set("Locations.Clear", null);
            FileManager.Files.LOCATIONS.saveFile();
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
    @Permission(value = "crazycrates.command.admin.additem", def = PermissionDefault.OP)
    public void onAdminCrateAddItem(Player player, @Suggestion("crates") String crateName, @Suggestion("prizes") String prize) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.AIR) {
            player.sendMessage(Messages.NO_ITEM_IN_HAND.getMessage());
            return;
        }

        Crate crate = crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            player.sendMessage(Messages.NOT_A_CRATE.getMessage("%Crate%", crateName));
            return;
        }

        try {
            crate.addEditorItem(prize, item);
        } catch (Exception e) {
            FancyLogger.error("Failed to add a new prize to the " + crate.getName() + " crate.");
            FancyLogger.debug(e.getMessage());
        }

        this.crazyManager.load(false);

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("%Crate%", crate.getName());
        placeholders.put("%Prize%", prize);

        player.sendMessage(Messages.ADDED_ITEM_WITH_EDITOR.getMessage(placeholders));
    }

    @SubCommand("preview")
    @Permission(value = "crazycrates.command.admin.preview", def = PermissionDefault.OP)
    public void onAdminCratePreview(CommandSender sender, @Suggestion("crates") String crateName, @Suggestion("online-players") Player player) {
        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            sender.sendMessage(Messages.NOT_A_CRATE.getMessage("%Crate%", crateName));
            return;
        }

        if (!crate.isPreviewEnabled()) {
            sender.sendMessage(Messages.PREVIEW_DISABLED.getMessage());
            return;
        }

        this.cratesLoader.getMenuManager().setPlayerInMenu(player, false);
        this.cratesLoader.getMenuManager().openNewPreview(player, crate);
    }

    @SubCommand("open-others")
    @Permission(value = "crazycrates.command.admin.open.others", def = PermissionDefault.OP)
    public void onAdminCrateOpenOthers(CommandSender sender, @Suggestion("crates") String crateName, @Suggestion("online-players") Player player) {
        openCrate(sender, player, crateName);
    }

    @SubCommand("open")
    @Permission(value = "crazycrates.command.admin.open", def = PermissionDefault.OP)
    public void onAdminCrateOpen(Player player, @Suggestion("crates") String crateName) {
        openCrate(player, player, crateName);
    }

    private void openCrate(CommandSender sender, Player player, String crateName) {
        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            sender.sendMessage(Messages.NOT_A_CRATE.getMessage("%Crate%", crateName));
            return;
        }

        if (this.crazyManager.isInOpeningList(player.getUniqueId())) {
            sender.sendMessage(Messages.CRATE_ALREADY_OPENED.getMessage());
            return;
        }

        CrateType type = crate.getCrateType();

        if (type == CrateType.CRATE_ON_THE_GO || type == CrateType.QUICK_CRATE || type == CrateType.FIRE_CRACKER || type == CrateType.QUAD_CRATE) {
            sender.sendMessage(Messages.CANT_BE_A_VIRTUAL_CRATE.getMessage());
            return;
        }

        FileConfiguration config = FileManager.Files.CONFIG.getFile();

        boolean hasKey = false;
        KeyType keyType = KeyType.VIRTUAL_KEY;

        if (this.userManager.getVirtualKeys(player.getUniqueId(), crate.getName()) >= 1) {
            hasKey = true;
        } else {
            if (config.getBoolean("Settings.Virtual-Accepts-Physical-Keys")) {
                if (this.userManager.hasPhysicalKey(player.getUniqueId(), crate.getName(), false)) {
                    hasKey = true;
                    keyType = KeyType.PHYSICAL_KEY;
                }
            }
        }

        if (!hasKey) {
            if (config.contains("Settings.Need-Key-Sound")) {
                Sound sound = Sound.valueOf(config.getString("Settings.Need-Key-Sound"));

                player.playSound(player.getLocation(), sound, 1f, 1f);
            }

            player.sendMessage(Messages.NO_VIRTUAL_KEY.getMessage());
            return;
        }

        if (this.methods.isInventoryFull(player)) {
            player.sendMessage(Messages.INVENTORY_FULL.getMessage());
            return;
        }

        this.crazyManager.openCrate(player, crate, keyType, player.getLocation(), true, false);

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("%Crate%", crate.getName());
        placeholders.put("%Player%", player.getName());

        sender.sendMessage(Messages.OPENED_A_CRATE.getMessage(placeholders));

        boolean logFile = config.getBoolean("Settings.Crate-Actions.Log-File");
        boolean logConsole = config.getBoolean("Settings.Crate-Actions.Log-Console");

        this.eventLogger.logKeyEvent(player, sender, crate, keyType, EventLogger.KeyEventType.KEY_EVENT_REMOVED, logFile, logConsole);
    }

    @SubCommand("mass-open")
    @Permission(value = "crazycrates.command.admin.massopen", def = PermissionDefault.OP)
    public void onAdminCrateMassOpen(Player player, @Suggestion("crates") String crateName, @Suggestion("numbers") int amount) {
        UUID uuid = player.getUniqueId();

        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            player.sendMessage(Messages.NOT_A_CRATE.getMessage("%Crate%", crateName));
            return;
        }

        this.crazyManager.addPlayerToOpeningList(uuid, crate);

        int keys = this.userManager.getVirtualKeys(uuid, crate.getName());
        int keysUsed = 0;

        if (keys == 0) {
            player.sendMessage(Messages.NO_VIRTUAL_KEY.getMessage());
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
    @Permission(value = "crazycrates.command.admin.forceopen", def = PermissionDefault.OP)
    public void onAdminForceOpen(CommandSender sender, @Suggestion("crates") String crateName, @Suggestion("online-players") Player player) {
        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            sender.sendMessage(Messages.NOT_A_CRATE.getMessage("%Crate%", crateName));
            return;
        }

        if (this.crazyManager.isInOpeningList(player.getUniqueId())) {
            sender.sendMessage(Messages.CRATE_ALREADY_OPENED.getMessage());
            return;
        }

        CrateType type = crate.getCrateType();

        if (type == CrateType.CRATE_ON_THE_GO || type == CrateType.QUICK_CRATE || type == CrateType.FIRE_CRACKER) {
            sender.sendMessage(Messages.CANT_BE_A_VIRTUAL_CRATE.getMessage());
            return;
        }

        this.crazyManager.openCrate(player, crate, KeyType.FREE_KEY, player.getLocation(), true, false);

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("%Crate%", crate.getName());
        placeholders.put("%Player%", player.getName());

        sender.sendMessage(Messages.OPENED_A_CRATE.getMessage(placeholders));

        FileConfiguration config = FileManager.Files.CONFIG.getFile();

        boolean logFile = config.getBoolean("Settings.Crate-Actions.Log-File");
        boolean logConsole = config.getBoolean("Settings.Crate-Actions.Log-Console");

        this.eventLogger.logKeyEvent(player, sender, crate, KeyType.FREE_KEY, EventLogger.KeyEventType.KEY_EVENT_REMOVED, logFile, logConsole);
    }

    @SubCommand("set")
    @Permission(value = "crazycrates.command.admin.set", def = PermissionDefault.OP)
    public void onAdminCrateSet(Player player, @Suggestion("crates") String crateName) {
        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            player.sendMessage(Messages.NOT_A_CRATE.getMessage("%Crate%", crateName));
            return;
        }

        Block block = player.getTargetBlock(null, 5);

        if (block.isEmpty()) {
            player.sendMessage(Messages.MUST_BE_LOOKING_AT_A_BLOCK.getMessage());
            return;
        }

        if (this.crazyManager.isCrateLocation(block.getLocation())) {
            player.sendMessage(this.methods.getPrefix("&cThis location already has a crate!"));
            return;
        }

        this.crazyManager.addCrateLocation(block.getLocation(), crate);

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("%Crate%", crate.getName());
        placeholders.put("%Prefix%", this.methods.getPrefix());

        player.sendMessage(Messages.CREATED_PHYSICAL_CRATE.getMessage(placeholders));
    }

    @SubCommand("give-random")
    @Permission(value = "crazycrates.command.admin.giverandomkey", def = PermissionDefault.OP)
    public void onAdminCrateGiveRandom(CommandSender sender, @Suggestion("key-types") String keyType, @Suggestion("numbers") int amount, @Suggestion("online-players") CustomPlayer player) {
        Crate crate = this.crazyManager.getCrates().get((int) this.crazyManager.pickNumber(0, (this.crazyManager.getCrates().size() - 2)));

        onAdminCrateGive(sender, keyType, crate.getName(), amount, player);
    }

    public record CustomPlayer(String name, CrazyCrates plugin) {
        public @NotNull OfflinePlayer getOfflinePlayer() {
            CompletableFuture<UUID> future = CompletableFuture.supplyAsync(() -> plugin.getServer().getOfflinePlayer(name)).thenApply(OfflinePlayer::getUniqueId);

            return plugin.getServer().getOfflinePlayer(future.join());
        }

        public Player getPlayer() {
            return plugin.getServer().getPlayer(name);
        }
    }

    private void addKey(CommandSender sender, Player player, OfflinePlayer offlinePlayer, UUID uuid, Crate crate, KeyType type, int amount) {
        PlayerReceiveKeyEvent event = new PlayerReceiveKeyEvent(uuid, crate, PlayerReceiveKeyEvent.KeyReceiveReason.GIVE_COMMAND, amount);

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
                sender.sendMessage(Messages.INTERNAL_ERROR.getMessage());
            } else {
                HashMap<String, String> placeholders = new HashMap<>();

                placeholders.put("%Amount%", String.valueOf(amount));
                placeholders.put("%Player%", offlinePlayer.getName());

                sender.sendMessage(Messages.GIVEN_OFFLINE_PLAYER_KEYS.getMessage(placeholders));

                FileConfiguration config = FileManager.Files.CONFIG.getFile();

                boolean logFile = config.getBoolean("Settings.Crate-Actions.Log-File");
                boolean logConsole = config.getBoolean("Settings.Crate-Actions.Log-Console");

                eventLogger.logKeyEvent(offlinePlayer, sender, crate, type, EventLogger.KeyEventType.KEY_EVENT_GIVEN, logFile, logConsole);

                return;
            }
        }

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("%Amount%", String.valueOf(amount));
        placeholders.put("%Player%", player.getName());
        placeholders.put("%Key%", crate.getKey().getItemMeta().getDisplayName());

        boolean fullMessage = FileManager.Files.CONFIG.getFile().getBoolean("Settings.Give-Virtual-Keys-When-Inventory-Full-Message");
        boolean inventoryCheck = FileManager.Files.CONFIG.getFile().getBoolean("Settings.Give-Virtual-Keys-When-Inventory-Full");

        sender.sendMessage(Messages.GIVEN_A_PLAYER_KEYS.getMessage(placeholders));
        if (!inventoryCheck || !fullMessage && !methods.isInventoryFull(player) && player.isOnline()) player.sendMessage(Messages.OBTAINING_KEYS.getMessage(placeholders));

        FileConfiguration config = FileManager.Files.CONFIG.getFile();

        boolean logFile = config.getBoolean("Settings.Crate-Actions.Log-File");
        boolean logConsole = config.getBoolean("Settings.Crate-Actions.Log-Console");

        eventLogger.logKeyEvent(player, sender, crate, type, EventLogger.KeyEventType.KEY_EVENT_GIVEN, logFile, logConsole);
    }

    @SubCommand("give")
    @Permission(value = "crazycrates.command.admin.givekey", def = PermissionDefault.OP)
    public void onAdminCrateGive(CommandSender sender, @Suggestion("key-types") String keyType, @Suggestion("crates") String crateName, @Suggestion("numbers") int amount, @Suggestion("online-players") CustomPlayer target) {
        KeyType type = KeyType.getFromName(keyType);
        Crate crate = this.crazyManager.getCrateFromName(crateName);

        if (type == null || type == KeyType.FREE_KEY) {
            sender.sendMessage(this.methods.color(this.methods.getPrefix() + "&cPlease use Virtual/V or Physical/P for a Key type."));
            return;
        }

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            sender.sendMessage(Messages.NOT_A_CRATE.getMessage("%Crate%", crateName));
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
        if (player != null) {
            this.userManager.takeKeys(amount, uuid, crate.getName(), type, false);
        } else {
            if (!this.userManager.takeKeys(amount, uuid, crate.getName(), type, false)) {
                sender.sendMessage(Messages.INTERNAL_ERROR.getMessage());
            } else {
                this.userManager.takeKeys(amount, uuid, crate.getName(), type, false);

                HashMap<String, String> placeholders = new HashMap<>();

                placeholders.put("%Amount%", String.valueOf(amount));
                placeholders.put("%Player%", offlinePlayer.getName());

                sender.sendMessage(Messages.TAKE_OFFLINE_PLAYER_KEYS.getMessage(placeholders));

                FileConfiguration config = FileManager.Files.CONFIG.getFile();

                boolean logFile = config.getBoolean("Settings.Crate-Actions.Log-File");
                boolean logConsole = config.getBoolean("Settings.Crate-Actions.Log-Console");

                eventLogger.logKeyEvent(offlinePlayer, sender, crate, type, EventLogger.KeyEventType.KEY_EVENT_REMOVED, logFile, logConsole);

                return;
            }
        }

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("%Amount%", String.valueOf(amount));
        placeholders.put("%Player%", player.getName());

        sender.sendMessage(Messages.TAKE_A_PLAYER_KEYS.getMessage(placeholders));

        FileConfiguration config = FileManager.Files.CONFIG.getFile();

        boolean logFile = config.getBoolean("Settings.Crate-Actions.Log-File");
        boolean logConsole = config.getBoolean("Settings.Crate-Actions.Log-Console");

        this.eventLogger.logKeyEvent(player, sender, crate, type, EventLogger.KeyEventType.KEY_EVENT_REMOVED, logFile, logConsole);
    }

    @SubCommand("take")
    @Permission(value = "crazycrates.command.admin.takekey", def = PermissionDefault.OP)
    public void onAdminCrateTake(CommandSender sender, @Suggestion("key-types") String keyType, @Suggestion("crates") String crateName, @Suggestion("numbers") int amount, @Suggestion("online-players") CustomPlayer target) {
        KeyType type = KeyType.getFromName(keyType);

        Crate crate = crazyManager.getCrateFromName(crateName);

        if (type == null || type == KeyType.FREE_KEY) {
            sender.sendMessage(methods.color(methods.getPrefix() + "&cPlease use Virtual/V or Physical/P for a Key type."));
            return;
        }

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            sender.sendMessage(Messages.NOT_A_CRATE.getMessage("%Crate%", crateName));
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
    @Permission(value = "crazycrates.command.admin.giveall", def = PermissionDefault.OP)
    public void onAdminCrateGiveAllKeys(CommandSender sender, @Suggestion("key-types") @ArgName("key-type") String keyType, @Suggestion("crates") @ArgName("crate-name") String crateName, @Suggestion("numbers") int amount) {
        KeyType type = KeyType.getFromName(keyType);

        if (type == null || type == KeyType.FREE_KEY) {
            sender.sendMessage(methods.color(methods.getPrefix() + "&cPlease use Virtual/V or Physical/P for a Key type."));
            return;
        }

        Crate crate = crazyManager.getCrateFromName(crateName);

        if (crate == null || crate.getCrateType() == CrateType.MENU) {
            sender.sendMessage(Messages.NOT_A_CRATE.getMessage("%Crate%", crateName));
            return;
        }

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("%Amount%", amount + "");
        placeholders.put("%Key%", crate.getKey().getItemMeta().getDisplayName());

        sender.sendMessage(Messages.GIVEN_EVERYONE_KEYS.getMessage(placeholders));

        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            if (methods.permCheck(onlinePlayer, Permissions.CRAZY_CRATES_PLAYER_EXCLUDE_GIVE_ALL, true)) continue;

            PlayerReceiveKeyEvent event = new PlayerReceiveKeyEvent(onlinePlayer.getUniqueId(), crate, PlayerReceiveKeyEvent.KeyReceiveReason.GIVE_ALL_COMMAND, amount);
            onlinePlayer.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                onlinePlayer.sendMessage(Messages.OBTAINING_KEYS.getMessage(placeholders));

                if (crate.getCrateType() == CrateType.CRATE_ON_THE_GO) {
                    onlinePlayer.getInventory().addItem(crate.getKey(amount));
                    return;
                }

                this.userManager.addKeys(amount, onlinePlayer.getUniqueId(), crate.getName(), type);

                boolean logFile = FileManager.Files.CONFIG.getFile().getBoolean("Settings.Crate-Actions.Log-File");
                boolean logConsole = FileManager.Files.CONFIG.getFile().getBoolean("Settings.Crate-Actions.Log-Console");

                eventLogger.logKeyEvent(onlinePlayer, sender, crate, type, EventLogger.KeyEventType.KEY_EVENT_GIVEN, logFile, logConsole);
            }
        }
    }
}