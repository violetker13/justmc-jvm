plugins {
    java
    kotlin("jvm") version "2.3.10"
}

apply("$rootDir/jjvm.gradle.kts")

group = "me.violetker13"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}