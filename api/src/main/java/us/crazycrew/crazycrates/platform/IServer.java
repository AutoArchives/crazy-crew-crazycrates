package us.crazycrew.crazycrates.platform;

import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.users.UserManager;
import java.io.File;
import java.util.List;

/**
 * A class containing available methods to use.
 *
 * @author Ryder Belserion
 * @version 0.7
 * @since 0.5
 */
public interface IServer {

    /**
     * Reloads the plugin
     *
     * @since 0.5
     */
    void reload();

    /**
     * Gets the crates folder
     *
     * @return {@link File}
     * @since 0.5
     */
    @NotNull File getCrateFolder();

    /**
     * Gets the plugin/mods folder
     *
     * @return {@link File}
     * @since 0.8
     */
    @NotNull File getDataFolder();

    /**
     * Gets a list of crate files
     *
     * @return {@link List<String>}
     * @since 0.5
     */
    List<String> getCrateFiles();

    /**
     * Gets the instance of the user manager
     *
     * @return {@link UserManager}
     * @since 0.5
     */
    @NotNull UserManager getUserManager();

    /**
     * Gets a current amount of settings
     *
     * @return {@link ISettings}
     * @since 0.5
     */
    @NotNull ISettings getSettings();

}