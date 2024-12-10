package solutions

import ProblemSolver
import Vec2i
import input
import parseMap

class Day10: ProblemSolver {
    private val heightMap: Map<Vec2i, Int> = parseMap(input) { pos, char ->
        pos to char.digitToInt()
    }.toMap()

    private val trailheadLocations = heightMap.filterValues { it == 0 }.keys

    private val locationModifiers = arrayOf(
        Vec2i(1, 0), Vec2i(-1, 0), Vec2i(0, 1), Vec2i(0, -1)
    )

    private fun getReachablePeaks(trailheadPos: Vec2i, initialValue: Int = 0, reachedPeaks: MutableSet<Vec2i> = mutableSetOf()): Set<Vec2i> {
        var currentPos = trailheadPos

        for (i in initialValue..<9) {
            val nextLocations = locationModifiers
                .map { currentPos + it }
                .filter { heightMap[it] == i + 1 }
                .toMutableList()
            if (nextLocations.isEmpty()) return reachedPeaks

            currentPos = nextLocations.removeFirst()

            for (alternativePathHead in nextLocations) {
                reachedPeaks.addAll(getReachablePeaks(alternativePathHead, i + 1, reachedPeaks))
            }
        }

        reachedPeaks += currentPos
        return reachedPeaks
    }

    private fun getTrailheadRating(trailheadPos: Vec2i, initialValue: Int = 0): Int {
        var currentPos = trailheadPos
        var rating = 0

        for (i in initialValue..<9) {
            val nextLocations = locationModifiers
                .map { currentPos + it }
                .filter { heightMap[it] == i + 1 }
                .toMutableList()
            if (nextLocations.isEmpty()) return rating

            currentPos = nextLocations.removeFirst()

            for (alternativePathHead in nextLocations) {
                rating += getTrailheadRating(alternativePathHead, i + 1)
            }
        }

        return ++rating
    }

    override fun solveFirstPart(): Int {
        return trailheadLocations.sumOf { getReachablePeaks(it).size }
    }

    override fun solveSecondPart(): Int {
        return trailheadLocations.sumOf { getTrailheadRating(it) }
    }
}