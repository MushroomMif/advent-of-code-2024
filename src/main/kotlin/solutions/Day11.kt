package solutions

import ProblemSolver
import input
import isEven

class Day11: ProblemSolver {
    private val stones = input.split(" ").map { it.toLong() }

    private fun simpleStoneTick(stone: Long): List<Long> {
        if (stone == 0L) return listOf(1L)

        val str = stone.toString()
        if (str.length.isEven) {
            return listOf(
                str.take(str.length / 2).toLong(),
                str.takeLast(str.length / 2).toLong()
            )
        }

        return listOf(stone * 2024L)
    }

    override fun solveFirstPart(): Int {
        var stones = this.stones
        repeat(25) {
            stones = buildList {
                for (stone in stones) {
                    addAll(simpleStoneTick(stone))
                }
            }
        }

        return stones.size
    }

    // I couldn't write the solution to the second part myself :(
}