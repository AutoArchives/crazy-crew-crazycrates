package us.crazycrew.crazycrates.api;

import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.frame.UserManager;
import us.crazycrew.crazycrates.api.platforms.Platform;
import java.io.File;

public interface CrazyCrates {

    @NotNull UserManager getUserManager();

    @NotNull Platform.Type getPlatform();

    @NotNull File getDataFolder();

}