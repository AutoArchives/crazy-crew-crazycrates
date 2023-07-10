package com.badbones69.crazycrates.commands.v2;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.CommandContext;
import com.badbones69.crazycrates.commands.engine.CommandEngine;
import com.badbones69.crazycrates.commands.engine.reqs.CommandRequirementsBuilder;
import com.badbones69.crazycrates.commands.engine.sender.args.Argument;
import com.badbones69.crazycrates.api.configs.types.Locale;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.commands.v2.args.PlayerArgument;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class KeyBaseCommand extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final SettingsManager config = plugin.getApiManager().getPluginConfig();
    private final SettingsManager locale = plugin.getApiManager().getLocale();

    public KeyBaseCommand() {
        super();

        this.prefix = "keys";

        this.optionalArgs.add(new Argument("player", 0, new PlayerArgument()));

        this.requirements = new CommandRequirementsBuilder()
                .asPlayer(true)
                .build();
    }

    @Override
    protected void perform(CommandContext context) {
        if (!context.getArgs().isEmpty()) {
            Player player = context.getArgAsPlayer(0, false, this.locale.getProperty(Locale.TARGET_NOT_ONLINE), "\\{player}");

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

            OfflinePlayer offlinePlayer = context.getArgAsOfflinePlayer(0, true, this.locale.getProperty(Locale.TARGET_NOT_ONLINE), "\\{player}");

            context.reply("Offline Player: " + offlinePlayer.getName());

            return;
        }

        context.reply("Guten Tag!");
    }
}