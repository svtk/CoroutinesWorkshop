plugins {
    id("kotlin")
}

group = "org.example"
version = "unspecified"

val ktor_version = "2.3.11"
val logback_version="1.3.14"

dependencies {
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation(project(mapOf("path" to ":shared")))
    testImplementation("app.cash.turbine:turbine:1.1.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}