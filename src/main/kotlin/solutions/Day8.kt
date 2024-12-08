package solutions

import ProblemSolver
import Vec2i
import input
import parseMap

@JvmInline
private value class Antenna(val frequency: Char)

private fun interface AntinodeChecker {
    fun check(currentPos: Vec2i, antenna1Pos: Vec2i, antenna2Pos: Vec2i): Boolean

    companion object {
        val PART1 = AntinodeChecker { currentPos, antenna1Pos, antenna2Pos ->
            val distanceToFirst = antenna1Pos.distanceTo(currentPos)
            val distanceToSecond = antenna2Pos.distanceTo(currentPos)
            val distanceBetweenAntennas = antenna2Pos.distanceTo(antenna1Pos)
            return@AntinodeChecker distanceToSecond == distanceToFirst * 2 && distanceBetweenAntennas == distanceToFirst
        }

        val PART2 = AntinodeChecker { currentPos, antenna1Pos, antenna2Pos ->
            val x1 = currentPos.x
            val y1 = currentPos.y
            val x2 = antenna1Pos.x
            val y2 = antenna1Pos.y
            val x3 = antenna2Pos.x
            val y3 = antenna2Pos.y

            // Unfortunately, I didn't know how to check if all dots are on the same line, so I googled it
            return@AntinodeChecker x3 * (y2 - y1) - y3 * (x2 - x1) == x1 * y2 - x2 * y1
        }
    }
}

class Day8: ProblemSolver {
    private val possibleFrequencies = mutableSetOf<Char>()
    private val map = parseMap(input) { pos, char ->
        possibleFrequencies += char
        return@parseMap pos to Antenna(char)
    }.toMap()

    private val width: Int
    private val height: Int

    init {
        input.lines().apply {
            width = first().length
            height = size
        }
    }

    override fun solveFirstPart(): Int {
        return countAntinodes(AntinodeChecker.PART1)
    }

    override fun solveSecondPart(): Int {
        return countAntinodes(AntinodeChecker.PART2)
    }

    private fun countAntinodes(checker: AntinodeChecker): Int {
        var antinodes = 0
        for (x in 0..<width) {
            pointCheck@ for (y in 0..<height) {
                val currentPos = Vec2i(x, y)
                for (frequency in possibleFrequencies) {
                    val antennas = map.filter { it.value.frequency == frequency }
                    for ((antenna1Pos, _) in antennas) {
                        for ((antenna2Pos, _) in antennas) {
                            if (antenna2Pos == antenna1Pos) continue
                            if (checker.check(currentPos, antenna1Pos, antenna2Pos)) {
                                antinodes += 1
                                continue@pointCheck
                            }
                        }
                    }
                }
            }
        }
        return antinodes
    }
}
