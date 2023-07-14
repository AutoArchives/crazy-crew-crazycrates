plugins {
    id("paper-plugin")

    id("xyz.jpenilla.run-paper") version "2.1.0"
}

repositories {
    flatDir {
        dirs("libs")
    }

    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    api(project(":crazycrates-api"))

    compileOnly("ch.jalu", "configme", "1.3.0")

    compileOnly("me.clip", "placeholderapi", "2.11.3")

    compileOnly("de.tr7zw", "item-nbt-api", "2.11.3")

    compileOnly("dev.triumphteam", "triumph-cmd-bukkit", "2.0.0-SNAPSHOT")

    compileOnly("com.github.decentsoftware-eu", "decentholograms", "2.8.2")

    implementation("org.bstats", "bstats-bukkit", "3.0.0")

    implementation("com.ryderbelserion.stick", "stick-paper", "2.2.1-snapshot")
}

tasks {
    reobfJar {
        val file = File("$rootDir/jars")

        if (!file.exists()) file.mkdirs()

        outputJar.set(layout.buildDirectory.file("$file/${rootProject.name}-${rootProject.version}.jar"))
    }

    shadowJar {
        fun reloc(pkg: String) = relocate(pkg, "${rootProject.group}.dep.$pkg")

        reloc("org.bstats")
        reloc("com.ryderbelserion.stick")
    }

    runServer {
        minecraftVersion("1.20.1")
    }

    processResources {
        filesMatching("paper-plugin.yml") {
            expand(
                "name" to rootProject.name,
                "group" to rootProject.group,
                "version" to rootProject.version,
                "description" to rootProject.description,
            )
        }
    }
}