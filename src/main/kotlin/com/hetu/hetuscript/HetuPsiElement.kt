package com.hetu.hetuscript

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

open class HetuPsiElement(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun toString(): String = "HetuPsiElement"
}