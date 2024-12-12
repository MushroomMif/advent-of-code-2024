import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import java.nio.charset.StandardCharsets
import kotlin.io.path.*

private var cachedInput: String? = null

fun getOrDownloadInput(): String {
    cachedInput?.let { return it }
    val cacheDir = Path("cache")
    if (cacheDir.notExists()) {
        cacheDir.toFile().mkdir()
    }

    val todayInputFile = cacheDir.resolve("${YEAR}_$CURRENT_DAY.txt")
    if (todayInputFile.exists()) return todayInputFile.readText(StandardCharsets.UTF_8)
        .also { cachedInput = it }

    val input = runBlocking { downloadInput() }
        .removeSuffix("\n")
    todayInputFile.writeText(input, StandardCharsets.UTF_8)
    return input.also { cachedInput = it }
}

private suspend fun downloadInput(): String {
    println("Downloading input...")
    return HttpClient(CIO) { expectSuccess = true }
        .use { client ->
            client.get("https://adventofcode.com/${YEAR}/day/${CURRENT_DAY}/input") {
                cookie("session", readAOCSession())
            }
        }.bodyAsText()
}

private fun readAOCSession(): String {
    val dotenv = dotenv()
    return dotenv["AOC_SESSION"]
}