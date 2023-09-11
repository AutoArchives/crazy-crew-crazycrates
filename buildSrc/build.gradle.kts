plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("io.papermc.paperweight", "paperweight-userdev", "1.5.5")

    implementation("org.jetbrains.kotlin", "kotlin-gradle-plugin", "1.9.0")

    implementation("com.github.johnrengelman", "shadow", "8.1.1")
}