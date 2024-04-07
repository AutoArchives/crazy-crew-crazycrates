plugins {
    alias(libs.plugins.userdev)
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(fileTree("$rootDir/libs/shade").include("*.jar"))

    paperweight.paperDevBundle(libs.versions.bundle)
}

tasks {
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