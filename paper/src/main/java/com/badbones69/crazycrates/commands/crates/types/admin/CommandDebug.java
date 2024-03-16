package com.badbones69.crazycrates.commands.crates.types.admin;

import com.badbones69.crazycrates.commands.crates.BaseCommand;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

public class CommandDebug extends BaseCommand {

    @Command("debug")
    @Permission(value = "crazycrates.debug", def = PermissionDefault.OP)
    public void debug(Player player) {
        //Crate crate = this.crateManager.getCrateFromName(crateName);

        //if (crate == null) {
        //    player.sendMessage(Messages.not_a_crate.getMessage("{crate}", crateName, player));

        //    return;
        //}

        //crate.getPrizes().forEach(prize -> PrizeManager.givePrize(player, prize, crate));
    }
}