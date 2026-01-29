package com.hetu.hetuscript

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import java.awt.Color

class HetuSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        return HetuScriptLexer()
    }

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        return when (tokenType) {
            // Basic tokens
            HetuTokenTypes.IDENTIFIER -> IDENTIFIER_KEYS
            HetuTokenTypes.VARIABLE_NAME -> VARIABLE_NAME_KEYS
            HetuTokenTypes.CLASS_NAME -> CLASS_NAME_KEYS
            HetuTokenTypes.FUNCTION_NAME -> FUNCTION_NAME_KEYS
            HetuTokenTypes.CLASS_MEMBER_VARIABLE -> CLASS_MEMBER_VARIABLE_KEYS
            HetuTokenTypes.CLASS_MEMBER_FUNCTION -> CLASS_MEMBER_FUNCTION_KEYS
            HetuTokenTypes.LOCAL_VARIABLE -> LOCAL_VARIABLE_KEYS
            HetuTokenTypes.GLOBAL_VARIABLE -> GLOBAL_VARIABLE_KEYS
            HetuTokenTypes.LOCAL_FUNCTION -> LOCAL_FUNCTION_KEYS
            HetuTokenTypes.NUMBER -> NUMBER_KEYS
            HetuTokenTypes.STRING_DOUBLE, HetuTokenTypes.STRING_SINGLE -> STRING_KEYS
            HetuTokenTypes.COMMENT -> COMMENT_KEYS
            HetuTokenTypes.KEYWORD -> KEYWORD_KEYS
            
            // More specific number types
            HetuTokenTypes.INTEGER_NUMBER -> INTEGER_NUMBER_KEYS
            HetuTokenTypes.FLOAT_NUMBER -> FLOAT_NUMBER_KEYS
            HetuTokenTypes.HEX_NUMBER -> HEX_NUMBER_KEYS
            
            // More specific string types
            HetuTokenTypes.REGULAR_STRING -> REGULAR_STRING_KEYS
            HetuTokenTypes.TEMPLATE_STRING -> TEMPLATE_STRING_KEYS
            HetuTokenTypes.ESCAPED_STRING -> ESCAPED_STRING_KEYS
            
            // String interpolation
            HetuTokenTypes.STRING_INTERPOLATION -> STRING_INTERPOLATION_KEYS
            HetuTokenTypes.STRING_TEMPLATE_PART -> STRING_TEMPLATE_PART_KEYS
            HetuTokenTypes.FUNCTION_STRING_ARGUMENT -> FUNCTION_STRING_ARGUMENT_KEYS
            
            // VS Code-style specific tokens
            HetuTokenTypes.LINE_COMMENT -> LINE_COMMENT_KEYS
            HetuTokenTypes.BLOCK_COMMENT -> BLOCK_COMMENT_KEYS
            HetuTokenTypes.ESCAPE_SEQUENCE -> ESCAPE_SEQUENCE_KEYS
            
            // More specific comment types
            HetuTokenTypes.SINGLE_LINE_COMMENT -> SINGLE_LINE_COMMENT_KEYS
            HetuTokenTypes.MULTI_LINE_COMMENT -> MULTI_LINE_COMMENT_KEYS
            
            // Keyword categories for specific constructs
            HetuTokenTypes.CONTROL_FLOW_KEYWORD -> CONTROL_FLOW_KEYWORD_KEYS
            HetuTokenTypes.DECLARATION_KEYWORD -> DECLARATION_KEYWORD_KEYS
            
            // Other keyword categories based on your custom file type configuration
            HetuTokenTypes.CONTROL_DECLARATION_KEYWORD -> CONTROL_DECLARATION_KEYWORD_KEYS
            HetuTokenTypes.OPERATOR_SPECIAL_KEYWORD -> OPERATOR_SPECIAL_KEYWORD_KEYS
            HetuTokenTypes.TYPE_KEYWORD -> TYPE_KEYWORD_KEYS
            HetuTokenTypes.LITERAL_BRACKET_KEYWORD -> LITERAL_BRACKET_KEYWORD_KEYS
            
            // Original keyword categories from VS Code
            HetuTokenTypes.KEYWORD_CONTROL -> KEYWORD_CONTROL_KEYS
            HetuTokenTypes.KEYWORD_DECLARATION -> KEYWORD_DECLARATION_KEYS
            HetuTokenTypes.STORAGE_MODIFIER -> STORAGE_MODIFIER_KEYS
            HetuTokenTypes.STORAGE_TYPE -> STORAGE_TYPE_KEYS
            HetuTokenTypes.PRIMITIVE_VALUE -> PRIMITIVE_VALUE_KEYS
            
            // More specific primitive value types
            HetuTokenTypes.BOOLEAN_LITERAL -> BOOLEAN_LITERAL_KEYS
            HetuTokenTypes.NULL_LITERAL -> NULL_LITERAL_KEYS
            HetuTokenTypes.THIS_SUPER_LITERAL -> THIS_SUPER_LITERAL_KEYS
            
            // More specific access level types
            HetuTokenTypes.PUBLIC_ACCESS -> PUBLIC_ACCESS_KEYS
            HetuTokenTypes.PRIVATE_ACCESS -> PRIVATE_ACCESS_KEYS
            HetuTokenTypes.PROTECTED_ACCESS -> PROTECTED_ACCESS_KEYS
            
            // More specific scope types
            HetuTokenTypes.GLOBAL_SCOPE -> GLOBAL_SCOPE_KEYS
            HetuTokenTypes.LOCAL_SCOPE -> LOCAL_SCOPE_KEYS
            HetuTokenTypes.FIELD_SCOPE -> FIELD_SCOPE_KEYS
            
            // More specific function types
            HetuTokenTypes.STATIC_FUNCTION -> STATIC_FUNCTION_KEYS
            HetuTokenTypes.INSTANCE_FUNCTION -> INSTANCE_FUNCTION_KEYS
            
            // More specific conditional types
            HetuTokenTypes.IF_CONDITIONAL -> IF_CONDITIONAL_KEYS
            HetuTokenTypes.ELSE_CONDITIONAL -> ELSE_CONDITIONAL_KEYS
            HetuTokenTypes.ELIF_CONDITIONAL -> ELIF_CONDITIONAL_KEYS
            
            // More specific loop types
            HetuTokenTypes.FOR_LOOP -> FOR_LOOP_KEYS
            HetuTokenTypes.WHILE_LOOP -> WHILE_LOOP_KEYS
            HetuTokenTypes.DO_WHILE_LOOP -> DO_WHILE_LOOP_KEYS
            
            // More specific error/warning keywords
            HetuTokenTypes.ERROR_KEYWORD -> ERROR_KEYWORD_KEYS
            HetuTokenTypes.WARNING_KEYWORD -> WARNING_KEYWORD_KEYS
            
            // More specific keyword categories based on VS Code grammar
            HetuTokenTypes.STORAGE_TYPE_HETU -> STORAGE_TYPE_HETU_KEYS
            HetuTokenTypes.ASSERT_KEYWORD -> ASSERT_KEYWORD_KEYS
            HetuTokenTypes.LOOP_KEYWORD -> LOOP_KEYWORD_KEYS
            HetuTokenTypes.FLOW_KEYWORD -> FLOW_KEYWORD_KEYS
            HetuTokenTypes.CONDITIONAL_KEYWORD -> CONDITIONAL_KEYWORD_KEYS
            HetuTokenTypes.SWITCH_KEYWORD -> SWITCH_KEYWORD_KEYS
            HetuTokenTypes.FUNCTION_KEYWORD -> FUNCTION_KEYWORD_KEYS
            HetuTokenTypes.CLASS_KEYWORD -> CLASS_KEYWORD_KEYS
            HetuTokenTypes.ENUM_KEYWORD -> ENUM_KEYWORD_KEYS
            HetuTokenTypes.IMPORT_KEYWORD -> IMPORT_KEYWORD_KEYS
            HetuTokenTypes.AS_KEYWORD -> AS_KEYWORD_KEYS
            HetuTokenTypes.MODIFIER_KEYWORD -> MODIFIER_KEYWORD_KEYS
            HetuTokenTypes.NEW_KEYWORD -> NEW_KEYWORD_KEYS
            HetuTokenTypes.TYPEOF_KEYWORD -> TYPEOF_KEYWORD_KEYS
            
            // More specific declaration types
            HetuTokenTypes.VAR_DECLARATION -> VAR_DECLARATION_KEYS
            HetuTokenTypes.CONST_DECLARATION -> CONST_DECLARATION_KEYS
            HetuTokenTypes.FINAL_DECLARATION -> FINAL_DECLARATION_KEYS
            
            // More specific modifier types
            HetuTokenTypes.PUBLIC_MODIFIER -> PUBLIC_MODIFIER_KEYS
            HetuTokenTypes.PRIVATE_MODIFIER -> PRIVATE_MODIFIER_KEYS
            HetuTokenTypes.STATIC_MODIFIER -> STATIC_MODIFIER_KEYS
            HetuTokenTypes.EXTERNAL_MODIFIER -> EXTERNAL_MODIFIER_KEYS
            
            // More specific function types
            HetuTokenTypes.CONSTRUCTOR_FUNCTION -> CONSTRUCTOR_FUNCTION_KEYS
            HetuTokenTypes.GET_SET_FUNCTION -> GET_SET_FUNCTION_KEYS
            
            // Type annotation
            HetuTokenTypes.TYPE_ANNOTATION -> TYPE_ANNOTATION_KEYS
            
            // Operators
            HetuTokenTypes.OPERATOR_ARITHMETIC -> OPERATOR_ARITHMETIC_KEYS
            HetuTokenTypes.OPERATOR_ASSIGNMENT -> OPERATOR_ASSIGNMENT_KEYS
            HetuTokenTypes.OPERATOR_COMPARISON -> OPERATOR_COMPARISON_KEYS
            HetuTokenTypes.OPERATOR_LOGICAL -> OPERATOR_LOGICAL_KEYS
            HetuTokenTypes.OPERATOR_BITWISE -> OPERATOR_BITWISE_KEYS
            HetuTokenTypes.OPERATOR_OTHER -> OPERATOR_OTHER_KEYS
            HetuTokenTypes.OPERATION_SIGN -> OPERATION_SIGN_KEYS
            
            // More specific operators based on VS Code grammar
            HetuTokenTypes.ASSIGNMENT_OPERATOR -> ASSIGNMENT_OPERATOR_KEYS
            HetuTokenTypes.COMPOUND_ASSIGNMENT_OPERATOR -> COMPOUND_ASSIGNMENT_OPERATOR_KEYS
            HetuTokenTypes.COMPARISON_OPERATOR -> COMPARISON_OPERATOR_KEYS
            HetuTokenTypes.RELATIONAL_OPERATOR -> RELATIONAL_OPERATOR_KEYS
            HetuTokenTypes.LOGICAL_OPERATOR -> LOGICAL_OPERATOR_KEYS
            HetuTokenTypes.BITWISE_OPERATOR -> BITWISE_OPERATOR_KEYS
            HetuTokenTypes.SHIFT_OPERATOR -> SHIFT_OPERATOR_KEYS
            HetuTokenTypes.ARITHMETIC_OPERATOR -> ARITHMETIC_OPERATOR_KEYS
            HetuTokenTypes.INCREMENT_DECREMENT_OPERATOR -> INCREMENT_DECREMENT_OPERATOR_KEYS
            HetuTokenTypes.TERNARY_OPERATOR -> TERNARY_OPERATOR_KEYS
            HetuTokenTypes.SPREAD_OPERATOR -> SPREAD_OPERATOR_KEYS
            HetuTokenTypes.ACCESSOR_OPERATOR -> ACCESSOR_OPERATOR_KEYS
            HetuTokenTypes.ARROW_OPERATOR -> ARROW_OPERATOR_KEYS
            HetuTokenTypes.NULL_COALESCING_OPERATOR -> NULL_COALESCING_OPERATOR_KEYS
            
            // Special tokens
            HetuTokenTypes.FUNCTION_CALL -> FUNCTION_CALL_KEYS
            HetuTokenTypes.CLASS_DECLARATION -> CLASS_DECLARATION_KEYS
            HetuTokenTypes.VARIABLE_DECLARATION -> VARIABLE_DECLARATION_KEYS
            
            // More specific bracket types
            HetuTokenTypes.ROUND_BRACKETS -> ROUND_BRACKETS_KEYS
            HetuTokenTypes.SQUARE_BRACKETS -> SQUARE_BRACKETS_KEYS
            HetuTokenTypes.CURLY_BRACES -> CURLY_BRACES_KEYS
            
            // Brackets and punctuation
            HetuTokenTypes.BRACE_LEFT, HetuTokenTypes.BRACE_RIGHT -> BRACES_KEYS
            HetuTokenTypes.BRACKET_LEFT, HetuTokenTypes.BRACKET_RIGHT -> BRACKETS_KEYS
            HetuTokenTypes.PAREN_LEFT, HetuTokenTypes.PAREN_RIGHT -> PARENTHESES_KEYS
            HetuTokenTypes.DOT -> DOT_KEYS
            HetuTokenTypes.COMMA -> COMMA_KEYS
            HetuTokenTypes.SEMICOLON -> SEMICOLON_KEYS
            
            // More specific punctuation
            HetuTokenTypes.PUNCTUATION_SEMICOLON -> PUNCTUATION_SEMICOLON_KEYS
            HetuTokenTypes.PUNCTUATION_COMMA -> PUNCTUATION_COMMA_KEYS
            HetuTokenTypes.PUNCTUATION_COLON -> COLON_PUNCTUATION_KEYS
            
            // HetuScript specific tokens
            HetuTokenTypes.EXTENDS_KEYWORD -> EXTENDS_KEYWORD_KEYS
            HetuTokenTypes.AWAIT_KEYWORD -> AWAIT_KEYWORD_KEYS
            HetuTokenTypes.YIELD_KEYWORD -> YIELD_KEYWORD_KEYS
            
            com.intellij.psi.TokenType.WHITE_SPACE -> EMPTY_KEYS
            else -> BAD_CHARACTER_KEYS
        }
    }

    companion object {
        // Basic tokens with more diverse colors
        val KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val IDENTIFIER_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_IDENTIFIER",
            DefaultLanguageHighlighterColors.IDENTIFIER
        )
        
        val VARIABLE_NAME_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_VARIABLE_NAME",
            TextAttributes().apply {
                foregroundColor = Color(0x0096FF) // Bright blue for variables
            }
        )
        
        val CLASS_NAME_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_CLASS_NAME",
            TextAttributes().apply {
                foregroundColor = Color(0xFF4136) // Bright red for class names
            }
        )
        
        val FUNCTION_NAME_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_FUNCTION_NAME",
            TextAttributes().apply {
                foregroundColor = Color(0x8A2BE2) // Rich purple for function names
            }
        )
        
        val CLASS_MEMBER_VARIABLE_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_CLASS_MEMBER_VARIABLE",
            DefaultLanguageHighlighterColors.INSTANCE_FIELD
        )
        
        val CLASS_MEMBER_FUNCTION_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_CLASS_MEMBER_FUNCTION",
            DefaultLanguageHighlighterColors.INSTANCE_METHOD
        )
        
        val LOCAL_VARIABLE_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_LOCAL_VARIABLE",
            DefaultLanguageHighlighterColors.LOCAL_VARIABLE
        )
        
        val GLOBAL_VARIABLE_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_GLOBAL_VARIABLE",
            DefaultLanguageHighlighterColors.GLOBAL_VARIABLE
        )
        
        val LOCAL_FUNCTION_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_LOCAL_FUNCTION",
            DefaultLanguageHighlighterColors.STATIC_METHOD
        )
        
        val NUMBER_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_NUMBER",
            DefaultLanguageHighlighterColors.NUMBER
        )
        
        val STRING_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_STRING",
            DefaultLanguageHighlighterColors.STRING
        )
        
        val COMMENT_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT
        )
        
        val OPERATION_SIGN_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_OPERATION_SIGN",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        // VS Code-style specific tokens
        val LINE_COMMENT_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_LINE_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT
        )
        
        val BLOCK_COMMENT_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_BLOCK_COMMENT",
            DefaultLanguageHighlighterColors.BLOCK_COMMENT
        )
        
        val STRING_DOUBLE_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_STRING_DOUBLE",
            DefaultLanguageHighlighterColors.STRING
        )
        
        val STRING_SINGLE_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_STRING_SINGLE",
            DefaultLanguageHighlighterColors.STRING
        )
        
        val ESCAPE_SEQUENCE_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_ESCAPE_SEQUENCE",
            DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE
        )
        
        val STRING_INTERPOLATION_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_STRING_INTERPOLATION",
            DefaultLanguageHighlighterColors.METADATA  // Different color for interpolated expressions
        )
        
        val STRING_TEMPLATE_PART_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_STRING_TEMPLATE_PART",
            DefaultLanguageHighlighterColors.STRING
        )
        
        val FUNCTION_STRING_ARGUMENT_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_FUNCTION_STRING_ARGUMENT",
            DefaultLanguageHighlighterColors.STRING  // May be colored differently for function arguments
        )
        
        // More specific number types with distinct colors
        val INTEGER_NUMBER_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_INTEGER_NUMBER",
            DefaultLanguageHighlighterColors.NUMBER
        )
        
        val FLOAT_NUMBER_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_FLOAT_NUMBER", 
            DefaultLanguageHighlighterColors.NUMBER
        )
        
        val HEX_NUMBER_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_HEX_NUMBER",
            DefaultLanguageHighlighterColors.NUMBER
        )
        
        // More specific string types
        val REGULAR_STRING_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_REGULAR_STRING",
            DefaultLanguageHighlighterColors.STRING
        )
        
        val TEMPLATE_STRING_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_TEMPLATE_STRING",
            DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE
        )
        
        val ESCAPED_STRING_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_ESCAPED_STRING",
            DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE
        )
        
        // More specific comment types
        val SINGLE_LINE_COMMENT_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_SINGLE_LINE_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT
        )
        
        val MULTI_LINE_COMMENT_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_MULTI_LINE_COMMENT",
            DefaultLanguageHighlighterColors.BLOCK_COMMENT
        )
        
        // Keyword categories for specific constructs
        val CONTROL_FLOW_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_CONTROL_FLOW_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val DECLARATION_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_DECLARATION_KEYWORD",
            DefaultLanguageHighlighterColors.METADATA
        )
        
        // Other keyword categories based on your custom file type configuration
        val CONTROL_DECLARATION_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_CONTROL_DECLARATION_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val OPERATOR_SPECIAL_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_OPERATOR_SPECIAL_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val TYPE_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_TYPE_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val LITERAL_BRACKET_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_LITERAL_BRACKET_KEYWORD",
            DefaultLanguageHighlighterColors.CONSTANT
        )
        
        // Original keyword categories from VS Code
        val KEYWORD_CONTROL_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_KEYWORD_CONTROL",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val KEYWORD_DECLARATION_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_KEYWORD_DECLARATION",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val STORAGE_MODIFIER_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_STORAGE_MODIFIER",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val STORAGE_TYPE_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_STORAGE_TYPE",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val PRIMITIVE_VALUE_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_PRIMITIVE_VALUE",
            DefaultLanguageHighlighterColors.CONSTANT
        )
        
        // More specific primitive value types
        val BOOLEAN_LITERAL_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_BOOLEAN_LITERAL",
            DefaultLanguageHighlighterColors.CONSTANT
        )
        
        val NULL_LITERAL_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_NULL_LITERAL", 
            DefaultLanguageHighlighterColors.CONSTANT
        )
        
        val THIS_SUPER_LITERAL_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_THIS_SUPER_LITERAL",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        // More specific access level types
        val PUBLIC_ACCESS_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_PUBLIC_ACCESS",
            DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL
        )
        
        val PRIVATE_ACCESS_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_PRIVATE_ACCESS",
            DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL
        )
        
        val PROTECTED_ACCESS_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_PROTECTED_ACCESS",
            DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL
        )
        
        // More specific scope types
        val GLOBAL_SCOPE_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_GLOBAL_SCOPE",
            DefaultLanguageHighlighterColors.GLOBAL_VARIABLE
        )
        
        val LOCAL_SCOPE_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_LOCAL_SCOPE",
            DefaultLanguageHighlighterColors.LOCAL_VARIABLE
        )
        
        val FIELD_SCOPE_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_FIELD_SCOPE",
            DefaultLanguageHighlighterColors.INSTANCE_FIELD
        )
        
        // More specific function types
        val STATIC_FUNCTION_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_STATIC_FUNCTION",
            DefaultLanguageHighlighterColors.STATIC_METHOD
        )
        
        val INSTANCE_FUNCTION_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_INSTANCE_FUNCTION", 
            DefaultLanguageHighlighterColors.INSTANCE_METHOD
        )
        
        // More specific conditional types
        val IF_CONDITIONAL_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_IF_CONDITIONAL",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val ELSE_CONDITIONAL_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_ELSE_CONDITIONAL",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val ELIF_CONDITIONAL_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_ELIF_CONDITIONAL",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        // More specific error/warning keywords
        val ERROR_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_ERROR_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val WARNING_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_WARNING_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        // More specific loop constructs
        val FOR_LOOP_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_FOR_LOOP",
            DefaultLanguageHighlighterColors.LABEL
        )
        
        val WHILE_LOOP_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_WHILE_LOOP",
            DefaultLanguageHighlighterColors.LABEL
        )
        
        val DO_WHILE_LOOP_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_DO_WHILE_LOOP",
            DefaultLanguageHighlighterColors.LABEL
        )
        
        // More diverse operator colors
        val OPERATOR_ARITHMETIC_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_OPERATOR_ARITHMETIC",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val OPERATOR_ASSIGNMENT_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_OPERATOR_ASSIGNMENT",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val OPERATOR_COMPARISON_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_OPERATOR_COMPARISON",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val OPERATOR_LOGICAL_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_OPERATOR_LOGICAL",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val OPERATOR_BITWISE_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_OPERATOR_BITWISE",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val OPERATOR_OTHER_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_OPERATOR_OTHER",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        // More specific declaration types
        val VAR_DECLARATION_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_VAR_DECLARATION",
            DefaultLanguageHighlighterColors.LOCAL_VARIABLE
        )
        
        val CONST_DECLARATION_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_CONST_DECLARATION",
            DefaultLanguageHighlighterColors.CONSTANT
        )
        
        val FINAL_DECLARATION_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_FINAL_DECLARATION",
            DefaultLanguageHighlighterColors.INSTANCE_FIELD
        )
        
        // More specific modifier types
        val PUBLIC_MODIFIER_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_PUBLIC_MODIFIER",
            DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL
        )
        
        val PRIVATE_MODIFIER_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_PRIVATE_MODIFIER",
            DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL
        )
        
        val STATIC_MODIFIER_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_STATIC_MODIFIER",
            DefaultLanguageHighlighterColors.INSTANCE_FIELD
        )
        
        val EXTERNAL_MODIFIER_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_EXTERNAL_MODIFIER",
            DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL
        )
        
        // More specific function types
        val CONSTRUCTOR_FUNCTION_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_CONSTRUCTOR_FUNCTION",
            DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
        )
        
        val GET_SET_FUNCTION_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_GET_SET_FUNCTION",
            DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL
        )
        
        // Type annotation
        val TYPE_ANNOTATION_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_TYPE_ANNOTATION",
            DefaultLanguageHighlighterColors.METADATA
        )
        
        // HetuScript specific constructs
        val EXTENDS_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_EXTENDS_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val AWAIT_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_AWAIT_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val YIELD_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_YIELD_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        // Special tokens with various colors
        val FUNCTION_CALL_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_FUNCTION_CALL",
            DefaultLanguageHighlighterColors.FUNCTION_CALL
        )
        
        val CLASS_DECLARATION_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_CLASS_DECLARATION",
            DefaultLanguageHighlighterColors.CLASS_NAME
        )
        
        val VARIABLE_DECLARATION_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_VARIABLE_DECLARATION",
            DefaultLanguageHighlighterColors.LOCAL_VARIABLE
        )
        
        // More specific bracket types
        val ROUND_BRACKETS_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_ROUND_BRACKETS",
            DefaultLanguageHighlighterColors.PARENTHESES
        )
        
        val SQUARE_BRACKETS_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_SQUARE_BRACKETS", 
            DefaultLanguageHighlighterColors.BRACKETS
        )
        
        val CURLY_BRACES_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_CURLY_BRACES",
            DefaultLanguageHighlighterColors.BRACES
        )
        
        // Brackets and punctuation with more distinct colors
        val BRACES_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_BRACES",
            DefaultLanguageHighlighterColors.BRACES
        )
        
        val BRACKETS_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_BRACKETS",
            DefaultLanguageHighlighterColors.BRACKETS
        )
        
        val PARENTHESES_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_PARENTHESES",
            DefaultLanguageHighlighterColors.PARENTHESES
        )
        
        val DOT_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_DOT",
            DefaultLanguageHighlighterColors.DOT
        )
        
        val COMMA_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_COMMA",
            DefaultLanguageHighlighterColors.COMMA
        )
        
        val SEMICOLON_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_SEMICOLON",
            DefaultLanguageHighlighterColors.SEMICOLON
        )
        
        val BAD_CHARACTER_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_BAD_CHARACTER",
            HighlighterColors.BAD_CHARACTER
        )

        // More specific keyword types with even more distinct colors
        val STORAGE_TYPE_HETU_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_STORAGE_TYPE_HETU",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val ASSERT_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_ASSERT_KEYWORD",
            DefaultLanguageHighlighterColors.INSTANCE_FIELD  // Using instance field color for assert
        )
        
        val LOOP_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_LOOP_KEYWORD",
            DefaultLanguageHighlighterColors.LABEL  // Using label for loop keywords
        )
        
        val FLOW_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_FLOW_KEYWORD",
            DefaultLanguageHighlighterColors.CONSTANT  // Using constant for flow keywords
        )
        
        val CONDITIONAL_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_CONDITIONAL_KEYWORD",
            DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL  // Using predefined symbol color
        )
        
        val SWITCH_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_SWITCH_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val FUNCTION_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_FUNCTION_KEYWORD",
            DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
        )
        
        val CLASS_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_CLASS_KEYWORD",
            DefaultLanguageHighlighterColors.CLASS_NAME
        )
        
        val ENUM_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_ENUM_KEYWORD",
            DefaultLanguageHighlighterColors.CLASS_NAME
        )
        
        val IMPORT_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_IMPORT_KEYWORD",
            DefaultLanguageHighlighterColors.METADATA  // Using metadata color for imports
        )
        
        val AS_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_AS_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val MODIFIER_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_MODIFIER_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val NEW_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_NEW_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val TYPEOF_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_TYPEOF_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        // Additional punctuation with even more distinct colors
        val PUNCTUATION_SEMICOLON_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_PUNCTUATION_SEMICOLON",
            DefaultLanguageHighlighterColors.SEMICOLON
        )
        
        val PUNCTUATION_COMMA_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_PUNCTUATION_COMMA",
            DefaultLanguageHighlighterColors.COMMA
        )
        
        val COLON_PUNCTUATION_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_COLON_PUNCTUATION",
            DefaultLanguageHighlighterColors.DOT
        )
        
        // Additional operators with more vibrant colors
        val ASSIGNMENT_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_ASSIGNMENT_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val COMPOUND_ASSIGNMENT_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_COMPOUND_ASSIGNMENT_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val COMPARISON_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_COMPARISON_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val RELATIONAL_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_RELATIONAL_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val LOGICAL_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_LOGICAL_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val BITWISE_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_BITWISE_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val SHIFT_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_SHIFT_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val ARITHMETIC_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_ARITHMETIC_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val INCREMENT_DECREMENT_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_INCREMENT_DECREMENT_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val TERNARY_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_TERNARY_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val SPREAD_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_SPREAD_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val ACCESSOR_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_ACCESSOR_OPERATOR",
            DefaultLanguageHighlighterColors.DOT
        )
        
        val ARROW_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_ARROW_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val NULL_COALESCING_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
            "HETU_NULL_COALESCING_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )

        // Basic tokens
        private val IDENTIFIER_KEYS = arrayOf(IDENTIFIER_KEY)
        private val VARIABLE_NAME_KEYS = arrayOf(VARIABLE_NAME_KEY)
        private val CLASS_NAME_KEYS = arrayOf(CLASS_NAME_KEY)
        private val FUNCTION_NAME_KEYS = arrayOf(FUNCTION_NAME_KEY)
        private val NUMBER_KEYS = arrayOf(NUMBER_KEY)
        private val STRING_KEYS = arrayOf(STRING_KEY)
        private val COMMENT_KEYS = arrayOf(COMMENT_KEY)
        private val KEYWORD_KEYS = arrayOf(KEYWORD_KEY)
        private val OPERATION_SIGN_KEYS = arrayOf(OPERATION_SIGN_KEY)
        
        // More specific number types
        private val INTEGER_NUMBER_KEYS = arrayOf(INTEGER_NUMBER_KEY)
        private val FLOAT_NUMBER_KEYS = arrayOf(FLOAT_NUMBER_KEY)
        private val HEX_NUMBER_KEYS = arrayOf(HEX_NUMBER_KEY)
        
        // More specific string types
        private val REGULAR_STRING_KEYS = arrayOf(REGULAR_STRING_KEY)
        private val TEMPLATE_STRING_KEYS = arrayOf(TEMPLATE_STRING_KEY)
        private val ESCAPED_STRING_KEYS = arrayOf(ESCAPED_STRING_KEY)
        
        // VS Code-style specific tokens
        private val LINE_COMMENT_KEYS = arrayOf(LINE_COMMENT_KEY)
        private val BLOCK_COMMENT_KEYS = arrayOf(BLOCK_COMMENT_KEY)
        private val STRING_DOUBLE_KEYS = arrayOf(STRING_DOUBLE_KEY)
        private val STRING_SINGLE_KEYS = arrayOf(STRING_SINGLE_KEY)
        private val ESCAPE_SEQUENCE_KEYS = arrayOf(ESCAPE_SEQUENCE_KEY)
        private val STRING_INTERPOLATION_KEYS = arrayOf(STRING_INTERPOLATION_KEY)
        private val STRING_TEMPLATE_PART_KEYS = arrayOf(STRING_TEMPLATE_PART_KEY)
        private val FUNCTION_STRING_ARGUMENT_KEYS = arrayOf(FUNCTION_STRING_ARGUMENT_KEY)
        
        // More specific comment types
        private val SINGLE_LINE_COMMENT_KEYS = arrayOf(SINGLE_LINE_COMMENT_KEY)
        private val MULTI_LINE_COMMENT_KEYS = arrayOf(MULTI_LINE_COMMENT_KEY)
        
        // More specific access level types
        private val PUBLIC_ACCESS_KEYS = arrayOf(PUBLIC_ACCESS_KEY)
        private val PRIVATE_ACCESS_KEYS = arrayOf(PRIVATE_ACCESS_KEY)
        private val PROTECTED_ACCESS_KEYS = arrayOf(PROTECTED_ACCESS_KEY)
        
        // More specific scope types
        private val GLOBAL_SCOPE_KEYS = arrayOf(GLOBAL_SCOPE_KEY)
        private val LOCAL_SCOPE_KEYS = arrayOf(LOCAL_SCOPE_KEY)
        private val FIELD_SCOPE_KEYS = arrayOf(FIELD_SCOPE_KEY)
        
        // More specific function types
        private val STATIC_FUNCTION_KEYS = arrayOf(STATIC_FUNCTION_KEY)
        private val INSTANCE_FUNCTION_KEYS = arrayOf(INSTANCE_FUNCTION_KEY)
        
        // More specific conditional types
        private val IF_CONDITIONAL_KEYS = arrayOf(IF_CONDITIONAL_KEY)
        private val ELSE_CONDITIONAL_KEYS = arrayOf(ELSE_CONDITIONAL_KEY)
        private val ELIF_CONDITIONAL_KEYS = arrayOf(ELIF_CONDITIONAL_KEY)
        
        // More specific error/warning keywords
        private val ERROR_KEYWORD_KEYS = arrayOf(ERROR_KEYWORD_KEY)
        private val WARNING_KEYWORD_KEYS = arrayOf(WARNING_KEYWORD_KEY)
        
        // More specific loop constructs
        private val FOR_LOOP_KEYS = arrayOf(FOR_LOOP_KEY)
        private val WHILE_LOOP_KEYS = arrayOf(WHILE_LOOP_KEY)
        private val DO_WHILE_LOOP_KEYS = arrayOf(DO_WHILE_LOOP_KEY)
        
        // Keyword categories for specific constructs
        private val CONTROL_FLOW_KEYWORD_KEYS = arrayOf(CONTROL_FLOW_KEYWORD_KEY)
        private val DECLARATION_KEYWORD_KEYS = arrayOf(DECLARATION_KEYWORD_KEY)
        
        // Other keyword categories based on custom file type configuration
        private val CONTROL_DECLARATION_KEYWORD_KEYS = arrayOf(CONTROL_DECLARATION_KEYWORD_KEY)
        private val OPERATOR_SPECIAL_KEYWORD_KEYS = arrayOf(OPERATOR_SPECIAL_KEYWORD_KEY)
        private val TYPE_KEYWORD_KEYS = arrayOf(TYPE_KEYWORD_KEY)
        private val LITERAL_BRACKET_KEYWORD_KEYS = arrayOf(LITERAL_BRACKET_KEYWORD_KEY)
        
        // Original keyword categories from VS Code
        private val KEYWORD_CONTROL_KEYS = arrayOf(KEYWORD_CONTROL_KEY)
        private val KEYWORD_DECLARATION_KEYS = arrayOf(KEYWORD_DECLARATION_KEY)
        private val STORAGE_MODIFIER_KEYS = arrayOf(STORAGE_MODIFIER_KEY)
        private val STORAGE_TYPE_KEYS = arrayOf(STORAGE_TYPE_KEY)
        private val PRIMITIVE_VALUE_KEYS = arrayOf(PRIMITIVE_VALUE_KEY)
        private val BOOLEAN_LITERAL_KEYS = arrayOf(BOOLEAN_LITERAL_KEY)
        private val NULL_LITERAL_KEYS = arrayOf(NULL_LITERAL_KEY)
        private val THIS_SUPER_LITERAL_KEYS = arrayOf(THIS_SUPER_LITERAL_KEY)
        
        // Operators
        private val OPERATOR_ARITHMETIC_KEYS = arrayOf(OPERATOR_ARITHMETIC_KEY)
        private val OPERATOR_ASSIGNMENT_KEYS = arrayOf(OPERATOR_ASSIGNMENT_KEY)
        private val OPERATOR_COMPARISON_KEYS = arrayOf(OPERATOR_COMPARISON_KEY)
        private val OPERATOR_LOGICAL_KEYS = arrayOf(OPERATOR_LOGICAL_KEY)
        private val OPERATOR_BITWISE_KEYS = arrayOf(OPERATOR_BITWISE_KEY)
        private val OPERATOR_OTHER_KEYS = arrayOf(OPERATOR_OTHER_KEY)
        
        // More specific declaration types
        private val VAR_DECLARATION_KEYS = arrayOf(VAR_DECLARATION_KEY)
        private val CONST_DECLARATION_KEYS = arrayOf(CONST_DECLARATION_KEY)
        private val FINAL_DECLARATION_KEYS = arrayOf(FINAL_DECLARATION_KEY)
        
        // More specific modifier types
        private val PUBLIC_MODIFIER_KEYS = arrayOf(PUBLIC_MODIFIER_KEY)
        private val PRIVATE_MODIFIER_KEYS = arrayOf(PRIVATE_MODIFIER_KEY)
        private val STATIC_MODIFIER_KEYS = arrayOf(STATIC_MODIFIER_KEY)
        private val EXTERNAL_MODIFIER_KEYS = arrayOf(EXTERNAL_MODIFIER_KEY)
        
        // More specific function types
        private val CONSTRUCTOR_FUNCTION_KEYS = arrayOf(CONSTRUCTOR_FUNCTION_KEY)
        private val GET_SET_FUNCTION_KEYS = arrayOf(GET_SET_FUNCTION_KEY)
        
        // Type annotation
        private val TYPE_ANNOTATION_KEYS = arrayOf(TYPE_ANNOTATION_KEY)
        
        // Special tokens
        private val FUNCTION_CALL_KEYS = arrayOf(FUNCTION_CALL_KEY)
        private val CLASS_DECLARATION_KEYS = arrayOf(CLASS_DECLARATION_KEY)
        private val VARIABLE_DECLARATION_KEYS = arrayOf(VARIABLE_DECLARATION_KEY)
        
        // More specific bracket types
        private val ROUND_BRACKETS_KEYS = arrayOf(ROUND_BRACKETS_KEY)
        private val SQUARE_BRACKETS_KEYS = arrayOf(SQUARE_BRACKETS_KEY)
        private val CURLY_BRACES_KEYS = arrayOf(CURLY_BRACES_KEY)
        
        // Brackets and punctuation
        private val BRACES_KEYS = arrayOf(BRACES_KEY)
        private val BRACKETS_KEYS = arrayOf(BRACKETS_KEY)
        private val PARENTHESES_KEYS = arrayOf(PARENTHESES_KEY)
        private val DOT_KEYS = arrayOf(DOT_KEY)
        private val COMMA_KEYS = arrayOf(COMMA_KEY)
        private val SEMICOLON_KEYS = arrayOf(SEMICOLON_KEY)
        
        // More specific token key arrays based on VS Code grammar
        private val STORAGE_TYPE_HETU_KEYS = arrayOf(STORAGE_TYPE_HETU_KEY)
        private val ASSERT_KEYWORD_KEYS = arrayOf(ASSERT_KEYWORD_KEY)
        private val LOOP_KEYWORD_KEYS = arrayOf(LOOP_KEYWORD_KEY)
        private val FLOW_KEYWORD_KEYS = arrayOf(FLOW_KEYWORD_KEY)
        private val CONDITIONAL_KEYWORD_KEYS = arrayOf(CONDITIONAL_KEYWORD_KEY)
        private val SWITCH_KEYWORD_KEYS = arrayOf(SWITCH_KEYWORD_KEY)
        private val FUNCTION_KEYWORD_KEYS = arrayOf(FUNCTION_KEYWORD_KEY)
        private val CLASS_KEYWORD_KEYS = arrayOf(CLASS_KEYWORD_KEY)
        private val ENUM_KEYWORD_KEYS = arrayOf(ENUM_KEYWORD_KEY)
        private val IMPORT_KEYWORD_KEYS = arrayOf(IMPORT_KEYWORD_KEY)
        private val AS_KEYWORD_KEYS = arrayOf(AS_KEYWORD_KEY)
        private val MODIFIER_KEYWORD_KEYS = arrayOf(MODIFIER_KEYWORD_KEY)
        private val NEW_KEYWORD_KEYS = arrayOf(NEW_KEYWORD_KEY)
        private val TYPEOF_KEYWORD_KEYS = arrayOf(TYPEOF_KEYWORD_KEY)
        
        // Additional punctuation
        private val PUNCTUATION_SEMICOLON_KEYS = arrayOf(PUNCTUATION_SEMICOLON_KEY)
        private val PUNCTUATION_COMMA_KEYS = arrayOf(PUNCTUATION_COMMA_KEY)
        private val COLON_PUNCTUATION_KEYS = arrayOf(COLON_PUNCTUATION_KEY)
        
        // Additional operators based on VS Code grammar
        private val ASSIGNMENT_OPERATOR_KEYS = arrayOf(ASSIGNMENT_OPERATOR_KEY)
        private val COMPOUND_ASSIGNMENT_OPERATOR_KEYS = arrayOf(COMPOUND_ASSIGNMENT_OPERATOR_KEY)
        private val COMPARISON_OPERATOR_KEYS = arrayOf(COMPARISON_OPERATOR_KEY)
        private val RELATIONAL_OPERATOR_KEYS = arrayOf(RELATIONAL_OPERATOR_KEY)
        private val LOGICAL_OPERATOR_KEYS = arrayOf(LOGICAL_OPERATOR_KEY)
        private val BITWISE_OPERATOR_KEYS = arrayOf(BITWISE_OPERATOR_KEY)
        private val SHIFT_OPERATOR_KEYS = arrayOf(SHIFT_OPERATOR_KEY)
        private val ARITHMETIC_OPERATOR_KEYS = arrayOf(ARITHMETIC_OPERATOR_KEY)
        private val INCREMENT_DECREMENT_OPERATOR_KEYS = arrayOf(INCREMENT_DECREMENT_OPERATOR_KEY)
        private val TERNARY_OPERATOR_KEYS = arrayOf(TERNARY_OPERATOR_KEY)
        private val SPREAD_OPERATOR_KEYS = arrayOf(SPREAD_OPERATOR_KEY)
        private val ACCESSOR_OPERATOR_KEYS = arrayOf(ACCESSOR_OPERATOR_KEY)
        private val ARROW_OPERATOR_KEYS = arrayOf(ARROW_OPERATOR_KEY)
        private val NULL_COALESCING_OPERATOR_KEYS = arrayOf(NULL_COALESCING_OPERATOR_KEY)
        
        // HetuScript specific token key arrays
        private val EXTENDS_KEYWORD_KEYS = arrayOf(EXTENDS_KEYWORD_KEY)
        private val AWAIT_KEYWORD_KEYS = arrayOf(AWAIT_KEYWORD_KEY)
        private val YIELD_KEYWORD_KEYS = arrayOf(YIELD_KEYWORD_KEY)
        
        // Context-specific identifier key arrays
        private val CLASS_MEMBER_VARIABLE_KEYS = arrayOf(CLASS_MEMBER_VARIABLE_KEY)
        private val CLASS_MEMBER_FUNCTION_KEYS = arrayOf(CLASS_MEMBER_FUNCTION_KEY)
        private val LOCAL_VARIABLE_KEYS = arrayOf(LOCAL_VARIABLE_KEY)
        private val GLOBAL_VARIABLE_KEYS = arrayOf(GLOBAL_VARIABLE_KEY)
        private val LOCAL_FUNCTION_KEYS = arrayOf(LOCAL_FUNCTION_KEY)
        
        private val BAD_CHARACTER_KEYS = arrayOf(BAD_CHARACTER_KEY)
        private val EMPTY_KEYS = arrayOf<TextAttributesKey>()
    }
}