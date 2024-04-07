plugins {
    `java-library`
}

rootProject.group = "com.badbones69.crazycrates"
rootProject.description = "Create unlimited crates with multiple types to choose from!"
rootProject.version = "alpha"

subprojects {
    apply(plugin = "java-library")

    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")

        flatDir { dirs("libs/shade") }

        mavenCentral()
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of("17"))
    }

    tasks {
        compileJava {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(17)
        }

        javadoc {
            options.encoding = Charsets.UTF_8.name()
        }

        processResources {
            filteringCharset = Charsets.UTF_8.name()
        }
    }
}