package com.hetu.hetuscript.annotator

import com.hetu.hetuscript.HetuSyntaxHighlighter
import com.hetu.hetuscript.HetuTokenTypes
import com.hetu.hetuscript.HetuPsiTypes
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement

class HetuSemanticAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element.node.elementType) {
            // Highlight identifiers that are part of variable declarations
            HetuTokenTypes.IDENTIFIER -> {
                val parent = element.parent
                when (parent?.node?.elementType) {
                    HetuPsiTypes.VAR_DECLARATION -> {
                        // This is a variable name in a variable declaration
                        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                            .textAttributes(HetuSyntaxHighlighter.VARIABLE_NAME_KEY)
                            .create()
                    }
                    HetuPsiTypes.FUNCTION_DECLARATION -> {
                        // This is a function name in a function declaration
                        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                            .textAttributes(HetuSyntaxHighlighter.FUNCTION_NAME_KEY)
                            .create()
                    }
                    HetuPsiTypes.CLASS_DECLARATION -> {
                        // This is a class name in a class declaration
                        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                            .textAttributes(HetuSyntaxHighlighter.CLASS_NAME_KEY)
                            .create()
                    }
                }
            }
        }
    }
}