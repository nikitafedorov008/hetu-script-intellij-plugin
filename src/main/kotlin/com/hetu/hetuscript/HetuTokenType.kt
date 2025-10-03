package com.hetu.hetuscript

import com.intellij.psi.tree.IElementType

class HetuTokenType(debugName: String) : IElementType(debugName, HetuLanguage) {
    override fun toString(): String = "HetuTokenType." + super.toString()
}