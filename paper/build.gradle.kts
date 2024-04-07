plugins {
    alias(libs.plugins.run.paper)
    alias(libs.plugins.userdev)
    alias(libs.plugins.shadow)
}

val mcVersion = libs.versions.bundle.get()

dependencies {
    implementation(fileTree("$rootDir/libs/shade").include("*.jar"))

    paperweight.paperDevBundle(mcVersion)
}

tasks {
    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(mcVersion)
    }

    assemble {
        dependsOn(reobfJar)
    }

    reobfJar {
        outputJar = rootProject.layout.buildDirectory.file("$rootDir/jars/paper/${rootProject.name.lowercase()}-${rootProject.version}.jar")
    }

    processResources {
        val props = mapOf(
            "name" to rootProject.name,
            "version" to rootProject.version,
            "group" to rootProject.group,
            "description" to rootProject.description,
            "apiVersion" to "1.20"
        )

        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}