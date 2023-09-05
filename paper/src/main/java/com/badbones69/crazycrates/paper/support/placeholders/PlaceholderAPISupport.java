package com.badbones69.crazycrates.paper.support.placeholders;

import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.CrazyManager;
import com.badbones69.crazycrates.paper.api.objects.Crate;
import com.badbones69.crazycrates.api.enums.types.CrateType;
import com.badbones69.crazycrates.paper.api.plugin.CrazyCratesPlugin;
import com.badbones69.crazycrates.paper.api.plugin.registry.CrazyCratesProvider;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.text.NumberFormat;

public class PlaceholderAPISupport extends PlaceholderExpansion {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesPlugin cratesPlugin = CrazyCratesProvider.get();
    private final @NotNull CrazyManager crazyManager = this.cratesPlugin.getCrazyManager();
    
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        if (player.isOnline()) {
            Player playerOnline = (Player) player;

            for (Crate crate : crazyManager.getCrates()) {
                if (crate.getCrateType() != CrateType.MENU) {
                    if (identifier.equalsIgnoreCase(crate.getName())) {
                        return NumberFormat.getNumberInstance().format(crazyManager.getVirtualKeys(playerOnline.getUniqueId(), crate));
                    } else if (identifier.equalsIgnoreCase(crate.getName() + "_physical")) {
                        return NumberFormat.getNumberInstance().format(crazyManager.getPhysicalKeys(playerOnline.getUniqueId(), crate));
                    } else if (identifier.equalsIgnoreCase(crate.getName() + "_total")) {
                        return NumberFormat.getNumberInstance().format(crazyManager.getTotalKeys(playerOnline.getUniqueId(), crate));
                    }
                }
            }
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