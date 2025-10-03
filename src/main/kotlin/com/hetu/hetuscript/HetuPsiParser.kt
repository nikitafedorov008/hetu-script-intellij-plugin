package com.hetu.hetuscript

import com.intellij.lang.*
import com.intellij.psi.tree.IElementType

class HetuPsiParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val marker = builder.mark()
        while (!builder.eof()) {
            builder.advanceLexer()
        }
        marker.done(root)
        return builder.treeBuilt
    }
}