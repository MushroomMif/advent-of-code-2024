package solutions

import ProblemSolver
import input
import java.util.LinkedList

private sealed class MemoryBlock {
    data object Empty: MemoryBlock()
    data class File(val id: Int): MemoryBlock()
}

class Day9: ProblemSolver {
    private val originalMemory = buildList<MemoryBlock> {
        var nextFileId = 0
        var isFileNext = true
        for (nextBlockSize in input.map { it.digitToInt() }) {
            if (isFileNext) {
                repeat(nextBlockSize) { add(MemoryBlock.File(nextFileId)) }
                nextFileId++
            } else {
                repeat(nextBlockSize) { add(MemoryBlock.Empty) }
            }
            isFileNext = !isFileNext
        }
    }

    private fun sortMemory1(memory: List<MemoryBlock>): List<MemoryBlock> {
        val firstFileBlocks = buildList {
            for (block in memory) {
                when (block) {
                    is MemoryBlock.File -> add(block)
                    is MemoryBlock.Empty -> return@buildList
                }
            }
        }
        val memoryCopy = LinkedList(memory.subList(firstFileBlocks.size, memory.size))

        fun getNextBlock(): MemoryBlock.File? {
            var result: MemoryBlock.File? = null
            var removedCount = 0

            for (i in memoryCopy.lastIndex downTo 0) {
                removedCount++
                val nextBlock = memoryCopy[i]
                if (nextBlock is MemoryBlock.File) {
                    result = nextBlock
                    break
                }
            }

            repeat(removedCount) { memoryCopy.removeLast() }

            return result
        }

        return buildList {
            addAll(firstFileBlocks)
            while (true) {
                when (val block = memoryCopy.firstOrNull()) {
                    null -> return@buildList
                    MemoryBlock.Empty -> {
                        getNextBlock()?.let {
                            add(it)
                        } ?: return@buildList
                    }
                    is MemoryBlock.File -> add(block)
                }
                memoryCopy.removeFirst()
            }
        }
    }

    // for debugging
    private fun render(memory: List<MemoryBlock>): String {
        return buildString {
            for (block in memory) {
                when (block) {
                    MemoryBlock.Empty -> append('.')
                    is MemoryBlock.File -> append(block.id)
                }
            }
        }
    }

    private fun getChecksum(memory: List<MemoryBlock>): Long {
        var index = -1L
        return memory.sumOf {
            index++
            when (it) {
                MemoryBlock.Empty -> return@sumOf 0
                is MemoryBlock.File -> return@sumOf it.id * index
            }
        }
    }

    override fun solveFirstPart(): Long {
        return getChecksum(
            sortMemory1(originalMemory)
        )
    }

    private fun sortMemory2(memory: List<MemoryBlock>): List<MemoryBlock> {
        data class FileData(val size: Int, val memoryIndexes: List<Int>)

        // file id to file data
        fun getFiles(): Map<Int, FileData>  = buildMap {
            var blocks = 0
            val memoryIndexes = mutableListOf<Int>()
            var fileId: Int? = null
            for (blockIndex in memory.lastIndex downTo 0) {
                val block = memory[blockIndex]
                if (block !is MemoryBlock.File) continue

                if (block.id != fileId) {
                    fileId?.let {
                        put(it, FileData(blocks, memoryIndexes.toList()))
                    }

                    blocks = 0
                    memoryIndexes.clear()
                    fileId = block.id
                }

                memoryIndexes += blockIndex
                blocks++
            }
        }

        val memoryCopy = memory.toMutableList()

        fun searchEmptyPlaces(size: Int, to: Int): List<Int>? {
            val places = mutableListOf<Int>()
            for (i in 0..<to) {
                when (memoryCopy[i]) {
                    MemoryBlock.Empty -> {
                        places += i
                        if (places.size == size) return places
                    }
                    is MemoryBlock.File -> {
                        places.clear()
                    }
                }
            }

            return null
        }

        for ((fileId, fileData) in getFiles()) {
            val fileIndexes = fileData.memoryIndexes
            val emptyPlaces = searchEmptyPlaces(fileIndexes.size, fileIndexes.first()) ?: continue
            for (originalFileIndex in fileIndexes) {
                memoryCopy[originalFileIndex] = MemoryBlock.Empty
            }

            for (newFileIndex in emptyPlaces) {
                memoryCopy[newFileIndex] = MemoryBlock.File(fileId)
            }
        }

        return memoryCopy
    }

    override fun solveSecondPart(): Long {
        return getChecksum(
            sortMemory2(originalMemory)
        )
    }
}