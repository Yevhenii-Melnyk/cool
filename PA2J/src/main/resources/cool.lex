package compilers.pa2j;

import java_cup.runtime.Symbol;
import static compilers.pa2j.TokenConstants.*;

%%

%{

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    private int curr_lineno = 1;
    int get_curr_lineno() {
	    return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
	    filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	    return filename;
    }

    StringBuilder sb;

    void newString(){
        sb = new StringBuilder();
    }

    void appendString(String str){
        sb.append(str);
    }

    String currentString(){
        return sb.toString();
    }

    AbstractSymbol addString(String str) {
        return AbstractTable.stringtable.addString(str, MAX_STR_CONST);
    }
    AbstractSymbol addInt(String num) {
        return AbstractTable.inttable.addString(num);
    }
    AbstractSymbol addId(String id) {
        return AbstractTable.idtable.addString(id, MAX_STR_CONST);
    }

    int nestedCommentCount = 0;

%}

%init{

%init}

%eofval{
    switch(yy_lexical_state) {
    case YYINITIAL:
	    break;
    case COMMENT:
	    yybegin(YYINITIAL);
	    return new Symbol(ERROR, "EOF in comment");
	case STRING:
        yybegin(YYINITIAL);
        return new Symbol(ERROR, "EOF in string constant");
    }
    return new Symbol(EOF);
%eofval}

%class CoolLexer
%cup
%state COMMENT
%state SINGLE_LINE_COMMENT
%state STRING

NEWLINE=[\n\012]|\r\n
SPACE=[ \032\t\011\r\015\013\022\f\014]

CLASS =    [cC][lL][aA][sS][sS]
IF =       [iI][fF]
FI =       [fF][iI]
THEN =     [tT][hH][eE][nN]
ELSE =     [Ee][Ll][Ss][Ee]
IN =       [iI][nN]
INHERITS = [iI][nN][hH][eE][rR][iI][tT][sS]
LET =      [lL][eE][tT]
LOOP =     [lL][oO][oO][pP]
POOL =     [pP][oO][oO][lL]
WHILE =    [wW][hH][iI][lL][eE]
CASE =     [cC][aA][sS][eE]
ESAC =     [eE][sS][aA][cC]
OF =       [oO][fF]
NEW =      [nN][eE][wW]
IS_VOID =  [iI][sS][vV][oO][iI][dD]
NOT =      [nN][oO][tT]
TRUE =     t[rR][uU][eE]
FALSE =    f[aA][lL][sS][eE]

TYPE_ID =   [A-Z][A-Za-z0-9_]*
OBJECT_ID = [a-z][A-Za-z0-9_]*

DIGIT = [0-9]

%%

<YYINITIAL> "(*" { nestedCommentCount = 1; yybegin(COMMENT); }
<YYINITIAL> "*)" { return new Symbol(ERROR, "Unmatched *)"); }
<COMMENT> "(*"   { nestedCommentCount++; }
<COMMENT> "*)"   {
    nestedCommentCount--;
    if (nestedCommentCount == 0)
        yybegin(YYINITIAL);
}
<YYINITIAL> "--" { yybegin(SINGLE_LINE_COMMENT); }
<SINGLE_LINE_COMMENT> {NEWLINE}+ { curr_lineno++; yybegin(YYINITIAL); }
<COMMENT, SINGLE_LINE_COMMENT> . { }


<YYINITIAL> {CLASS}     { return new Symbol(CLASS); }
<YYINITIAL> {ELSE}		{ return new Symbol(ELSE); }
<YYINITIAL> {IF}		{ return new Symbol(IF); }
<YYINITIAL> {FI}		{ return new Symbol(FI); }
<YYINITIAL> {IN}		{ return new Symbol(IN); }
<YYINITIAL> {INHERITS}	{ return new Symbol(INHERITS); }
<YYINITIAL> {IS_VOID}	{ return new Symbol(ISVOID); }
<YYINITIAL> {LET}		{ return new Symbol(LET); }
<YYINITIAL> {LOOP}		{ return new Symbol(LOOP); }
<YYINITIAL> {POOL}		{ return new Symbol(POOL); }
<YYINITIAL> {THEN}		{ return new Symbol(THEN); }
<YYINITIAL> {WHILE}		{ return new Symbol(WHILE); }
<YYINITIAL> {CASE}		{ return new Symbol(CASE); }
<YYINITIAL> {ESAC}		{ return new Symbol(ESAC); }
<YYINITIAL> {NEW}		{ return new Symbol(NEW); }
<YYINITIAL> {OF}		{ return new Symbol(OF); }
<YYINITIAL> {NOT}		{ return new Symbol(NOT); }
<YYINITIAL> {TRUE}		{ return new Symbol(BOOL_CONST, Boolean.TRUE); }
<YYINITIAL> {FALSE}     { return new Symbol(BOOL_CONST, Boolean.FALSE); }

<YYINITIAL> "+"			{ return new Symbol(PLUS); }
<YYINITIAL> "/"			{ return new Symbol(DIV); }
<YYINITIAL> "-"			{ return new Symbol(MINUS); }
<YYINITIAL> "*"			{ return new Symbol(MULT); }
<YYINITIAL> "="			{ return new Symbol(EQ); }
<YYINITIAL> "<"			{ return new Symbol(LT); }
<YYINITIAL> "."			{ return new Symbol(DOT); }
<YYINITIAL> "~"			{ return new Symbol(NEG); }
<YYINITIAL> ","			{ return new Symbol(COMMA); }
<YYINITIAL> ";"			{ return new Symbol(SEMI); }
<YYINITIAL> ":"			{ return new Symbol(COLON); }
<YYINITIAL> "("			{ return new Symbol(LPAREN); }
<YYINITIAL> ")"			{ return new Symbol(RPAREN); }
<YYINITIAL> "@"			{ return new Symbol(AT); }
<YYINITIAL> "{"			{ return new Symbol(LBRACE); }
<YYINITIAL> "}"			{ return new Symbol(RBRACE); }
<YYINITIAL> "=>"		{ return new Symbol(DARROW); }
<YYINITIAL> "<="		{ return new Symbol(LE); }
<YYINITIAL> "<-"		{ return new Symbol(ASSIGN); }

<YYINITIAL> {TYPE_ID}   { return new Symbol(TYPEID, addId(yytext())); }
<YYINITIAL> {OBJECT_ID} { return new Symbol(OBJECTID, addId(yytext())); }
<YYINITIAL> {DIGIT}+	{ return new Symbol(INT_CONST, addInt(yytext())); }

<YYINITIAL> "\""		{ yybegin(STRING); newString(); }
<STRING> \\n            { appendString("\n"); }
<STRING> \\b            { appendString("\b"); }
<STRING> \\t            { appendString("\t"); }
<STRING> \\f            { appendString("\f"); }
<STRING> \\\\           { appendString("\\"); }
<STRING> \\\"           { appendString("\""); }
<STRING> \\.            { appendString(yytext().substring(1)); }
<STRING> "\""           {
    yybegin(YYINITIAL);
    if( currentString().length() >= MAX_STR_CONST )
        return new Symbol(ERROR, "String constant too long");
    if( !currentString().contains("\0") )
        return new Symbol(STR_CONST, addString(currentString()));
    else
        return new Symbol(ERROR, "String contains null character");
}
<STRING> {NEWLINE}+     {
    curr_lineno++;
    yybegin(YYINITIAL);
    return new Symbol(ERROR, "Unterminated string constant");
}
<STRING> {SPACE}+       { appendString(yytext());}
<STRING> \\{NEWLINE}    { curr_lineno++; appendString(yytext().substring(1)); }
<STRING> .              { appendString(yytext()); }

{SPACE}+   { }
{NEWLINE}+ { curr_lineno++; }

. {
    return new Symbol(ERROR, yytext());
}

