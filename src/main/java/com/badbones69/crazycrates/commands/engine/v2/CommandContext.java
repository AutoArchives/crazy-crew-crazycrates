package com.badbones69.crazycrates.commands.engine.v2;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.v2.builders.CommandActor;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.CommandArgs;
import com.ryderbelserion.stick.core.utils.AdventureUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommandContext implements CommandActor, CommandArgs {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final Audience audience;
    private final ArrayList<String> args;

    private Player player;

    public CommandContext(Audience audience, ArrayList<String> args) {
        this.audience = audience;

        if (audience instanceof Player) {
            this.player = (Player) audience;
        }

        this.args = args;
    }

    @Override
    public void reply(String message) {
        if (message.isBlank() || message.isEmpty()) return;

        Component component = AdventureUtils.parse(message);

        this.audience.sendMessage(component);
    }

    @Override
    public void reply(boolean hasPrefix, String prefix, String message) {
        if (message.isBlank() || message.isEmpty()) return;

        if (hasPrefix) {
            Component component = AdventureUtils.parse(prefix).append(AdventureUtils.parse(prefix));

            this.audience.sendMessage(component);

            return;
        }

        Component component = AdventureUtils.parse(message);

        this.audience.sendMessage(component);
    }

    @Override
    public void reply(boolean hasPrefix, String prefix, Component component) {
        if (hasPrefix) {
            this.audience.sendMessage(AdventureUtils.parse(prefix).append(component));
            return;
        }

        this.audience.sendMessage(component);
    }

    @Override
    public void reply(Component component) {
        this.audience.sendMessage(component);
    }

    @Override
    public void send(Audience audience, String message) {
        if (message.isBlank() || message.isEmpty()) return;

        Component component = AdventureUtils.parse(message);

        audience.sendMessage(component);
    }

    @Override
    public void send(Audience audience, Component component) {
        audience.sendMessage(component);
    }

    @Override
    public void send(Audience audience, String message, String prefix, boolean hasPrefix) {
        if (hasPrefix) {
            audience.sendMessage(AdventureUtils.parse(prefix).append(AdventureUtils.parse(message)));
            return;
        }

        send(audience, message);
    }

    @Override
    public void send(Audience audience, Component message, String prefix, boolean hasPrefix) {
        if (hasPrefix) {
            audience.sendMessage(AdventureUtils.parse(prefix).append(message));
            return;
        }

        send(audience, message);
    }

    @Override
    public Audience getSender() {
        return this.audience;
    }

    @Override
    public boolean isPlayer() {
        return getPlayer() != null;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return this.player.hasPermission(permission);
    }

    @Override
    public boolean hasPermission(String rawPermission) {
        return this.player.hasPermission(rawPermission);
    }

    public List<String> getArgs() {
        return Collections.unmodifiableList(this.args);
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