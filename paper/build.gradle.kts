plugins {
    id("paper-plugin")
}

group = "${rootProject.group}.${project.name}"

repositories {
    flatDir { dirs("libs") }
}

dependencies {
    api(project(":api"))

    implementation("dev.triumphteam", "triumph-cmd-bukkit", "2.0.0-SNAPSHOT")

    //implementation("com.ryderbelserion.ruby", "ruby-paper-small", "1.0")

    //implementation("dev.triumphteam", "triumph-gui", "3.1.2")

    implementation("org.bstats", "bstats-bukkit", "3.0.2")

    implementation("de.tr7zw", "item-nbt-api", "2.11.3")

    compileOnly("ch.jalu", "configme", "1.4.0")

    compileOnly("com.github.decentsoftware-eu", "decentholograms","2.8.3")

    compileOnly("me.clip", "placeholderapi", "2.11.3")

    compileOnly(fileTree("libs").include("*.jar"))
}

val component: SoftwareComponent = components["java"]

tasks {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = rootProject.group.toString()
                artifactId = "${rootProject.name.lowercase()}-${project.name.lowercase()}"
                version = rootProject.version.toString()

                from(component)
            }
        }
    }

    shadowJar {
        listOf(
            "dev.triumphteam",
            "org.jetbrains",
            "org.bstats",
            "de.tr7zw"
        ).forEach {
            relocate(it, "libs.$it")
        }
    }

    processResources {
        val props = mapOf(
            "name" to rootProject.name,
            "group" to project.group,
            "version" to rootProject.version,
            "description" to rootProject.description,
            "authors" to rootProject.properties["authors"],
            "apiVersion" to "1.20",
            "website" to "https://modrinth.com/plugin/${rootProject.name.lowercase()}"
        )

        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}