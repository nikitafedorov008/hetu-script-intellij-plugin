package com.hetu.hetuscript

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class HetuSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        return HetuLexer()
    }

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        return when (tokenType) {
            HetuTokenTypes.IDENTIFIER -> IDENTIFIER_KEYS
            HetuTokenTypes.NUMBER -> NUMBER_KEYS
            HetuTokenTypes.STRING -> STRING_KEYS
            HetuTokenTypes.COMMENT -> COMMENT_KEYS
            HetuTokenTypes.KEYWORD -> KEYWORD_KEYS
            HetuTokenTypes.OPERATION_SIGN -> OPERATION_SIGN_KEYS
            com.intellij.psi.TokenType.WHITE_SPACE -> EMPTY_KEYS
            else -> BAD_CHARACTER_KEYS
        }
    }

    companion object {
        // Keywords - use more vibrant color
        val KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        // Identifiers - use default identifier color
        val IDENTIFIER_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_IDENTIFIER",
            DefaultLanguageHighlighterColors.IDENTIFIER
        )
        
        // Numbers - use number color
        val NUMBER_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_NUMBER",
            DefaultLanguageHighlighterColors.NUMBER
        )
        
        // Strings - use string color
        val STRING_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_STRING",
            DefaultLanguageHighlighterColors.STRING
        )
        
        // Comments - use comment color
        val COMMENT_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT
        )
        
        // Operation signs (operators) - use operator color
        val OPERATION_SIGN_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_OPERATION_SIGN",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        // Bad characters - for invalid characters
        val BAD_CHARACTER_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_BAD_CHARACTER",
            HighlighterColors.BAD_CHARACTER
        )

        private val IDENTIFIER_KEYS = arrayOf(IDENTIFIER_KEY)
        private val NUMBER_KEYS = arrayOf(NUMBER_KEY)
        private val STRING_KEYS = arrayOf(STRING_KEY)
        private val COMMENT_KEYS = arrayOf(COMMENT_KEY)
        private val KEYWORD_KEYS = arrayOf(KEYWORD_KEY)
        private val OPERATION_SIGN_KEYS = arrayOf(OPERATION_SIGN_KEY)
        private val BAD_CHARACTER_KEYS = arrayOf(BAD_CHARACTER_KEY)
        private val EMPTY_KEYS = arrayOf<TextAttributesKey>()
    }
}