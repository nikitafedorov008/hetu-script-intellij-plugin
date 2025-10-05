package com.hetu.hetuscript

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

// Ultra diverse color attributes for maximum color variety
object HetuUltraDiverseAttributes {
    
    // Colors for different number formats
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
    
    // Different colors for different string types
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
    
    // Colors for different bracket types
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
    
    // Different colors for different comments
    val SINGLE_LINE_COMMENT_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_SINGLE_LINE_COMMENT",
        DefaultLanguageHighlighterColors.LINE_COMMENT
    )
    
    val MULTI_LINE_COMMENT_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_MULTI_LINE_COMMENT",
        DefaultLanguageHighlighterColors.BLOCK_COMMENT
    )
    
    // Colors for different access levels
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
    
    // Colors for different scope levels
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
    
    // Colors for different function attributes 
    val STATIC_FUNCTION_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_STATIC_FUNCTION",
        DefaultLanguageHighlighterColors.STATIC_METHOD
    )
    
    val INSTANCE_FUNCTION_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_INSTANCE_FUNCTION", 
        DefaultLanguageHighlighterColors.INSTANCE_METHOD
    )
    
    // Colors for different conditional keywords
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
    
    // Colors for error/warning keywords
    val ERROR_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_ERROR_KEYWORD",
        DefaultLanguageHighlighterColors.KEYWORD
    )
    
    val WARNING_KEYWORD_KEY = TextAttributesKey.createTextAttributesKey(
        "HETU_WARNING_KEYWORD",
        DefaultLanguageHighlighterColors.KEYWORD
    )
    
    // Colors for different loop constructs
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
}