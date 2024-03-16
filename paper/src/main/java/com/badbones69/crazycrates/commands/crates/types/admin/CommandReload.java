package com.badbones69.crazycrates.commands.crates.types.admin;

import com.badbones69.crazycrates.commands.crates.BaseCommand;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

public class CommandReload extends BaseCommand {

    @Command("reload")
    @Permission(value = "crazycrates.reload", def = PermissionDefault.OP)
    public void reload(CommandSender sender) {
        /*ConfigManager.reload();

        this.fileManager.reloadAllFiles();
        this.fileManager.setup();

        FileUtils.loadFiles();

        boolean isEnabled = this.config.getProperty(ConfigKeys.toggle_metrics);

        if (!isEnabled) {
            this.plugin.getMetrics().stop();
        } else {
            this.plugin.getMetrics().start();
        }

        FileUtils.cleanFiles();

        // Close previews
        if (this.config.getProperty(ConfigKeys.take_out_of_preview)) {
            this.plugin.getServer().getOnlinePlayers().forEach(player -> {
                //if (this.inventoryManager.inCratePreview(player)) {
                //this.inventoryManager.closeCratePreview(player);

                if (this.config.getProperty(ConfigKeys.send_preview_taken_out_message)) {
                    player.sendMessage(Messages.reloaded_forced_out_of_preview.getMessage(player));
                }
                //}
            });
        }

        //this.crateManager.loadCrates();

        if (sender instanceof Player player) {
            player.sendMessage(Messages.reloaded_plugin.getMessage(player));

            return;
        }

        sender.sendMessage(Messages.reloaded_plugin.getMessage());*/
    }
}