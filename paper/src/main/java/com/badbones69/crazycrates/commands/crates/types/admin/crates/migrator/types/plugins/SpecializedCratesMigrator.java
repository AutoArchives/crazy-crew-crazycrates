package com.badbones69.crazycrates.commands.crates.types.admin.crates.migrator.types.plugins;

import com.badbones69.crazycrates.commands.crates.types.admin.crates.migrator.ICrateMigrator;
import com.badbones69.crazycrates.commands.crates.types.admin.crates.migrator.enums.MigrationType;
import me.PM2.customcrates.crates.Crate;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import java.io.File;

public class SpecializedCratesMigrator extends ICrateMigrator {

    public SpecializedCratesMigrator(final CommandSender sender) {
        super(sender, MigrationType.SPECIALIZED_CRATES);
    }

    @Override
    public void run() {
        final File directory = getCratesDirectory();

        if (!directory.exists()) {
            directory.mkdirs();
        }

        Crate.getLoadedCrates().forEach((name, crate) -> {
            this.plugin.getLogger().warning("Name: " + name);
        });
    }

    @Override
    public <T> void set(final ConfigurationSection section, final String path, T value) {
        section.set(path, value);
    }

    @Override
    public final File getCratesDirectory() {
        return new File(this.plugin.getDataFolder(), "crates");
    }
}