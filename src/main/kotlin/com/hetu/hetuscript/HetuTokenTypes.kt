package com.hetu.hetuscript

object HetuTokenTypes {
    // Basic tokens
    @JvmField
    val IDENTIFIER = HetuTokenType("IDENTIFIER")
    @JvmField
    val NUMBER = HetuTokenType("NUMBER")
    @JvmField
    val STRING = HetuTokenType("STRING")
    @JvmField
    val COMMENT = HetuTokenType("COMMENT")
    @JvmField
    val KEYWORD = HetuTokenType("KEYWORD")
    @JvmField
    val OPERATION_SIGN = HetuTokenType("OPERATION_SIGN")
    @JvmField
    val BAD_CHARACTER = HetuTokenType("BAD_CHARACTER")
    val WHITE_SPACE = com.intellij.psi.TokenType.WHITE_SPACE
    
    // VS Code-style specific tokens
    @JvmField
    val LINE_COMMENT = HetuTokenType("LINE_COMMENT")
    @JvmField
    val BLOCK_COMMENT = HetuTokenType("BLOCK_COMMENT")
    @JvmField
    val LINE_COMMENT_START = HetuTokenType("LINE_COMMENT_START")
    @JvmField
    val BLOCK_COMMENT_START = HetuTokenType("BLOCK_COMMENT_START")
    @JvmField
    val BLOCK_COMMENT_END = HetuTokenType("BLOCK_COMMENT_END")
    
    // String tokens
    @JvmField
    val STRING_DOUBLE = HetuTokenType("STRING_DOUBLE")
    @JvmField
    val STRING_SINGLE = HetuTokenType("STRING_SINGLE")
    @JvmField
    val ESCAPE_SEQUENCE = HetuTokenType("ESCAPE_SEQUENCE")
    
    // Keyword categories from VS Code
    @JvmField
    val KEYWORD_CONTROL = HetuTokenType("KEYWORD_CONTROL")  // if, else, for, while, break, continue, return, etc.
    @JvmField
    val KEYWORD_DECLARATION = HetuTokenType("KEYWORD_DECLARATION")  // var, final, fun, class, etc.
    @JvmField
    val STORAGE_MODIFIER = HetuTokenType("STORAGE_MODIFIER")  // public, private, protected
    @JvmField
    val STORAGE_TYPE = HetuTokenType("STORAGE_TYPE")  // int, double, bool, etc.
    @JvmField
    val PRIMITIVE_VALUE = HetuTokenType("PRIMITIVE_VALUE")  // true, false, null, undefined
    
    // More specific primitive value types
    @JvmField
    val BOOLEAN_LITERAL = HetuTokenType("BOOLEAN_LITERAL")  // true, false
    @JvmField
    val NULL_LITERAL = HetuTokenType("NULL_LITERAL")  // null, undefined
    @JvmField
    val THIS_SUPER_LITERAL = HetuTokenType("THIS_SUPER_LITERAL")  // this, super
    
    // Operators
    @JvmField
    val OPERATOR_ARITHMETIC = HetuTokenType("OPERATOR_ARITHMETIC")  // +, -, *, /, %
    @JvmField
    val OPERATOR_ASSIGNMENT = HetuTokenType("OPERATOR_ASSIGNMENT")  // =
    @JvmField
    val OPERATOR_COMPARISON = HetuTokenType("OPERATOR_COMPARISON")  // ==, !=, >, <, >=, <=
    @JvmField
    val OPERATOR_LOGICAL = HetuTokenType("OPERATOR_LOGICAL")  // &&, ||, !
    @JvmField
    val OPERATOR_BITWISE = HetuTokenType("OPERATOR_BITWISE")  // &, |, ^, ~
    @JvmField
    val OPERATOR_OTHER = HetuTokenType("OPERATOR_OTHER")
    
    // Special tokens
    @JvmField
    val FUNCTION_CALL = HetuTokenType("FUNCTION_CALL")
    @JvmField
    val CLASS_DECLARATION = HetuTokenType("CLASS_DECLARATION")
    @JvmField
    val VARIABLE_DECLARATION = HetuTokenType("VARIABLE_DECLARATION")
    
    // More specific variable declaration types
    @JvmField
    val VAR_DECLARATION = HetuTokenType("VAR_DECLARATION")  // var
    @JvmField
    val CONST_DECLARATION = HetuTokenType("CONST_DECLARATION")  // const
    @JvmField
    val FINAL_DECLARATION = HetuTokenType("FINAL_DECLARATION")  // final
    
    // More specific modifier types
    @JvmField
    val PUBLIC_MODIFIER = HetuTokenType("PUBLIC_MODIFIER")  // public
    @JvmField
    val PRIVATE_MODIFIER = HetuTokenType("PRIVATE_MODIFIER")  // private
    @JvmField
    val STATIC_MODIFIER = HetuTokenType("STATIC_MODIFIER")  // static
    @JvmField
    val EXTERNAL_MODIFIER = HetuTokenType("EXTERNAL_MODIFIER")  // external
    
    // More specific function types
    @JvmField
    val CONSTRUCTOR_FUNCTION = HetuTokenType("CONSTRUCTOR_FUNCTION")  // constructor, construct
    @JvmField
    val GET_SET_FUNCTION = HetuTokenType("GET_SET_FUNCTION")  // get, set
    
    // Type annotation
    @JvmField
    val TYPE_ANNOTATION = HetuTokenType("TYPE_ANNOTATION")
    
    // More specific keyword types based on VS Code grammar
    @JvmField
    val STORAGE_TYPE_HETU = HetuTokenType("STORAGE_TYPE_HETU")  // str, num, bool, etc.
    @JvmField
    val ASSERT_KEYWORD = HetuTokenType("ASSERT_KEYWORD")  // assert
    @JvmField
    val LOOP_KEYWORD = HetuTokenType("LOOP_KEYWORD")  // break, continue, do, while
    @JvmField
    val FLOW_KEYWORD = HetuTokenType("FLOW_KEYWORD")  // return, throw
    @JvmField
    val CONDITIONAL_KEYWORD = HetuTokenType("CONDITIONAL_KEYWORD")  // else, if
    @JvmField
    val SWITCH_KEYWORD = HetuTokenType("SWITCH_KEYWORD")  // switch, when, case, default
    @JvmField
    val FUNCTION_KEYWORD = HetuTokenType("FUNCTION_KEYWORD")  // function, fun
    @JvmField
    val CLASS_KEYWORD = HetuTokenType("CLASS_KEYWORD")  // class, struct, namespace
    @JvmField
    val ENUM_KEYWORD = HetuTokenType("ENUM_KEYWORD")  // enum
    @JvmField
    val IMPORT_KEYWORD = HetuTokenType("IMPORT_KEYWORD")  // import, export
    @JvmField
    val AS_KEYWORD = HetuTokenType("AS_KEYWORD")  // as
    @JvmField
    val MODIFIER_KEYWORD = HetuTokenType("MODIFIER_KEYWORD")  // async, abstract
    @JvmField
    val NEW_KEYWORD = HetuTokenType("NEW_KEYWORD")  // new
    @JvmField
    val TYPEOF_KEYWORD = HetuTokenType("TYPEOF_KEYWORD")  // typeof, instanceof
    
    // Specific punctuation based on VS Code grammar
    @JvmField
    val PUNCTUATION_SEMICOLON = HetuTokenType("PUNCTUATION_SEMICOLON")
    @JvmField
    val PUNCTUATION_COMMA = HetuTokenType("PUNCTUATION_COMMA")
    @JvmField
    val PUNCTUATION_COLON = HetuTokenType("PUNCTUATION_COLON")
    @JvmField
    val BRACE_LEFT = HetuTokenType("BRACE_LEFT")
    @JvmField
    val BRACE_RIGHT = HetuTokenType("BRACE_RIGHT")
    @JvmField
    val BRACKET_LEFT = HetuTokenType("BRACKET_LEFT")
    @JvmField
    val BRACKET_RIGHT = HetuTokenType("BRACKET_RIGHT")
    @JvmField
    val PAREN_LEFT = HetuTokenType("PAREN_LEFT")
    @JvmField
    val PAREN_RIGHT = HetuTokenType("PAREN_RIGHT")
    @JvmField
    val DOT = HetuTokenType("DOT")
    @JvmField
    val COMMA = HetuTokenType("COMMA")
    @JvmField
    val SEMICOLON = HetuTokenType("SEMICOLON")
    
    // More specific number types
    @JvmField
    val INTEGER_NUMBER = HetuTokenType("INTEGER_NUMBER")
    @JvmField
    val FLOAT_NUMBER = HetuTokenType("FLOAT_NUMBER")
    @JvmField
    val HEX_NUMBER = HetuTokenType("HEX_NUMBER")
    
    // More specific string types
    @JvmField
    val REGULAR_STRING = HetuTokenType("REGULAR_STRING")
    @JvmField
    val TEMPLATE_STRING = HetuTokenType("TEMPLATE_STRING")
    @JvmField
    val ESCAPED_STRING = HetuTokenType("ESCAPED_STRING")
    
    // More specific bracket types
    @JvmField
    val ROUND_BRACKETS = HetuTokenType("ROUND_BRACKETS")  // ()
    @JvmField
    val SQUARE_BRACKETS = HetuTokenType("SQUARE_BRACKETS")  // []
    @JvmField
    val CURLY_BRACES = HetuTokenType("CURLY_BRACES")  // {}
    
    // More specific comment types
    @JvmField
    val SINGLE_LINE_COMMENT = HetuTokenType("SINGLE_LINE_COMMENT")  // //
    @JvmField
    val MULTI_LINE_COMMENT = HetuTokenType("MULTI_LINE_COMMENT")  // /* */
    
    // More specific access level types
    @JvmField
    val PUBLIC_ACCESS = HetuTokenType("PUBLIC_ACCESS")  // public
    @JvmField
    val PRIVATE_ACCESS = HetuTokenType("PRIVATE_ACCESS")  // private
    @JvmField
    val PROTECTED_ACCESS = HetuTokenType("PROTECTED_ACCESS")  // protected
    
    // More specific scope types
    @JvmField
    val GLOBAL_SCOPE = HetuTokenType("GLOBAL_SCOPE")
    @JvmField
    val LOCAL_SCOPE = HetuTokenType("LOCAL_SCOPE")
    @JvmField
    val FIELD_SCOPE = HetuTokenType("FIELD_SCOPE")
    
    // More specific function types
    @JvmField
    val STATIC_FUNCTION = HetuTokenType("STATIC_FUNCTION")  // static function
    @JvmField
    val INSTANCE_FUNCTION = HetuTokenType("INSTANCE_FUNCTION")  // instance function
    
    // More specific conditional types
    @JvmField
    val IF_CONDITIONAL = HetuTokenType("IF_CONDITIONAL")  // if
    @JvmField
    val ELSE_CONDITIONAL = HetuTokenType("ELSE_CONDITIONAL")  // else
    @JvmField
    val ELIF_CONDITIONAL = HetuTokenType("ELIF_CONDITIONAL")  // elif (if exists in Hetu)
    
    // More specific error/warning keywords
    @JvmField
    val ERROR_KEYWORD = HetuTokenType("ERROR_KEYWORD")  // error, raise, etc.
    @JvmField
    val WARNING_KEYWORD = HetuTokenType("WARNING_KEYWORD")  // warn, warning, etc.
    
    // More specific loop constructs
    @JvmField
    val FOR_LOOP = HetuTokenType("FOR_LOOP")  // for
    @JvmField
    val WHILE_LOOP = HetuTokenType("WHILE_LOOP")  // while
    @JvmField
    val DO_WHILE_LOOP = HetuTokenType("DO_WHILE_LOOP")  // do-while
    
    // Operators based on VS Code grammar
    @JvmField
    val ASSIGNMENT_OPERATOR = HetuTokenType("ASSIGNMENT_OPERATOR")  // =
    @JvmField
    val COMPOUND_ASSIGNMENT_OPERATOR = HetuTokenType("COMPOUND_ASSIGNMENT_OPERATOR")  // *=, /=, +=, -=
    @JvmField
    val COMPARISON_OPERATOR = HetuTokenType("COMPARISON_OPERATOR")  // ==, !=
    @JvmField
    val RELATIONAL_OPERATOR = HetuTokenType("RELATIONAL_OPERATOR")  // <=, >=, <, >
    @JvmField
    val LOGICAL_OPERATOR = HetuTokenType("LOGICAL_OPERATOR")  // !, &&, ||, ??
    @JvmField
    val BITWISE_OPERATOR = HetuTokenType("BITWISE_OPERATOR")  // &, ~, ^, |
    @JvmField
    val SHIFT_OPERATOR = HetuTokenType("SHIFT_OPERATOR")  // <<, >>, >>>
    @JvmField
    val ARITHMETIC_OPERATOR = HetuTokenType("ARITHMETIC_OPERATOR")  // %, *, /, -, +
    @JvmField
    val INCREMENT_DECREMENT_OPERATOR = HetuTokenType("INCREMENT_DECREMENT_OPERATOR")  // ++, --
    @JvmField
    val TERNARY_OPERATOR = HetuTokenType("TERNARY_OPERATOR")  // ?, :
    @JvmField
    val SPREAD_OPERATOR = HetuTokenType("SPREAD_OPERATOR")  // ...
    @JvmField
    val ACCESSOR_OPERATOR = HetuTokenType("ACCESSOR_OPERATOR")  // .
    @JvmField
    val ARROW_OPERATOR = HetuTokenType("ARROW_OPERATOR")  // ->
    @JvmField
    val NULL_COALESCING_OPERATOR = HetuTokenType("NULL_COALESCING_OPERATOR")  // ??
}