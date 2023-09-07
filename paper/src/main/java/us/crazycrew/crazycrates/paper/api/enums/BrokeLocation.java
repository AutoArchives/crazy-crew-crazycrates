package us.crazycrew.crazycrates.paper.api.enums;

import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BrokeLocation {

    /**
     * Empty values that get instantiated below.
     */
    private int x, y, z;
    private String world;
    private final String locationName;
    private Crate crate;

    /**
     * Fetch the plugin instance.
     */
    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    /**
     * Builds a location that represents a broken crate.
     * Usually never activates...
     *
     * @param locationName the location name
     * @param crate the crate object
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @param world the world name
     */
    public BrokeLocation(String locationName, Crate crate, int x, int y, int z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.crate = crate;
        this.locationName = locationName;
    }

    /**
     * @return the location name
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @return the X coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Set the X coordinate.
     *
     * @param x the new X coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the Y coordinate
     */
    public int getY() {
        return y;
    }


    /**
     * Set the Y coordinate.
     *
     * @param y the new Y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the Z coordinate
     */
    public int getZ() {
        return z;
    }

    /**
     * Set the Z coordinate.
     *
     * @param z the new Z coordinate
     */
    public void setZ(int z) {
        this.z = z;
    }

    /**
     * @return the world name
     */
    public String getWorld() {
        return world;
    }

    /**
     * Set the World name.
     *
     * @param world the world name
     */
    public void setWorld(String world) {
        this.world = world;
    }

    /**
     * @return the crate object
     */
    public Crate getCrate() {
        return crate;
    }

    /**
     * Set the crate object.
     *
     * @param crate the new crate object
     */
    public void setCrate(Crate crate) {
        this.crate = crate;
    }

    /**
     * Get the location of the Broken Crate.
     *
     * @return the location of the broken crate
     */
    public Location getLocation() {
        return new Location(plugin.getServer().getWorld(world), x, y, z);
    }
}