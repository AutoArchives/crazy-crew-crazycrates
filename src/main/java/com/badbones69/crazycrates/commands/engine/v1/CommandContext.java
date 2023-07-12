package com.badbones69.crazycrates.commands.engine.v1;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.ApiManager;
import com.badbones69.crazycrates.commands.engine.v1.builder.CommandHelpEntry;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.CommandArgs;
import com.badbones69.crazycrates.api.support.InternalPlaceholderSupport;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommandContext implements CommandArgs {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final ApiManager apiManager = this.plugin.getApiManager();
    private final SettingsManager locale = this.apiManager.getLocale();
    private final InternalPlaceholderSupport placeholderSupport = this.apiManager.getPlaceholderSupport();

    private final CommandSender sender;
    private String alias;
    private final ArrayList<String> args;
    private final CommandHelpEntry helpEntry;

    private Player player;

    public CommandContext(CommandSender sender, String alias, ArrayList<String> args, CommandHelpEntry helpEntry) {
        this.sender = sender;

        if (sender instanceof Player) {
            this.player = (Player) sender;
        }

        this.alias = alias;
        this.args = args;
        this.helpEntry = helpEntry;
    }

    @Override
    public int getArgAsInt(int index, boolean notifySender) {
        Integer value = null;

        try {
            value = Integer.parseInt(this.args.get(index));
        } catch (NumberFormatException exception) {
            //if (notifySender) reply(this.placeholderSupport.setPlaceholders(this.locale.getProperty(Locale.INVALID_ACTION)
                    //.replaceAll("\\{value}", this.args.get(index))
                    //.replaceAll("\\{action}", "number")));
        }

        if (value != null) return value;

        return 1;
    }

    @Override
    public long getArgAsLong(int index, boolean notifySender) {
        Long value = null;

        try {
            value = Long.parseLong(this.args.get(index));
        } catch (NumberFormatException exception) {
            //if (notifySender) reply(this.placeholderSupport.setPlaceholders(this.locale.getProperty(Locale.INVALID_ACTION)
                    //.replaceAll("\\{value}", this.args.get(index))
                    //.replaceAll("\\{action}", "number")));
        }

        if (value != null) return value;

        return 1L;
    }

    @Override
    public double getArgAsDouble(int index, boolean notifySender) {
        Double value = null;

        try {
            value = Double.parseDouble(this.args.get(index));
        } catch (NumberFormatException exception) {
            //if (notifySender) reply(this.placeholderSupport.setPlaceholders(this.locale.getProperty(Locale.INVALID_ACTION)
                    //.replaceAll("\\{value}", this.args.get(index))
                    //.replaceAll("\\{action}", "number")));
        }

        if (value != null) return value;

        return 0.1;
    }

    @Override
    public boolean getArgAsBoolean(int index, boolean notifySender) {
        String lowercase = this.args.get(index).toLowerCase();

        switch (lowercase) {
            case "true", "on", "1" -> {
                return true;
            }
            case "false", "off", "0" -> {
                return false;
            }
            default -> {
                //if (notifySender) {
                    //reply(this.placeholderSupport.setPlaceholders(this.locale.getProperty(Locale.INVALID_ACTION)
                            //.replaceAll("\\{value}", this.args.get(index).toLowerCase())
                            //.replaceAll("\\{action}", "boolean")));
                //}

                return false;
            }
        }
    }

    @Override
    public float getArgAsFloat(int index, boolean notifySender) {
        Float value = null;

        try {
            value = Float.parseFloat(this.args.get(index));
        } catch (NumberFormatException exception) {
            //if (notifySender) reply(this.placeholderSupport.setPlaceholders(this.locale.getProperty(Locale.INVALID_ACTION)
                    //.replaceAll("\\{value}", this.args.get(index))
                    //.replaceAll("\\{action}", "number")));
        }

        if (value != null) return value;

        return 1F;
    }

    @Override
    public Player getArgAsPlayer(int index, boolean notifySender) {
        Player player = this.plugin.getServer().getPlayer(this.args.get(index));

        if (player == null) {
            //if (notifySender) reply(this.placeholderSupport.setPlaceholders(this.locale.getProperty(Locale.INVALID_ACTION)
                    //.replaceAll("\\{value}", this.args.get(index))
                    //.replaceAll("\\{action}", "player")));

            return null;
        }

        return player;
    }

    @Override
    public OfflinePlayer getArgAsOfflinePlayer(int index) {
        CompletableFuture<UUID> future = CompletableFuture.supplyAsync(() -> this.plugin.getServer().getOfflinePlayer(this.args.get(index))).thenApply(OfflinePlayer::getUniqueId);

        return this.plugin.getServer().getOfflinePlayer(future.join());
    }
}