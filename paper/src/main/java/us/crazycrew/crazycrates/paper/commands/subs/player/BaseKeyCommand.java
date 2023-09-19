package us.crazycrew.crazycrates.paper.commands.subs.player;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import us.crazycrew.crazycrates.common.config.types.PluginConfig;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.enums.Translation;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyHandler;
import java.util.HashMap;
import java.util.List;

@Command(value = "keys", alias = {"key"})
public class BaseKeyCommand extends BaseCommand {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyHandler crazyHandler = this.plugin.getCrazyHandler();
    private final @NotNull CrazyManager crazyManager = this.crazyHandler.getCrazyManager();

    @Default
    @Permission(value = "crazycrates.key", def = PermissionDefault.TRUE)
    public void viewPersonal(Player player) {
        Translation personalHeader = Translation.command_keys_personal_virtual_keys_header.getMessage();

        Translation personalKeys = Translation.command_keys_personal_no_virtual_keys.getMessage();

        getKeys(player, player, personalHeader.toListComponent(), personalKeys.toComponent(), personalHeader.toListString(), personalKeys.toString());
    }

    @SubCommand("view")
    @Permission(value = "crazycrates.key-others", def = PermissionDefault.TRUE)
    public void viewOthers(CommandSender sender, @Suggestion ("online-players") Player target) {
        if (target == sender) {
            sender.sendMessage(Translation.same_player.getMessage().toComponent());
            return;
        }

        Translation otherHeader = Translation.command_keys_other_player_virtual_keys_header.getMessage("{player}", target.getName());

        Translation otherKeys = Translation.command_keys_other_player_no_virtual_keys.getMessage("{player}", target.getName());

        getKeys(target, sender, otherHeader.toListComponent(), otherKeys.toComponent(), otherHeader.toListString(), otherKeys.toString());
    }

    //TODO() Remove the mini message if check in 2.1
    private void getKeys(Player player, CommandSender sender, List<Component> header, Component messageContent, List<String> legacyHeader, String legacyContent) {
        boolean useMiniMessage = this.crazyHandler.getConfigManager().getPluginConfig().getProperty(PluginConfig.use_mini_message);

        HashMap<Crate, Integer> keys = new HashMap<>();

        this.crazyManager.getCrates().forEach(crate -> keys.put(crate, this.crazyHandler.getUserManager().getVirtualKeys(player.getUniqueId(), crate.getName())));

        boolean hasKeys = false;

        if (useMiniMessage) {
            List<Component> message = Lists.newArrayList();

            message.addAll(header);

            for (Crate crate : keys.keySet()) {
                int amount = keys.get(crate);

                if (amount > 0) {
                    HashMap<String, String> placeholders = new HashMap<>();

                    hasKeys = true;

                    placeholders.put("{crate}", crate.getFile().getString("Crate.Name"));
                    placeholders.put("{keys}", String.valueOf(amount));
                    message.add(Translation.command_keys_crate_format.getMessage(placeholders).toComponent());
                }
            }

            if (hasKeys) {
                message.forEach(sender::sendMessage);
                return;
            }

            sender.sendMessage(messageContent);

            return;
        }

        List<String> message = Lists.newArrayList();

        message.addAll(legacyHeader);

        for (Crate crate : keys.keySet()) {
            int amount = keys.get(crate);

            if (amount > 0) {
                HashMap<String, String> placeholders = new HashMap<>();

                hasKeys = true;

                placeholders.put("{crate}", crate.getFile().getString("Crate.Name"));
                placeholders.put("{keys}", String.valueOf(amount));
                message.add(Translation.command_keys_crate_format.getMessage(placeholders).toString());
            }
        }

        if (hasKeys) {
            message.forEach(sender::sendMessage);
            return;
        }

        sender.sendMessage(legacyContent);
    }
}