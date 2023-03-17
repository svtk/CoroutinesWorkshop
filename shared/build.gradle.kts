val datetime_version: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.8.0"
}

group = "com.kcworkshop.coroutines"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0-RC")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetime_version")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
//    targetCompatibility = JavaVersion.VERSION_17
//    sourceCompatibility = JavaVersion.VERSION_17
}