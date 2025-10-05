package com.hetu.hetuscript.run

import com.hetu.hetuscript.cli.HetuCliRunner
import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.process.CapturingProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.EmptyProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.actionSystem.AnAction
import java.util.concurrent.CompletableFuture

// Custom ProcessHandler for async execution
class HetuProcessHandler(
    private val consoleView: ConsoleView,
    private val project: Project,
    private val future: CompletableFuture<com.intellij.execution.process.ProcessOutput>
) : ProcessHandler() {
    
    init {
        // Handle the async result
        future.whenComplete { output, error ->
            if (error != null) {
                ApplicationManager.getApplication().invokeLater {
                    consoleView.print("Error executing script: ${error.message}\n", ConsoleViewContentType.ERROR_OUTPUT)
                    notifyProcessTerminated(1)
                }
            } else {
                ApplicationManager.getApplication().invokeLater {
                    consoleView.print(output.stdout, ConsoleViewContentType.NORMAL_OUTPUT)
                    if (output.stderr.isNotEmpty()) {
                        consoleView.print(output.stderr, ConsoleViewContentType.ERROR_OUTPUT)
                    }
                    notifyProcessTerminated(output.exitCode)
                }
            }
        }
    }
    
    override fun startNotify() {
        // Start listening for process output
    }
    
    override fun getProcessInput() = null
    
    override fun detachProcessImpl() {
        // Clean up resources if needed
        future.cancel(true)
    }
    
    override fun destroyProcessImpl() {
        // Clean up resources if needed
        future.cancel(true)
    }
    
    override fun detachIsDefault() = true
}

class HetuRunProfileState(private val environment: com.intellij.execution.runners.ExecutionEnvironment) : RunProfileState {
    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult? {
        val configuration = environment.runProfile as? HetuRunConfiguration
            ?: throw RuntimeException("Invalid configuration type")

        val project = environment.project
        val scriptPath = configuration.getScriptPath()

        // Get the virtual file for the script
        val file = LocalFileSystem.getInstance().findFileByPath(scriptPath)
            ?: throw RuntimeException("Script file not found: $scriptPath")

        // Create console view to display output
        val consoleView = com.intellij.execution.impl.ConsoleViewImpl(project, true)

        // Run the hetu script asynchronously using the CLI
        val future = HetuCliRunner.runAsync(project, file)

        // Create a custom process handler to manage the async execution
        val processHandler = HetuProcessHandler(consoleView, project, future)

        // Return execution result with the process handler
        return object : ExecutionResult {
            override fun getExecutionConsole() = consoleView
            override fun getProcessHandler() = processHandler
            override fun getActions() = emptyArray<AnAction>()
        }
    }
}