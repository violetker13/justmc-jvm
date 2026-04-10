plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "me.violetker13"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation(kotlin("stdlib"))
}