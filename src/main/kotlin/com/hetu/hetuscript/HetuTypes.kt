package com.hetu.hetuscript

import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.PsiElement
import com.intellij.lang.ASTNode

object HetuPsiTypes {
    val FILE = IFileElementType(HetuLanguage)

    // These will be the element types for PSI elements
    val VAR_DECLARATION = HetuElementType("VAR_DECLARATION")
    val FUNCTION_DECLARATION = HetuElementType("FUNCTION_DECLARATION")
    val CLASS_DECLARATION = HetuElementType("CLASS_DECLARATION")
    
    object Factory {
        fun createElement(node: ASTNode): PsiElement {
            return HetuPsiElement(node)
        }
    }
}

class HetuElementType(debugName: String) : IElementType(debugName, HetuLanguage)