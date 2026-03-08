val jvmVersion = property("justmc.jvm.version").toString()
val jdkVersion = property("justmc.jdk.version").toString()

val jvmPath = "jjvm/justmc-jvm-$jvmVersion.jar"
val jdkPath = "libs/justmc-jdk-$jdkVersion.jar"

fun download(url: String, dir: String) {
    println("Download $url")
    val fileName = url.substringAfterLast('/')
    val dirFile = file(dir)
    dirFile.mkdirs()
    ant.invokeMethod("get", mapOf(
        "src" to url,
        "dest" to File(dirFile, fileName),
        "verbose" to true
    ))
}

fun checkJars() {
    if (!file(jvmPath).exists()) download("https://github.com/unidok/justmc-jvm/releases/download/jvm-$jvmVersion/justmc-jvm-$jvmVersion.jar", "jjvm")
    if (!file(jdkPath).exists()) download("https://github.com/unidok/justmc-jvm/releases/download/jdk-$jdkVersion/justmc-jdk-$jdkVersion.jar", "libs")
}

checkJars()

repositories {
    mavenCentral()
    flatDir {
        dirs("libs")
    }
}

dependencies {
    add("implementation", fileTree("libs") { include("*.jar") })
}

tasks.named("jar", Jar::class.java) {
    if (hasProperty("justmc.dir.out")) {
        destinationDirectory.set(file(property("justmc.dir.out").toString()))
    }
    doFirst {
        val libsPath = "$projectDir\\libs"
        from(configurations.named("runtimeClasspath", Configuration::class.java).get().mapNotNull {
            if (!it.path.startsWith(libsPath)) return@mapNotNull null
            if (it.isDirectory) it else zipTree(it)
        })
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register("buildModule", JavaExec::class.java) {
    doFirst {
        checkJars()
    }
    group = "justmc"
    dependsOn("jar")
    classpath = files(jvmPath)
    workingDir = file("jjvm")
    args((tasks.named("jar").get() as Jar).archiveFile.get().asFile.absolutePath)
}

tasks.register("uploadModule", JavaExec::class.java) {
    doFirst {
        checkJars()
    }
    group = "justmc"
    dependsOn("jar")
    classpath = files(jvmPath)
    workingDir = file("jjvm")
    args(
        (tasks.named("jar").get() as Jar).archiveFile.get().asFile.absolutePath,
        "-u"
    )
}