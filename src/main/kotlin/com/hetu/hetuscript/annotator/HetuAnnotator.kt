package com.hetu.hetuscript.annotator

import com.hetu.hetuscript.HetuLanguage
import com.hetu.hetuscript.cli.HetuCliRunner
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import java.util.concurrent.CompletableFuture

class HetuAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        // Only run analysis on file level elements
        if (element !is PsiFile || element.language != HetuLanguage) {
            return
        }

        // Perform analysis asynchronously to avoid blocking EDT
        analyzeFileAsync(element, holder)
    }

    private fun analyzeFileAsync(file: PsiFile, holder: AnnotationHolder) {
        // Schedule analysis to run in background
        val future = HetuCliRunner.analyzeAsync(file.project, file.virtualFile)
        
        future.whenComplete { analysisOutput, error ->
            if (error != null) {
                // Handle error, possibly log it
                return@whenComplete
            }
            
            // Run the annotation on the EDT after analysis completes
            ApplicationManager.getApplication().invokeLater {
                if (analysisOutput.exitCode != 0 || analysisOutput.stdout.isNotEmpty()) {
                    // Parse the analysis output for error/warning messages
                    val outputText = analysisOutput.stdout + analysisOutput.stderr
                    parseAndAnnotate(outputText, file, holder)
                }
            }
        }
    }

    private fun parseAndAnnotate(output: String, file: PsiFile, holder: AnnotationHolder) {
        // Parse the hetu analyze output to extract error/warning information
        // This is a simplified implementation - in practice you'd need to parse the actual CLI output format
        val lines = output.split("\n")
        
        for (line in lines) {
            // Example format might be: "filename:line:column: error|warning: message"
            // This is a simplified parsing - you'd adapt this to the actual hetu CLI output format
            if (line.contains("error:") || line.contains("warning:")) {
                // Extract line number and message - this is just a basic example
                // You'd need to parse the actual hetu CLI output format
                val regex = Regex("""(?:.*:)?(\d+):(\d+):\s*(error|warning):\s*(.*)""")
                val match = regex.find(line)
                
                if (match != null) {
                    val (lineNum, colNum, level, message) = match.destructured
                    
                    try {
                        val lineNumInt = lineNum.toInt()
                        val colNumInt = colNum.toInt()
                        
                        // Get the document to calculate the correct offset
                        val document = file.viewProvider.document
                        if (document != null) {
                            val startOffset = getLineStartOffset(document, lineNumInt - 1) + (colNumInt - 1)
                            val endOffset = startOffset + 1 // Just highlight one character for now
                            
                            val severity = if (level == "error") HighlightSeverity.ERROR else HighlightSeverity.WARNING
                            
                            holder.newAnnotation(severity, message)
                                .range(TextRange.create(startOffset, endOffset))
                                .create()
                        }
                    } catch (e: NumberFormatException) {
                        // Handle parsing errors
                    }
                }
            }
        }
    }

    private fun getLineStartOffset(document: Document, line: Int): Int {
        if (line < 0 || line >= document.lineCount) {
            return 0
        }
        return document.getLineStartOffset(line)
    }
}