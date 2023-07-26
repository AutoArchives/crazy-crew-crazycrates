plugins {
    id("root-plugin")
}

defaultTasks("build")

rootProject.group = "com.badbones69.crazycrates"
rootProject.description = "Add unlimited crates to your server with 10 different crate types to choose from!"
rootProject.version = "2.0.0-rc1"

val combine by tasks.registering(Jar::class) {
    dependsOn("build")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(files(subprojects.map {
        it.layout.buildDirectory.file("libs/${rootProject.name}-${it.name}-${it.version}.jar").get()
    }).filter { it.name != "MANIFEST.MF" }.map { if (it.isDirectory) it else zipTree(it) })
}

allprojects {
    listOf(
        ":core",
        ":paper"
    ).forEach {
        project(it) {
            apply(plugin = "java")

            if (this.name == "paper") {
                dependencies {
                    implementation("de.tr7zw", "item-nbt-api", "2.11.3")
                    implementation("org.bstats", "bstats-bukkit", "3.0.2")
                }
            }

            //if (this.name == "core") {
            //    dependencies {
                    //compileOnly("net.kyori", "adventure-api", "4.14.0")
                    //compileOnly("net.kyori", "adventure-text-minimessage", "4.14.0")

                    //compileOnly("com.google.code.gson", "gson", "2.10.1")
            //    }
            //}

            dependencies {
                implementation("ch.jalu", "configme", "1.3.1")

                implementation("com.github.Carleslc.Simple-YAML", "Simple-Yaml", "1.8.4") {
                    exclude("org.yaml", "snakeyaml")
                }
            }
        }
    }
}

tasks {
    assemble {
        subprojects.forEach {
            dependsOn(":${it.project.name}:build")
        }

        finalizedBy(combine)
    }
}