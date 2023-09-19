import io.papermc.hangarpublishplugin.model.Platforms

plugins {
    id("io.papermc.hangar-publish-plugin") version("0.1.0")

    id("com.modrinth.minotaur") version("2.8.2")

    id("xyz.jpenilla.run-paper") version("2.1.0")

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

    implementation("dev.triumphteam", "triumph-cmd-bukkit", "2.0.0-SNAPSHOT")

    implementation("org.bstats", "bstats-bukkit", "3.0.2")

    implementation("de.tr7zw", "item-nbt-api", "2.11.3")

    implementation(libs.cluster.bukkit.api) {
        exclude("com.ryderbelserion.cluster", "cluster-api")
    }

    compileOnly("me.filoghost.holographicdisplays", "holographicdisplays-api", "3.0.0")

    compileOnly("com.github.decentsoftware-eu", "decentholograms","2.8.3")

    compileOnly("com.github.LoneDev6", "API-ItemsAdder", "3.5.0b")

    compileOnly("com.github.oraxen", "oraxen", "1.160.0")

    compileOnly("me.clip", "placeholderapi", "2.11.3")

    compileOnly(fileTree("libs").include("*.jar"))
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

    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        minecraftVersion("1.20.1")
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
## Changes:
 * All internal placeholders have been changed from %placeholder% to {placeholder}
  * This only effects our placeholders like %crate% or %key-amount%, PlaceholderAPI placeholders still use %
  * This change was made so you can distinguish what is ours and what isn't.
 * Legacy color codes such as `&7` or `&c` are now deprecated in CrazyCrates.
  * A toggle has been added to `plugin-config.yml` called `use-minimessage`, It will default to true.
  * If you still use legacy color codes, Simply set `use-minimessage` to false and reload the plugin.
 * The plugin prefix is no longer automatically appended to your message, You must use {prefix}.
 * The messages file has been converted to be in the locale directory called en-US.yml which is just English.
 * Added a config option to turn off most logging except errors.
 * Properly turn off metrics when you set the option to false.
 * `toggle_metrics` and `prefix` have been migrated to `plugin-config.yml`.
 * Added more verbose logging in places it was needed with the ability to change the logger prefix in `plugin-config.yml`.
 * All sounds now have a category that respect client side sound settings.
 * Metrics now track what crate types you are using.
 * Allow mass open and single open on quickcrate/firecracker.
 * Updated command permissions to be more simplified, You can view the changes https://docs.crazycrew.us/crazycrates/info/commands/v2/permissions
  * All permissions except a few default to `false` instead of `op` as I want people to configure their permissions properly.
 * Removed the admin help command ( We only need one help command, It will show the admin help page if they have the proper permission )
    
## Fixes:
 * Fixed giving keys to offline players.

## Optimizations:
 * Stopped storing the player object in hashmap's or arraylist's when we didn't need to, We only store the UUID as god intended.

## Developers / APi:
This is nerd talk so only read this if you need to.
 * Re-organized code to be a bit more consistent.
 * Cleaned up the command classes to be more readable
 * Removed wildcard imports
 * Added a common module for multi platform purposes
 * Added adventure api/other dependencies to api/commons module
  * We set the adventure api to compileOnly so it should always use the platform implementation.
 * Created an api module with proper interfacing so you no longer need to sift through the main plugin's source code.
 * You can add, get or check, remove keys from offline and online players.
    
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