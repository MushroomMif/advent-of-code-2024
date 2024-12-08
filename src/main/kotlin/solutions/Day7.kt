package solutions

import AdventOfCodeDay
import ProblemSolver
import input

@AdventOfCodeDay(2024, 7)
class Day7: ProblemSolver {
    private val equations: Map<Long, MutableList<List<Long>>> = buildMap {
        for (line in input.lines()) {
            getOrPut(
                line.substringBefore(':').toLong()
            ) { mutableListOf() }
                .add(line.substringAfter(": ")
                    .split(' ')
                    .map(String::toLong)
                )
        }
    }

    override fun solveFirstPart(): Long {
        var sum = 0L
        for ((requiredResult, allNumbers) in equations) {
            for (numbers in allNumbers) {
                // Unfortunately, I wasn't able to write this function on my own, so I used the Internet
                fun calcPossibleResults(nextIndex: Int = 0, current: Long = 0L): List<Long> {
                    if (nextIndex == numbers.size) return listOf(current)

                    val nextNumber = numbers[nextIndex]
                    return calcPossibleResults(
                        nextIndex + 1, current + nextNumber
                    ) + calcPossibleResults(
                        nextIndex + 1, current * nextNumber
                    )
                }

                if (requiredResult in calcPossibleResults()) sum += requiredResult
            }
        }

        return sum
    }

    override fun solveSecondPart(): Long {
        var sum = 0L
        for ((requiredResult, allNumbers) in equations) {
            for (numbers in allNumbers) {
                // Unfortunately, I wasn't able to write this function on my own, so I used the Internet
                fun calcPossibleResults(nextIndex: Int = 0, current: Long = 0L): List<Long> {
                    if (nextIndex == numbers.size) {
                        //if (current == requiredResult) totalSum += requiredResult
                        return listOf(current)
                    }

                    val nextNumber = numbers[nextIndex]
                    return calcPossibleResults(
                        nextIndex + 1, current + nextNumber
                    ) + calcPossibleResults(
                        nextIndex + 1, current * nextNumber
                    ) + calcPossibleResults(
                        nextIndex + 1, "$current$nextNumber".toLong()
                    )
                }

                if (requiredResult in calcPossibleResults()) sum += requiredResult
            }
        }

        return sum
    }
}
