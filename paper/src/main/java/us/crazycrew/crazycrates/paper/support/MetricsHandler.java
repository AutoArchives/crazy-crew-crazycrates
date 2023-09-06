package us.crazycrew.crazycrates.paper.support;

import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesPlugin;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MetricsHandler {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    //private final @NotNull CrazyCratesPlugin cratesPlugin = null;
    //private final @NotNull CrazyManager crazyManager = this.cratesPlugin.getCrazyManager();

    private Metrics metrics;

    public void start() {
        if (this.metrics != null) {
            FancyLogger.error("Metrics is already enabled.");
            return;
        }

        this.metrics = new Metrics(this.plugin, 4514);

        /*this.crazyManager.getCrates().forEach(crate -> {
            CrateType crateType = crate.getCrateType();

            SimplePie chart = new SimplePie("crate_types", crateType::getName);

            this.metrics.addCustomChart(chart);
        });*/

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