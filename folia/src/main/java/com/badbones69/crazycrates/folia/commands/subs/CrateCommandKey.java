package com.badbones69.crazycrates.folia.commands.subs;

/*
import com.badbones69.crazycrates.api.enums.Permissions;
import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.CrazyManager;
import com.badbones69.crazycrates.paper.api.enums.settings.Messages;
import com.badbones69.crazycrates.paper.api.objects.Crate;
import com.google.common.collect.Lists;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandContext;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandEngine;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CrateCommandKey extends BukkitCommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final CrazyManager crazyManager = plugin.getCrazyManager();

    @SuppressWarnings("deprecation")
    public CrateCommandKey() {
        super("key", Permissions.PLAYER_HELP.getDescription(), "/crazycrates:key", Collections.emptyList());

        setPermission(Permissions.PLAYER_HELP.getBuiltPermission());

        setPermissionMessage(Messages.NO_PERMISSION.getMessage());
    }

    @Override
    public void perform(BukkitCommandContext context, String[] args) {
        if (args.length == 0) {
            if (!context.isPlayer()) {
                context.sendLegacyMessage(Messages.MUST_BE_A_PLAYER.getMessage());
                return;
            }

            String header = Messages.PERSONAL_HEADER.getMessageNoPrefix();
            String emptyKeys = Messages.PERSONAL_NO_VIRTUAL_KEYS.getMessage();

            getKeys(context.getPlayer(), context.getPlayer(), header, emptyKeys);

            return;
        }

        Player player = this.plugin.getServer().getPlayer(args[0]);

        if (player == null || !player.isOnline()) {
            context.sendLegacyMessage(Messages.NOT_ONLINE.getMessage("%player%", args[0]));
            return;
        }

        if (context.getSender() == player) {
            context.sendLegacyMessage(Messages.SAME_PLAYER.getMessage());
            return;
        }

        String otherPlayerHead = Messages.OTHER_PLAYER_HEADER.getMessageNoPrefix("%player%", player.getName());
        String otherPlayerEmptyKeys = Messages.OTHER_PLAYER_NO_VIRTUAL_KEYS.getMessage("%player%", player.getName());

        getKeys(player, context.getSender(), otherPlayerHead, otherPlayerEmptyKeys);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 2) return Collections.emptyList();

        ArrayList<String> completions = new ArrayList<>();

        this.plugin.getServer().getOnlinePlayers().forEach(player -> {
            if (player == sender) return;

            completions.add(player.getName());
        });

        return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
    }

    private void getKeys(Player target, CommandSender sender, String header, String messageContent) {
        List<String> message = Lists.newArrayList();

        message.add(header);

        HashMap<Crate, Integer> keys = this.crazyManager.getVirtualKeys(target);

        boolean hasKeys = false;

        for (Crate crate : keys.keySet()) {
            int amount = keys.get(crate);

            if (amount > 0) {
                hasKeys = true;
                HashMap<String, String> placeholders = new HashMap<>();
                placeholders.put("%Crate%", crate.getFile().getString("Crate.Name"));
                placeholders.put("%Keys%", amount + "");
                message.add(Messages.PER_CRATE.getMessageNoPrefix(placeholders));
            }
        }

        if (hasKeys) {
            sender.sendMessage(Messages.convertList(message));
        } else {
            sender.sendMessage(messageContent);
        }
    }
}
 */