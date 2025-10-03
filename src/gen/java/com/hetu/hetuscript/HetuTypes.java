package com.hetu.hetuscript;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

public interface HetuTypes {
    IElementType IDENTIFIER = com.hetu.hetuscript.HetuTokenTypes.IDENTIFIER;
    IElementType NUMBER = com.hetu.hetuscript.HetuTokenTypes.NUMBER;
    IElementType STRING = com.hetu.hetuscript.HetuTokenTypes.STRING;
    IElementType COMMENT = com.hetu.hetuscript.HetuTokenTypes.COMMENT;
    IElementType KEYWORD = com.hetu.hetuscript.HetuTokenTypes.KEYWORD;
    IElementType OPERATION_SIGN = com.hetu.hetuscript.HetuTokenTypes.OPERATION_SIGN;
    IElementType BAD_CHARACTER = com.hetu.hetuscript.HetuTokenTypes.BAD_CHARACTER;
    
    TokenSet WHITE_SPACES = TokenSet.create(com.intellij.psi.TokenType.WHITE_SPACE);
    TokenSet COMMENTS = TokenSet.create(COMMENT);
    TokenSet STRINGS = TokenSet.create(STRING);
    TokenSet KEYWORDS = TokenSet.create(KEYWORD);
    TokenSet OPERATIONS = TokenSet.create(OPERATION_SIGN);
}