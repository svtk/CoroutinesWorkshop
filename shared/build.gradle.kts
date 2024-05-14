val datetime_version: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.0.0-RC3"
}

group = "com.kcworkshop.coroutines"
version = "unspecified"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetime_version")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
//    targetCompatibility = JavaVersion.VERSION_17
//    sourceCompatibility = JavaVersion.VERSION_17
}