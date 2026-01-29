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
        
        // Strings - enhanced to support template strings with interpolation
        if (ch == '"') {
            tokenEnd = tokenStart + 1
            var hasInterpolation = false
            while (tokenEnd < endOffset && buffer[tokenEnd] != '"') {
                // Handle escape sequences
                if (buffer[tokenEnd] == '\\') {
                    tokenEnd++
                    if (tokenEnd < endOffset) tokenEnd++
                    continue
                }
                // Check for string interpolation
                if (tokenEnd + 1 < endOffset && buffer[tokenEnd] == '$' && buffer[tokenEnd + 1] == '{') {
                    hasInterpolation = true
                    tokenEnd++  // Consume '$'
                    tokenEnd++  // Consume '{'
                    // Skip until matching '}' (simple approach for lexer)
                    var braceCount = 1
                    var pos = tokenEnd
                    while (pos < endOffset && braceCount > 0) {
                        if (buffer[pos] == '{') {
                            braceCount++
                        } else if (buffer[pos] == '}') {
                            braceCount--
                        }
                        if (braceCount > 0) {
                            pos++
                        }
                    }
                    if (braceCount == 0 && pos < endOffset) {
                        pos++  // Include the closing '}'
                    }
                    tokenEnd = pos
                    continue
                }
                tokenEnd++
            }
            if (tokenEnd < endOffset) {
                tokenEnd++ // Closing quote
            }
            tokenType = if (hasInterpolation) HetuTokenTypes.TEMPLATE_STRING else HetuTokenTypes.STRING_DOUBLE
            return
        }
        
        // Single quote strings
        if (ch == '\'') {
            tokenEnd = tokenStart + 1
            var hasInterpolation = false
            while (tokenEnd < endOffset && buffer[tokenEnd] != '\'') {
                // Handle escape sequences
                if (buffer[tokenEnd] == '\\') {
                    tokenEnd++
                    if (tokenEnd < endOffset) tokenEnd++
                    continue
                }
                // Check for string interpolation
                if (tokenEnd + 1 < endOffset && buffer[tokenEnd] == '$' && buffer[tokenEnd + 1] == '{') {
                    hasInterpolation = true
                    tokenEnd++  // Consume '$'
                    tokenEnd++  // Consume '{'
                    // Skip until matching '}' (simple approach for lexer)
                    var braceCount = 1
                    var pos = tokenEnd
                    while (pos < endOffset && braceCount > 0) {
                        if (buffer[pos] == '{') {
                            braceCount++
                        } else if (buffer[pos] == '}') {
                            braceCount--
                        }
                        if (braceCount > 0) {
                            pos++
                        }
                    }
                    if (braceCount == 0 && pos < endOffset) {
                        pos++  // Include the closing '}'
                    }
                    tokenEnd = pos
                    continue
                }
                tokenEnd++
            }
            if (tokenEnd < endOffset) {
                tokenEnd++ // Closing quote
            }
            tokenType = if (hasInterpolation) HetuTokenTypes.TEMPLATE_STRING else HetuTokenTypes.STRING_SINGLE
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
                // Control flow keywords
                "if", "else", "elif", "for", "while", "do", "switch", "case", "default", "try", "catch", "finally" ->
                    HetuTokenTypes.CONTROL_FLOW_KEYWORD
                
                // Declaration keywords
                "class", "abstract", "async", "await", "func", "interface", "extends", "construct" ->
                    HetuTokenTypes.DECLARATION_KEYWORD
                
                // Other Column 1 keywords: as, assert, break, case, catch, const, continue, default, export, extends, external, final, finally, fun, import, in, is, new, private, protected, public, return, static, throw, try, var, when, while
                "as", "assert", "break", "case", "catch", "const", "continue", "export", "extends", "external", "final", "finally", "fun", "import", "in", "is", "new", "private", "protected", "public", "return", "static", "throw", "try", "var", "when", "while" ->
                    HetuTokenTypes.CONTROL_DECLARATION_KEYWORD
                
                // Custom file type Column 2: Operators and special methods
                "build", "construct", "fetch", "print", "rebuild" -> HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD
                
                // Custom file type Column 3: Type keywords
                "any", "array", "bool", "boolean", "dict", "dictionary", "double", "dynamic", "float", "int", "list", "map", "num", "object", "str", "string", "unit", "void" ->
                    HetuTokenTypes.TYPE_KEYWORD
                
                // Custom file type Column 4: Literal values and special identifiers
                "true", "false" -> HetuTokenTypes.LITERAL_BRACKET_KEYWORD
                "null", "undefined" -> HetuTokenTypes.LITERAL_BRACKET_KEYWORD
                "this", "super" -> HetuTokenTypes.LITERAL_BRACKET_KEYWORD
                
                // More specific control keywords (fallback for keywords not in Column 1)
                "if" -> HetuTokenTypes.IF_CONDITIONAL
                "else" -> HetuTokenTypes.ELSE_CONDITIONAL
                "elif" -> HetuTokenTypes.ELIF_CONDITIONAL
                
                // More specific loop keywords
                "for" -> HetuTokenTypes.FOR_LOOP
                "while" -> HetuTokenTypes.WHILE_LOOP
                "do" -> HetuTokenTypes.DO_WHILE_LOOP
                "break", "continue" -> HetuTokenTypes.LOOP_KEYWORD
                "return", "throw" -> HetuTokenTypes.FLOW_KEYWORD
                "switch", "when", "case", "default" -> HetuTokenTypes.SWITCH_KEYWORD
                "in", "is" -> HetuTokenTypes.KEYWORD_CONTROL
                
                // Variable declaration keywords (fallback for keywords not in Column 1) 
                "var" -> HetuTokenTypes.VAR_DECLARATION
                "const" -> HetuTokenTypes.CONST_DECLARATION
                "final" -> HetuTokenTypes.FINAL_DECLARATION
                
                // More specific function keywords (fallback for keywords not in Column 1)
                "function" -> HetuTokenTypes.FUNCTION_KEYWORD
                "get", "set" -> HetuTokenTypes.GET_SET_FUNCTION
                "constructor", "construct" -> HetuTokenTypes.CONSTRUCTOR_FUNCTION
                
                // Class and structure keywords (fallback for keywords not in Column 1)
                "struct", "namespace" -> HetuTokenTypes.CLASS_KEYWORD
                "interface", "override", "virtual", "late", "typedef", "extends" -> HetuTokenTypes.KEYWORD_DECLARATION
                
                // Storage modifiers (fallback for keywords not in Column 1)
                "public" -> HetuTokenTypes.PUBLIC_MODIFIER
                "private" -> HetuTokenTypes.PRIVATE_MODIFIER
                "protected" -> HetuTokenTypes.STORAGE_MODIFIER
                "external" -> HetuTokenTypes.EXTERNAL_MODIFIER
                "static" -> HetuTokenTypes.STATIC_MODIFIER
                
                // Storage types (Hetu-specific) - fallback if not in Column 3
                "double", "str", "num", "void", "never", "unknown", "String", "List", "Map", "dynamic", "Object" ->
                    HetuTokenTypes.STORAGE_TYPE_HETU
                
                // Import/export keywords
                "import", "export" -> HetuTokenTypes.IMPORT_KEYWORD
                "as" -> HetuTokenTypes.AS_KEYWORD
                
                // Enum keyword
                "enum" -> HetuTokenTypes.ENUM_KEYWORD
                
                // Other keywords (fallback for keywords not in Column 1)
                "typeof", "instanceof" -> HetuTokenTypes.TYPEOF_KEYWORD
                
                // Primitive values - fallback if not in Column 4
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
            '{' -> HetuTokenTypes.LITERAL_BRACKET_KEYWORD  // "{" from custom Column 4
            '}' -> HetuTokenTypes.LITERAL_BRACKET_KEYWORD  // "}" from custom Column 4
            '[' -> HetuTokenTypes.LITERAL_BRACKET_KEYWORD  // "[" from custom Column 4
            ']' -> HetuTokenTypes.LITERAL_BRACKET_KEYWORD  // "]" from custom Column 4
            '(' -> HetuTokenTypes.LITERAL_BRACKET_KEYWORD  // "(" from custom Column 4
            ')' -> HetuTokenTypes.LITERAL_BRACKET_KEYWORD  // ")" from custom Column 4
            '.' -> {
                HetuTokenTypes.DOT
            }
            ',' -> HetuTokenTypes.COMMA
            ';' -> HetuTokenTypes.SEMICOLON
            ':' -> {
                // In your custom file type, ":" is in Column 2 (operators and special methods)
                HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD
            }
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
                } else if (tokenEnd < endOffset && buffer[tokenEnd] == '>') {
                    tokenEnd++  // consume the >
                    HetuTokenTypes.ARROW_OPERATOR  // Arrow function: -> 
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
                            HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // "==" as operator from custom config
                        }
                        '>' -> {
                            tokenEnd++  // consume the >
                            HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // "=>" as operator from custom config
                        }
                        else -> HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // "=" from custom config
                    }
                } else {
                    HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // "=" from custom config
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
                            HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // ">=" as operator from custom config
                        }
                        '>' -> {
                            tokenEnd++  // consume the second >
                            if (tokenEnd < endOffset && buffer[tokenEnd] == '>') {
                                tokenEnd++  // consume the third >
                                HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // ">>>" as operator from custom config
                            } else {
                                HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // ">>" as operator from custom config
                            }
                        }
                        else -> HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // ">" from custom config
                    }
                } else {
                    HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // ">" from custom config
                }
            }
            '<' -> {
                if (tokenEnd < endOffset) {
                    when (buffer[tokenEnd]) {
                        '=' -> {
                            tokenEnd++  // consume the =
                            HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // "<=" as operator from custom config
                        }
                        '<' -> {
                            tokenEnd++  // consume the second <
                            HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // "<<" as operator from custom config
                        }
                        else -> HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // "<" from custom config
                    }
                } else {
                    HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // "<" from custom config
                }
            }
            '&' -> {
                if (tokenEnd < endOffset && buffer[tokenEnd] == '&') {
                    tokenEnd++  // consume the second &
                    HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // "&&" as operator from custom config
                } else {
                    HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // "&" as operator from custom config
                }
            }
            '|' -> {
                if (tokenEnd < endOffset && buffer[tokenEnd] == '|') {
                    tokenEnd++  // consume the second |
                    HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // "||" as operator from custom config
                } else {
                    HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // "|" as operator from custom config
                }
            }
            '^' -> HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // "^" as operator from custom config
            '~' -> HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD  // "~" as operator from custom config
            '?' -> {
                if (tokenEnd < endOffset) {
                    when (buffer[tokenEnd]) {
                        '?' -> {
                            tokenEnd++  // consume the second ?
                            HetuTokenTypes.NULL_COALESCING_OPERATOR  // ??
                        }
                        else -> HetuTokenTypes.TERNARY_OPERATOR  // ? in ternary
                    }
                } else {
                    HetuTokenTypes.TERNARY_OPERATOR
                }
            }
            // Handle escape sequences in strings - this will be relevant if we handle them properly
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