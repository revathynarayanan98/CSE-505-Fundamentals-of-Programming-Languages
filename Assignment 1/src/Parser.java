// This file requires several changes

// program -> { function } end
class Program {
	public Program() {

		while (Lexer.nextToken == Token.KEY_INT) {

			SymTab.initialize(); // initialize for every function parsed
			ByteCode.initialize(); // initialize for every function parsed

			Function f = new Function();

			ByteCode.output(f.header);

			Interpreter.initialize(f.fname, SymTab.idptr - 1, f.p.npars, ByteCode.code, ByteCode.arg, ByteCode.codeptr);
		}
		FunTab.output();
	}
}

// function -> int id '(' [ pars ] ')' '{' body '}'
class Function {
	String fname; // name of the function
	Pars p;
	Body b;
	String header;

	public Function() {

		// Fill in code here
		// Must invoke: FunTab.add(fname);
		// Code ends with following two statements:

		// Skip int declaration
		Lexer.lex();

		fname = Lexer.ident;
		FunTab.add(fname);
		this.p = new Pars();
		this.b = new Body();

		this.header = "int " + fname + "(" + p.types + ");";
		return;
	}
}

// pars --> int id { ',' int id }
class Pars {

	String types = ""; // comma-separated sequence of types, e.g., int,int
	int npars = 0; // the number of parameters

	public Pars() {
		// Fill in code here
		// Must insert each id that is parsed
		// into the symbol table using:
		// SymTab.add(id)

		while (Lexer.nextToken != Token.RIGHT_PAREN) {
			if (Lexer.nextToken == Token.KEY_INT) {
				if (types.isEmpty())
					this.types = Lexer.ident;
				else
					this.types += "," + Lexer.ident;
				this.npars++;
			}

			Lexer.lex();
		}

	}
}

// body -> [ decls ] stmts
class Body {
	Decls d;
	Stmts s;

	public Body() {
		Lexer.lex();
		Lexer.lex();
		if (Lexer.nextToken == Token.KEY_INT)
			this.d = new Decls();
		this.s = new Stmts();
	}
}

// decls -> int idlist ';'
class Decls {
	Idlist il;

	public Decls() {
		// Fill in code here
		Lexer.lex();
		this.il = new Idlist();
	}
}

// idlist -> id { ',' id }
class Idlist {
	String id;
	Idlist il;

	public Idlist() {
		// Fill in code here
		// Must insert each id that is parsed
		// into the symbol table using:
		// SymTab.add(id);

		this.id = Lexer.ident;
		SymTab.add(this.id);
		Lexer.lex();
		if (Lexer.nextToken == Token.COMMA) {
			Lexer.lex();
			this.il = new Idlist();
		} else
			Lexer.lex();
	}
}

// stmts -> stmt [ stmts ]
class Stmts {
	Stmt s;
	Stmts ss;

	public Stmts() {
		s = new Stmt();
		if (Lexer.nextToken == Token.KEY_END)
			return;
		if (Lexer.nextToken == Token.ID || Lexer.nextToken == Token.LEFT_BRACE || Lexer.nextToken == Token.KEY_IF
				|| Lexer.nextToken == Token.KEY_WHILE) {
			ss = new Stmts();
		}
	}
}

// stmt -> assign ';' | loop | cond | cmpd | return ';' | print expr ';'
class Stmt {
	Stmt s;
	Assign a;
	Cond c;
	Loop l;
	Cmpd cm;

	public Stmt() {
		// Fill in code here
		switch (Lexer.nextToken) {
		case Token.ID: {
			a = new Assign();
			break;
		}
		case Token.KEY_IF: {
			c = new Cond();
			break;
		}
		case Token.KEY_WHILE: {
			l = new Loop();
			break;
		}
		case Token.LEFT_BRACE: {
			cm = new Cmpd();
			break;
		}
		default:
			break;
		}
	}

	public Stmt(int d) {
		// Leave the body empty.
		// This helps avoid infinite loop - why?
	}
}

// assign -> id '=' expr
class Assign extends Stmt {
	String id;
	Expr e;
	int i;

	public Assign() {
		super(0); // superclass initialization
		// Fill in code here.

		i = SymTab.index(Lexer.ident);
		if (i == -1)
			System.err.println("Identifier not in the table!");
		
		Lexer.lex();
		Lexer.lex();
		
		e = new Expr();
		if (i < 4) {
			ByteCode.gen("istore", i);
		} else {
			ByteCode.gen("istore", i);
			ByteCode.skip(1);
		}
		
		Lexer.lex();

		// End with this statement:
		// ByteCode.gen("istore", SymTab.index(id));
	}
}

// loop -> while '(' relexp ')' stmt
class Loop extends Stmt {
	Relexp b;
	Stmt c;

	public Loop() {
		super(0);
		Lexer.lex(); // skip over 'while'
		Lexer.lex(); // skip over '('
		int boolpoint = ByteCode.str_codeptr;
		b = new Relexp();
		Lexer.lex(); // skip over ')'
		int whilepoint = ByteCode.skip(3);
		c = new Stmt();
		ByteCode.gen_goto(boolpoint);
		ByteCode.skip(2);
		ByteCode.patch(whilepoint, ByteCode.str_codeptr);
	}
}

// cond -> if '(' relexp ')' stmt [ else stmt ]
class Cond extends Stmt {
	Relexp r;
	Stmt s1;
	Stmt s2;
	
	int n2, n3;

	public Cond() {
		super(0);
		// Fill in code here. Refer to
		// code in class Loop for guidance
		
		Lexer.lex();
		Lexer.lex();
		r = new Relexp();
		ByteCode.skip(2);
		n3 = Relexp.n1;
		Lexer.lex();
		s1 = new Stmt();
		if(Lexer.nextToken == Token.KEY_ELSE){
			Lexer.lex();
			n2 = ByteCode.str_codeptr;
			ByteCode.gen_goto(n2);
			ByteCode.skip(2);
			ByteCode.patch(n3, ByteCode.str_codeptr);
			s2 = new Stmt();
			ByteCode.patch(n2, ByteCode.str_codeptr);
		}
		else
			ByteCode.patch(n3, ByteCode.str_codeptr);
	}
}

// cmpd -> '{' stmts '}'
class Cmpd extends Stmt {
	Stmts s;

	public Cmpd() {
		super(0);
		// Fill in code here
	}
}

// return -> 'return' expr
class Return extends Stmt {
	Expr e;

	public Return() {
		super(0);
		// Fill in code here. End with:
		ByteCode.gen_return();
	}
}

// print -> 'print' expr
class Print extends Stmt {
	Expr e;

	public Print() {
		super(0);
		// Fill in code here. End with:
		ByteCode.gen_print();
	}
}

// relexp -> expr ('<' | '>' | '<=' | '>=' | '==' | '!= ') expr
class Relexp {
	Expr e1;
	Expr e2;
	String op = "";
	
	public static int n1;

	public Relexp() {
		// Fill in code here
	}
}

// expr -> term (+ | -) expr | term
class Expr {
	Term t;
	Expr e;
	char op;

	public Expr() {
		// Fill in code here
	}
}

// term -> factor (* | /) term | factor
class Term {
	Factor f;
	Term t;
	char op;

	public Term() {
		// Fill in code here
	}
}

// factor -> int_lit | id | '(' expr ')' | funcall
class Factor {
	int i;
	String id;
	Funcall fc;
	Expr e;

	public Factor() {
		// Fill in code here
	}
}

// funcall -> id '(' [ exprlist ] ')'
class Funcall {
	String id;
	ExprList el;

	public Funcall(String id) {
		this.id = id;
		Lexer.lex(); // (
		ByteCode.gen("aload", 0);
		el = new ExprList();
		Lexer.lex(); // skip over the )
		int funid = FunTab.index(id);
		ByteCode.gen_invoke(funid);
		ByteCode.skip(2);
	}
}

// exprlist -> expr [ , exprlist ]
class ExprList {
	Expr e;
	ExprList el;

	public ExprList() {
		// Fill in code here
	}
}
