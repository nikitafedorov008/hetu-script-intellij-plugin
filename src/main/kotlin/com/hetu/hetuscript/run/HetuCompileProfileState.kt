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

class HetuCompileProfileState(private val environment: com.intellij.execution.runners.ExecutionEnvironment) : RunProfileState {
    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult? {
        val configuration = environment.runProfile as? HetuCompileConfiguration
            ?: throw RuntimeException("Invalid configuration type")

        val project = environment.project
        val sourceScriptPath = configuration.getSourceScriptPath()
        val outputPath = configuration.getOutputPath()

        // Get the virtual file for the script
        val file = LocalFileSystem.getInstance().findFileByPath(sourceScriptPath)
            ?: throw RuntimeException("Source script file not found: $sourceScriptPath")

        // Create console view to display output
        val consoleView = com.intellij.execution.impl.ConsoleViewImpl(project, true)

        // Run the hetu compile command using the CLI in background to avoid blocking EDT
        val output = try {
            HetuCliRunner.compile(project, file, outputPath)
        } catch (e: Exception) {
            // If there's an exception, print it to console and return empty ProcessOutput
            consoleView.print("Error executing compile: ${e.message}\n", ConsoleViewContentType.ERROR_OUTPUT)
            return object : ExecutionResult {
                override fun getExecutionConsole() = consoleView
                override fun getProcessHandler(): ProcessHandler? = null
                override fun getActions() = emptyArray<AnAction>()
            }
        }

        // Print the command output to console
        consoleView.print(output.getStdout(), ConsoleViewContentType.NORMAL_OUTPUT)
        if (output.getStderr().isNotEmpty()) {
            consoleView.print(output.getStderr(), ConsoleViewContentType.ERROR_OUTPUT)
        }

        // Return execution result - for simple implementation, return just console view
        return object : ExecutionResult {
            override fun getExecutionConsole() = consoleView
            override fun getProcessHandler(): ProcessHandler? = null
            override fun getActions() = emptyArray<AnAction>()
        }
    }
}