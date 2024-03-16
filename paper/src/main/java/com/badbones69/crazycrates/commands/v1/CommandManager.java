package com.badbones69.crazycrates.commands.v1;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.utils.FileUtils;
import com.badbones69.crazycrates.commands.v1.subs.CrateBaseCommand;
import com.badbones69.crazycrates.commands.v1.subs.BaseKeyCommand;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManager {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final @NotNull BukkitCommandManager<CommandSender> bukkitCommandManager = null;

    /**
     * Loads commands.
     */
    public void load() {
        this.bukkitCommandManager.registerSuggestion(SuggestionKey.of("crates"), (sender, context) -> {
            List<String> crates = new ArrayList<>(FileUtils.getAllCratesNames());

            crates.add("Menu");

            return crates;
        });

        this.bukkitCommandManager.registerSuggestion(SuggestionKey.of("key-types"), (sender, context) -> List.of("virtual", "v", "physical", "p"));

        this.bukkitCommandManager.registerSuggestion(SuggestionKey.of("online-players"), (sender, context) -> this.plugin.getServer().getOnlinePlayers().stream().map(Player::getName).toList());

        //this.bukkitCommandManager.registerSuggestion(SuggestionKey.of("locations"), (sender, context) -> this.plugin.getCrateManager().getCrateLocations().stream().map(CrateLocation::getID).toList());

        this.bukkitCommandManager.registerSuggestion(SuggestionKey.of("locations"), (sender, context) -> Collections.emptyList());

        this.bukkitCommandManager.registerSuggestion(SuggestionKey.of("prizes"), (sender, context) -> {
            List<String> numbers = new ArrayList<>();

            //this.plugin.getCrateManager().getCrateFromName(context.getArgs().get(0)).getPrizes().forEach(prize -> numbers.add(prize.getPrizeNumber()));

            return numbers;
        });

        this.bukkitCommandManager.registerSuggestion(SuggestionKey.of("tiers"), (sender, context) -> {
            List<String> numbers = new ArrayList<>();

            //this.plugin.getCrateManager().getCrateFromName(context.getArgs().get(0)).getTiers().forEach(tier -> numbers.add(tier.getName()));

            return numbers;
        });

        this.bukkitCommandManager.registerSuggestion(SuggestionKey.of("numbers"), (sender, context) -> {
            List<String> numbers = new ArrayList<>();

            for (int i = 1; i <= 100; i++) numbers.add(String.valueOf(i));

            return numbers;
        });

        //this.bukkitCommandManager.registerArgument(CrateBaseCommand.CustomPlayer.class, (sender, context) -> new CrateBaseCommand.CustomPlayer(context));

        this.bukkitCommandManager.registerCommand(new CrateBaseCommand());
        this.bukkitCommandManager.registerCommand(new BaseKeyCommand());
    }
}