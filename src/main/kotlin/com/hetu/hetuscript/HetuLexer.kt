package com.hetu.hetuscript

import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerPosition
import com.intellij.psi.tree.IElementType
import com.intellij.psi.TokenType

class HetuLexer : Lexer() {
    private var buffer: CharSequence = ""
    private var startOffset = 0
    private var endOffset = 0
    private var tokenStart = 0
    private var tokenEnd = 0
    private var tokenType: IElementType? = null
    
    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        this.tokenType = null
        advance()
    }
    
    override fun getState(): Int = 0
    
    override fun getTokenType(): IElementType? = tokenType
    
    override fun getTokenStart(): Int = tokenStart
    
    override fun getTokenEnd(): Int = tokenEnd
    
    override fun advance() {
        tokenStart = tokenEnd
        if (tokenStart >= endOffset) {
            tokenType = null
            return
        }
        
        val ch = buffer[tokenStart]
        
        // Handle whitespace as a separate token
        if (ch.isWhitespace()) {
            tokenEnd = tokenStart
            while (tokenEnd < endOffset && buffer[tokenEnd].isWhitespace()) {
                tokenEnd++
            }
            tokenType = TokenType.WHITE_SPACE
            return
        }
        
        // Comments
        if (ch == '/' && tokenStart + 1 < endOffset && buffer[tokenStart + 1] == '/') {
            tokenEnd = tokenStart + 2
            while (tokenEnd < endOffset && buffer[tokenEnd] != '\n') {
                tokenEnd++
            }
            tokenType = HetuTokenTypes.COMMENT
            return
        }
        
        // Strings
        if (ch == '"') {
            tokenEnd = tokenStart + 1
            while (tokenEnd < endOffset && buffer[tokenEnd] != '"') {
                tokenEnd++
            }
            if (tokenEnd < endOffset) {
                tokenEnd++ // Closing quote
            }
            tokenType = HetuTokenTypes.STRING
            return
        }
        
        // Numbers
        if (ch.isDigit()) {
            tokenEnd = tokenStart
            while (tokenEnd < endOffset && (buffer[tokenEnd].isDigit() || buffer[tokenEnd] == '.')) {
                tokenEnd++
            }
            tokenType = HetuTokenTypes.NUMBER
            return
        }
        
        // Identifiers and keywords
        if (ch.isLetter() || ch == '_') {
            tokenEnd = tokenStart
            while (tokenEnd < endOffset && (buffer[tokenEnd].isLetterOrDigit() || buffer[tokenEnd] == '_')) {
                tokenEnd++
            }
            
            val text = buffer.subSequence(tokenStart, tokenEnd).toString()
            tokenType = when (text) {
                "class", "func", "var", "final", "if", "else", "for", "while", "break", "continue",
                "return", "import", "export", "assert", "true", "false", "null", "this", "super", "new" -> 
                    HetuTokenTypes.KEYWORD
                else -> HetuTokenTypes.IDENTIFIER
            }
            return
        }
        
        // Single character operators/punctuation
        tokenEnd = tokenStart + 1
        tokenType = HetuTokenTypes.OPERATION_SIGN
    }
    
    override fun getBufferSequence(): CharSequence = buffer
    
    override fun getBufferEnd(): Int = endOffset
    
    override fun getCurrentPosition(): LexerPosition {
        return object : LexerPosition {
            override fun getOffset(): Int = tokenStart
            override fun getState(): Int = this@HetuLexer.getState()
        }
    }
    
    override fun restore(state: LexerPosition) {
        tokenStart = state.offset
        tokenEnd = state.offset
        // Reset other state variables as needed
        // This is a basic implementation - you might need more sophisticated state management
        advance()
    }
}