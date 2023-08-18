pluginManagement {
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")

        gradlePluginPortal()
    }
}

rootProject.name = "CrazyCrates"

listOf(
    "api",
    "paper",
).forEach {
    include(it)
}