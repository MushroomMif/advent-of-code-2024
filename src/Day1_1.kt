import kotlin.math.abs

fun main() {
    val (firstList, secondList) = parseTwoIntListsFromInput() ?: return

    firstList.sort()
    secondList.sort()

    val distance = firstList.zip(secondList).sumOf {
        abs(it.first - it.second)
    }
    println(distance)
}