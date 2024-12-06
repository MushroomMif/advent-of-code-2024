import kotlin.properties.Delegates

private data class Vec2i(var x: Int, var y: Int)

private enum class Direction {
    UP {
        override fun turn(): Direction = RIGHT
        override fun move(pos: Vec2i): Vec2i = pos.copy(y = pos.y - 1)
    },
    RIGHT {
        override fun turn(): Direction = DOWN
        override fun move(pos: Vec2i): Vec2i = pos.copy(x = pos.x + 1)
    },
    DOWN {
        override fun turn(): Direction = LEFT
        override fun move(pos: Vec2i): Vec2i = pos.copy(y = pos.y + 1)
    },
    LEFT {
        override fun turn(): Direction = UP
        override fun move(pos: Vec2i): Vec2i = pos.copy(x = pos.x - 1)
    };

    abstract fun turn(): Direction
    abstract fun move(pos: Vec2i): Vec2i
}

private val INITIAL_DIRECTION = Direction.UP

private enum class TryMoveResult {
    MOVE, BLOCK, WIN
}

fun main() {
    // SETUP
    val input = readInput()?.lines() ?: return
    val height = input.size
    val width = input.first().length

    var direction = INITIAL_DIRECTION
    var guardPos: Vec2i by Delegates.notNull()

    for ((lineIndex, line) in input.withIndex()) {
        val guardMarkerIndex = line.indexOf('^')
        if (guardMarkerIndex == -1) continue
        guardPos = Vec2i(guardMarkerIndex, lineIndex)
    }
    val initialGuardPos = guardPos

    fun checkMove(newPos: Vec2i, blockPredicate: () -> Boolean = { input[newPos.y][newPos.x] == '#' }): TryMoveResult {
        if (newPos.x < 0 || newPos.y < 0 || newPos.x == width || newPos.y == height) return TryMoveResult.WIN
        return if (blockPredicate()) TryMoveResult.BLOCK else TryMoveResult.MOVE
    }

    // PART 1
    val allGuardPositions: MutableSet<Vec2i> = mutableSetOf(guardPos)
    while (true) {
        val newPos = direction.move(guardPos)
        when (checkMove(newPos)) {
            TryMoveResult.MOVE -> {
                guardPos = newPos
                allGuardPositions += newPos
            }
            TryMoveResult.BLOCK -> {
                direction = direction.turn()
            }
            TryMoveResult.WIN -> {
                break
            }
        }
    }

    println(allGuardPositions.size)

    // PART 2
    var cycles = 0
    for (newObstaclePos in allGuardPositions) {
        if (newObstaclePos == initialGuardPos) continue
        var steps = 0

        guardPos = initialGuardPos
        direction = INITIAL_DIRECTION

        while (true) {
            // This solution is really bad, but I couldn't find a better one
            if (steps > 10_000) {
                cycles += 1
                break
            }

            val newPos = direction.move(guardPos)
            when (checkMove(newPos) { newPos == newObstaclePos || input[newPos.y][newPos.x] == '#' }) {
                TryMoveResult.MOVE -> {
                    guardPos = newPos
                    steps++
                }
                TryMoveResult.BLOCK -> {
                    direction = direction.turn()
                }
                TryMoveResult.WIN -> {
                    break
                }
            }
        }
    }

    println(cycles)
}