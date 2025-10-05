package com.hetu.hetuscript.cli

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.util.concurrent.CompletableFuture

class HetuCliRunner {
    companion object {
        private const val HETU_COMMAND = "hetu"

        fun run(project: Project, hetuFile: VirtualFile): ProcessOutput {
            val commandLine = GeneralCommandLine(HETU_COMMAND, "run", hetuFile.path)
                .withWorkDirectory(hetuFile.parent.path)
                .withRedirectErrorStream(true)

            return runCommandLine(commandLine)
        }

        fun analyze(project: Project, hetuFile: VirtualFile): ProcessOutput {
            val commandLine = GeneralCommandLine(HETU_COMMAND, "analyze", hetuFile.path)
                .withWorkDirectory(hetuFile.parent.path)
                .withRedirectErrorStream(true)

            return runCommandLine(commandLine)
        }

        fun format(project: Project, hetuFile: VirtualFile, outFile: String? = null): ProcessOutput {
            val commandLine = if (outFile != null) {
                GeneralCommandLine(HETU_COMMAND, "format", hetuFile.path, "-o", outFile)
                    .withWorkDirectory(hetuFile.parent.path)
                    .withRedirectErrorStream(true)
            } else {
                GeneralCommandLine(HETU_COMMAND, "format", hetuFile.path)
                    .withWorkDirectory(hetuFile.parent.path)
                    .withRedirectErrorStream(true)
            }

            return runCommandLine(commandLine)
        }

        fun compile(project: Project, hetuFile: VirtualFile, outputPath: String): ProcessOutput {
            val commandLine = GeneralCommandLine(HETU_COMMAND, "compile", hetuFile.path, outputPath)
                .withWorkDirectory(hetuFile.parent.path)
                .withRedirectErrorStream(true)

            return runCommandLine(commandLine)
        }

        // Asynchronous versions of the methods
        fun runAsync(project: Project, hetuFile: VirtualFile): CompletableFuture<ProcessOutput> {
            return runInBackground(project, "Running Hetu Script") {
                run(project, hetuFile)
            }
        }

        fun analyzeAsync(project: Project, hetuFile: VirtualFile): CompletableFuture<ProcessOutput> {
            return runInBackground(project, "Analyzing Hetu Script") {
                analyze(project, hetuFile)
            }
        }

        fun formatAsync(project: Project, hetuFile: VirtualFile, outFile: String? = null): CompletableFuture<ProcessOutput> {
            return runInBackground(project, "Formatting Hetu Script") {
                format(project, hetuFile, outFile)
            }
        }

        fun compileAsync(project: Project, hetuFile: VirtualFile, outputPath: String): CompletableFuture<ProcessOutput> {
            return runInBackground(project, "Compiling Hetu Script") {
                compile(project, hetuFile, outputPath)
            }
        }

        private fun runInBackground(project: Project, title: String, action: () -> ProcessOutput): CompletableFuture<ProcessOutput> {
            val future = CompletableFuture<ProcessOutput>()
            
            ProgressManager.getInstance().run(object : Task.Backgroundable(project, title, true) {
                override fun run(indicator: ProgressIndicator) {
                    try {
                        val result = action()
                        future.complete(result)
                    } catch (e: Exception) {
                        future.completeExceptionally(e)
                    }
                }

                override fun onCancel() {
                    future.cancel(true)
                }

                override fun onThrowable(error: Throwable) {
                    future.completeExceptionally(error)
                }
            })
            
            return future
        }

        private fun runCommandLine(commandLine: GeneralCommandLine): ProcessOutput {
            val handler = CapturingProcessHandler(commandLine)
            return handler.runProcess()
        }
    }
}