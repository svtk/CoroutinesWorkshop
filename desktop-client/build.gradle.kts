import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "2.0.0-RC3"
    id("org.jetbrains.compose") version "1.6.10-rc01"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0-RC3"
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
}

group = "com.kotlinconf.workshop"
version = "1.0-SNAPSHOT"

val ktor_version: String by project
val datetime_version: String by project
val logback_version: String by project
val retrofit_version: String by project
val coroutines_version: String by project
val serialization_version: String by project


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation(compose.desktop.currentOs)
                implementation(compose.materialIconsExtended)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")
                implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
                implementation("com.squareup.retrofit2:retrofit-mock:$retrofit_version")
                implementation("com.squareup.retrofit2:converter-kotlinx-serialization:$retrofit_version")
                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-cio:$ktor_version")
                implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
                implementation("io.ktor:ktor-client-serialization:$ktor_version")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
                implementation("io.ktor:ktor-client-okhttp:$ktor_version")
                implementation("io.ktor:ktor-client-okhttp:$ktor_version")
                implementation("com.squareup.okhttp3:okhttp:4.9.0")
                implementation("io.ktor:ktor-client-websockets:$ktor_version")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetime_version")
                implementation("ch.qos.logback:logback-classic:$logback_version")
                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version")
                implementation("org.junit.jupiter:junit-jupiter:5.8.1")
                implementation("app.cash.turbine:turbine:0.12.3")

            }
        }
        all {
            languageSettings.optIn("kotlin.time.ExperimentalTime")
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.kotlinconf.workshop.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "desktop-client"
            packageVersion = "1.0.0"
        }
    }
}
