package com.badbones69.crazycrates.paper.support.placeholders;

import com.badbones69.crazycrates.paper.CrazyCrates;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPISupport extends PlaceholderExpansion {

    private final CrazyCrates plugin = CrazyCrates.getPlugin(CrazyCrates.class);
    
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        if (player.isOnline()) {
            Player playerOnline = (Player) player;

            /*for (Crate crate : crazyManager.getCrates()) {
                //if (crate.getCrateType() != CrateType.MENU) {
                    if (identifier.equalsIgnoreCase(crate.getName())) {
                        return NumberFormat.getNumberInstance().format(crazyManager.getVirtualKeys(playerOnline, crate));
                    } else if (identifier.equalsIgnoreCase(crate.getName() + "_physical")) {
                        return NumberFormat.getNumberInstance().format(crazyManager.getPhysicalKeys(playerOnline, crate));
                    } else if (identifier.equalsIgnoreCase(crate.getName() + "_total")) {
                        return NumberFormat.getNumberInstance().format(crazyManager.getTotalKeys(playerOnline, crate));
                    }
                //}
            }*/
        }

        return "";
    }
    
    @Override
    public boolean persist() {
        return true;
    }
    
    @Override
    public @NotNull String getIdentifier() {
        return "crazycrates";
    }
    
    @Override
    public @NotNull String getAuthor() {
        return "BadBones69";
    }
    
    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }
}