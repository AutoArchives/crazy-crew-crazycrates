package us.crazycrew.crazycrates.common.config.api;

import com.ryderbelserion.cluster.api.adventure.FancyLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.crazycrew.crazycrates.api.CrazyCrates;
import us.crazycrew.crazycrates.api.CrazyCratesProvider;
import us.crazycrew.crazycrates.api.platforms.Platform;
import us.crazycrew.crazycrates.common.config.ConfigManager;
import us.crazycrew.crazycrates.common.config.api.registry.PluginRegistry;
import java.io.File;
import java.util.List;

public abstract class AbstractPlugin implements CrazyCrates {

    @Nullable
    public abstract String identifyClassLoader(ClassLoader classLoader) throws Exception;

    @NotNull
    public abstract ConfigManager getConfigManager();

    private final Platform.Type platform;
    private final File dataFolder;

    public AbstractPlugin(File dataFolder, Platform.Type platform) {
        this.dataFolder = dataFolder;
        this.platform = platform;
    }

    public void enablePlugin() {
        PluginRegistry.start(this);
    }

    public void disablePlugin() {
        PluginRegistry.stop();
    }

    /**
     * Made by lucko (luckperms)
     */
    public void apiWasLoadedByOurPlugin() {
        ClassLoader classLoader = this.platform.getClass().getClassLoader();

        for (Class<?> apiClass : new Class[]{CrazyCrates.class, CrazyCratesProvider.class}) {
            ClassLoader apiClassLoader = apiClass.getClassLoader();

            if (!apiClassLoader.equals(classLoader)) {
                String guilty = "unknown";

                try {
                    guilty = identifyClassLoader(apiClassLoader);
                } catch (Exception exception) {
                    // ignore
                }

                List.of(
                        "It seems that CrazyCrates API has been class-loaded by a plugin other than CrazyCrates!",
                        "The API seems to have been loaded by " + apiClassLoader + " (" + guilty + ") and the ",
                        "CrazyCrates plugin was loaded by " + classLoader.toString() + ".",
                        "Another plugin must've incorrectly shaded CrazyCrates API into the jar file which can cause runtime errors."
                ).forEach(FancyLogger::warn);

                return;
            }
        }
    }

    @NotNull
    @Override
    public Platform.Type getPlatform() {
        return this.platform;
    }

    @NotNull
    @Override
    public File getDataFolder() {
        return this.dataFolder;
    }
}