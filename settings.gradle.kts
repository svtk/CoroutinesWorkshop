pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

fun getRuntimeVersion(): Int {
    val version = System.getProperty("java.version")
    return version.substringBefore('.').toInt()
}

if (getRuntimeVersion() < 11) {
    throw GradleException(
        """
        This repository requires running Gradle daemon on JDK 11 or later.
        Please either modify your 'JAVA_HOME' environment variable
         or add following into 'gradle.properties': org.gradle.java.home=/path_to_jdk11+_directory
         or in IDEA open Settings -> Build, Execution, Deployment -> Build and Deployment -> Gradle and select proper Gradle JVM.
        """.trimIndent()
    )
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    repositoriesMode = RepositoriesMode.PREFER_SETTINGS
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "CoroutinesWorkshop"
include(":desktop-client")
include(":server")
include("shared")
include("livedemo")
