plugins {
    `paper-plugin`
}

project.group = "us.crazycrew.crazycrates"
project.version = "1.0-snapshot"

dependencies {
    compileOnly(libs.placeholderapi)

    compileOnly(libs.vital.paper)

    compileOnly(libs.oraxen)

    compileOnly(libs.paper)
}