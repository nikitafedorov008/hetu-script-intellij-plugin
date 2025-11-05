package com.hetu.hetuscript

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class HetuParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer = HetuScriptLexer()

    override fun createParser(project: Project?): PsiParser = HetuPsiParser()

    override fun getFileNodeType(): IFileElementType = HetuFileElementType

    override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = STRINGS

    override fun createElement(node: ASTNode): PsiElement = HetuPsiTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = HetuFile(viewProvider)

    companion object {
        val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)
        val COMMENTS = TokenSet.create(
            HetuTokenTypes.COMMENT,
            HetuTokenTypes.LINE_COMMENT,
            HetuTokenTypes.BLOCK_COMMENT
        )
        val STRINGS = TokenSet.create(
            HetuTokenTypes.STRING,
            HetuTokenTypes.STRING_DOUBLE,
            HetuTokenTypes.STRING_SINGLE,
            HetuTokenTypes.TEMPLATE_STRING
        )
    }
}