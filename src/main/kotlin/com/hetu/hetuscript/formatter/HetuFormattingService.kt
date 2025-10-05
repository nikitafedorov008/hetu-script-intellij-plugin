package com.hetu.hetuscript.formatter

import com.hetu.hetuscript.cli.HetuCliRunner
import com.intellij.execution.ExecutionException
import com.intellij.formatting.service.AsyncDocumentFormattingService
import com.intellij.formatting.service.AsyncFormattingRequest
import com.intellij.formatting.service.FormattingService
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile

class HetuFormattingService : AsyncDocumentFormattingService() {
    override fun getFeatures(): MutableSet<FormattingService.Feature> {
        return mutableSetOf(FormattingService.Feature.FORMAT_FRAGMENTS)
    }

    override fun getName(): String {
        return "Hetu CLI Formatter"
    }

    override fun canFormat(file: PsiFile): Boolean {
        return file.name.endsWith(".ht")
    }

    override fun createFormattingTask(request: AsyncFormattingRequest): FormattingTask? {
        return object : FormattingTask {
            override fun run() {
                val file = request.context.containingFile
                val document = file.viewProvider.document  // Fix: use viewProvider.document instead of context.document
                
                try {
                    // Run the format command in the background task context (not on EDT)
                    val output = HetuCliRunner.format(file.project, file.virtualFile)
                    
                    if (output.exitCode == 0) {
                        // Apply the formatted content back to the document
                        val formattedText = output.stdout
                        
                        // Update the document on the EDT
                        com.intellij.openapi.application.ApplicationManager.getApplication().invokeLater {
                            document?.setText(formattedText)
                        }
                    }
                } catch (e: ExecutionException) {
                    // Handle execution errors
                }
            }

            override fun cancel(): Boolean {
                return false
            }
        }
    }

    override fun getNotificationGroupId(): String {
        return "Hetu Formatting"
    }
}