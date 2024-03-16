package com.badbones69.crazycrates.commands;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.commands.relations.ArgumentRelations;
import com.badbones69.crazycrates.commands.crates.types.CommandHelp;
import com.badbones69.crazycrates.commands.crates.types.admin.CommandAdmin;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandManager {

    private final static @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final static @NotNull BukkitCommandManager<CommandSender> commandManager = BukkitCommandManager.create(plugin);

    public static void load() {
        new ArgumentRelations().build();

        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();

        getCommandManager().registerSuggestion(SuggestionKey.of("players"), (sender, context) -> players.stream().map(Player::getName).toList());

        getCommandManager().registerSuggestion(SuggestionKey.of("numbers"), (sender, context) -> {
            List<String> numbers = new ArrayList<>();

            for (int i = 1; i <= 64; i++) numbers.add(String.valueOf(i));

            return numbers;
        });

        List.of(
                // Admin commands
                new CommandAdmin(),

                // Player commands
                new CommandHelp()
        ).forEach(getCommandManager()::registerCommand);
    }

    public static @NotNull BukkitCommandManager<CommandSender> getCommandManager() {
        return commandManager;
    }
}