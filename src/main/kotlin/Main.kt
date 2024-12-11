import io.github.classgraph.ClassGraph
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import kotlin.time.TimeSource

const val YEAR: Int = 2024
const val CURRENT_DAY: Int = 11

fun main() {
    val solver = findCurrentSolver()
    println("===== YEAR $YEAR Day $CURRENT_DAY =====")

    val timeSource = TimeSource.Monotonic
    val startTimeMark = timeSource.markNow()

    val firstAnswer = solver.solveFirstPart()?.toString()
    val secondAnswer = solver.solveSecondPart()?.toString()

    if (firstAnswer == null && secondAnswer == null) {
        println("No answers were provided")
        return
    }

    val endTimeMark = timeSource.markNow()
    firstAnswer?.let { println("Answer to first part: $it") }
    secondAnswer?.let { println("Answer to second part: $it") }

    println("All tasks completed in ${endTimeMark - startTimeMark}")

    val toCopy = secondAnswer ?: firstAnswer
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(StringSelection(toCopy), null)
}

private fun findCurrentSolver(): ProblemSolver {
    ClassGraph()
        .enableClassInfo()
        .enableMethodInfo()
        .enableAnnotationInfo()
        .acceptPackages("solutions")
        .scan()
        .allClasses
        .forEach { solverClass ->
            val day = try {
                solverClass.simpleName.substringAfter("Day").toInt()
            } catch (_: NumberFormatException) {
                return@forEach
            }
            if (day != CURRENT_DAY) return@forEach

            if (!solverClass.implementsInterface(ProblemSolver::class.java)) {
                error("Class for solving day $day AoC problem does not implement ProblemSolver interface!")
            }

            return solverClass.constructorInfo.first().loadClassAndGetConstructor().newInstance() as ProblemSolver
        }

    error("Problem solver fdr day $CURRENT_DAY was not found in solutions package")
}