package com.badbones69.crazycrates.commands.v2;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.utils.MiscUtils;
import com.badbones69.crazycrates.commands.engine.CommandContext;
import com.badbones69.crazycrates.commands.engine.CommandEngine;
import com.badbones69.crazycrates.commands.engine.requirements.CommandRequirementsBuilder;
import com.badbones69.crazycrates.commands.engine.sender.args.Argument;
import com.badbones69.crazycrates.commands.engine.sender.args.builder.custom.PlayerArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KeyBaseCommand extends CommandEngine implements TabCompleter, CommandExecutor {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final SettingsManager config = plugin.getApiManager().getPluginConfig();
    private final SettingsManager locale = plugin.getApiManager().getLocale();

    public KeyBaseCommand() {
        this.prefix = "keys";

        this.optionalArgs.add(new Argument("player", 0, new PlayerArgument()));

        this.requirements = new CommandRequirementsBuilder()
                .asPlayer(true)
                .build();
    }

    @Override
    protected void perform(CommandContext context) {
        /*if (!context.getArgs().isEmpty()) {
            Player player = context.getArgAsPlayer(0, false);

            if (player != null && player.isOnline()) {
                //this.locale.getProperty(Locale.NO_VIRTUAL_KEYS_OTHER_HEADER).forEach(line -> context.reply(line.replaceAll("\\{player}", player.getName())));

                boolean hasKeys = false;

                for (Crate crate : this.plugin.getApiManager().getCrateManager().getCrates()) {
                    int amount = this.plugin.getApiManager().getUserManager().getUser(player.getUniqueId(), crate).getKey(crate);

                    if (amount > 0) {
                        hasKeys = true;
                    }
                }

                context.reply("Player: " + player.getName());
                return;
            }

            context.reply(this.locale.getProperty(Locale.GRABBING_OFFLINE_PLAYER));

            OfflinePlayer offlinePlayer = context.getArgAsOfflinePlayer(0);

            context.reply("Offline Player: " + offlinePlayer.getName());

            return;
        }

        context.reply("Guten Tag!");*/
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        CommandContext context =
                new CommandContext(
                        sender,
                        "",
                        new ArrayList<>(Arrays.asList(args))
                );

        this.execute(context);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (requirements.isPlayer() && !MiscUtils.hasPermission((HumanEntity) sender, this.requirements.getPermission())) return Collections.emptyList();

        return handleTabComplete(args);
    }
}