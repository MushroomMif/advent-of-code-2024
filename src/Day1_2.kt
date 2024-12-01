fun main() {
    val (firstList, secondList) = parseTwoIntListsFromInput() ?: return

    val secondListAppearances = buildMap<Int, Int> {
        for(id in secondList) {
            this[id] = this[id]?.plus(1) ?: 1
        }
    }
    val similarity = firstList.sumOf {
        it * (secondListAppearances[it] ?: 0)
    }
    println(similarity)
}