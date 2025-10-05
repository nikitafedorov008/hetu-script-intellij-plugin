package com.hetu.hetuscript

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class HetuColorSettingsPage : ColorSettingsPage {
    
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return DESCRIPTORS
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return "Hetu Script"
    }

    override fun getIcon(): Icon? {
        return null
    }

    override fun getHighlighter(): SyntaxHighlighter {
        return HetuSyntaxHighlighter()
    }

    override fun getDemoText(): String {
        return """// Hetu Script demo code
import hetu.std.*

/* This is a 
   multi-line comment */

var someNumber = 42
var someString = "Hello, World!"
var someBool = true
var someList = [1, 2, 3, 4]

fun fibonacci(n: int) -> int {
    if (n <= 1) {
        return n
    } else {
        return fibonacci(n - 1) + fibonacci(n - 2)
    }
}

class Person {
    var name: String
    var age: int
    
    constructor(name: String, age: int) {
        this.name = name
        this.age = age
    }
    
    fun greet() -> String {
        return "Hello, I'm " + this.name + " and I'm " + this.age + " years old."
    }
}

enum Color {
    RED, GREEN, BLUE
}

var p = Person("Alice", 30)
println(p.greet())

for (i in 0..10) {
    if (i % 2 == 0) {
        println("Even: " + i)
    } else {
        println("Odd: " + i)
    }
}

var result = someBool ? "Yes" : "No"
assert(result == "Yes")

// Different variable types
const MAX_SIZE = 100
late var lazyValue: String
external fun externalFunction(): void
static var count: int = 0
"""
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? {
        // This is where you can map additional demo text tags to attributes
        val map: MutableMap<String, TextAttributesKey> = HashMap()
        // We can add tags for the demo text if needed
        return map
    }

    companion object {
        private val DESCRIPTORS = arrayOf(
            // Basic tokens
            AttributesDescriptor("Keyword", HetuSyntaxHighlighter.KEYWORD_KEY),
            AttributesDescriptor("Identifier", HetuSyntaxHighlighter.IDENTIFIER_KEY),
            AttributesDescriptor("Number", HetuSyntaxHighlighter.NUMBER_KEY),
            AttributesDescriptor("String", HetuSyntaxHighlighter.STRING_KEY),
            AttributesDescriptor("Comment", HetuSyntaxHighlighter.COMMENT_KEY),
            AttributesDescriptor("Operation Sign", HetuSyntaxHighlighter.OPERATION_SIGN_KEY),
            
            // More specific number types
            AttributesDescriptor("Integer Number", HetuSyntaxHighlighter.INTEGER_NUMBER_KEY),
            AttributesDescriptor("Float Number", HetuSyntaxHighlighter.FLOAT_NUMBER_KEY),
            AttributesDescriptor("Hex Number", HetuSyntaxHighlighter.HEX_NUMBER_KEY),
            
            // More specific string types
            AttributesDescriptor("Regular String", HetuSyntaxHighlighter.REGULAR_STRING_KEY),
            AttributesDescriptor("Template String", HetuSyntaxHighlighter.TEMPLATE_STRING_KEY),
            AttributesDescriptor("Escaped String", HetuSyntaxHighlighter.ESCAPED_STRING_KEY),
            
            // VS Code-style specific tokens
            AttributesDescriptor("Line Comment", HetuSyntaxHighlighter.LINE_COMMENT_KEY),
            AttributesDescriptor("Block Comment", HetuSyntaxHighlighter.BLOCK_COMMENT_KEY),
            AttributesDescriptor("String Double", HetuSyntaxHighlighter.STRING_DOUBLE_KEY),
            AttributesDescriptor("String Single", HetuSyntaxHighlighter.STRING_SINGLE_KEY),
            AttributesDescriptor("Escape Sequence", HetuSyntaxHighlighter.ESCAPE_SEQUENCE_KEY),
            
            // More specific comment types
            AttributesDescriptor("Single Line Comment", HetuSyntaxHighlighter.SINGLE_LINE_COMMENT_KEY),
            AttributesDescriptor("Multi Line Comment", HetuSyntaxHighlighter.MULTI_LINE_COMMENT_KEY),
            
            // Keyword categories from VS Code
            AttributesDescriptor("Keyword Control", HetuSyntaxHighlighter.KEYWORD_CONTROL_KEY),
            AttributesDescriptor("Keyword Declaration", HetuSyntaxHighlighter.KEYWORD_DECLARATION_KEY),
            AttributesDescriptor("Storage Modifier", HetuSyntaxHighlighter.STORAGE_MODIFIER_KEY),
            AttributesDescriptor("Storage Type", HetuSyntaxHighlighter.STORAGE_TYPE_KEY),
            AttributesDescriptor("Storage Type Hetu", HetuSyntaxHighlighter.STORAGE_TYPE_HETU_KEY),
            AttributesDescriptor("Primitive Value", HetuSyntaxHighlighter.PRIMITIVE_VALUE_KEY),
            
            // More specific primitive value types
            AttributesDescriptor("Boolean Literal", HetuSyntaxHighlighter.BOOLEAN_LITERAL_KEY),
            AttributesDescriptor("Null Literal", HetuSyntaxHighlighter.NULL_LITERAL_KEY),
            AttributesDescriptor("This/Super Literal", HetuSyntaxHighlighter.THIS_SUPER_LITERAL_KEY),
            
            // More specific access level types
            AttributesDescriptor("Public Access", HetuSyntaxHighlighter.PUBLIC_ACCESS_KEY),
            AttributesDescriptor("Private Access", HetuSyntaxHighlighter.PRIVATE_ACCESS_KEY),
            AttributesDescriptor("Protected Access", HetuSyntaxHighlighter.PROTECTED_ACCESS_KEY),
            
            // More specific scope types
            AttributesDescriptor("Global Scope", HetuSyntaxHighlighter.GLOBAL_SCOPE_KEY),
            AttributesDescriptor("Local Scope", HetuSyntaxHighlighter.LOCAL_SCOPE_KEY),
            AttributesDescriptor("Field Scope", HetuSyntaxHighlighter.FIELD_SCOPE_KEY),
            
            // More specific function types
            AttributesDescriptor("Static Function", HetuSyntaxHighlighter.STATIC_FUNCTION_KEY),
            AttributesDescriptor("Instance Function", HetuSyntaxHighlighter.INSTANCE_FUNCTION_KEY),
            
            // More specific conditional types
            AttributesDescriptor("If Conditional", HetuSyntaxHighlighter.IF_CONDITIONAL_KEY),
            AttributesDescriptor("Else Conditional", HetuSyntaxHighlighter.ELSE_CONDITIONAL_KEY),
            AttributesDescriptor("Elif Conditional", HetuSyntaxHighlighter.ELIF_CONDITIONAL_KEY),
            
            // More specific loop types
            AttributesDescriptor("For Loop", HetuSyntaxHighlighter.FOR_LOOP_KEY),
            AttributesDescriptor("While Loop", HetuSyntaxHighlighter.WHILE_LOOP_KEY),
            AttributesDescriptor("Do-While Loop", HetuSyntaxHighlighter.DO_WHILE_LOOP_KEY),
            
            // More specific error/warning keywords
            AttributesDescriptor("Error Keyword", HetuSyntaxHighlighter.ERROR_KEYWORD_KEY),
            AttributesDescriptor("Warning Keyword", HetuSyntaxHighlighter.WARNING_KEYWORD_KEY),
            
            // More specific declaration types
            AttributesDescriptor("Var Declaration", HetuSyntaxHighlighter.VAR_DECLARATION_KEY),
            AttributesDescriptor("Const Declaration", HetuSyntaxHighlighter.CONST_DECLARATION_KEY),
            AttributesDescriptor("Final Declaration", HetuSyntaxHighlighter.FINAL_DECLARATION_KEY),
            
            // More specific modifier types
            AttributesDescriptor("Public Modifier", HetuSyntaxHighlighter.PUBLIC_MODIFIER_KEY),
            AttributesDescriptor("Private Modifier", HetuSyntaxHighlighter.PRIVATE_MODIFIER_KEY),
            AttributesDescriptor("Static Modifier", HetuSyntaxHighlighter.STATIC_MODIFIER_KEY),
            AttributesDescriptor("External Modifier", HetuSyntaxHighlighter.EXTERNAL_MODIFIER_KEY),
            
            // More specific function types
            AttributesDescriptor("Constructor Function", HetuSyntaxHighlighter.CONSTRUCTOR_FUNCTION_KEY),
            AttributesDescriptor("Get/Set Function", HetuSyntaxHighlighter.GET_SET_FUNCTION_KEY),
            
            // Type annotation
            AttributesDescriptor("Type Annotation", HetuSyntaxHighlighter.TYPE_ANNOTATION_KEY),
            
            // Specific keywords
            AttributesDescriptor("Assert Keyword", HetuSyntaxHighlighter.ASSERT_KEYWORD_KEY),
            AttributesDescriptor("Loop Keyword", HetuSyntaxHighlighter.LOOP_KEYWORD_KEY),
            AttributesDescriptor("Flow Keyword", HetuSyntaxHighlighter.FLOW_KEYWORD_KEY),
            AttributesDescriptor("Conditional Keyword", HetuSyntaxHighlighter.CONDITIONAL_KEYWORD_KEY),
            AttributesDescriptor("Switch Keyword", HetuSyntaxHighlighter.SWITCH_KEYWORD_KEY),
            AttributesDescriptor("Function Keyword", HetuSyntaxHighlighter.FUNCTION_KEYWORD_KEY),
            AttributesDescriptor("Class Keyword", HetuSyntaxHighlighter.CLASS_KEYWORD_KEY),
            AttributesDescriptor("Enum Keyword", HetuSyntaxHighlighter.ENUM_KEYWORD_KEY),
            AttributesDescriptor("Import Keyword", HetuSyntaxHighlighter.IMPORT_KEYWORD_KEY),
            AttributesDescriptor("As Keyword", HetuSyntaxHighlighter.AS_KEYWORD_KEY),
            AttributesDescriptor("Modifier Keyword", HetuSyntaxHighlighter.MODIFIER_KEYWORD_KEY),
            AttributesDescriptor("New Keyword", HetuSyntaxHighlighter.NEW_KEYWORD_KEY),
            AttributesDescriptor("Typeof Keyword", HetuSyntaxHighlighter.TYPEOF_KEYWORD_KEY),
            
            // Operators
            AttributesDescriptor("Operator Arithmetic", HetuSyntaxHighlighter.ARITHMETIC_OPERATOR_KEY),
            AttributesDescriptor("Operator Assignment", HetuSyntaxHighlighter.ASSIGNMENT_OPERATOR_KEY),
            AttributesDescriptor("Operator Comparison", HetuSyntaxHighlighter.COMPARISON_OPERATOR_KEY),
            AttributesDescriptor("Operator Logical", HetuSyntaxHighlighter.LOGICAL_OPERATOR_KEY),
            AttributesDescriptor("Operator Bitwise", HetuSyntaxHighlighter.BITWISE_OPERATOR_KEY),
            AttributesDescriptor("Operator Other", HetuSyntaxHighlighter.OPERATOR_OTHER_KEY),
            
            // Additional operators
            AttributesDescriptor("Compound Assignment Operator", HetuSyntaxHighlighter.COMPOUND_ASSIGNMENT_OPERATOR_KEY),
            AttributesDescriptor("Relational Operator", HetuSyntaxHighlighter.RELATIONAL_OPERATOR_KEY),
            AttributesDescriptor("Bitwise Operator", HetuSyntaxHighlighter.BITWISE_OPERATOR_KEY),
            AttributesDescriptor("Shift Operator", HetuSyntaxHighlighter.SHIFT_OPERATOR_KEY),
            AttributesDescriptor("Increment/Decrement Operator", HetuSyntaxHighlighter.INCREMENT_DECREMENT_OPERATOR_KEY),
            AttributesDescriptor("Ternary Operator", HetuSyntaxHighlighter.TERNARY_OPERATOR_KEY),
            AttributesDescriptor("Spread Operator", HetuSyntaxHighlighter.SPREAD_OPERATOR_KEY),
            AttributesDescriptor("Accessor Operator", HetuSyntaxHighlighter.ACCESSOR_OPERATOR_KEY),
            AttributesDescriptor("Arrow Operator", HetuSyntaxHighlighter.ARROW_OPERATOR_KEY),
            AttributesDescriptor("Null Coalescing Operator", HetuSyntaxHighlighter.NULL_COALESCING_OPERATOR_KEY),
            
            // More specific bracket types
            AttributesDescriptor("Round Brackets", HetuSyntaxHighlighter.ROUND_BRACKETS_KEY),
            AttributesDescriptor("Square Brackets", HetuSyntaxHighlighter.SQUARE_BRACKETS_KEY),
            AttributesDescriptor("Curly Braces", HetuSyntaxHighlighter.CURLY_BRACES_KEY),
            
            // Special tokens
            AttributesDescriptor("Function Call", HetuSyntaxHighlighter.FUNCTION_CALL_KEY),
            AttributesDescriptor("Class Declaration", HetuSyntaxHighlighter.CLASS_DECLARATION_KEY),
            AttributesDescriptor("Variable Declaration", HetuSyntaxHighlighter.VARIABLE_DECLARATION_KEY),
            
            // Brackets and punctuation
            AttributesDescriptor("Braces", HetuSyntaxHighlighter.BRACES_KEY),
            AttributesDescriptor("Brackets", HetuSyntaxHighlighter.BRACKETS_KEY),
            AttributesDescriptor("Parentheses", HetuSyntaxHighlighter.PARENTHESES_KEY),
            AttributesDescriptor("Dot", HetuSyntaxHighlighter.DOT_KEY),
            AttributesDescriptor("Comma", HetuSyntaxHighlighter.COMMA_KEY),
            AttributesDescriptor("Semicolon", HetuSyntaxHighlighter.SEMICOLON_KEY),
            AttributesDescriptor("Punctuation Semicolon", HetuSyntaxHighlighter.PUNCTUATION_SEMICOLON_KEY),
            AttributesDescriptor("Punctuation Comma", HetuSyntaxHighlighter.PUNCTUATION_COMMA_KEY),
            AttributesDescriptor("Punctuation Colon", HetuSyntaxHighlighter.COLON_PUNCTUATION_KEY),
            
            // Error highlighting
            AttributesDescriptor("Bad Character", HetuSyntaxHighlighter.BAD_CHARACTER_KEY)
        )
    }
}