package us.crazycrew.crazycrates.paper.listeners;

import org.bukkit.plugin.java.JavaPlugin;
import us.crazycrew.crazycrates.common.config.ConfigManager;
import us.crazycrew.crazycrates.common.config.types.PluginConfig;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.enums.BrokeLocation;
import us.crazycrew.crazycrates.paper.api.objects.CrateLocation;
import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyHandler;

// Only use for this class is to check if for broken locations and to try and fix them when the server loads the world.
public class BrokeLocationsListener implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyHandler crazyHandler = this.plugin.getCrazyHandler();
    private final @NotNull CrazyManager crazyManager = this.crazyHandler.getCrazyManager();
    
    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        if (!this.crazyManager.getBrokeCrateLocations().isEmpty()) {
            int fixedAmount = 0;

            for (BrokeLocation brokeLocation : this.crazyManager.getBrokeCrateLocations()) {
                Location location = brokeLocation.getLocation();

                if (location.getWorld() != null) {
                    if (brokeLocation.getCrate() != null) {
                        this.crazyManager.addCrateLocation(new CrateLocation(brokeLocation.getLocationName(), brokeLocation.getCrate(), location));

                        if (brokeLocation.getCrate().getHologram().isEnabled() && this.crazyManager.getHologramController() != null) this.crazyManager.getHologramController().createHologram(location.getBlock(), brokeLocation.getCrate());

                        this.crazyManager.removeBrokeCrateLocation(brokeLocation);
                        fixedAmount++;
                    }
                }
            }

            if (this.crazyHandler.getConfigManager().getPluginConfig().getProperty(PluginConfig.verbose_logging)) {
                FancyLogger.debug("Fixed " + fixedAmount + " broken crate locations.");

                if (this.crazyManager.getBrokeCrateLocations().isEmpty()) FancyLogger.success("All broken crate locations have been fixed.");
            }
        }
    }
}