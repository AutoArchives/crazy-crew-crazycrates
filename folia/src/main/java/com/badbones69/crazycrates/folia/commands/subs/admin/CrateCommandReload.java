package com.badbones69.crazycrates.folia.commands.subs.admin;

/*
import com.badbones69.crazycrates.api.enums.Permissions;
import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.CrazyManager;
import com.badbones69.crazycrates.paper.api.FileManager;
import com.badbones69.crazycrates.paper.api.FileManager.Files;
import com.badbones69.crazycrates.paper.api.enums.settings.Messages;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandContext;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandEngine;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Collections;

public class CrateCommandReload extends BukkitCommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final FileManager fileManager = this.plugin.getFileManager();
    private final CrazyManager crazyManager = this.plugin.getCrazyManager();

    @SuppressWarnings("deprecation")
    public CrateCommandReload() {
        super("reload", Permissions.ADMIN_RELOAD.getDescription(), "/crazycrates:reload", Collections.emptyList());

        setPermission(Permissions.ADMIN_RELOAD.getBuiltPermission());

        setPermissionMessage(Messages.NO_PERMISSION.getMessage());
    }

    @Override
    public void perform(BukkitCommandContext context, String[] strings) {
        this.fileManager.reloadAllFiles();
        this.fileManager.setup();

        // TODO() Migrate old configs
        this.plugin.getConfigManager().reload();

        // Clean files if we have to.
        if (!Files.LOCATIONS.getFile().contains("Locations")) {
            Files.LOCATIONS.getFile().set("Locations.Clear", null);
            Files.LOCATIONS.saveFile();
        }

        if (!Files.DATA.getFile().contains("Players")) {
            Files.DATA.getFile().set("Players.Clear", null);
            Files.DATA.saveFile();
        }

        this.crazyManager.loadCrates();

        context.sendLegacyMessage(Messages.RELOAD.getMessage());
    }
}
 */