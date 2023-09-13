package us.crazycrew.crazycrates.paper.commands.subs.player;

import net.kyori.adventure.text.Component;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;
import java.util.List;
import java.util.UUID;

@Command(value = "keys", alias = {"key"})
public class BaseKeyCommand extends BaseCommand {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesLoader cratesLoader = this.plugin.getCratesLoader();
    private final @NotNull CrazyManager crazyManager = this.cratesLoader.getCrazyManager();

    @Default
    @Permission(value = "crazycrates.key", def = PermissionDefault.TRUE)
    public void viewPersonal(Player player) {
        //Messages1 header = Messages1.command_keys_personal_virtual_keys_header.getMessage();

        //Messages1 noKeys = Messages1.command_keys_personal_no_virtual_keys.getMessage();

        //getKeys(player.getUniqueId(), player, header.toListComponent(), noKeys.toComponent());
    }

    @SubCommand("view")
    @Permission(value = "crazycrates.key-others", def = PermissionDefault.TRUE)
    public void viewOthers(CommandSender sender, @Suggestion ("online-players") Player target) {
        if (target == sender) {
            //sender.sendMessage(Messages1.same_player.getMessage().toComponent());
            return;
        }

        //Messages1 header = Messages1.command_keys_other_player_virtual_keys_header.getMessage("\\{player}", target.getName());

        //Messages1.command_keys_other_player_no_virtual_keys.getMessage("\\{player}", target.getName());

        //Messages1 otherPlayer = Messages1.command_keys_other_player_no_virtual_keys.getMessage("\\{player}", target.getName());

        //getKeys(target.getUniqueId(), sender, header.toListComponent(), otherPlayer.toComponent());
    }

    private void getKeys(UUID uuid, CommandSender sender, List<Component> header, Component messageContent) {
        header.forEach(sender::sendMessage);

        sender.sendMessage(messageContent);

        /*List<String> message = Lists.newArrayList();

        message.add(header);

        Map<Crate, Integer> keys = new HashMap<>();

        for (Crate crate : this.crazyManager.getCrates()) {
            keys.put(crate, this.cratesLoader.getUserManager().getVirtualKeys(uuid, crate.getName()));
        }

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
        }*/
    }
}