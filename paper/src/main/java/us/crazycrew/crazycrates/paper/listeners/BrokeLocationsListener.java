package us.crazycrew.crazycrates.paper.listeners;

import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.FileManager;
import us.crazycrew.crazycrates.paper.api.enums.BrokeLocation;
import us.crazycrew.crazycrates.paper.api.objects.CrateLocation;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesPlugin;
import us.crazycrew.crazycrates.paper.api.plugin.registry.CrazyCratesProvider;
import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.jetbrains.annotations.NotNull;

// Only use for this class is to check if for broken locations and to try and fix them when the server loads the world.
public class BrokeLocationsListener implements Listener {

    private final @NotNull CrazyCratesPlugin cratesPlugin = CrazyCratesProvider.get();
    private final @NotNull FileManager fileManager = this.cratesPlugin.getFileManager();
    private final @NotNull CrazyManager crazyManager = this.cratesPlugin.getCrazyManager();
    
    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        if (!crazyManager.getBrokeCrateLocations().isEmpty()) {
            int fixedAmount = 0;

            for (BrokeLocation brokeLocation : crazyManager.getBrokeCrateLocations()) {
                Location location = brokeLocation.getLocation();

                if (location.getWorld() != null) {
                    if (brokeLocation.getCrate() != null) {
                        crazyManager.addCrateLocation(new CrateLocation(brokeLocation.getLocationName(), brokeLocation.getCrate(), location));

                        if (brokeLocation.getCrate().getHologram().isEnabled() && crazyManager.getHologramController() != null) crazyManager.getHologramController().createHologram(location.getBlock(), brokeLocation.getCrate());

                        crazyManager.removeBrokeCrateLocation(brokeLocation);
                        fixedAmount++;
                    }
                }
            }

            if (fileManager.isLogging()) {
                FancyLogger.debug("Fixed " + fixedAmount + " broken crate locations.");

                if (crazyManager.getBrokeCrateLocations().isEmpty()) FancyLogger.success("All broken crate locations have been fixed.");
            }
        }
    }
}