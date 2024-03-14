package com.badbones69.crazycrates.listeners;

import com.badbones69.crazycrates.CrazyCratesPaper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

// Only use for this class is to check if for broken locations and to try and fix them when the server loads the world.
public class BrokeLocationsListener implements Listener {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);
    
    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        /*if (this.crateManager.getBrokeLocations().isEmpty()) return;

        int fixedAmount = 0;
        List<BrokeLocation> fixedWorlds = new ArrayList<>();

        for (BrokeLocation brokeLocation : this.crateManager.getBrokeLocations()) {
            Location location = brokeLocation.getLocation();

            if (location.getWorld() != null) {
                if (brokeLocation.getCrate() != null) {
                    this.crateManager.addLocation(new CrateLocation(brokeLocation.getLocationName(), brokeLocation.getCrate(), location));

                    //if (brokeLocation.getCrate().getHologram().isEnabled() && this.crateManager.getHolograms() != null) this.crateManager.getHolograms().createHologram(location.getBlock(), brokeLocation.getCrate());

                    fixedWorlds.add(brokeLocation);
                    fixedAmount++;
                }
            }
        }

        this.crateManager.removeBrokeLocation(fixedWorlds);

        if (MiscUtils.isLogging()) {
            this.plugin.getLogger().warning("Fixed " + fixedAmount + " broken crate locations.");

            if (this.crateManager.getBrokeLocations().isEmpty()) this.plugin.getLogger().warning("All broken crate locations have been fixed.");
        }*/
    }
}