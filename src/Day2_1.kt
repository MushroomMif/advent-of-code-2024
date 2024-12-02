import kotlin.math.abs

typealias IntChecker = (Int, Int) -> Boolean
val defaultChecker: IntChecker = { a, b -> abs(a - b) <= 3 }
val increasingChecker: IntChecker = { a,b -> a > b && defaultChecker(a, b) }
val decreasingChecker: IntChecker = { a,b -> a < b && defaultChecker(a, b) }

fun main() {
    val input = parseHorizontalIntListsFromInput() ?: return

    val safeReportsCount = input.sumOf { if (checkIntList(it)) 1 else 0.toInt() }
    println(safeReportsCount)
}

fun checkIntList(list: List<Int>): Boolean {
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