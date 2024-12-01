import java.io.File

private const val INPUT_FILE_NAME = "input.txt"

fun readInput(): String? {
    val file = File(INPUT_FILE_NAME)
    if (!file.exists()) {
        println("${file.absolutePath} file not found")
        return null
    }

    return file.readText()
}

fun parseTwoIntListsFromInput(): Pair<MutableList<Int>, MutableList<Int>>? {
    val input = readInput()?.lines() ?: return null

    try {
        val firstList = mutableListOf<Int>()
        val secondList = mutableListOf<Int>()

        for (line in input) {
            val (first, second) = line.split("   ")
            firstList.add(first.toInt())
            secondList.add(second.toInt())
        }

        return firstList to secondList
    } catch (e: Exception) {
        println("Failed to parse two int lists from the input file")
        e.printStackTrace()
        return null
    }
}