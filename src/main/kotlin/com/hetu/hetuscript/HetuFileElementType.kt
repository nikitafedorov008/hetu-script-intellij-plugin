package com.hetu.hetuscript

import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

object HetuFileElementType : IFileElementType(HetuLanguage) {
    override fun toString(): String = "Hetu Script File"
}