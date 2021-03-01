/* 		OBJECT-ORIENTED PARSER FOR SIMPLE EXPRESSIONS
 * 
expr    -> term   (+ | -) expr | term

term    -> factor (* | /) term | factor

factor  -> int_lit | '(' expr ')'    
 
*/

public class Parser {
	public static void main(String[] args) {
		System.out.println("Enter an expression, end with semi-colon!\n");
		Lexer.lex();
		Expr e = new Expr();
		Code.output();
	}
}

class Expr   { // expr -> term (+ | -) expr | term
	Expr e;
    char op;
    Term t;
	public Expr() {
		t = new Term();
		if (Lexer.nextToken == Token.ADD_OP || Lexer.nextToken == Token.SUB_OP) {
			op = Lexer.nextChar;
			Lexer.lex();
			e = new Expr();
			Code.gen(Code.opcode(op));	 
		}
	}
}

class Term    { // term -> factor (* | /) term | factor
	char op;
	Factor f;
	Term t;
	public Term() {
		f = new Factor();
		if (Lexer.nextToken == Token.MULT_OP || Lexer.nextToken == Token.DIV_OP) {
			op = Lexer.nextChar;
			Lexer.lex();
			t = new Term();
			Code.gen(Code.opcode(op));
			}
	}
}

class Factor { // factor -> int_lit | '(' expr ')'
	Expr e;
	int i;

	public Factor() {
		switch (Lexer.nextToken) {
		case Token.INT_LIT:      // integer literal
			i = Lexer.intValue;
			Lexer.lex();
			Code.gen(Code.intcode(i));
			break;
		case Token.LEFT_PAREN:   // '('
			Lexer.lex();
			e = new Expr();
	        Lexer.lex(); // skip over ')'
			break;
		default:
		}
	}
}



class Code {
	static String[] code = new String[100];
	static int codeptr = 0;
	
	public static void gen(String s) {
		code[codeptr] = s;
		codeptr++;
	}
	
	public static String intcode(int i) {
		if (i > 127) return "sipush " + i;
		if (i > 5) return "bipush " + i;
		return "iconst_" + i;
	}
	
	public static String opcode(char op) {
		switch(op) {
		case '+' : return "iadd";
		case '-':  return "isub";
		case '*':  return "imul";
		case '/':  return "idiv";
		default: return "";
		}
	}
	
	public static void output() {
		for (int i=0; i<codeptr; i++)
			System.out.println(code[i]);
	}
}

