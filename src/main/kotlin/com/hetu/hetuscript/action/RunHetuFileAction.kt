package com.hetu.hetuscript.action

import com.hetu.hetuscript.cli.HetuCliRunner
import com.intellij.execution.ExecutionManager
import com.intellij.execution.ExecutorRegistry
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager

class RunHetuFileAction : AnAction() {
    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = file?.extension == "ht"
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        if (file.extension != "ht") return

        // Option 1: Run directly using CLI
        runDirectly(project, file)

        // Option 2: Create and run a run configuration (alternative approach)
        // createAndRunConfiguration(project, file)
    }

    private fun runDirectly(project: Project, file: VirtualFile) {
        // Show a simple message while running in background
        val task = object : com.intellij.openapi.progress.Task.Backgroundable(project, "Running Hetu Script", true) {
            override fun run(indicator: com.intellij.openapi.progress.ProgressIndicator) {
                try {
                    indicator.isIndeterminate = true
                    indicator.text = "Executing Hetu script..."
                    
                    // Run the hetu file directly using CLI in background
                    val output = HetuCliRunner.run(project, file)
                    
                    // Show output in console or message on the EDT
                    com.intellij.openapi.application.ApplicationManager.getApplication().invokeLater {
                        com.intellij.openapi.ui.Messages.showMessageDialog(
                            project,
                            output.stdout + if (output.stderr.isNotEmpty()) "\nErrors:\n" + output.stderr else "",
                            "Hetu Script Output",
                            com.intellij.openapi.ui.Messages.getInformationIcon()
                        )
                    }
                } catch (e: Exception) {
                    // Show error message on the EDT
                    com.intellij.openapi.application.ApplicationManager.getApplication().invokeLater {
                        com.intellij.openapi.ui.Messages.showErrorDialog(
                            project,
                            "Error running Hetu script: ${e.message}",
                            "Execution Error"
                        )
                    }
                }
            }
        }
        
        com.intellij.openapi.progress.ProgressManager.getInstance().run(task)
    }

    private fun createAndRunConfiguration(project: Project, file: VirtualFile) {
        // Implementation to create and run a configuration - this would be more complex
        // and is typically handled through the run configuration UI
    }
}