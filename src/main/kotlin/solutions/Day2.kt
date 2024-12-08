package solutions

import ProblemSolver
import input
import kotlin.math.abs

private typealias IntChecker = (Int, Int) -> Boolean

class Day2: ProblemSolver {
    val lists = input.lines().map { line ->
        line.split(" ").map(String::toInt)
    }

    val defaultChecker: IntChecker = { a, b -> abs(a - b) <= 3 }
    val increasingChecker: IntChecker = { a,b -> a > b && defaultChecker(a, b) }
    val decreasingChecker: IntChecker = { a,b -> a < b && defaultChecker(a, b) }

    override fun solveFirstPart(): Int {
        var safeReports = 0
        for (list in lists) {
            if (checkIntList(list)) safeReports++
        }
        return safeReports
    }

    override fun solveSecondPart(): Int {
        return lists.sumOf { list ->
            val initialResult = checkIntList(list)
            if (initialResult) return@sumOf 1

            for (i in list.indices) {
                val newList = list.filterIndexed { index, _ -> index != i }
                if (checkIntList(newList)) return@sumOf 1
            }

            return@sumOf 0.toInt()
        }
    }

    private fun checkIntList(list: List<Int>): Boolean {
        val firstInt = list[0]
        var previousInt = list[1]

        val checker: IntChecker = if (increasingChecker(firstInt, previousInt)) {
            increasingChecker
        } else if (decreasingChecker(firstInt, previousInt)) {
            decreasingChecker
        } else {
            return false
        }

        for (i in 2..<list.size) {
            val int = list[i]
            if (!checker(previousInt, int)) return false
            previousInt = int
        }

        return true
    }
}