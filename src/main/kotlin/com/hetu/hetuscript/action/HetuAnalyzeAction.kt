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
                    
                    // Analyze each file and collect structured results
                    val results = mutableListOf<com.hetu.hetuscript.toolwindow.AnalysisResult>()
                    val totalFiles = htFiles.size

                    htFiles.forEachIndexed { index, file ->
                        indicator.text = "Analyzing file ${index + 1}/${totalFiles}: ${file.name}"
                        indicator.fraction = 0.2 + (0.7 * index.toDouble() / totalFiles)

                        try {
                            val output = HetuCliRunner.analyze(project, file)
                            val combined = StringBuilder()
                            if (output.exitCode != 0) combined.append("Exit Code: ${output.exitCode}\n")
                            if (output.stdout.isNotEmpty()) combined.append(output.stdout.trim()).append('\n')
                            if (output.stderr.isNotEmpty()) combined.append("Error:\n").append(output.stderr.trim()).append('\n')

                            val message = combined.toString().trim()

                            val status = when {
                                output.exitCode != 0 || message.contains("error", ignoreCase = true) || message.contains("LateInitializationError") -> com.hetu.hetuscript.toolwindow.AnalysisStatus.ERROR
                                message.isBlank() || message.contains("no issues", ignoreCase = true) || message.contains("found 0 problem") -> com.hetu.hetuscript.toolwindow.AnalysisStatus.SUCCESS
                                message.contains("problem", ignoreCase = true) || message.contains("warning", ignoreCase = true) -> com.hetu.hetuscript.toolwindow.AnalysisStatus.WARNING
                                else -> com.hetu.hetuscript.toolwindow.AnalysisStatus.UNKNOWN
                            }

                            results.add(com.hetu.hetuscript.toolwindow.AnalysisResult(file.path, status, if (message.isBlank()) "No issues found" else message))
                        } catch (ex: Exception) {
                            results.add(com.hetu.hetuscript.toolwindow.AnalysisResult(file.path, com.hetu.hetuscript.toolwindow.AnalysisStatus.ERROR, "${ex.message}"))
                        }
                    }

                    indicator.fraction = 0.9

                    // Show results in a separate window (only problematic entries will be shown in the toolwindow)
                    ApplicationManager.getApplication().invokeLater {
                        // Show the results in the tool window via service
                        val service = project.service<com.hetu.hetuscript.service.HetuAnalyzerService>()
                        service.updateResults(results)

                        // Make sure tool window is visible
                        val toolWindowManager = com.intellij.openapi.wm.ToolWindowManager.getInstance(project)
                        toolWindowManager.getToolWindow("Hetu Analyzer")?.show { }
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
            // do not descend into build directories
            if (current.isDirectory && current.name == "build") return

            if (current.isDirectory) {
                current.children.forEach { child ->
                    if (!child.isDirectory) {
                        if (child.extension == "ht") {
                            // skip files inside any build/ ancestor
                            var p = child.parent
                            var inBuild = false
                            while (p != null) {
                                if (p.name == "build") { inBuild = true; break }
                                p = p.parent
                            }
                            if (!inBuild) result.add(child)
                        }
                    } else {
                        // Skip hidden directories like .git and any build directories
                        if (!child.name.startsWith(".") && child.name != "build") {
                            collectRecursively(child)
                        }
                    }
                }
            } else if (current.extension == "ht") {
                // ensure file is not inside a build/ tree
                var p = current.parent
                var inBuild = false
                while (p != null) {
                    if (p.name == "build") { inBuild = true; break }
                    p = p.parent
                }
                if (!inBuild) result.add(current)
            }
        }
        
        collectRecursively(root)
        return result
    }
    
    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null
    }
    
    private fun showResultsInToolWindow(project: Project, results: List<com.hetu.hetuscript.toolwindow.AnalysisResult>) {
        val toolWindowManager = com.intellij.openapi.wm.ToolWindowManager.getInstance(project)
        val toolWindow = toolWindowManager.getToolWindow("Hetu Analyzer")

        if (toolWindow != null) {
            // Update the results via the service
            val service = project.service<com.hetu.hetuscript.service.HetuAnalyzerService>()
            service.updateResults(results)

            // Show the tool window
            toolWindow.show { }
        } else {
            // Fallback to dialog if tool window is not available — show textual summary
            val resultsText = if (results.isEmpty()) "No problems found" else results.joinToString("\n\n") { "File: ${it.filePath}\n${it.message}" }
            val analyzeResultsDialog = HetuAnalyzeResultsDialog(project, resultsText, results.map { it.filePath })
            analyzeResultsDialog.show()
        }
    }
}