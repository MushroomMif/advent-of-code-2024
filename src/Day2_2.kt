fun main() {
    val input = parseHorizontalIntListsFromInput() ?: return

    val safeReportsCount = input.sumOf { list ->
        val initialResult = checkIntList(list)
        if (initialResult) return@sumOf 1

        for (i in list.indices) {
            val newList = list.filterIndexed { index, _ -> index != i }
            if (checkIntList(newList)) return@sumOf 1
        }

        return@sumOf 0.toInt()
    }
    println(safeReportsCount)
}