package us.crazycrew.crazycrates.paper;

import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.enums.settings.Messages;
import us.crazycrew.crazycrates.paper.api.managers.QuadCrateManager;
import us.crazycrew.crazycrates.paper.api.objects.CrateLocation;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;
import us.crazycrew.crazycrates.paper.commands.subs.CrateBaseCommand;
import us.crazycrew.crazycrates.paper.commands.subs.player.BaseKeyCommand;
import us.crazycrew.crazycrates.paper.cratetypes.CSGO;
import us.crazycrew.crazycrates.paper.cratetypes.Cosmic;
import us.crazycrew.crazycrates.paper.cratetypes.CrateOnTheGo;
import us.crazycrew.crazycrates.paper.cratetypes.FireCracker;
import us.crazycrew.crazycrates.paper.cratetypes.QuadCrate;
import us.crazycrew.crazycrates.paper.cratetypes.QuickCrate;
import us.crazycrew.crazycrates.paper.cratetypes.Roulette;
import us.crazycrew.crazycrates.paper.cratetypes.War;
import us.crazycrew.crazycrates.paper.cratetypes.Wheel;
import us.crazycrew.crazycrates.paper.cratetypes.Wonder;
import us.crazycrew.crazycrates.paper.listeners.BrokeLocationsListener;
import us.crazycrew.crazycrates.paper.listeners.CrateControlListener;
import us.crazycrew.crazycrates.paper.listeners.FireworkDamageListener;
import us.crazycrew.crazycrates.paper.listeners.MenuListener;
import us.crazycrew.crazycrates.paper.listeners.MiscListener;
import us.crazycrew.crazycrates.paper.listeners.PreviewListener;
import us.crazycrew.crazycrates.paper.support.libraries.PluginSupport;
import us.crazycrew.crazycrates.paper.support.placeholders.PlaceholderAPISupport;
import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.message.MessageKey;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class CrazyCrates extends JavaPlugin {

    private final BukkitCommandManager<CommandSender> manager = BukkitCommandManager.create(this);

    private CrazyCratesLoader cratesLoader;

    @Override
    public void onEnable() {
        this.cratesLoader = new CrazyCratesLoader(getDataFolder());
        getCratesLoader().enable();

        // Add extra messages.
        Messages.addMissingMessages();

        enable();
    }

    @Override
    public void onDisable() {
        QuadCrateManager.getCrateSessions().forEach(session -> session.endCrateForce(false));
        QuadCrateManager.clearSessions();

        getQuickCrate().removeAllRewards();

        if (getCratesLoader().getCrazyManager().getHologramController() != null) getCratesLoader().getCrazyManager().getHologramController().removeAllHolograms();
    }

    public @NotNull CrazyCratesLoader getCratesLoader() {
        return this.cratesLoader;
    }

    private War war;
    private CSGO csgo;
    private Wheel wheel;
    private Wonder wonder;
    private Cosmic cosmic;
    private Roulette roulette;
    private QuickCrate quickCrate;

    public War getWar() {
        return this.war;
    }

    public CSGO getCsgo() {
        return this.csgo;
    }

    public Wheel getWheel() {
        return this.wheel;
    }

    public Wonder getWonder() {
        return this.wonder;
    }

    public Cosmic getCosmic() {
        return this.cosmic;
    }

    public Roulette getRoulette() {
        return this.roulette;
    }

    public QuickCrate getQuickCrate() {
        return this.quickCrate;
    }

    private FireCracker fireCracker;

    public FireCracker getFireCracker() {
        return this.fireCracker;
    }

    private void enable() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new PreviewListener(), this);
        pluginManager.registerEvents(new MenuListener(), this);
        pluginManager.registerEvents(new FireworkDamageListener(), this);
        pluginManager.registerEvents(new CrateControlListener(), this);
        pluginManager.registerEvents(new MiscListener(), this);

        pluginManager.registerEvents(this.war = new War(), this);
        pluginManager.registerEvents(this.csgo = new CSGO(), this);
        pluginManager.registerEvents(this.wheel = new Wheel(), this);
        pluginManager.registerEvents(this.wonder = new Wonder(), this);
        pluginManager.registerEvents(this.cosmic = new Cosmic(), this);
        pluginManager.registerEvents(this.roulette = new Roulette(), this);
        pluginManager.registerEvents(this.quickCrate = new QuickCrate(), this);
        pluginManager.registerEvents(new CrateOnTheGo(), this);
        pluginManager.registerEvents(new QuadCrate(), this);

        this.fireCracker = new FireCracker();

        getCratesLoader().getCrazyManager().load(true);

        if (!getCratesLoader().getCrazyManager().getBrokeCrateLocations().isEmpty()) pluginManager.registerEvents(new BrokeLocationsListener(), this);

        if (PluginSupport.PLACEHOLDERAPI.isPluginEnabled()) new PlaceholderAPISupport().register();

        this.manager.registerMessage(MessageKey.UNKNOWN_COMMAND, (sender, context) -> sender.sendMessage(Messages.UNKNOWN_COMMAND.getMessage()));

        this.manager.registerMessage(MessageKey.TOO_MANY_ARGUMENTS, (sender, context) -> {
            String command = context.getCommand();
            String subCommand = context.getSubCommand();

            String commandOrder = "/" + command + " " + subCommand + " ";

            String correctUsage = null;

            switch (command) {
                case "crates" -> correctUsage = getString(subCommand, commandOrder);
                case "keys" -> {
                    if (subCommand.equals("view")) correctUsage = "/keys " + subCommand;
                }
            }

            if (correctUsage != null) sender.sendMessage(Messages.CORRECT_USAGE.getMessage().replace("%usage%", correctUsage));
        });

        this.manager.registerMessage(MessageKey.NOT_ENOUGH_ARGUMENTS, (sender, context) -> {
            String command = context.getCommand();
            String subCommand = context.getSubCommand();

            String commandOrder = "/" + command + " " + subCommand + " ";

            String correctUsage = null;

            switch (command) {
                case "crates" -> correctUsage = getString(subCommand, commandOrder);
                case "keys" -> {
                    if (subCommand.equals("view")) correctUsage = "/keys " + subCommand + " <player-name>";
                }
            }

            if (correctUsage != null) sender.sendMessage(Messages.CORRECT_USAGE.getMessage().replace("%usage%", correctUsage));
        });

        this.manager.registerMessage(MessageKey.INVALID_ARGUMENT, (sender, context) -> sender.sendMessage(Messages.NOT_ONLINE.getMessage().replace("%player%", context.getTypedArgument())));

        this.manager.registerMessage(BukkitMessageKey.NO_PERMISSION, (sender, context) -> sender.sendMessage(Messages.NO_PERMISSION.getMessage()));

        this.manager.registerMessage(BukkitMessageKey.PLAYER_ONLY, (sender, context) -> sender.sendMessage(Messages.MUST_BE_A_PLAYER.getMessage()));

        this.manager.registerMessage(BukkitMessageKey.CONSOLE_ONLY, (sender, context) -> sender.sendMessage(Messages.MUST_BE_A_CONSOLE_SENDER.getMessage()));

        this.manager.registerSuggestion(SuggestionKey.of("crates"), (sender, context) -> getCratesLoader().getFileManager().getAllCratesNames(this).stream().toList());

        this.manager.registerSuggestion(SuggestionKey.of("key-types"), (sender, context) -> KEYS);

        this.manager.registerSuggestion(SuggestionKey.of("online-players"), (sender, context) -> getServer().getOnlinePlayers().stream().map(Player::getName).toList());

        this.manager.registerSuggestion(SuggestionKey.of("locations"), (sender, context) -> getCratesLoader().getCrazyManager().getCrateLocations().stream().map(CrateLocation::getID).toList());

        this.manager.registerSuggestion(SuggestionKey.of("prizes"), (sender, context) -> {
            List<String> numbers = new ArrayList<>();

            getCratesLoader().getCrazyManager().getCrateFromName(context.getArgs().get(0)).getPrizes().forEach(prize -> numbers.add(prize.getName()));

            return numbers;
        });

        this.manager.registerSuggestion(SuggestionKey.of("numbers"), (sender, context) -> {
            List<String> numbers = new ArrayList<>();

            for (int i = 1; i <= 250; i++) numbers.add(i + "");

            return numbers;
        });

        this.manager.registerArgument(CrateBaseCommand.CustomPlayer.class, (sender, context) -> new CrateBaseCommand.CustomPlayer(context, this));

        this.manager.registerCommand(new BaseKeyCommand());
        this.manager.registerCommand(new CrateBaseCommand());

        printHooks();
    }

    private String getString(String subCommand, String commandOrder) {
        String correctUsage = null;

        switch (subCommand) {
            case "transfer" -> correctUsage = commandOrder + "<crate-name> " + "<player-name> " + "<amount>";
            case "debug", "open", "set" -> correctUsage = commandOrder + "<crate-name>";
            case "tp" -> correctUsage = commandOrder + "<id>";
            case "additem" -> correctUsage = commandOrder + "<crate-name> " + "<prize-number>";
            case "preview", "open-others", "forceopen" -> correctUsage = commandOrder + "<crate-name> " + "<player-name>";
            case "mass-open" -> correctUsage = commandOrder + "<crate-name> " + "<amount>";
            case "give-random" -> correctUsage = commandOrder + "<key-type> " + "<amount> " + "<player-name>";
            case "give", "take" -> correctUsage = commandOrder + "<key-type> " + "<crate-name> " + "<amount> " + "<player-name>";
            case "giveall" -> correctUsage = commandOrder + "<key-type> " + "<crate-name> " + "<amount>";
        }

        return correctUsage;
    }

    private static final List<String> KEYS = List.of("virtual", "v", "physical", "p");

    private void printHooks() {
        for (PluginSupport value : PluginSupport.values()) {
            if (value.isPluginEnabled()) {
                FancyLogger.info("<gold>" + value.name() + "</gold> <bold><green>FOUND</green></bold>");
            } else {
                FancyLogger.info("<gold>" + value.name() + "</gold> <bold><red>NOT FOUND</red></bold>");
            }
        }
    }
}