@file:JvmName("Main")

package me.unidok.jjvm

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import me.unidok.jjvm.model.JJVMConfig
import me.unidok.jjvm.model.UploadResponse
import me.unidok.jjvm.nativemethod.NativeMethods
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import kotlin.math.round
import kotlin.time.measureTimedValue

@OptIn(ExperimentalSerializationApi::class)
fun main(args: Array<out String>) {
    val jarPath = args.getOrElse(0) { return println("Expecting 1 argument: path to jar") }
    val uploadFlag = "-u" == args.getOrNull(1)

    val prettyJson = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    val config: JJVMConfig = try {
        val file = File("./jjvm-config.json")
        if (file.createNewFile()) {
            file.outputStream().use { stream ->
                JJVMConfig().also { prettyJson.encodeToStream(it, stream) }
            }
        } else {
            file.inputStream().use { stream ->
                prettyJson.decodeFromStream(stream)
            }
        }
    } catch (e: Exception) {
        println("Could not load config: $e")
        JJVMConfig()
    }

    NativeMethods.register()
    val (handlers, time) = measureTimedValue {
        JarTranslator.translate(jarPath, config)
    }
    val json = if (!uploadFlag && config.prettyOutput) prettyJson else Json
    val stream = ByteArrayOutputStream(4096)
    json.encodeToStream(handlers.serialize(), stream)
    val size = stream.size()
    println("Generated!")
    println("Handlers: ${handlers.handlers.size}")
    println("Size: ${if (size > 1024) "${round(size / 1024f * 100f) / 100f} KiB" else "$size B"}")
    println("Time: $time")

    val file = File(jarPath.substringBeforeLast('.') + ".json")
    file.createNewFile()
    file.outputStream().use { stream.writeTo(it) }
    println("Saved to ${file.absolutePath}")

    if (uploadFlag) {
        println("Uploading...")

        val (response, time) = measureTimedValue {
            val client = HttpClient.newBuilder().build()
            val request = HttpRequest.newBuilder()
                .uri(URI("https://m.justmc.ru/api/upload"))
                .POST(HttpRequest.BodyPublishers.ofByteArray(stream.toByteArray()))
                .timeout(Duration.ofSeconds(10))
                .build()
            client.send(request, HttpResponse.BodyHandlers.ofString())
        }

        val body = json.decodeFromString<UploadResponse>(response.body())
        val statusCode = response.statusCode()
        if (statusCode != 200) {
            println("HTTP $statusCode: ${body.error}")
            return
        }

        println("Uploaded ($time)")
        val url = "https://m.justmc.ru/api/" + body.id
        if (config.independent) {
            println("/module loadUrl force $url")
        } else {
            println("/module loadUrl $url")
        }
    }
}