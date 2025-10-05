package com.hetu.hetuscript.action

import com.hetu.hetuscript.cli.HetuCliRunner
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.JBTextArea
import com.intellij.openapi.components.service
import java.awt.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

class HetuAnalyzeAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        
        // Run the analysis in a background task to avoid blocking the UI
        ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Analyzing Hetu Files", true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = false
                indicator.fraction = 0.0
                
                try {
                    // Find all .ht files in the project
                    val htFiles = findHetuFiles(project, indicator)
                    indicator.fraction = 0.2
                    
                    if (htFiles.isEmpty()) {
                        ApplicationManager.getApplication().invokeLater {
                            Messages.showInfoMessage(project, "No .ht files found in project", "Hetu Analyze")
                        }
                        return
                    }
                    
                    // Analyze each file and collect results
                    val results = mutableListOf<String>()
                    val totalFiles = htFiles.size
                    
                    htFiles.forEachIndexed { index, file ->
                        indicator.text = "Analyzing file ${index + 1}/${totalFiles}: ${file.name}"
                        indicator.fraction = 0.2 + (0.7 * index.toDouble() / totalFiles)
                        
                        try {
                            // Always add the file to results, even if there are no issues
                            val output = HetuCliRunner.analyze(project, file)
                            val result = StringBuilder("File: ${file.path}\n")
                            
                            if (output.exitCode != 0) {
                                result.append("Exit Code: ${output.exitCode}\n")
                            }
                            
                            if (output.stdout.isNotEmpty()) {
                                result.append("Output:\n${output.stdout}\n")
                            }
                            
                            if (output.stderr.isNotEmpty()) {
                                result.append("Error:\n${output.stderr}\n")
                            }
                            
                            if (result.length > ("File: ${file.path}\n").length) {
                                results.add(result.toString())
                            } else {
                                // Even if there's no output, indicate the file was analyzed
                                results.add("File: ${file.path}\nStatus: Analyzed (no output)\n")
                            }
                        } catch (ex: Exception) {
                            results.add("File: ${file.path}\nError: ${ex.message}\n")
                        }
                    }
                    
                    indicator.fraction = 0.9
                    
                    // Show results in a separate window
                    ApplicationManager.getApplication().invokeLater {
                        val resultsText = if (results.isEmpty()) {
                            "Analysis completed. No .ht files found in project."
                        } else {
                            "Analysis Results (${results.size} file(s) analyzed):\n\n${results.joinToString("\n---\n")}"
                        }
                        
                        // Show the results in the tool window via service
                        showResultsInToolWindow(project, resultsText)
                    }
                } catch (ex: Exception) {
                    ApplicationManager.getApplication().invokeLater {
                        Messages.showErrorDialog(project, "Error during analysis: ${ex.message}\n\nPlease make sure the 'hetu' command is available in your PATH and can be executed from the IDE.", "Analysis Error")
                    }
                }
                
                indicator.fraction = 1.0
            }
        })
    }
    
    private fun findHetuFiles(project: Project, indicator: ProgressIndicator): List<VirtualFile> {
        val result = mutableListOf<VirtualFile>()
        indicator.text = "Searching for .ht files..."
        
        val rootManager = ProjectRootManager.getInstance(project)
        val contentRoots = rootManager.contentRoots
        
        var totalFiles = 0
        // First count the total files for progress indicator
        contentRoots.forEach { root ->
            root.refresh(false, true) // Refresh to get the latest files
            val files = collectHetuFilesRecursively(root)
            totalFiles += files.size
        }
        
        // Handle case when no files are found
        if (totalFiles == 0) {
            return result  // Return empty list early
        }
        
        var filesProcessed = 0
        contentRoots.forEach { root ->
            val files = collectHetuFilesRecursively(root)
            files.forEach { file ->
                result.add(file)
                filesProcessed++
                indicator.fraction = 0.1 * (filesProcessed.toDouble() / totalFiles)
            }
        }
        
        return result
    }
    
    private fun collectHetuFilesRecursively(root: VirtualFile): List<VirtualFile> {
        val result = mutableListOf<VirtualFile>()
        
        fun collectRecursively(current: VirtualFile) {
            if (current.isDirectory) {
                current.children.forEach { child ->
                    if (!child.isDirectory) {
                        if (child.extension == "ht") {
                            result.add(child)
                        }
                    } else {
                        // Skip hidden directories like .git
                        if (!child.name.startsWith(".")) {
                            collectRecursively(child)
                        }
                    }
                }
            } else if (current.extension == "ht") {
                result.add(current)
            }
        }
        
        collectRecursively(root)
        return result
    }
    
    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null
    }
    
    private fun showResultsInToolWindow(project: Project, resultsText: String) {
        val toolWindowManager = com.intellij.openapi.wm.ToolWindowManager.getInstance(project)
        val toolWindow = toolWindowManager.getToolWindow("Hetu Analyzer")
        
        if (toolWindow != null) {
            // Update the results via the service
            val service = project.service<com.hetu.hetuscript.service.HetuAnalyzerService>()
            service.updateResults(resultsText)
            
            // Show the tool window
            toolWindow.show { }
        } else {
            // Fallback to dialog if tool window is not available
            val analyzeResultsDialog = HetuAnalyzeResultsDialog(project, resultsText, emptyList())
            analyzeResultsDialog.show()
        }
    }
}