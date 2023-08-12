plugins {
    alias(libs.plugins.shadow)
}

val projectName = "${rootProject.name}-${project.name.substring(0, 1).uppercase() + project.name.substring(1)}"

base {
    archivesName.set(projectName)
}

dependencies {
    compileOnly("io.papermc.paper", "paper-api","1.20.1-R0.1-SNAPSHOT")

    compileOnly("com.ryderbelserion.lexicon", "lexicon-core-api", "2.4")

    api("ch.jalu", "configme", "1.3.1")
}

val component: SoftwareComponent = components["java"]

tasks {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = rootProject.group.toString()
                artifactId = "${rootProject.name.lowercase()}-${project.name.lowercase()}-api"
                version = project.version.toString()

                from(component)
            }
        }
    }

    assemble {
        dependsOn(shadowJar)
    }
}