package com.badbones69.crazycrates.paper.listeners;

import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.v1.FileManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

// Only use for this class is to check if for broken locations and to try and fix them when the server loads the world.
public class BrokeLocationsListener implements Listener {

    private final CrazyCrates plugin = CrazyCrates.getPlugin(CrazyCrates.class);

    //private final CrazyManager crazyManager = plugin.getCrazyManager();

    private final FileManager fileManager = plugin.getFileManager();
    
    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        /*if (!this.crazyManager.getBrokeCrateLocations().isEmpty()) {
            int fixedAmount = 0;
            List<BrokeLocation> fixedWorlds = new ArrayList<>();

            for (BrokeLocation brokeLocation : this.crazyManager.getBrokeCrateLocations()) {
                Location location = brokeLocation.getLocation();

                if (location.getWorld() != null) {
                    if (brokeLocation.getCrate() != null) {
                        this.crazyManager.getCrateLocations().add(new CrateLocation(brokeLocation.getLocationName(), brokeLocation.getCrate(), location));

                        if (brokeLocation.getCrate().getHologram().isEnabled() && this.plugin.getHolograms() != null) this.plugin.getHolograms().create(location, brokeLocation.getCrate().getHologram());

                        fixedWorlds.add(brokeLocation);
                        fixedAmount++;
                    }
                }
            }

            this.crazyManager.getBrokeCrateLocations().removeAll(fixedWorlds);

            if (this.fileManager.isLogging()) {
                this.plugin.getLogger().warning("Fixed " + fixedAmount + " broken crate locations.");

                if (this.crazyManager.getBrokeCrateLocations().isEmpty()) this.plugin.getLogger().warning("All broken crate locations have been fixed.");
            }
        }
         */
    }
}