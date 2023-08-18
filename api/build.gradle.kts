plugins {
    id("paper-plugin")
}

group = "${rootProject.group}.${project.name}"

dependencies {
    compileOnly("io.papermc.paper", "paper-api","1.20.1-R0.1-SNAPSHOT")

    compileOnly("ch.jalu", "configme", "1.4.0")
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
}