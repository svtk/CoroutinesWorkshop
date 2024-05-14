pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("jvm")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    repositoriesMode = RepositoriesMode.PREFER_SETTINGS
}

rootProject.name = "CoroutinesWorkshop"
include(":desktop-client")
include(":server")
include("shared")
include("livedemo")
