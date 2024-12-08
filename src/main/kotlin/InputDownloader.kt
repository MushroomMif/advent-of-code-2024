import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import java.nio.charset.StandardCharsets
import kotlin.io.path.*

fun getOrDownloadTodayInput(): String {
    val cacheDir = Path("cache")
    if (cacheDir.notExists()) {
        cacheDir.toFile().mkdir()
    }

    val todayInputFile = cacheDir.resolve("${currentDay.year}_${currentDay.day}.txt")
    if (todayInputFile.exists()) return todayInputFile.readText(StandardCharsets.UTF_8)

    val input = runBlocking { downloadTodayInput() }
        .removeSuffix("\n")
    todayInputFile.writeText(input, StandardCharsets.UTF_8)
    return input
}

private suspend fun downloadTodayInput(): String {
    println("Downloading input...")
    return HttpClient(CIO) { expectSuccess = true }
        .use { client ->
            client.get("https://adventofcode.com/${currentDay.year}/day/${currentDay.day}/input") {
                cookie("session", readAOCSession())
            }
        }.bodyAsText()
}

private fun readAOCSession(): String {
    val envFile = Path(".env")
    if (envFile.notExists()) {
        error(".env file does not exist! You shuld specify AOC_SESSION there")
    }

    return envFile.readLines().first().substringAfter("AOC_SESSION=")
}