import java.math.BigInteger

fun main() {
    // SETUP
    val input = readInput()?.lines() ?: return
    val equations = buildMap<Long, List<Long>> {
        for (line in input) {
            put(
                line.substringBefore(':')
                    .toLong(),
                line.substringAfter(": ")
                    .split(' ')
                    .map(String::toLong)
            )
        }
    }

    // PART 1
    var sum = 0L
    for ((requiredResult, numbers) in equations) {
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

    println(sum)

    // PART 2
    var bigSum = BigInteger.valueOf(0)
    for ((requiredResult, numbers) in equations) {
        // Unfortunately, I wasn't able to write this function on my own, so I used the Internet
        fun calcPossibleResults(nextIndex: Int = 0, current: Long = 0L): List<Long> {
            if (nextIndex == numbers.size) return listOf(current)

            val nextNumber = numbers[nextIndex]
            return calcPossibleResults(
                nextIndex + 1, current + nextNumber
            ) + calcPossibleResults(
                nextIndex + 1, current * nextNumber
            ) + calcPossibleResults(
                nextIndex + 1, "$current$nextNumber".toLong()
            )
        }

        if (requiredResult in calcPossibleResults()) {
            bigSum = bigSum.add(BigInteger.valueOf(requiredResult))
        }
    }
    println(bigSum)
}
