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
import com.hetu.hetuscript.run.OutputPathResolver
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
        val outputPath = getDefaultOutputPath(project, virtualFile)

        try {
            // Run the hetu compile command on the selected file
            val output = HetuCliRunner.compile(project, virtualFile, outputPath)

            // If compilation succeeded, present a compact summary: saved path, total time, files involved
            if (output.exitCode == 0) {
                val stdout = try { output.getStdout() } catch (_: Throwable) { output.stdout }

                // extract saved path (e.g. saved file to [/path/to/out])
                val savedPath = Regex("saved file to \\[([^]]+)\\]", RegexOption.IGNORE_CASE).find(stdout)?.groupValues?.get(1)

                // if the saved file is inside the project, display it as "<projectName>/<relative/path>"
                val displaySavedPath: String? = if (savedPath != null && project.basePath != null && savedPath.startsWith(project.basePath!!)) {
                    val rel = savedPath.removePrefix(project.basePath!!.trimEnd('/') + "/").trimStart('/')
                    "${project.name}/$rel"
                } else savedPath

                // collect timings like "hetu: 28ms to parse [/path/file.ht]" — robust capture of ms + .ht path
                val timingRegex = Regex("(\\d+)ms[^\\[]*\\[([^]]+?\\.ht)\\]")
                val perFileTimes = mutableMapOf<String, Long>()
                for (m in timingRegex.findAll(stdout)) {
                    val ms = m.groupValues[1].toLongOrNull() ?: 0L
                    val file = m.groupValues[2]
                    perFileTimes[file] = (perFileTimes[file] ?: 0L) + ms
                }

                val genericMsRegex = Regex("(\\d+)ms")

                // fallback: if no detailed timing lines, sum all ms occurrences
                val totalMs = if (perFileTimes.isNotEmpty()) {
                    perFileTimes.values.sum()
                } else {
                    genericMsRegex.findAll(stdout).map { it.groupValues[1].toLong() }.sum()
                }

                // collect list of files mentioned in brackets (e.g. [..file.ht]) and include timed files
                val bracketFileRegex = Regex("\\[([^]]+?\\.ht)\\]")
                val files = mutableSetOf<String>()
                for (m in bracketFileRegex.findAll(stdout)) files.add(m.groupValues[1])
                files.addAll(perFileTimes.keys)

                val fileCount = files.size

                val details = if (perFileTimes.isNotEmpty()) {
                    perFileTimes.entries.sortedByDescending { it.value }
                        .joinToString("\n") { (f, t) -> "- ${f}: ${t} ms" }
                } else {
                    "(detailed per-file timings not available)"
                }

                val summary = buildString {
                    append("Compilation successful!\n")
                    if (displaySavedPath != null) append("Saved: $displaySavedPath\n")
                    append("Files involved: $fileCount\n")
                    append("Total time: ${totalMs} ms\n\n")
                    append("Details:\n")
                    append(details)
                    append("\n\nFull output:\n${stdout}")
                }

                Messages.showInfoMessage(project, summary, "Hetu Compile Result")
            } else {
                val resultText = "Compilation failed!\nstdout: ${output.stdout}\nstderr: ${output.stderr}"
                Messages.showErrorDialog(project, resultText, "Hetu Compile Result")
            }
        } catch (ex: Exception) {
            Messages.showErrorDialog(project, "Error during compilation: ${ex.message}", "Compilation Error")
        }
    }

    private fun getDefaultOutputPath(project: Project, sourceFile: VirtualFile): String {
        val path = OutputPathResolver.resolveOutputPath(project, sourceFile, null)
        java.io.File(path).parentFile?.mkdirs()
        return path
    }
}