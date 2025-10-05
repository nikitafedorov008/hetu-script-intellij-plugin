package com.hetu.hetuscript

import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerPosition
import com.intellij.psi.tree.IElementType
import com.intellij.psi.TokenType

class HetuScriptLexer : Lexer() {
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
        
        // Line comments
        if (ch == '/' && tokenStart + 1 < endOffset && buffer[tokenStart + 1] == '/') {
            tokenEnd = tokenStart + 2
            while (tokenEnd < endOffset && buffer[tokenEnd] != '\n' && buffer[tokenEnd] != '\r') {
                tokenEnd++
            }
            tokenType = HetuTokenTypes.LINE_COMMENT
            return
        }
        
        // Block comments
        if (ch == '/' && tokenStart + 1 < endOffset && buffer[tokenStart + 1] == '*') {
            tokenEnd = tokenStart + 2
            while (tokenEnd + 1 < endOffset) {
                if (buffer[tokenEnd] == '*' && buffer[tokenEnd + 1] == '/') {
                    tokenEnd += 2
                    break
                }
                tokenEnd++
            }
            tokenType = HetuTokenTypes.BLOCK_COMMENT
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
            // Check if it's a hex number (starts with 0x)
            if (ch == '0' && tokenEnd + 1 < endOffset && (buffer[tokenEnd + 1] == 'x' || buffer[tokenEnd + 1] == 'X')) {
                tokenEnd += 2 // Skip 0x
                while (tokenEnd < endOffset && (buffer[tokenEnd].isDigit() || buffer[tokenEnd].lowercaseChar() in 'a'..'f')) {
                    tokenEnd++
                }
                tokenType = HetuTokenTypes.HEX_NUMBER
            } else {
                var hasDecimalPoint = false
                while (tokenEnd < endOffset && (buffer[tokenEnd].isDigit() || buffer[tokenEnd] == '.')) {
                    if (buffer[tokenEnd] == '.') {
                        if (hasDecimalPoint) break // More than one decimal point, stop
                        hasDecimalPoint = true
                    }
                    tokenEnd++
                }
                tokenType = if (hasDecimalPoint) HetuTokenTypes.FLOAT_NUMBER else HetuTokenTypes.INTEGER_NUMBER
            }
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
                // More specific control keywords
                "if" -> HetuTokenTypes.IF_CONDITIONAL
                "else" -> HetuTokenTypes.ELSE_CONDITIONAL
                "elif" -> HetuTokenTypes.ELIF_CONDITIONAL  // if Hetu supports elif
                
                // More specific loop keywords
                "for" -> HetuTokenTypes.FOR_LOOP
                "while" -> HetuTokenTypes.WHILE_LOOP
                "do" -> HetuTokenTypes.DO_WHILE_LOOP
                "break", "continue" -> HetuTokenTypes.LOOP_KEYWORD
                "return", "throw" -> HetuTokenTypes.FLOW_KEYWORD
                "switch", "when", "case", "default" -> HetuTokenTypes.SWITCH_KEYWORD
                "in", "is" -> HetuTokenTypes.KEYWORD_CONTROL
                
                // Variable declaration keywords  
                "var" -> HetuTokenTypes.VAR_DECLARATION
                "const" -> HetuTokenTypes.CONST_DECLARATION
                "final" -> HetuTokenTypes.FINAL_DECLARATION
                
                // More specific function keywords
                "fun", "function" -> HetuTokenTypes.FUNCTION_KEYWORD
                "get", "set" -> HetuTokenTypes.GET_SET_FUNCTION
                "constructor", "construct" -> HetuTokenTypes.CONSTRUCTOR_FUNCTION
                
                // Class and structure keywords
                "class", "struct", "namespace" -> HetuTokenTypes.CLASS_KEYWORD
                "interface", "override", "virtual", "late", "typedef" -> HetuTokenTypes.KEYWORD_DECLARATION
                
                // Storage modifiers
                "public" -> HetuTokenTypes.PUBLIC_MODIFIER
                "private" -> HetuTokenTypes.PRIVATE_MODIFIER
                "protected" -> HetuTokenTypes.STORAGE_MODIFIER
                "external" -> HetuTokenTypes.EXTERNAL_MODIFIER
                "static" -> HetuTokenTypes.STATIC_MODIFIER
                "async", "abstract" -> HetuTokenTypes.MODIFIER_KEYWORD
                
                // Storage types (Hetu-specific)
                "int", "double", "bool", "str", "num", "any", "void", "never", "unknown", "String", "List", "Map", "dynamic", "Object" ->
                    HetuTokenTypes.STORAGE_TYPE_HETU
                
                // Import/export keywords
                "import", "export" -> HetuTokenTypes.IMPORT_KEYWORD
                "as" -> HetuTokenTypes.AS_KEYWORD
                
                // Enum keyword
                "enum" -> HetuTokenTypes.ENUM_KEYWORD
                
                // Other keywords
                "assert" -> HetuTokenTypes.ASSERT_KEYWORD
                "new" -> HetuTokenTypes.NEW_KEYWORD
                "typeof", "instanceof" -> HetuTokenTypes.TYPEOF_KEYWORD
                
                // Primitive values - more specific types
                "true", "false" -> HetuTokenTypes.BOOLEAN_LITERAL
                "null", "undefined" -> HetuTokenTypes.NULL_LITERAL
                "this", "super" -> HetuTokenTypes.THIS_SUPER_LITERAL
                
                else -> 
                    HetuTokenTypes.IDENTIFIER
            }
            return
        }
        
        // Single character operators/punctuation
        tokenEnd = tokenStart + 1
        tokenType = when (ch) {
            '{' -> HetuTokenTypes.CURLY_BRACES
            '}' -> HetuTokenTypes.CURLY_BRACES
            '[' -> HetuTokenTypes.SQUARE_BRACKETS
            ']' -> HetuTokenTypes.SQUARE_BRACKETS
            '(' -> HetuTokenTypes.ROUND_BRACKETS
            ')' -> HetuTokenTypes.ROUND_BRACKETS
            '.' -> HetuTokenTypes.DOT
            ',' -> HetuTokenTypes.COMMA
            ';' -> HetuTokenTypes.SEMICOLON
            ':' -> HetuTokenTypes.PUNCTUATION_COLON
            '+' -> {
                if (tokenEnd < endOffset && buffer[tokenEnd] == '+') {
                    tokenEnd++  // consume the second +
                    HetuTokenTypes.INCREMENT_DECREMENT_OPERATOR
                } else {
                    HetuTokenTypes.ARITHMETIC_OPERATOR
                }
            }
            '-' -> {
                if (tokenEnd < endOffset && buffer[tokenEnd] == '-') {
                    tokenEnd++  // consume the second -
                    HetuTokenTypes.INCREMENT_DECREMENT_OPERATOR
                } else {
                    HetuTokenTypes.ARITHMETIC_OPERATOR
                }
            }
            '*' -> {
                if (tokenEnd < endOffset && buffer[tokenEnd] == '=') {
                    tokenEnd++  // consume the =
                    HetuTokenTypes.COMPOUND_ASSIGNMENT_OPERATOR
                } else {
                    HetuTokenTypes.ARITHMETIC_OPERATOR
                }
            }
            '/' -> {
                if (tokenEnd < endOffset && buffer[tokenEnd] == '=') {
                    tokenEnd++  // consume the =
                    HetuTokenTypes.COMPOUND_ASSIGNMENT_OPERATOR
                } else {
                    HetuTokenTypes.ARITHMETIC_OPERATOR
                }
            }
            '%' -> {
                if (tokenEnd < endOffset && buffer[tokenEnd] == '=') {
                    tokenEnd++  // consume the =
                    HetuTokenTypes.COMPOUND_ASSIGNMENT_OPERATOR
                } else {
                    HetuTokenTypes.ARITHMETIC_OPERATOR
                }
            }
            '=' -> {
                if (tokenEnd < endOffset) {
                    when (buffer[tokenEnd]) {
                        '=' -> {
                            tokenEnd++  // consume the second =
                            HetuTokenTypes.COMPARISON_OPERATOR
                        }
                        '>' -> {
                            tokenEnd++  // consume the >
                            HetuTokenTypes.ARROW_OPERATOR  // we'll create this if needed
                        }
                        else -> HetuTokenTypes.ASSIGNMENT_OPERATOR
                    }
                } else {
                    HetuTokenTypes.ASSIGNMENT_OPERATOR
                }
            }
            '!' -> {
                if (tokenEnd < endOffset && buffer[tokenEnd] == '=') {
                    tokenEnd++  // consume the =
                    HetuTokenTypes.COMPARISON_OPERATOR
                } else {
                    HetuTokenTypes.LOGICAL_OPERATOR
                }
            }
            '>' -> {
                if (tokenEnd < endOffset) {
                    when (buffer[tokenEnd]) {
                        '=' -> {
                            tokenEnd++  // consume the =
                            HetuTokenTypes.RELATIONAL_OPERATOR
                        }
                        '>' -> {
                            tokenEnd++  // consume the second >
                            if (tokenEnd < endOffset && buffer[tokenEnd] == '>') {
                                tokenEnd++  // consume the third >
                                HetuTokenTypes.SHIFT_OPERATOR  // unsigned right shift >>>
                            } else {
                                HetuTokenTypes.SHIFT_OPERATOR  // right shift >>
                            }
                        }
                        else -> HetuTokenTypes.RELATIONAL_OPERATOR
                    }
                } else {
                    HetuTokenTypes.RELATIONAL_OPERATOR
                }
            }
            '<' -> {
                if (tokenEnd < endOffset) {
                    when (buffer[tokenEnd]) {
                        '=' -> {
                            tokenEnd++  // consume the =
                            HetuTokenTypes.RELATIONAL_OPERATOR
                        }
                        '<' -> {
                            tokenEnd++  // consume the second <
                            HetuTokenTypes.SHIFT_OPERATOR  // left shift <<
                        }
                        else -> HetuTokenTypes.RELATIONAL_OPERATOR
                    }
                } else {
                    HetuTokenTypes.RELATIONAL_OPERATOR
                }
            }
            '&' -> {
                if (tokenEnd < endOffset && buffer[tokenEnd] == '&') {
                    tokenEnd++  // consume the second &
                    HetuTokenTypes.LOGICAL_OPERATOR  // &&
                } else {
                    HetuTokenTypes.BITWISE_OPERATOR  // &
                }
            }
            '|' -> {
                if (tokenEnd < endOffset && buffer[tokenEnd] == '|') {
                    tokenEnd++  // consume the second |
                    HetuTokenTypes.LOGICAL_OPERATOR  // ||
                } else {
                    HetuTokenTypes.BITWISE_OPERATOR  // |
                }
            }
            '^' -> HetuTokenTypes.BITWISE_OPERATOR
            '~' -> HetuTokenTypes.BITWISE_OPERATOR
            '?' -> {
                if (tokenEnd < endOffset) {
                    when (buffer[tokenEnd]) {
                        '?' -> {
                            tokenEnd++  // consume the second ?
                            HetuTokenTypes.LOGICAL_OPERATOR  // ??
                        }
                        else -> HetuTokenTypes.TERNARY_OPERATOR  // ? in ternary
                    }
                } else {
                    HetuTokenTypes.TERNARY_OPERATOR
                }
            }
            else -> HetuTokenTypes.OPERATION_SIGN
        }
    }
    
    override fun getBufferSequence(): CharSequence = buffer
    
    override fun getBufferEnd(): Int = endOffset
    
    override fun getCurrentPosition(): LexerPosition {
        return object : LexerPosition {
            override fun getOffset(): Int = tokenStart
            override fun getState(): Int = this.getState()
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