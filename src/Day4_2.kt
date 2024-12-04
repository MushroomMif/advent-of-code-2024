fun main() {
    val input = readInput()?.lines() ?: return

    var count = 0
    for ((lineIndex, line) in input.withIndex()) {
        for ((charIndex, char) in line.withIndex()) {
            if (char == 'A') {
                if (hasX(input, lineIndex, charIndex)) count++
            }
        }
    }
    println(count)
}

private fun hasX(input: List<String>, lineIndex: Int, charIndex: Int): Boolean {
    try {
        if (!checkMS(input, intArrayOf(
                lineIndex - 1, charIndex - 1, lineIndex + 1, charIndex + 1
        ))) return false

        return checkMS(input, intArrayOf(
            lineIndex - 1, charIndex + 1, lineIndex + 1, charIndex - 1
        ))
    } catch (_: IndexOutOfBoundsException) {
        return false
    }
}

private fun checkMS(input: List<String>, positions: IntArray): Boolean {
    val firstChar = input[positions[0]][positions[1]]
    val secondCharShouldBe = when (firstChar) {
        'M' -> 'S'
        'S' -> 'M'
        else -> return false
    }

    return input[positions[2]][positions[3]] == secondCharShouldBe
}