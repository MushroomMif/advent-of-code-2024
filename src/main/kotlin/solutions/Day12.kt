package solutions

import Direction
import ProblemSolver
import Vec2i
import getOrDownloadInput
import parseMap

class Day12: ProblemSolver {
    companion object {
        private val map = parseMap(getOrDownloadInput()) { pos, char ->
            pos to char
        }.toMap()

        private val visitedPositions = mutableSetOf<Vec2i>()
        private val regions = buildSet<CropRegion> {
            for ((pos, _) in map) {
                if (pos in visitedPositions) continue
                add(buildRegion(pos))
            }
        }

        private fun buildRegion(initialPos: Vec2i): CropRegion {
            val positions = mutableSetOf<Vec2i>()
            val regionCrop = map[initialPos]!!
            var perimeter = 0

            fun checkPos(pos: Vec2i) {
                if (pos in positions) return
                positions += pos

                for (touching in pos.getTouching()) {
                    if (map[touching] != regionCrop) {
                        perimeter++
                        continue
                    }

                    checkPos(touching)
                }
            }
            checkPos(initialPos)

            visitedPositions.addAll(positions)
            return CropRegion(positions, perimeter, regionCrop)
        }

        private fun getRegionsInThisRegion(input: CropRegion): List<CropRegion> {
            return regions.filter { region ->
                for (pos in region.positions) {
                    for (touching in pos.getTouching()) {
                        if (!(touching in region.positions || touching in input.positions)) {
                            return@filter false
                        }
                    }
                }
                return@filter true
            }
        }
    }

    private data class CropRegion(
        val positions: Set<Vec2i>, val perimeter: Int, val cropType: Char
    ) {
        val area = positions.size

        private fun getTopLeftPos(): Vec2i {
            var topLeft: Vec2i? = null
            for (pos in positions) {
                if (topLeft == null || pos.x < topLeft.x || pos.y < topLeft.y) {
                    topLeft = pos
                }
            }
            return topLeft!!
        }

        // doesn't work :(
        val sideCount: Int by lazy {
            //println("Crop type is $cropType")
            var sides = 0
            var movingDirection = Direction.RIGHT
            var insidePos = getTopLeftPos()
            //println("Top left is $insidePos")
            var outsidePos = movingDirection.turnLeft().move(insidePos)
            //println("First outside is $outsidePos")
            val visited = mutableSetOf<Pair<Vec2i, Vec2i>>() // inside pos to outside pos


            fun getOutsidePos(insidePos: Vec2i, direction: Direction): Vec2i {
                return direction.turnLeft().move(insidePos)
            }

            fun getInsidePos(outsidePos: Vec2i, direction: Direction = movingDirection): Vec2i {
                return direction.turnRight().move(outsidePos)
            }

            fun turnRight(outsidePos: Vec2i): Vec2i {
                val forward = movingDirection.move(outsidePos)
                return movingDirection.turnRight().move(forward)
            }

            while (true) {
                val posPair = insidePos to outsidePos
                if (!visited.add(posPair)) break

                var possibleNextOutsidePos = movingDirection.move(outsidePos)
                var possibleNextInsidePos = getInsidePos(possibleNextOutsidePos)
                if (possibleNextInsidePos in positions && possibleNextOutsidePos !in positions) {
                    outsidePos = possibleNextOutsidePos
                    //println("Next crop is great, setting outside to $outsidePos")
                    insidePos = possibleNextInsidePos
                    continue
                }

                var possibleNewDirection = movingDirection.turnRight()
                possibleNextOutsidePos = turnRight(outsidePos)
                possibleNextInsidePos = getInsidePos(possibleNextOutsidePos, possibleNewDirection)
                if (possibleNextInsidePos in positions && possibleNextOutsidePos !in positions) {
                    movingDirection = possibleNewDirection
                    outsidePos = possibleNextOutsidePos
                    insidePos = possibleNextInsidePos
                    //if (cropType == 'C') println("Turned right to $insidePos with outside $outsidePos\"")
                    //println("Inside is $insidePos, outside is $outsidePos")
                    sides++
                    continue
                }
               //println("Tried $possibleNextInsidePos (turn right) it doesn't work, outside is $possibleNextOutsidePos")

                possibleNewDirection = movingDirection.turnLeft()
                possibleNextOutsidePos = outsidePos
                possibleNextInsidePos = getInsidePos(possibleNextOutsidePos, possibleNewDirection)
                if (possibleNextInsidePos in positions && possibleNextOutsidePos !in positions) {
                    movingDirection = possibleNewDirection
                    outsidePos = possibleNextOutsidePos
                    insidePos = possibleNextInsidePos
                    //if (cropType == 'C') println("Turned left to $insidePos with outside $outsidePos")
                    sides++
                    continue
                }
                error("We are in $cropType, possible inside pos is $possibleNextInsidePos, outside is $possibleNextOutsidePos sides is $sides")
            }

            for (innerRegion in getRegionsInThisRegion(this)) {
                sides += innerRegion.sideCount
            }
            return@lazy sides
        }
    }
    // too high 984694

    override fun solveFirstPart(): Any {
        return regions.sumOf { it.area * it.perimeter }
    }

    override fun solveSecondPart(): Any {
        return regions.sumOf { it.area * it.sideCount }
    }
}