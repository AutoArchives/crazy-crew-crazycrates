package us.crazycrew.crazycrates.paper.commands.subs.player;

import org.bukkit.plugin.java.JavaPlugin;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.enums.settings.Messages;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import com.google.common.collect.Lists;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Command(value = "keys", alias = {"key"})
public class BaseKeyCommand extends BaseCommand {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesLoader cratesLoader = this.plugin.getCratesLoader();
    private final @NotNull CrazyManager crazyManager = this.cratesLoader.getCrazyManager();

    @Default
    @Permission("crazycrates.command.player.key")
    public void viewPersonal(Player player) {
        String header = Messages.PERSONAL_HEADER.getMessageNoPrefix();

        String noKeys = Messages.PERSONAL_NO_VIRTUAL_KEYS.getMessage();

        getKeys(player.getUniqueId(), player, header, noKeys);
    }

    @SubCommand("view")
    @Permission("crazycrates.command.player.key.others")
    public void viewOthers(CommandSender sender, @Suggestion ("online-players") Player target) {
        if (target == sender) {
            sender.sendMessage(Messages.SAME_PLAYER.getMessage());
            return;
        }

        String header = Messages.OTHER_PLAYER_HEADER.getMessageNoPrefix("%Player%", target.getName());

        String otherPlayer = Messages.OTHER_PLAYER_NO_VIRTUAL_KEYS.getMessage("%Player%", target.getName());

        getKeys(target.getUniqueId(), sender, header, otherPlayer);
    }

    private void getKeys(UUID uuid, CommandSender sender, String header, String messageContent) {
        List<String> message = Lists.newArrayList();

        message.add(header);

        Map<Crate, Integer> keys = crazyManager.getVirtualKeys(uuid);

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