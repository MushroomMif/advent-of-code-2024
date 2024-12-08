fun main() {
    val input = readInput()?.lines() ?: return

    var count = 0
    for ((lineIndex, line) in input.withIndex()) {
        for ((charIndex, char) in line.withIndex()) {
            if (char == 'X') {
                count += searchWords(input, lineIndex, charIndex)
            }
        }
    }
    println(count)
}

private val chars = charArrayOf('M', 'A', 'S')

private class IntHolder(var value: Int)

private fun interface IndexIncrementer {
    fun increment(lineIndex: IntHolder, charIndex: IntHolder)
}

private val incrementers = arrayOf(
    IndexIncrementer { lineIndex, _ -> lineIndex.value++ },
    IndexIncrementer { lineIndex, _ -> lineIndex.value-- },
    IndexIncrementer { _, charIndex -> charIndex.value++ },
    IndexIncrementer { _, charIndex -> charIndex.value-- },
    IndexIncrementer { lineIndex, charIndex -> lineIndex.value++; charIndex.value-- },
    IndexIncrementer { lineIndex, charIndex -> lineIndex.value++; charIndex.value++ },
    IndexIncrementer { lineIndex, charIndex -> lineIndex.value--; charIndex.value-- },
    IndexIncrementer { lineIndex, charIndex -> lineIndex.value--; charIndex.value++ }
)


private fun searchWords(input: List<String>, lineIndex: Int, charIndex: Int): Int {
    var count = 0

    search@ for (incrementer in incrementers) {
        val lineIndexHolder = IntHolder(lineIndex)
        val charIndexHolder = IntHolder(charIndex)
        for (char in chars) {
            incrementer.increment(lineIndexHolder, charIndexHolder)
            try {
                if (input[lineIndexHolder.value][charIndexHolder.value] == char) {
                    continue
                }
            } catch (_: IndexOutOfBoundsException) {}

            continue@search
        }
        count++
    }

    return count
}