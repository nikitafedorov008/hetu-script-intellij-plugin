package com.hetu.hetuscript.external

import com.hetu.hetuscript.HetuLanguage
import com.hetu.hetuscript.cli.HetuCliRunner
import com.intellij.codeInsight.daemon.HighlightDisplayKey
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.progress.EmptyProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager

class HetuAnalyzerInspection : LocalInspectionTool() {
    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor>? {
        if (file.language != HetuLanguage) {
            return null
        }

        // For performance and threading reasons, we skip CLI analysis during inspections
        // CLI-based analysis should happen in dedicated background tasks or annotators
        // that run outside of the inspection framework
        return emptyArray()
    }
}