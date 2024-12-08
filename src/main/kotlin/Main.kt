import io.github.classgraph.ClassGraph
import io.github.classgraph.MethodInfo
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

val currentDay: AdventOfCodeDay = AdventOfCodeDay(2024, 7)

fun main() {
    val currentDayData = AOCDayData(currentDay)
    val solvers = findSolutions()[currentDayData]?.map { constructor ->
        try {
            constructor.loadClassAndGetConstructor().newInstance() as ProblemSolver
        } catch (e: IllegalArgumentException) {
            error("Primary constructor of a ProblemSolver class for $currentDay should not have any parameters")
        }
    } ?: error("No solvers detected for current day $currentDay")

    println("====== Year ${currentDayData.year} Day ${currentDayData.day} ======")
    if (solvers.size == 1) {
        printAnswers(solvers.first())
    } else {
        for (solver in solvers) {
            println("Running ${solver::class.simpleName}:")
            printAnswers(solver, copyAnswerToClipboard = false)
        }
    }
}

private fun printAnswers(solver: ProblemSolver, copyAnswerToClipboard: Boolean = true) {
    val firstAnswer = solver.solveFirstPart()?.toString()
    val secondAnswer = solver.solveSecondPart()?.toString()

    if (firstAnswer == null && secondAnswer == null) {
         println("No answers were provided")
         return
    }

    firstAnswer?.let { println("Answer to first part: $it") }
    secondAnswer?.let { println("Answer to second part: $it") }

    if (!copyAnswerToClipboard) return
    val toCopy = if (secondAnswer != null) secondAnswer else firstAnswer
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(StringSelection(toCopy), null)
}

private fun findSolutions(): Map<AOCDayData, MutableSet<MethodInfo>> {
    return buildMap {
        ClassGraph()
            .enableClassInfo()
            .enableMethodInfo()
            .enableAnnotationInfo()
            .acceptPackages("solutions")
            .scan()
            .getClassesWithAnnotation(AdventOfCodeDay::class.java)
            .forEach { markedClass ->
                if (!markedClass.implementsInterface(ProblemSolver::class.java)) {
                    error("Marked with AdventOfCodeDay class ${markedClass.name} does not implement ProblemSolver interface")
                }

                val annotationParams = markedClass.annotationInfo
                    .first { info -> info.classInfo.name == "AdventOfCodeDay" }
                    .parameterValues
                val year = annotationParams["year"].value as Int
                val day = annotationParams["day"].value as Int

                val classPrimaryConstructor = markedClass.constructorInfo.first()
                getOrPut(AOCDayData(year, day)) { mutableSetOf() } += classPrimaryConstructor
            }
    }
}