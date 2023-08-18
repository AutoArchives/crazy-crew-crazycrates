plugins {
    id("paper-plugin")
}

group = "${rootProject.group}.${project.name}"

dependencies {
    compileOnly("io.papermc.paper", "paper-api","1.20.1-R0.1-SNAPSHOT")

    api("ch.jalu", "configme", "1.3.1")
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