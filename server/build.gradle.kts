plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kover)
}

group = "com.kcworkshop.coroutines"
version = "0.0.1"
application {
    mainClass.set("com.kotlinconf.workshop.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin.jvmToolchain(11)

dependencies {
    implementation(projects.shared)
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-websockets")
    implementation("io.ktor:ktor-server-netty")
    implementation(libs.logback.classic)
    implementation(libs.kotlinx.datetime)
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation(libs.kotlin.test.junit)
}