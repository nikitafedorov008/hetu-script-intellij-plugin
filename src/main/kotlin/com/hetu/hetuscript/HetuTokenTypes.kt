package com.hetu.hetuscript

object HetuTokenTypes {
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
}