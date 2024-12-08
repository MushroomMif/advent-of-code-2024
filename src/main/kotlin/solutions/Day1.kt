package solutions

import AdventOfCodeDay
import ProblemSolver
import input
import kotlin.math.abs

@AdventOfCodeDay(2024, 1)
class Day1: ProblemSolver {
    private val firstList = input.lines()
        .map { it.substringBefore("   ").toInt() }
    private val secondList = input.lines()
        .map { it.substringAfter("   ").toInt() }

    override fun solveFirstPart(): Int {
        return firstList.sorted().zip(secondList.sorted()).sumOf {
            abs(it.first - it.second)
        }
    }

    override fun solveSecondPart(): Int {
        val secondListAppearances = buildMap<Int, Int> {
            for(id in secondList) {
                this[id] = this[id]?.plus(1) ?: 1
            }
        }

        return firstList.sumOf {
            it * (secondListAppearances[it] ?: 0)
        }
    }
}
