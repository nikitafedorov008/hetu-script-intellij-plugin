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
    
    fun updateResults(results: String) {
        toolWindow?.updateResults(results)
    }
    
    fun clearResults() {
        toolWindow?.updateResults("")
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
                        
                        val result = if (exitCode == 0) {
                            output
                        } else {
                            "Analysis completed with errors:\n$output\n\nErrors:\n$errorOutput"
                        }
                        
                        // Update the tool window with results in EDT
                        ApplicationManager.getApplication().invokeLater {
                            updateResults(result)
                        }
                    } else {
                        // If the script doesn't exist in the project, try to run hetu analyze directly
                        val result = analyzeProjectDirectly(projectBasePath)
                        ApplicationManager.getApplication().invokeLater {
                            updateResults(result)
                        }
                    }
                } else {
                    ApplicationManager.getApplication().invokeLater {
                        updateResults("Error: Project base path is null")
                    }
                }
            } catch (e: Exception) {
                ApplicationManager.getApplication().invokeLater {
                    updateResults("Error running analysis: ${e.message}")
                }
            }
        }
    }
    
    private fun analyzeProjectDirectly(projectPath: String): String {
        val htFiles = File(projectPath).walk()
            .filter { it.extension == "ht" }
            .toList()
        
        if (htFiles.isEmpty()) {
            return "No .ht files found in project: $projectPath"
        }
        
        val results = StringBuilder()
        results.append("Analyzing ${htFiles.size} .ht files in project: $projectPath\n")
        results.append("===============================================\n\n")
        
        for (file in htFiles) {
            try {
                results.append("Analyzing: ${file.absolutePath}\n")
                results.append("-----------------------------------------------\n")
                
                // Run hetu analyze on the individual file
                val process = ProcessBuilder("hetu", "analyze", file.absolutePath).start()
                val output = process.inputStream.bufferedReader().readText()
                val errorOutput = process.errorStream.bufferedReader().readText()
                
                val exitCode = process.waitFor()
                
                if (exitCode == 0) {
                    results.append(output)
                } else {
                    results.append("Errors:\n$errorOutput\n")
                }
                
                results.append("\n===============================================\n\n")
            } catch (e: Exception) {
                results.append("Error analyzing ${file.absolutePath}: ${e.message}\n\n")
            }
        }
        
        return results.toString()
    }
}