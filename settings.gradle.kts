pluginManagement {
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")

        gradlePluginPortal()
    }
}

rootProject.name = "CrazyCrates"

listOf(
    "api",
    "folia",
    "paper",
).forEach {
    include(it)
}