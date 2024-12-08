@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AdventOfCodeDay(val year: Int, val day: Int)

data class AOCDayData(val year: Int, val day: Int) {
    constructor(marker: AdventOfCodeDay): this(marker.year, marker.day)
}