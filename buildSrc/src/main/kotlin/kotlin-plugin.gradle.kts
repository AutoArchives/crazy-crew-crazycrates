plugins {
    id("java-plugin")

    kotlin("jvm")
}

dependencies {
    compileOnly(kotlin("stdlib"))
}

kotlin {
    jvmToolchain(17)

    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
            javaParameters = true
        }
    }
}