import io.papermc.hangarpublishplugin.model.Platforms

plugins {
    id("io.papermc.hangar-publish-plugin") version("0.1.0")

    id("com.modrinth.minotaur") version("2.8.2")

    id("paper-plugin")
}

project.group = "${rootProject.group}.paper"
project.version = "${rootProject.version}"

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

    maven("https://repo.codemc.org/repository/maven-public/")

    maven("https://repo.aikar.co/content/groups/aikar/")

    maven("https://repo.triumphteam.dev/snapshots/")

    maven("https://jitpack.io/")

    mavenCentral()

    flatDir { dirs("libs") }
}

dependencies {
    implementation(project(":common"))

    implementation("de.tr7zw", "item-nbt-api", "2.11.3")

    implementation("org.bstats", "bstats-bukkit", "3.0.2")

    implementation("com.ryderbelserion.cluster", "cluster-bukkit", "0.4") {
        exclude("com.ryderbelserion.cluster", "cluster-api")
    }

    implementation("dev.triumphteam", "triumph-cmd-bukkit", "2.0.0-SNAPSHOT")

    compileOnly(fileTree("libs").include("*.jar"))

    compileOnly("me.filoghost.holographicdisplays", "holographicdisplays-api", "3.0.0")

    compileOnly("com.github.decentsoftware-eu", "decentholograms","2.8.3")

    compileOnly("com.github.LoneDev6", "API-ItemsAdder", "3.5.0b")

    compileOnly("com.github.oraxen", "oraxen", "1.160.0")

    compileOnly("me.clip", "placeholderapi", "2.11.3")
}

tasks {
    shadowJar {
        listOf(
            "com.ryderbelserion.cluster",
            "dev.triumphteam",
            "org.jetbrains",
            "org.bstats",
            "de.tr7zw",
            "ch.jalu"
        ).forEach {
            relocate(it, "libs.$it")
        }
    }

    processResources {
        val props = mapOf(
            "name" to rootProject.name,
            "group" to project.group.toString(),
            "version" to rootProject.version,
            "description" to rootProject.description,
            "authors" to rootProject.properties["authors"],
            "apiVersion" to "1.20",
            "website" to "https://modrinth.com/plugin/${rootProject.name.lowercase()}"
        )

        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

val isSnapshot = rootProject.version.toString().contains("snapshot")

val file = file("${rootProject.rootDir}/jars/${rootProject.name}-${rootProject.version}.jar")

val description = """
## New Features:
 * Added back HolographicDisplays support as filoghost has updated it to 1.20.1
 * Added direct support for Oraxen/ItemsAdder
   * https://docs.crazycrew.us/crazycrates/info/prizes/options#custom-items
 * Added the ability to color maps.
   * https://docs.crazycrew.us/crazycrates/info/prizes/items/colored-map
    
## Other:
 * [Feature Requests](https://github.com/Crazy-Crew/${rootProject.name}/issues)
 * [Bug Reports](https://github.com/Crazy-Crew/${rootProject.name}/issues)
""".trimIndent()

val versions = listOf(
    "1.20",
    "1.20.1",
    "1.20.2"
)

modrinth {
    autoAddDependsOn.set(false)

    token.set(System.getenv("MODRINTH_TOKEN"))

    projectId.set(rootProject.name.lowercase())

    versionName.set("${rootProject.name} ${rootProject.version}")
    versionNumber.set("${rootProject.version}")

    versionType.set(if (isSnapshot) "beta" else "release")

    uploadFile.set(file)

    gameVersions.addAll(versions)

    changelog.set(description)

    loaders.addAll("paper", "purpur")
}

hangarPublish {
    publications.register("plugin") {
        version.set(rootProject.version as String)

        id.set(rootProject.name)

        channel.set(if (isSnapshot) "Beta" else "Release")

        changelog.set(description)

        apiKey.set(System.getenv("hangar_key"))

        platforms {
            register(Platforms.PAPER) {
                jar.set(file)
                platformVersions.set(versions)
            }
        }
    }
}