package us.crazycrew.crazycrates.api;

import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.platforms.Platform;

public abstract class CrazyCrates {

    public abstract @NotNull Platform.Type getPlatform();

}