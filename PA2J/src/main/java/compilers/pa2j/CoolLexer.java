package compilers.pa2j;
import java_cup.runtime.Symbol;
import static compilers.pa2j.TokenConstants.*;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

	}

	private boolean yy_eof_done = false;
	private final int STRING = 3;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int SINGLE_LINE_COMMENT = 2;
	private final int yy_state_dtrans[] = {
		0,
		66,
		93,
		98
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NOT_ACCEPT,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NOT_ACCEPT,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NOT_ACCEPT,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NO_ANCHOR,
		/* 175 */ YY_NO_ANCHOR,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NO_ANCHOR,
		/* 178 */ YY_NO_ANCHOR,
		/* 179 */ YY_NO_ANCHOR,
		/* 180 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"7:9,62,5,62:2,6,7:4,62,7:7,62,7:5,62,7,59,7:5,1,3,2,27,33,4,31,28,58:10,35," +
"34,30,29,39,7,36,40,41,42,43,44,14,41,45,46,41:2,47,41,48,49,50,41,51,52,18" +
",53,54,55,41:3,7,60,7:2,56,7,10,61,8,21,12,26,57,16,13,57:2,9,57,15,20,22,5" +
"7,17,11,24,25,19,23,57:3,37,7,38,32,7,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,181,
"0,1,2,3,1,4,1,5,1,6,7,1:2,8,9,1:8,10,1:4,11,12,13,11,1:3,11:7,13,11:7,14,1:" +
"5,15,1:9,16,17,18,19,20,13,21,11,13:8,11,13:5,22,23,24,25,26,27,28,29,30,1," +
"31,32,33,18,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54," +
"55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79," +
"80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,11,13,1" +
"02,103,104,105,106,107,108,109,110")[0];

	private int yy_nxt[][] = unpackFromString(111,63,
"1,2,3,4,5,6,7,8,9,130,170:2,172,69,10,132,170:2,70,170,95,170,174,176,178,1" +
"70,99,11,12,13,14,15,16,17,18,19,20,21,22,8,171:2,173,171,175,171,96,131,13" +
"3,100,177,171:4,179,8,170,23,24,8,170,68,-1:65,25,-1:63,26,-1:63,27,-1:63,6" +
"7,68,-1:55,68,-1:8,170,180,134,170:16,-1:13,134,170:6,180,170:11,-1:2,170,-" +
"1:9,171:5,30,171:13,-1:13,171:6,30,171:12,-1:2,171,-1:40,32,-1:27,33,-1:24," +
"34,-1:91,23,-1:12,170:19,-1:13,170:19,-1:2,170,-1:9,170:8,158,170:10,-1:13," +
"170:5,158,170:13,-1:2,170,-1:9,171:19,-1:13,171:19,-1:2,171,-1:3,51,-1:65,9" +
"1,92,-1:55,92,1,50,88,97:2,6,7,97:55,101,-1:5,67,-1:63,68,-1:55,68,-1:8,170" +
":3,142,170:2,28,29,170:10,28,-1:13,170:8,29,170:3,142,170:6,-1:2,170,-1:9,1" +
"71:8,135,171:10,-1:13,171:5,135,171:13,-1:2,171,-1:9,171:8,157,171:10,-1:13" +
",171:5,157,171:13,-1:2,171,-1:4,52,-1:64,89,-1:58,58:4,59,-1,58:8,60,58:8,6" +
"1,58,62,58:32,63,64,65,58,-1:5,91,-1:63,92,-1:55,92,1,97:4,53,94,97:55,101," +
"-1:5,89,68,-1:55,68,-1:8,170:6,31,170:11,31,-1:13,170:19,-1:2,170,-1:9,171:" +
"3,145,171:2,71,72,171:10,71,-1:13,171:8,72,171:3,145,171:6,-1:2,171,-1,1,54" +
":4,55,56,54:52,57,90,54,92,-1:8,170:2,152,170:2,73,170:13,-1:13,152,170:5,7" +
"3,170:12,-1:2,170,-1:9,171:6,74,171:11,74,-1:13,171:19,-1:2,171,-1:9,170:10" +
",35,170:5,35,170:2,-1:13,170:19,-1:2,170,-1:9,171:10,75,171:5,75,171:2,-1:1" +
"3,171:19,-1:2,171,-1:9,170:15,36,170:3,-1:13,170:15,36,170:3,-1:2,170,-1:9," +
"171:15,76,171:3,-1:13,171:15,76,171:3,-1:2,171,-1:9,170:10,37,170:5,37,170:" +
"2,-1:13,170:19,-1:2,170,-1:9,171:10,77,171:5,77,171:2,-1:13,171:19,-1:2,171" +
",-1:9,170:4,38,170:14,-1:13,170:4,38,170:14,-1:2,170,-1:9,171:7,42,171:11,-" +
"1:13,171:8,42,171:10,-1:2,171,-1:9,170:14,39,170:4,-1:13,170:10,39,170:8,-1" +
":2,170,-1:9,171:4,78,171:14,-1:13,171:4,78,171:14,-1:2,171,-1:9,170:4,40,17" +
"0:14,-1:13,170:4,40,170:14,-1:2,170,-1:9,171:4,80,171:14,-1:13,171:4,80,171" +
":14,-1:2,171,-1:9,41,170:18,-1:13,170:2,41,170:16,-1:2,170,-1:9,81,171:18,-" +
"1:13,171:2,81,171:16,-1:2,171,-1:9,170,43,170:17,-1:13,170:7,43,170:11,-1:2" +
",170,-1:9,171:14,79,171:4,-1:13,171:10,79,171:8,-1:2,171,-1:9,170:7,82,170:" +
"11,-1:13,170:8,82,170:10,-1:2,170,-1:9,171,83,171:17,-1:13,171:7,83,171:11," +
"-1:2,171,-1:9,170:4,44,170:14,-1:13,170:4,44,170:14,-1:2,170,-1:9,171:3,84," +
"171:15,-1:13,171:12,84,171:6,-1:2,171,-1:9,170:3,45,170:15,-1:13,170:12,45," +
"170:6,-1:2,170,-1:9,171:4,85,171:14,-1:13,171:4,85,171:14,-1:2,171,-1:9,170" +
":4,46,170:14,-1:13,170:4,46,170:14,-1:2,170,-1:9,171:13,86,171:5,-1:13,171:" +
"3,86,171:15,-1:2,171,-1:9,170:4,47,170:14,-1:13,170:4,47,170:14,-1:2,170,-1" +
":9,171:3,87,171:15,-1:13,171:12,87,171:6,-1:2,171,-1:9,170:13,48,170:5,-1:1" +
"3,170:3,48,170:15,-1:2,170,-1:9,170:3,49,170:15,-1:13,170:12,49,170:6,-1:2," +
"170,-1:9,170:4,102,170:7,136,170:6,-1:13,170:4,102,170:4,136,170:9,-1:2,170" +
",-1:9,171:4,103,171:7,147,171:6,-1:13,171:4,103,171:4,147,171:9,-1:2,171,-1" +
":9,170:4,104,170:7,106,170:6,-1:13,170:4,104,170:4,106,170:9,-1:2,170,-1:9," +
"171:4,105,171:7,107,171:6,-1:13,171:4,105,171:4,107,171:9,-1:2,171,-1:9,170" +
":3,108,170:15,-1:13,170:12,108,170:6,-1:2,170,-1:9,171:4,109,171:14,-1:13,1" +
"71:4,109,171:14,-1:2,171,-1:9,170:12,110,170:6,-1:13,170:9,110,170:9,-1:2,1" +
"70,-1:9,171:2,153,171:16,-1:13,153,171:18,-1:2,171,-1:9,170:3,112,170:15,-1" +
":13,170:12,112,170:6,-1:2,170,-1:9,171:3,111,171:15,-1:13,171:12,111,171:6," +
"-1:2,171,-1:9,170:2,114,170:16,-1:13,114,170:18,-1:2,170,-1:9,171:3,113,171" +
":15,-1:13,171:12,113,171:6,-1:2,171,-1:9,170:11,156,170:7,-1:13,170:14,156," +
"170:4,-1:2,170,-1:9,171:2,115,171:16,-1:13,115,171:18,-1:2,171,-1:9,170:12," +
"116,170:6,-1:13,170:9,116,170:9,-1:2,170,-1:9,171:11,155,171:7,-1:13,171:14" +
",155,171:4,-1:2,171,-1:9,170:5,160,170:13,-1:13,170:6,160,170:12,-1:2,170,-" +
"1:9,171:12,117,171:6,-1:13,171:9,117,171:9,-1:2,171,-1:9,170:4,118,170:14,-" +
"1:13,170:4,118,170:14,-1:2,170,-1:9,171:12,119,171:6,-1:13,171:9,119,171:9," +
"-1:2,171,-1:9,170:17,120,170,-1:13,170:13,120,170:5,-1:2,170,-1:9,171:5,159" +
",171:13,-1:13,171:6,159,171:12,-1:2,171,-1:9,170,162,170:17,-1:13,170:7,162" +
",170:11,-1:2,170,-1:9,171:3,121,171:15,-1:13,171:12,121,171:6,-1:2,171,-1:9" +
",170:3,122,170:15,-1:13,170:12,122,170:6,-1:2,170,-1:9,171:12,161,171:6,-1:" +
"13,171:9,161,171:9,-1:2,171,-1:9,170:12,164,170:6,-1:13,170:9,164,170:9,-1:" +
"2,170,-1:9,171:4,163,171:14,-1:13,171:4,163,171:14,-1:2,171,-1:9,170:4,166," +
"170:14,-1:13,170:4,166,170:14,-1:2,170,-1:9,171,123,171:17,-1:13,171:7,123," +
"171:11,-1:2,171,-1:9,170,124,170:17,-1:13,170:7,124,170:11,-1:2,170,-1:9,17" +
"1:5,125,171:13,-1:13,171:6,125,171:12,-1:2,171,-1:9,170:3,126,170:15,-1:13," +
"170:12,126,170:6,-1:2,170,-1:9,171:9,165,171:9,-1:13,171:11,165,171:7,-1:2," +
"171,-1:9,170:5,128,170:13,-1:13,170:6,128,170:12,-1:2,170,-1:9,171:5,167,17" +
"1:13,-1:13,171:6,167,171:12,-1:2,171,-1:9,170:9,168,170:9,-1:13,170:11,168," +
"170:7,-1:2,170,-1:9,171:10,127,171:5,127,171:2,-1:13,171:19,-1:2,171,-1:9,1" +
"70:5,169,170:13,-1:13,170:6,169,170:12,-1:2,170,-1:9,170:10,129,170:5,129,1" +
"70:2,-1:13,170:19,-1:2,170,-1:9,170,138,170,140,170:15,-1:13,170:7,138,170:" +
"4,140,170:6,-1:2,170,-1:9,171,137,139,171:16,-1:13,139,171:6,137,171:11,-1:" +
"2,171,-1:9,170:12,144,170:6,-1:13,170:9,144,170:9,-1:2,170,-1:9,171,141,171" +
",143,171:15,-1:13,171:7,141,171:4,143,171:6,-1:2,171,-1:9,170:8,146,170:10," +
"-1:13,170:5,146,170:13,-1:2,170,-1:9,171:12,149,171:6,-1:13,171:9,149,171:9" +
",-1:2,171,-1:9,170:8,148,150,170:9,-1:13,170:5,148,170:5,150,170:7,-1:2,170" +
",-1:9,171:8,151,171:10,-1:13,171:5,151,171:13,-1:2,171,-1:9,170:2,154,170:1" +
"6,-1:13,154,170:18,-1:2,170,-1");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

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
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return new Symbol(LPAREN); }
					case -3:
						break;
					case 3:
						{ return new Symbol(MULT); }
					case -4:
						break;
					case 4:
						{ return new Symbol(RPAREN); }
					case -5:
						break;
					case 5:
						{ return new Symbol(MINUS); }
					case -6:
						break;
					case 6:
						{ curr_lineno++; }
					case -7:
						break;
					case 7:
						{ }
					case -8:
						break;
					case 8:
						{
    return new Symbol(ERROR, yytext());
}
					case -9:
						break;
					case 9:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -10:
						break;
					case 10:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -11:
						break;
					case 11:
						{ return new Symbol(PLUS); }
					case -12:
						break;
					case 12:
						{ return new Symbol(DIV); }
					case -13:
						break;
					case 13:
						{ return new Symbol(EQ); }
					case -14:
						break;
					case 14:
						{ return new Symbol(LT); }
					case -15:
						break;
					case 15:
						{ return new Symbol(DOT); }
					case -16:
						break;
					case 16:
						{ return new Symbol(NEG); }
					case -17:
						break;
					case 17:
						{ return new Symbol(COMMA); }
					case -18:
						break;
					case 18:
						{ return new Symbol(SEMI); }
					case -19:
						break;
					case 19:
						{ return new Symbol(COLON); }
					case -20:
						break;
					case 20:
						{ return new Symbol(AT); }
					case -21:
						break;
					case 21:
						{ return new Symbol(LBRACE); }
					case -22:
						break;
					case 22:
						{ return new Symbol(RBRACE); }
					case -23:
						break;
					case 23:
						{ return new Symbol(INT_CONST, addInt(yytext())); }
					case -24:
						break;
					case 24:
						{ yybegin(STRING); newString(); }
					case -25:
						break;
					case 25:
						{ nestedCommentCount = 1; yybegin(COMMENT); }
					case -26:
						break;
					case 26:
						{ return new Symbol(ERROR, "Unmatched *)"); }
					case -27:
						break;
					case 27:
						{ yybegin(SINGLE_LINE_COMMENT); }
					case -28:
						break;
					case 28:
						{ return new Symbol(IF); }
					case -29:
						break;
					case 29:
						{ return new Symbol(IN); }
					case -30:
						break;
					case 30:
						{ return new Symbol(FI); }
					case -31:
						break;
					case 31:
						{ return new Symbol(OF); }
					case -32:
						break;
					case 32:
						{ return new Symbol(DARROW); }
					case -33:
						break;
					case 33:
						{ return new Symbol(ASSIGN); }
					case -34:
						break;
					case 34:
						{ return new Symbol(LE); }
					case -35:
						break;
					case 35:
						{ return new Symbol(LET); }
					case -36:
						break;
					case 36:
						{ return new Symbol(NEW); }
					case -37:
						break;
					case 37:
						{ return new Symbol(NOT); }
					case -38:
						break;
					case 38:
						{ return new Symbol(CASE); }
					case -39:
						break;
					case 39:
						{ return new Symbol(LOOP); }
					case -40:
						break;
					case 40:
						{ return new Symbol(ELSE); }
					case -41:
						break;
					case 41:
						{ return new Symbol(ESAC); }
					case -42:
						break;
					case 42:
						{ return new Symbol(THEN); }
					case -43:
						break;
					case 43:
						{ return new Symbol(POOL); }
					case -44:
						break;
					case 44:
						{ return new Symbol(BOOL_CONST, Boolean.TRUE); }
					case -45:
						break;
					case 45:
						{ return new Symbol(CLASS); }
					case -46:
						break;
					case 46:
						{ return new Symbol(WHILE); }
					case -47:
						break;
					case 47:
						{ return new Symbol(BOOL_CONST, Boolean.FALSE); }
					case -48:
						break;
					case 48:
						{ return new Symbol(ISVOID); }
					case -49:
						break;
					case 49:
						{ return new Symbol(INHERITS); }
					case -50:
						break;
					case 50:
						{ }
					case -51:
						break;
					case 51:
						{ nestedCommentCount++; }
					case -52:
						break;
					case 52:
						{
    nestedCommentCount--;
    if (nestedCommentCount == 0)
        yybegin(YYINITIAL);
}
					case -53:
						break;
					case 53:
						{ curr_lineno++; yybegin(YYINITIAL); }
					case -54:
						break;
					case 54:
						{ appendString(yytext()); }
					case -55:
						break;
					case 55:
						{
    curr_lineno++;
    yybegin(YYINITIAL);
    return new Symbol(ERROR, "Unterminated string constant");
}
					case -56:
						break;
					case 56:
						{ appendString(yytext());}
					case -57:
						break;
					case 57:
						{
    yybegin(YYINITIAL);
    if( currentString().length() >= MAX_STR_CONST )
        return new Symbol(ERROR, "String constant too long");
    if( !currentString().contains("\0") )
        return new Symbol(STR_CONST, addString(currentString()));
    else
        return new Symbol(ERROR, "String contains null character");
}
					case -58:
						break;
					case 58:
						{ appendString(yytext().substring(1)); }
					case -59:
						break;
					case 59:
						{ curr_lineno++; appendString(yytext().substring(1)); }
					case -60:
						break;
					case 60:
						{ appendString("\n"); }
					case -61:
						break;
					case 61:
						{ appendString("\t"); }
					case -62:
						break;
					case 62:
						{ appendString("\f"); }
					case -63:
						break;
					case 63:
						{ appendString("\""); }
					case -64:
						break;
					case 64:
						{ appendString("\\"); }
					case -65:
						break;
					case 65:
						{ appendString("\b"); }
					case -66:
						break;
					case 67:
						{ curr_lineno++; }
					case -67:
						break;
					case 68:
						{ }
					case -68:
						break;
					case 69:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -69:
						break;
					case 70:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -70:
						break;
					case 71:
						{ return new Symbol(IF); }
					case -71:
						break;
					case 72:
						{ return new Symbol(IN); }
					case -72:
						break;
					case 73:
						{ return new Symbol(FI); }
					case -73:
						break;
					case 74:
						{ return new Symbol(OF); }
					case -74:
						break;
					case 75:
						{ return new Symbol(LET); }
					case -75:
						break;
					case 76:
						{ return new Symbol(NEW); }
					case -76:
						break;
					case 77:
						{ return new Symbol(NOT); }
					case -77:
						break;
					case 78:
						{ return new Symbol(CASE); }
					case -78:
						break;
					case 79:
						{ return new Symbol(LOOP); }
					case -79:
						break;
					case 80:
						{ return new Symbol(ELSE); }
					case -80:
						break;
					case 81:
						{ return new Symbol(ESAC); }
					case -81:
						break;
					case 82:
						{ return new Symbol(THEN); }
					case -82:
						break;
					case 83:
						{ return new Symbol(POOL); }
					case -83:
						break;
					case 84:
						{ return new Symbol(CLASS); }
					case -84:
						break;
					case 85:
						{ return new Symbol(WHILE); }
					case -85:
						break;
					case 86:
						{ return new Symbol(ISVOID); }
					case -86:
						break;
					case 87:
						{ return new Symbol(INHERITS); }
					case -87:
						break;
					case 88:
						{ }
					case -88:
						break;
					case 89:
						{ curr_lineno++; yybegin(YYINITIAL); }
					case -89:
						break;
					case 90:
						{ appendString(yytext()); }
					case -90:
						break;
					case 91:
						{
    curr_lineno++;
    yybegin(YYINITIAL);
    return new Symbol(ERROR, "Unterminated string constant");
}
					case -91:
						break;
					case 92:
						{ appendString(yytext());}
					case -92:
						break;
					case 94:
						{ }
					case -93:
						break;
					case 95:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -94:
						break;
					case 96:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -95:
						break;
					case 97:
						{ }
					case -96:
						break;
					case 99:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -97:
						break;
					case 100:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -98:
						break;
					case 101:
						{ }
					case -99:
						break;
					case 102:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -100:
						break;
					case 103:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -101:
						break;
					case 104:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -102:
						break;
					case 105:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -103:
						break;
					case 106:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -104:
						break;
					case 107:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -105:
						break;
					case 108:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -106:
						break;
					case 109:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -107:
						break;
					case 110:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -108:
						break;
					case 111:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -109:
						break;
					case 112:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -110:
						break;
					case 113:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -111:
						break;
					case 114:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -112:
						break;
					case 115:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -113:
						break;
					case 116:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -114:
						break;
					case 117:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -115:
						break;
					case 118:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -116:
						break;
					case 119:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -117:
						break;
					case 120:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -118:
						break;
					case 121:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -119:
						break;
					case 122:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -120:
						break;
					case 123:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -121:
						break;
					case 124:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -122:
						break;
					case 125:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -123:
						break;
					case 126:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -124:
						break;
					case 127:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -125:
						break;
					case 128:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -126:
						break;
					case 129:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -127:
						break;
					case 130:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -128:
						break;
					case 131:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -129:
						break;
					case 132:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -130:
						break;
					case 133:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -131:
						break;
					case 134:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -132:
						break;
					case 135:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -133:
						break;
					case 136:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -134:
						break;
					case 137:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -135:
						break;
					case 138:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -136:
						break;
					case 139:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -137:
						break;
					case 140:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -138:
						break;
					case 141:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -139:
						break;
					case 142:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -140:
						break;
					case 143:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -141:
						break;
					case 144:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -142:
						break;
					case 145:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -143:
						break;
					case 146:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -144:
						break;
					case 147:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -145:
						break;
					case 148:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -146:
						break;
					case 149:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -147:
						break;
					case 150:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -148:
						break;
					case 151:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -149:
						break;
					case 152:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -150:
						break;
					case 153:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -151:
						break;
					case 154:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -152:
						break;
					case 155:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -153:
						break;
					case 156:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -154:
						break;
					case 157:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -155:
						break;
					case 158:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -156:
						break;
					case 159:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -157:
						break;
					case 160:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -158:
						break;
					case 161:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -159:
						break;
					case 162:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -160:
						break;
					case 163:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -161:
						break;
					case 164:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -162:
						break;
					case 165:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -163:
						break;
					case 166:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -164:
						break;
					case 167:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -165:
						break;
					case 168:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -166:
						break;
					case 169:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -167:
						break;
					case 170:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -168:
						break;
					case 171:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -169:
						break;
					case 172:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -170:
						break;
					case 173:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -171:
						break;
					case 174:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -172:
						break;
					case 175:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -173:
						break;
					case 176:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -174:
						break;
					case 177:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -175:
						break;
					case 178:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -176:
						break;
					case 179:
						{ return new Symbol(TYPEID, addId(yytext())); }
					case -177:
						break;
					case 180:
						{ return new Symbol(OBJECTID, addId(yytext())); }
					case -178:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
