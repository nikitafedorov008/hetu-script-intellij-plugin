package com.hetu.hetuscript

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

// Additional color attributes for even more diverse highlighting
object HetuAdditionalAttributes {
    
    // Different colors for different types of literals
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
    
    // Different colors for different variable declaration types
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
    
    // Colors for different modifiers
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
    
    // Colors for different function types
    val CONSTRUCTOR_FUNCTION_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_CONSTRUCTOR_FUNCTION",
        DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
    )
    
    val GET_SET_FUNCTION_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_GET_SET_FUNCTION",
        DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL
    )
    
    // Color for type annotations
    val TYPE_ANNOTATION_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_TYPE_ANNOTATION",
        DefaultLanguageHighlighterColors.METADATA
    )
    
    // Colors for different punctuation
    val COLON_PUNCTUATION_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_COLON_PUNCTUATION",
        DefaultLanguageHighlighterColors.DOT
    )
    
    val BACKTICK_PUNCTUATION_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_BACKTICK_PUNCTUATION",
        DefaultLanguageHighlighterColors.OPERATION_SIGN
    )
    
    // Colors for different brackets
    val SQUARE_BRACKETS_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_SQUARE_BRACKETS",
        DefaultLanguageHighlighterColors.BRACKETS
    )
    
    val CURLY_BRACES_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_CURLY_BRACES",
        DefaultLanguageHighlighterColors.BRACES
    )
    
    val PARENTHESES_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_PARENTHESES",
        DefaultLanguageHighlighterColors.PARENTHESES
    )
    
    // Colors for different operators
    val TERNARY_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_TERNARY_OPERATOR",
        DefaultLanguageHighlighterColors.OPERATION_SIGN
    )
    
    val SPREAD_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_SPREAD_OPERATOR",
        DefaultLanguageHighlighterColors.OPERATION_SIGN
    )
    
    val NULL_COALESCING_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_NULL_COALESCING_OPERATOR",
        DefaultLanguageHighlighterColors.OPERATION_SIGN
    )
}