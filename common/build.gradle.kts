plugins {
    id("root-plugin")
}

project.group = "${rootProject.group}.common"
project.version = "${rootProject.version}"

dependencies {
    api(project(":api"))

    api("ch.jalu", "configme", "1.4.1") {
        exclude("org.yaml", "snakeyaml")
    }

    //api("com.zaxxer", "HikariCP", "5.0.1")
}