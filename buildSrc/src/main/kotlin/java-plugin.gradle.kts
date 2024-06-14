import com.ryderbelserion.feather.enums.Repository
import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

plugins {
    id("com.ryderbelserion.feather-core")

    `maven-publish`

    `java-library`
}

repositories {
    flatDir { dirs("libs") }

    maven("https://repo.codemc.io/repository/maven-public")

    maven(Repository.CrazyCrewReleases.url)

    maven(Repository.Jitpack.url)

    mavenCentral()
}

dependencies {
    compileOnlyApi(libs.annotations)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}