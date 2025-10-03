package com.hetu.hetuscript;

import com.intellij.psi.tree.IElementType;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import static com.hetu.hetuscript.HetuTypes.*;

%%

%class HetuLexer
%implements FlexLexer
%function advance
%type IElementType

WHITE_SPACE=[ \\t\\f]+
IDENTIFIER=[a-zA-Z_][a-zA-Z0-9_]*
NUMBER=\\b\\d+(\\.\\d+)?([eE][+-]?\\d+)?\\b

%%

<YYINITIAL> {
  {WHITE_SPACE}           { return TokenType.WHITE_SPACE; }
  "class"|"func"|"var"|"final"|"if"|"else"|"for"|"while"|"break"|"continue"|"return"|"import"|"export"|"assert"|"true"|"false"|"null"|"this"|"super"|"new"
                          { return KEYWORD; }
  {IDENTIFIER}            { return IDENTIFIER; }
  {NUMBER}                { return NUMBER; }
  "("|")"|"{"|"}"|"["|"]"|"."|","|";"|"+"|"-"|"*"|"/"|"="|"=="|"!="|"<="|">="
                          { return OPERATION_SIGN; }
  "//".*                  { return COMMENT; }
  [^]                     { return TokenType.BAD_CHARACTER; }
}
