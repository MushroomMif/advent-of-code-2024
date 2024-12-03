fun main() {
    val input = readInput() ?: return
    val split = input.split("mul(")
    val mulResult = split.sumOf(::processPossibleMulString)
    println(mulResult)

    var shouldDo = true
    var secondMulResult = 0
    for (string in split) {
        if (shouldDo) {
            secondMulResult += processPossibleMulString(string)
        }

        shouldDo = lookForDo(string, shouldDo)
    }
    println(secondMulResult)
}

private fun processPossibleMulString(string: String): Int {
    var input = string

    fun parseIntUntilBreak(string: String): Int? {
        return buildString {
            for ((index, char) in string.withIndex()) {
                if (char in '0'..'9') {
                    append(char)
                } else {
                    input = input.substring(index)
                    break
                }
            }

            if (isEmpty()) return null
        }.toInt()
    }

    val firstNumber = parseIntUntilBreak(input) ?: return 0

    if (input.first() != ',') return 0
    input = input.substring(1)

    val secondNumber = parseIntUntilBreak(input) ?: return 0

    if (input.first() != ')') return 0
    return firstNumber * secondNumber
}

private fun lookForDo(string: String, initial: Boolean): Boolean {
    val doIndex = string.lastIndexOf("do()")
    val dontIndex = string.lastIndexOf("don't()")

    if (doIndex == -1 && dontIndex == -1) return initial
    return doIndex > dontIndex
}

private fun String.lastIndexOf(lookingFor: String): Int {
    if (lookingFor.isEmpty() || this.length < lookingFor.length) return -1
    if (this.length == lookingFor.length && this == lookingFor) return 0
    if (lookingFor.length == 1) return lookingFor.indexOf(lookingFor.first())

    var index = -1
    var inProcess = false
    var result = -1

    for (char in this) {
        if (!inProcess && char == lookingFor.first()) {
            inProcess = true
            index = 1
            continue
        }

        if (inProcess) {
            if (char == lookingFor[index++]) {
                if (index == lookingFor.length) {
                    result = index
                } else {
                    continue
                }
            }

            index = -1
            inProcess = false
        }
    }

    return result
}
