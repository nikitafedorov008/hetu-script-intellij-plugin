package com.hetu.hetuscript

import com.hetu.hetuscript.HetuTokenTypes
import com.hetu.hetuscript.HetuPsiTypes
import com.intellij.lang.*
import com.intellij.psi.tree.IElementType

class HetuPsiParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val marker = builder.mark()
        
        parseFile(builder)
        
        marker.done(root)
        return builder.treeBuilt
    }
    
    private fun parseFile(builder: PsiBuilder) {
        while (!builder.eof()) {
            parseStatement(builder)
        }
    }
    
    private fun parseStatement(builder: PsiBuilder) {
        if (builder.tokenType == null) return
        
        // Check for variable declaration: var|const|final name [= value]
        when (builder.tokenType) {
            HetuTokenTypes.VAR_DECLARATION, 
            HetuTokenTypes.CONST_DECLARATION, 
            HetuTokenTypes.FINAL_DECLARATION -> {
                val marker = builder.mark()
                builder.advanceLexer() // consume the keyword
                
                // Expect identifier (variable name)
                if (builder.tokenType == HetuTokenTypes.IDENTIFIER) {
                    builder.advanceLexer() // consume variable name
                }
                
                // Check for type annotation
                if (builder.tokenType == HetuTokenTypes.PUNCTUATION_COLON) {
                    builder.advanceLexer() // consume ':'
                    // consume type name (next token)
                    if (builder.tokenType != null && builder.tokenType != HetuTokenTypes.ASSIGNMENT_OPERATOR && 
                        builder.tokenType != HetuTokenTypes.SEMICOLON && builder.tokenType != HetuTokenTypes.OPERATION_SIGN) {
                        builder.advanceLexer()
                    }
                }
                
                // Check for assignment operator
                if (builder.tokenType == HetuTokenTypes.ASSIGNMENT_OPERATOR) {
                    builder.advanceLexer() // consume '='
                    
                    // Parse the expression (simplified)
                    parseExpression(builder)
                }
                
                marker.done(HetuPsiTypes.VAR_DECLARATION)
                return
            }
            HetuTokenTypes.FUNCTION_KEYWORD -> {
                parseFunctionDeclaration(builder)
                return
            }
            HetuTokenTypes.CLASS_KEYWORD -> {
                parseClassDeclaration(builder)
                return
            }
        }
        
        // Default: consume one token
        builder.advanceLexer()
    }
    
    private fun parseFunctionDeclaration(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume 'fun'
        
        // Expect function name (identifier)
        if (builder.tokenType == HetuTokenTypes.IDENTIFIER) {
            builder.advanceLexer() // consume function name
        }
        
        // Parse parameters in parentheses
        if (builder.tokenType == HetuTokenTypes.ROUND_BRACKETS || builder.tokenType == HetuTokenTypes.PAREN_LEFT) {
            // Skip parameters for now - advance through the parenthesized section
            var parenCount = 0
            while (!builder.eof()) {
                if (builder.tokenType == HetuTokenTypes.PAREN_LEFT) {
                    parenCount++
                } else if (builder.tokenType == HetuTokenTypes.PAREN_RIGHT) {
                    parenCount--
                }
                
                builder.advanceLexer()
                if (parenCount <= 0 && builder.tokenType == HetuTokenTypes.PAREN_RIGHT) {
                    builder.advanceLexer()
                    break
                }
            }
        }
        
        // Check for return type annotation (arrow operator)
        if (builder.tokenType == HetuTokenTypes.ARROW_OPERATOR) {
            builder.advanceLexer() // consume '->'
            // consume return type
            if (builder.tokenType == HetuTokenTypes.IDENTIFIER) {
                builder.advanceLexer()
            }
        }
        
        marker.done(HetuPsiTypes.FUNCTION_DECLARATION)
    }
    
    private fun parseClassDeclaration(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume 'class'
        
        // Expect class name (identifier)
        if (builder.tokenType == HetuTokenTypes.IDENTIFIER) {
            builder.advanceLexer() // consume class name
        }
        
        marker.done(HetuPsiTypes.CLASS_DECLARATION)
    }
    
    private fun parseExpression(builder: PsiBuilder) {
        // Simplified expression parsing
        var continueParsing = true
        while (continueParsing && !builder.eof()) {
            when (builder.tokenType) {
                HetuTokenTypes.IDENTIFIER,
                HetuTokenTypes.NUMBER,
                HetuTokenTypes.STRING,
                HetuTokenTypes.STRING_DOUBLE,
                HetuTokenTypes.STRING_SINGLE -> {
                    builder.advanceLexer()
                }
                else -> {
                    // Stop when we hit a semicolon or other statement-ending token
                    if (builder.tokenType == HetuTokenTypes.SEMICOLON || 
                        builder.tokenType == HetuTokenTypes.PAREN_RIGHT ||
                        builder.tokenType == HetuTokenTypes.CURLY_BRACES ||
                        builder.tokenType == HetuTokenTypes.ROUND_BRACKETS) {
                        continueParsing = false
                    } else {
                        builder.advanceLexer()
                    }
                }
            }
        }
    }
}