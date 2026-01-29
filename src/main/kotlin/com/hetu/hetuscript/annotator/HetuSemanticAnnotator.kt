package com.hetu.hetuscript.annotator

import com.hetu.hetuscript.HetuSyntaxHighlighter
import com.hetu.hetuscript.HetuTokenTypes
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace

class HetuSemanticAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element.node.elementType == HetuTokenTypes.IDENTIFIER) {
            val identifierText = element.text
            
            // Look for contextual clues around the identifier
            val prevElement = getPreviousNonWhitespaceElement(element)
            val nextElement = getNextNonWhitespaceElement(element)
            
            when (prevElement?.text) {
                "var", "const", "final", "late" -> {
                    // This identifier follows a variable declaration keyword
                    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .textAttributes(HetuSyntaxHighlighter.VARIABLE_NAME_KEY)
                        .create()
                }
                "fun", "function", "func" -> {
                    // This identifier follows a function declaration keyword
                    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .textAttributes(HetuSyntaxHighlighter.FUNCTION_NAME_KEY)
                        .create()
                }
                "class", "interface" -> {
                    // This identifier follows a class declaration keyword
                    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .textAttributes(HetuSyntaxHighlighter.CLASS_NAME_KEY)
                        .create()
                }
                else -> {
                    // Check if this identifier is followed by parentheses (function call)
                    if (nextElement?.node?.elementType == HetuTokenTypes.PAREN_LEFT) {
                        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                            .textAttributes(HetuSyntaxHighlighter.FUNCTION_NAME_KEY)
                            .create()
                    }
                }
            }
        }
    }

    private fun getPreviousNonWhitespaceElement(element: PsiElement): PsiElement? {
        var prev = element.prevSibling
        while (prev != null) {
            if (prev.node.elementType != com.intellij.psi.TokenType.WHITE_SPACE) {
                return prev
            }
            prev = prev.prevSibling
        }
        return null
    }

    private fun getNextNonWhitespaceElement(element: PsiElement): PsiElement? {
        var next = element.nextSibling
        while (next != null) {
            if (next.node.elementType != com.intellij.psi.TokenType.WHITE_SPACE) {
                return next
            }
            next = next.nextSibling
        }
        return null
    }
}