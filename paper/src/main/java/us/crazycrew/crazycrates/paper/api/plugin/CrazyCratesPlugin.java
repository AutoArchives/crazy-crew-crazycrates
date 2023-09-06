package us.crazycrew.crazycrates.paper.api.plugin;

import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.EventLogger;
import us.crazycrew.crazycrates.paper.api.FileManager;
import us.crazycrew.crazycrates.common.config.ConfigManager;
import us.crazycrew.crazycrates.paper.api.managers.MenuManager;
import us.crazycrew.crazycrates.paper.support.MetricsHandler;
import us.crazycrew.crazycrates.paper.support.structures.blocks.ChestManager;
import com.ryderbelserion.cluster.bukkit.BukkitPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class CrazyCratesPlugin {

    public abstract @NotNull ChestManager getChestManager();

    public abstract @NotNull ConfigManager getConfigManager();

    public abstract @NotNull BukkitPlugin getBukkitPlugin();

    public abstract @NotNull CrazyManager getCrazyManager();

    public abstract @NotNull MenuManager getMenuManager();

    public abstract @NotNull FileManager getFileManager();

    public abstract @NotNull EventLogger getEventLogger();

    public abstract @NotNull MetricsHandler getMetrics();

    public abstract @NotNull Methods getMethods();
}