package com.badbones69.crazycrates.paper;

import com.badbones69.crazycrates.core.ApiManager;
import com.badbones69.crazycrates.core.config.types.PluginConfig;
import com.badbones69.crazycrates.paper.api.frame.PaperCore;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class CrazyStarter implements PluginBootstrap, PluginLoader {

    private ApiManager apiManager;

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        this.apiManager = new ApiManager(context.getDataDirectory());
        this.apiManager.load();
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        PaperCore paperCore = new PaperCore(context.getDataDirectory(), Bukkit.getConsoleSender(), ApiManager.getPluginConfig().getProperty(PluginConfig.COMMAND_PREFIX), ApiManager.getPluginConfig().getProperty(PluginConfig.CONSOLE_PREFIX));
        paperCore.enable();

        return new CrazyCrates(this.apiManager, paperCore);
    }

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addDependency(new Dependency(new DefaultArtifact("ch.jalu:configme:1.3.0"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("de.tr7zw:item-nbt-api:2.11.2"), null));

        resolver.addDependency(new Dependency(new DefaultArtifact("org.bstats:bstats-bukkit:3.0.2"), null));

        //resolver.addDependency(new Dependency(new DefaultArtifact("net.kyori:adventure-platform-bukkit:4.3.0"), null));

        resolver.addDependency(new Dependency(new DefaultArtifact("com.github.Carleslc.Simple-YAML:Simple-Yaml:1.8.4"), null));

        resolver.addRepository(new RemoteRepository.Builder("jitpack", "default", "https://jitpack.io").build());
        resolver.addRepository(new RemoteRepository.Builder("maven2", "default", "https://repo1.maven.org/maven2").build());
        resolver.addRepository(new RemoteRepository.Builder("codemc-repo", "default", "https://repo.codemc.org/repository/maven-public/").build());

        classpathBuilder.addLibrary(resolver);
    }
}