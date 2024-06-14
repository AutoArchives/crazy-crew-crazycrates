plugins {
    alias(libs.plugins.shadowJar)

    `java-plugin`
}

project.group = "us.crazycrew.crazycrates"
project.version = "1.0-snapshot"

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")

    maven("https://repo.oraxen.com/releases")

    mavenCentral()
}

dependencies {
    compileOnly(libs.vital.paper)

    compileOnly(libs.placeholderapi)

    compileOnly(libs.oraxen)

    compileOnly(libs.paper)
}

val javaComponent: SoftwareComponent = components["java"]

tasks {
    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    val javadocJar by creating(Jar::class) {
        dependsOn.add(javadoc)
        archiveClassifier.set("javadoc")
        from(javadoc)
    }
}