package com.hetu.hetuscript.service

import com.hetu.hetuscript.toolwindow.HetuAnalyzerToolWindow
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.application.ApplicationManager
import java.io.File

@Service(Service.Level.PROJECT)
class HetuAnalyzerService(val project: Project) {
    private var toolWindow: HetuAnalyzerToolWindow? = null

    fun setToolWindow(toolWindow: HetuAnalyzerToolWindow) {
        this.toolWindow = toolWindow
        // When tool window is set, add functionality to the refresh button
        toolWindow.setAnalyzeAllCallback { analyzeAllFiles() }
    }

    /** Accept legacy text or structured results */
    fun updateResults(resultsText: String) {
        val parsed = com.hetu.hetuscript.toolwindow.parseAnalysisOutput(resultsText)
        updateResults(parsed)
    }

    fun updateResults(results: List<com.hetu.hetuscript.toolwindow.AnalysisResult>) {
        toolWindow?.updateResults(results)
    }

    fun clearResults() {
        toolWindow?.updateResults(emptyList())
    }

    fun analyzeAllFiles() {
        ApplicationManager.getApplication().executeOnPooledThread {
            try {
                val projectBasePath = project.basePath
                if (projectBasePath != null) {
                    val scriptPath = "$projectBasePath/hetu_analyze_all.sh"
                    val scriptFile = File(scriptPath)

                    // Check if our script exists in the project directory
                    if (scriptFile.exists()) {
                        val process = ProcessBuilder("bash", scriptPath, projectBasePath).start()
                        val output = process.inputStream.bufferedReader().readText()
                        val errorOutput = process.errorStream.bufferedReader().readText()

                        val exitCode = process.waitFor()

                        val resultText = if (exitCode == 0) {
                            output
                        } else {
                            "Analysis completed with errors:\n$output\n\nErrors:\n$errorOutput"
                        }

                        // parse and update the tool window with structured results in EDT
                        val parsed = com.hetu.hetuscript.toolwindow.parseAnalysisOutput(resultText)
                        ApplicationManager.getApplication().invokeLater {
                            updateResults(parsed)
                        }
                    } else {
                        // If the script doesn't exist in the project, try to run hetu analyze directly
                        val parsed = analyzeProjectDirectly(projectBasePath)
                        ApplicationManager.getApplication().invokeLater {
                            updateResults(parsed)
                        }
                    }
                } else {
                    ApplicationManager.getApplication().invokeLater {
                        updateResults(listOf(com.hetu.hetuscript.toolwindow.AnalysisResult("", com.hetu.hetuscript.toolwindow.AnalysisStatus.UNKNOWN, "Error: Project base path is null")))
                    }
                }
            } catch (e: Exception) {
                ApplicationManager.getApplication().invokeLater {
                    updateResults(listOf(com.hetu.hetuscript.toolwindow.AnalysisResult("", com.hetu.hetuscript.toolwindow.AnalysisStatus.UNKNOWN, "Error running analysis: ${e.message}")))
                }
            }
        }
    }

    private fun analyzeProjectDirectly(projectPath: String): List<com.hetu.hetuscript.toolwindow.AnalysisResult> {
        // collect .ht files but exclude any under a 'build' directory
        val htFiles = File(projectPath).walk()
            .filter { it.extension == "ht" }
            .filter { file ->
                var p: java.io.File? = file.parentFile
                var inBuild = false
                while (p != null) {
                    if (p.name == "build") { inBuild = true; break }
                    p = p.parentFile
                }
                !inBuild
            }
            .toList()

        if (htFiles.isEmpty()) {
            return listOf(com.hetu.hetuscript.toolwindow.AnalysisResult("", com.hetu.hetuscript.toolwindow.AnalysisStatus.UNKNOWN, "No .ht files found in project: $projectPath"))
        }

        val results = mutableListOf<com.hetu.hetuscript.toolwindow.AnalysisResult>()

        for (file in htFiles) {
            try {
                val process = ProcessBuilder("hetu", "analyze", file.absolutePath).start()
                val output = process.inputStream.bufferedReader().readText()
                val errorOutput = process.errorStream.bufferedReader().readText()

                val exitCode = process.waitFor()

                val combined = StringBuilder()
                if (exitCode != 0) combined.append("Exit Code: $exitCode\n")
                if (output.isNotBlank()) combined.append(output.trim()).append('\n')
                if (errorOutput.isNotBlank()) combined.append("Error:\n").append(errorOutput.trim()).append('\n')

                val message = combined.toString().trim()

                val status = when {
                    exitCode != 0 || message.contains("error", ignoreCase = true) || message.contains("LateInitializationError") -> com.hetu.hetuscript.toolwindow.AnalysisStatus.ERROR
                    message.isBlank() || message.contains("no issues", ignoreCase = true) || message.contains("found 0 problem") -> com.hetu.hetuscript.toolwindow.AnalysisStatus.SUCCESS
                    message.contains("problem", ignoreCase = true) || message.contains("warning", ignoreCase = true) -> com.hetu.hetuscript.toolwindow.AnalysisStatus.WARNING
                    else -> com.hetu.hetuscript.toolwindow.AnalysisStatus.UNKNOWN
                }

                results.add(com.hetu.hetuscript.toolwindow.AnalysisResult(file.absolutePath, status, if (message.isBlank()) "No issues found" else message))
            } catch (e: Exception) {
                results.add(com.hetu.hetuscript.toolwindow.AnalysisResult(file.absolutePath, com.hetu.hetuscript.toolwindow.AnalysisStatus.ERROR, "Error analyzing: ${e.message}"))
            }
        }

        return results
    }
}