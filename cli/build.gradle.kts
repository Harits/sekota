plugins {
    id("org.jetbrains.kotlin.jvm")
    application
}

application {
    mainClass.set("com.sekota.cli.MainKt")
}

dependencies {
    implementation(projects.shared)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("io.ktor:ktor-client-java:3.4.3") // Matching ktor version
}
