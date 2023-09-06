plugins {
    id("root-plugin")
}

project.group = "${rootProject.group}.common"


dependencies {
    api(project(":api"))

    api("ch.jalu", "configme", "1.4.1") {
        exclude("org.yaml", "snakeyaml")
    }
}

val component: SoftwareComponent = components["java"]

tasks {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = project.group.toString()
                artifactId = "${rootProject.name.lowercase()}-${project.name.lowercase()}-api"
                version = project.version.toString()

                from(component)
            }
        }
    }
}