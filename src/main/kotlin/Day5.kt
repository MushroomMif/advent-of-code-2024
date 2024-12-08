fun main() {
    val input = readInput()?.lines() ?: return
    val rules = mutableMapOf<Int, MutableSet<Int>>()
    val updates = mutableListOf<List<Int>>()

    fun parseRule(string: String) {
        val (x, y) = string.split("|").map(String::toInt)
        rules.getOrPut(y) { mutableSetOf() } += x
    }

    fun parseUpdate(string: String) {
        updates += string.split(",").map(String::toInt)
    }

    var isUpdate = false
    for (line in input) {
        if (line.isEmpty()) {
            isUpdate = true
            continue
        }

        if (isUpdate) parseUpdate(line) else parseRule(line)
    }
    fun isUpdateCorrect(update: List<Int>): Boolean {
        for (index in 0..<update.size - 1) {
            if (update.subList(index + 1, update.size).any { rules[update[index]]?.contains(it) == true }) return false
        }

        return true
    }

    val correctUpdates = mutableListOf<List<Int>>()
    val incorrectUpdates = mutableListOf<List<Int>>()

    for (update in updates) {
        if (isUpdateCorrect(update)) correctUpdates += update else incorrectUpdates += update
    }

    println(correctUpdates.sumOf {
        it[it.size / 2]
    })

    fun fixUpdates(): List<List<Int>> {
        return buildList {
            for (update in incorrectUpdates) {
                var correctedUpdate = update.toMutableList()

                for (currentNumIndex in 0..<update.size - 1) {
                    for (breakerNum in correctedUpdate.subList(currentNumIndex + 1, update.size)) {

                        if (rules[correctedUpdate[currentNumIndex]]?.contains(breakerNum) != true) continue

                        val newUpdate = mutableListOf<Int>()

                        for (i in 0..<currentNumIndex) {
                            newUpdate += correctedUpdate[i]
                        }

                        newUpdate += breakerNum

                        for (i in currentNumIndex..<correctedUpdate.size) {
                            val num = correctedUpdate[i]
                            if (num == breakerNum) continue
                            newUpdate += num
                        }

                        correctedUpdate = newUpdate
                    }
                }
                add(correctedUpdate)
            }
        }
    }

    println(fixUpdates().sumOf {
        it[it.size / 2]
    })
}