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

    AbstractSymbol addString(String str) {
        return AbstractTable.stringtable.addString(str, MAX_STR_CONST);
    }

%}

%init{

%init}

%eofval{
    switch(yy_lexical_state) {
    case YYINITIAL:
	    break;
    case COMMENT:
    case SINGLE_LINE_COMMENT:
	    yybegin(YYINITIAL);
	    return new Symbol(TokenConstants.ERROR, "EOF in comment");
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup
%state COMMENT
%state SINGLE_LINE_COMMENT

DIGIT=[0-9]
NEWLINE=\n

%%

<YYINITIAL> "(*" { yybegin(COMMENT); }
<COMMENT> "*)" { yybegin(YYINITIAL);}
<YYINITIAL> "*)" { return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }
<YYINITIAL> "--" { yybegin(SINGLE_LINE_COMMENT); }
<SINGLE_LINE_COMMENT> {NEWLINE} { yybegin(YYINITIAL); }
<COMMENT, SINGLE_LINE_COMMENT> . {}



<YYINITIAL> . { return new Symbol(STR_CONST, addString(yytext())); }

{NEWLINE} { curr_lineno++; }

. {
    System.err.println("LEXER BUG - UNMATCHED: " + yytext());
}

