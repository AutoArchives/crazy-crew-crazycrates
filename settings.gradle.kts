enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "CrazyCrates"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

include("paper")
include("api")