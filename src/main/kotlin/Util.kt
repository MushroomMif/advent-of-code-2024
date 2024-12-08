import java.io.File
import kotlin.math.pow
import kotlin.math.sqrt

private const val INPUT_FILE_NAME = "input.txt"

@Deprecated("I didn't move all solutions at the moment to the new system, so I need to keep this function")
fun readInput(): String? {
    val file = File(INPUT_FILE_NAME)
    if (!file.exists()) {
        println("${file.absolutePath} file not found")
        return null
    }

    return file.readText()
}

data class Vec2i(var x: Int, var y: Int) {
    fun distanceTo(other: Vec2i): Double {
        return sqrt(
            (other.x - this.x).toDouble().pow(2) + (other.y - this.y).toDouble().pow(2)
        )
    }
}

fun <T> parseMap(input: String, emptyMark: Char = '.', creator: (Vec2i, Char) -> T): List<T> {
    return buildList {
        for ((lineIndex, line) in input.lines().withIndex()) {
            for ((charIndex, char) in line.withIndex()) {
                if (char == emptyMark) continue
                add(creator(Vec2i(charIndex, lineIndex), char))
            }
        }
    }
}
