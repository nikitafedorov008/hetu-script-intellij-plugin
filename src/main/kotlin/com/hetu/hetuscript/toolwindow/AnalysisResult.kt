package com.hetu.hetuscript.toolwindow

data class AnalysisResult(
    val filePath: String,
    val status: AnalysisStatus,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class AnalysisStatus {
    ERROR, WARNING, SUCCESS, UNKNOWN
}

fun parseAnalysisOutput(output: String): List<AnalysisResult> {
    val results = mutableListOf<AnalysisResult>()

    val lines = output.lines()
    var i = 0

    fun readBlock(startIndex: Int): Pair<String, Pair<Int, String>> {
        // returns Pair(filePath, Pair(nextIndex, combinedMessage))
        var idx = startIndex
        val header = lines.getOrNull(idx)?.trim() ?: ""
        val filePath = when {
            header.startsWith("Analyzing:") -> header.substringAfter("Analyzing:").trim()
            header.startsWith("File:") -> header.substringAfter("File:").trim()
            else -> header
        }

        // advance to content separator or next header
        idx++
        // skip separators
        while (idx < lines.size && (lines[idx].startsWith("-") || lines[idx].startsWith("=") || lines[idx].isBlank())) {
            idx++
        }

        val contentStart = idx
        while (idx < lines.size && !lines[idx].startsWith("Analyzing:") && !lines[idx].startsWith("File:") && !lines[idx].startsWith("===") ) {
            idx++
        }

        val combined = lines.subList(contentStart, idx).joinToString("\n").trim()
        return Pair(filePath, Pair(idx, combined))
    }

    while (i < lines.size) {
        val line = lines[i].trim()
        if (line.startsWith("Analyzing:") || line.startsWith("File:")) {
            val (filePath, pair) = readBlock(i)
            val (nextIdx, combined) = pair

            // Determine status
            val lower = combined.lowercase()
            val status = when {
                lower.contains("lateinitializationerror") || lower.contains("error") || lower.contains("exception") -> AnalysisStatus.ERROR
                // explicit success messages
                lower.contains("found 0 problem") || lower.contains("no issues") || lower.contains("analyzed (no output)") -> AnalysisStatus.SUCCESS
                lower.contains("problem") || lower.contains("warning") -> AnalysisStatus.WARNING
                combined.isBlank() -> AnalysisStatus.SUCCESS
                else -> AnalysisStatus.UNKNOWN
            }

            val message = if (combined.isBlank()) {
                if (status == AnalysisStatus.SUCCESS) "No issues found" else ""
            } else combined

            results.add(AnalysisResult(filePath, status, message))
            i = nextIdx
            continue
        }
        i++
    }

    return results
}