package com.badbones69.crazycrates.paper;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class CrazyLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        //resolver.addDependency(new Dependency(new DefaultArtifact("ch.jalu:configme:1.4.0"), null));

        resolver.addDependency(new Dependency(new DefaultArtifact("de.tr7zw:item-nbt-api:2.11.3"), null));

        resolver.addRepository(new RemoteRepository.Builder("codemc-repo", "default", "https://repo.codemc.io/repository/maven-public/").build());
    }
}