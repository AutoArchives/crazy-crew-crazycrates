plugins {
    `java-plugin`
}

val buildNumber: String? = System.getenv("BUILD_NUMBER")

rootProject.version = if (buildNumber != null) "2.1.6-$buildNumber" else "2.1.6"

subprojects.filter { it.name != "api" }.forEach {
    it.project.version = rootProject.version
}