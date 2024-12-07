fun main() {
    // SETUP
    val input = readInput()?.lines() ?: return
    val equations = buildMap<Long, MutableList<List<Long>>> {
        for (line in input) {
            getOrPut(
                line.substringBefore(':').toLong()
            ) { mutableListOf() }
                .add(line.substringAfter(": ")
                    .split(' ')
                    .map(String::toLong)
                )
        }
    }

    // PART 1
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

    println(sum)

    // PART 2
    var bigSum = 0L
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

            if (requiredResult in calcPossibleResults()) bigSum += requiredResult
        }
    }

    println(bigSum)
}
