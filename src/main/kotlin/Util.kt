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

    operator fun plus(other: Vec2i): Vec2i {
        return Vec2i(this.x + other.x, this.y + other.y)
    }

    fun getTouching(): List<Vec2i> {
        return listOf(
            this + Vec2i(0, 1),
            this + Vec2i(1, 0),
            this + Vec2i(0, -1),
            this + Vec2i(-1, 0)
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

val Int.isEven: Boolean
    inline get() = this % 2 == 0

enum class Direction {
    UP {
        override fun turnRight(): Direction = RIGHT
        override fun turnLeft(): Direction = LEFT
        override fun move(pos: Vec2i): Vec2i = pos.copy(y = pos.y - 1)
    },
    RIGHT {
        override fun turnRight(): Direction = DOWN
        override fun turnLeft(): Direction = UP
        override fun move(pos: Vec2i): Vec2i = pos.copy(x = pos.x + 1)
    },
    DOWN {
        override fun turnRight(): Direction = LEFT
        override fun turnLeft(): Direction = RIGHT
        override fun move(pos: Vec2i): Vec2i = pos.copy(y = pos.y + 1)
    },
    LEFT {
        override fun turnRight(): Direction = UP
        override fun turnLeft(): Direction = DOWN
        override fun move(pos: Vec2i): Vec2i = pos.copy(x = pos.x - 1)
    };

    abstract fun turnRight(): Direction
    abstract fun turnLeft(): Direction
    abstract fun move(pos: Vec2i): Vec2i
}
