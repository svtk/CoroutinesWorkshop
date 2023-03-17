import kotlinx.kover.api.IntellijEngine
import kotlinx.kover.api.JacocoEngine

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.8.0"
    id("io.ktor.plugin") version "2.2.3"
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
}

val kotlin_version: String by project // = "1.8.0"
val logback_version: String by project // = "1.2.11"
val datetime_version: String by project // = "0.4.0"

group = "com.kcworkshop.coroutines"
version = "0.0.1"
application {
    mainClass.set("com.kcworkshop.coroutines.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
//    targetCompatibility = JavaVersion.VERSION_17
//    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":shared"))
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-websockets")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetime_version")
    testImplementation("io.ktor:ktor-server-tests")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}