package com.hetu.hetuscript.action

import com.hetu.hetuscript.cli.HetuCliRunner
import com.hetu.hetuscript.run.HetuCompileConfiguration
import com.intellij.execution.ExecutionManager
import com.intellij.execution.ExecutorRegistry
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.impl.ExecutionManagerImpl
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.EmptyProgressIndicator

class HetuCompileAction : AnAction() {
    override fun update(e: AnActionEvent) {
        val project = e.project
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        
        // Enable the action if we have a project and a .ht file selected
        e.presentation.isEnabledAndVisible = project != null && 
                                              virtualFile != null && 
                                              virtualFile.extension == "ht"
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        if (virtualFile.extension != "ht") return

        // Create a temporary compile configuration for the selected file
        val sourcePath = virtualFile.path
        val outputPath = getDefaultOutputPath(project, sourcePath)
        
        try {
            // Run the hetu compile command on the selected file
            val output = HetuCliRunner.compile(project, virtualFile, outputPath)
            
            // Show the output in a message
            val resultText = if (output.exitCode == 0) {
                "Compilation successful!\n${output.stdout}"
            } else {
                "Compilation failed!\nstdout: ${output.stdout}\nstderr: ${output.stderr}"
            }
            
            Messages.showInfoMessage(project, resultText, "Hetu Compile Result")
        } catch (ex: Exception) {
            Messages.showErrorDialog(project, "Error during compilation: ${ex.message}", "Compilation Error")
        }
    }

    private fun getDefaultOutputPath(project: Project, sourcePath: String): String {
        // Use project base path for the build directory
        val projectBasePath = project.basePath ?: System.getProperty("user.dir")
        val buildDir = java.io.File(projectBasePath, "build/hetu")
        buildDir.mkdirs() // Create directory if it doesn't exist
        return java.io.File(buildDir, "output.out").absolutePath
    }
}