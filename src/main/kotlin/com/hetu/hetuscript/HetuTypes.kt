package com.hetu.hetuscript

import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.PsiElement
import com.intellij.lang.ASTNode

object HetuPsiTypes {
    val FILE = IFileElementType(HetuLanguage)

    // These will be the element types for PSI elements
    // For now, we can create placeholders
    
    object Factory {
        fun createElement(node: ASTNode): PsiElement {
            return HetuPsiElement(node)
        }
    }
}