package us.crazycrew.crazycrates.paper.support;

import org.bstats.charts.SimplePie;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyHandler;

public class MetricsHandler {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyHandler crazyHandler = this.plugin.getCrazyHandler();
    private final @NotNull CrazyManager crazyManager = this.crazyHandler.getCrazyManager();

    private Metrics metrics;

    public void start() {
        if (this.metrics != null) {
            FancyLogger.error("Metrics is already enabled.");
            return;
        }

        this.metrics = new Metrics(this.plugin, 4514);

        this.crazyManager.getCrates().forEach(crate -> {
            CrateType crateType = crate.getCrateType();

            SimplePie chart = new SimplePie("crate_types", crateType::getName);

            this.metrics.addCustomChart(chart);
        });

        FancyLogger.success("Metrics has been enabled.");
    }

    public void stop() {
        if (this.metrics == null) {
            FancyLogger.error("Metrics isn't enabled so we do nothing.");
            return;
        }

        this.metrics.shutdown();

        FancyLogger.success("Metrics has been turned off.");
    }
}