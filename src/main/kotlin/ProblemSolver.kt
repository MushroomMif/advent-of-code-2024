interface ProblemSolver {
    fun solveFirstPart(): Any? = null
    fun solveSecondPart(): Any? = null
}

val ProblemSolver.input: String
    inline get() = getOrDownloadTodayInput()
