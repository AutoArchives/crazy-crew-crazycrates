package com.badbones69.crazycrates.paper;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class CrazyStarter implements PluginBootstrap, PluginLoader {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {

    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        return new CrazyCrates();
    }

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addDependency(new Dependency(new DefaultArtifact("ch.jalu:configme:1.3.0"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("de.tr7zw:item-nbt-api:2.11.2"), null));

        resolver.addDependency(new Dependency(new DefaultArtifact("org.bstats:bstats-bukkit:3.0.2"), null));

        resolver.addDependency(new Dependency(new DefaultArtifact("com.github.Carleslc.Simple-YAML:Simple-Yaml:1.8.4"), null));

        resolver.addDependency(new Dependency(new DefaultArtifact("dev.jorel:commandapi-bukkit-core:9.0.3"), null));

        resolver.addRepository(new RemoteRepository.Builder("jitpack", "default", "https://jitpack.io").build());
        resolver.addRepository(new RemoteRepository.Builder("maven2", "default", "https://repo1.maven.org/maven2").build());
        resolver.addRepository(new RemoteRepository.Builder("codemc-repo", "default", "https://repo.codemc.org/repository/maven-public/").build());

        classpathBuilder.addLibrary(resolver);
    }
}