plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "me.unidok"
version = "2.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":justmc-code"))
    implementation("org.ow2.asm:asm:9.9")
    implementation("org.ow2.asm:asm-tree:9.9")
    implementation("org.ow2.asm:asm-util:9.9")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation(kotlin("stdlib"))
}

kotlin {
    jvmToolchain(21)
}

tasks.jar {
    destinationDirectory.set(file("$rootDir/justmc-jvm-test/jjvm"))
    manifest.attributes["Main-Class"] = "me.unidok.jjvm.Main"
    doFirst {
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register<Exec>("publishReleaseJVM") {
    doFirst {
        require(System.getenv("GH_TOKEN") != null) { "env GH_TOKEN is null" }
    }
    group = "publishing"
    dependsOn("jar")
    val tagName = "jvm-$version"
    val jarFile = tasks.jar.get().archiveFile.get().asFile
    commandLine(
        "gh", "release", "create", tagName,
        jarFile.absolutePath,
        "--repo", "unidok/justmc-jvm",
        "--title", tagName,
    )
}