import java.io.File

private const val INPUT_FILE_NAME = "input.txt"

@Deprecated("I didn't move all solutions at the moment to the new system, so I need to keep this function")
fun readInput(): String? {
    val file = File(INPUT_FILE_NAME)
    if (!file.exists()) {
        println("${file.absolutePath} file not found")
        return null
    }

    return file.readText()
}
