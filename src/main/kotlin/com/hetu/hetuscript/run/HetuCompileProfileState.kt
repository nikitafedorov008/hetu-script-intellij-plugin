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
        val configuredOutput = configuration.getOutputPath()

        // Get the virtual file for the script
        val file = LocalFileSystem.getInstance().findFileByPath(sourceScriptPath)
            ?: throw RuntimeException("Source script file not found: $sourceScriptPath")

        // resolve effective output path (may use pubspec.yaml/version when available)
        val outputPath = OutputPathResolver.resolveOutputPath(project, file, configuredOutput)
        java.io.File(outputPath).parentFile?.mkdirs()

        // Create console view to display output
        val consoleView = com.intellij.execution.impl.ConsoleViewImpl(project, true) 

        // Create a cancellable ProcessHandler so the Run toolwindow knows when the run finishes
        val handler = object : ProcessHandler() {
            @Volatile
            private var futureRef: java.util.concurrent.Future<*>? = null

            fun setFuture(f: java.util.concurrent.Future<*>) {
                this.futureRef = f
            }

            // public helper to safely notify termination from outside the subclass
            fun finish(exitCode: Int) {
                notifyProcessTerminated(exitCode)
            }

            override fun destroyProcessImpl() {
                // try to cancel background compilation
                futureRef?.cancel(true)
                finish(1)
            }

            override fun detachProcessImpl() {
                notifyProcessDetached()
            }

            override fun detachIsDefault(): Boolean = false

            override fun getProcessInput(): java.io.OutputStream? = null
        }

        // Attach a listener to show termination status in console
        com.intellij.execution.process.ProcessTerminatedListener.attach(handler, project)

        // Run the hetu compile on a background thread and stream output to console
        val future = ApplicationManager.getApplication().executeOnPooledThread {
            try {
                val output = HetuCliRunner.compile(project, file, outputPath)

                // publish output on EDT
                ApplicationManager.getApplication().invokeLater {
                    consoleView.print(output.getStdout(), ConsoleViewContentType.NORMAL_OUTPUT)
                    if (output.getStderr().isNotEmpty()) {
                        consoleView.print(output.getStderr(), ConsoleViewContentType.ERROR_OUTPUT)
                    }
                }

                handler.finish(0)
            } catch (e: Exception) {
                ApplicationManager.getApplication().invokeLater {
                    consoleView.print("Error executing compile: ${e.message}\n", ConsoleViewContentType.ERROR_OUTPUT)
                }
                handler.finish(1)
            }
        }

        // keep future for cancellation
        handler.setFuture(future)

        return com.intellij.execution.DefaultExecutionResult(consoleView, handler)
    }
}