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
		if (FunTab.index(fname) == -1)
			FunTab.add(fname);
		
		Lexer.lex();
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
	String id = "";

	public Pars() {
		// Fill in code here
		// Must insert each id that is parsed
		// into the symbol table using:
		// SymTab.add(id)

		while (Lexer.nextToken != Token.RIGHT_PAREN) {
			this.id = Lexer.ident;

			if (Lexer.nextToken == Token.KEY_INT) {
				if (types.isEmpty())
					this.types = this.id;
				else
					this.types += "," + this.id;
				this.npars++;
			} else if (Lexer.nextToken == Token.ID) {
				if (SymTab.index(this.id) == -1)
					SymTab.add(this.id);
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
		Lexer.lex();
		
		if (SymTab.index(this.id) == -1)
			SymTab.add(this.id);
		
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
				|| Lexer.nextToken == Token.KEY_WHILE || Lexer.nextToken == Token.KEY_RETURN
				|| Lexer.nextToken == Token.KEY_PRINT) {
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
	Return ret;
	Print print;

	public Stmt() {
		// Fill in code here
		switch (Lexer.nextToken) {
		case Token.ID:
			this.a = new Assign();
			break;
		case Token.KEY_IF:
			this.c = new Cond();
			break;
		case Token.KEY_WHILE:
			this.l = new Loop();
			break;
		case Token.LEFT_BRACE:
			this.cm = new Cmpd();
			break;
		case Token.KEY_RETURN:
			this.ret = new Return();
			break;
		case Token.KEY_PRINT:
			this.print = new Print();
			break;
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

	public Assign() {
		super(0); // superclass initialization
		// Fill in code here.

		this.id = Lexer.ident;

		Lexer.lex();
		Lexer.lex();

		e = new Expr();
		Lexer.lex();

		// End with this statement:
		if(Lexer.nextToken != Token.KEY_RETURN && Lexer.nextToken != Token.KEY_END)
			ByteCode.gen("istore", SymTab.index(this.id));
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

		if (Lexer.nextToken == Token.KEY_IF) {
			Lexer.lex();
			if (Lexer.nextToken == Token.LEFT_PAREN) {
				Lexer.lex();
				this.r = new Relexp();
				ByteCode.skip(2);
				n3 = Relexp.n1;
				Lexer.lex();
				s1 = new Stmt();
				if (Lexer.nextToken == Token.KEY_ELSE) {
					Lexer.lex();
					n2 = ByteCode.str_codeptr;
					ByteCode.gen_goto(n2);
					ByteCode.skip(2);
					ByteCode.patch(n3, ByteCode.str_codeptr);
					s2 = new Stmt();
					ByteCode.patch(n2, ByteCode.str_codeptr);
				} else
					ByteCode.patch(n3, ByteCode.str_codeptr);
			}
		}
	}
}

// cmpd -> '{' stmts '}'
class Cmpd extends Stmt {
	Stmts s;

	public Cmpd() {
		super(0);

		// Fill in code here
		Lexer.lex();
		s = new Stmts();

		// Scan over the right brace
		Lexer.lex();
	}
}

// return -> 'return' expr
class Return extends Stmt {
	Expr e;

	public Return() {
		super(0);
		// Fill in code here. End with:

		Lexer.lex();
		ByteCode.gen("iload", SymTab.index(Lexer.ident));

		// End with this statement:
		ByteCode.gen_return();
	}
}

// print -> 'print' expr
class Print extends Stmt {
	Expr e;

	public Print() {
		super(0);
		// Fill in code here.

		Lexer.lex();
		ByteCode.gen("iload", SymTab.index(Lexer.ident));

		// End with:
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

		this.e1 = new Expr();
		switch (Lexer.nextToken) {
		case Token.LESSER_OP:
			Lexer.lex();
			this.e2 = new Expr();
			this.n1 = ByteCode.str_codeptr;
			ByteCode.gen_if("<");
			break;
		case Token.GREATER_OP:
			Lexer.lex();
			this.e2 = new Expr();
			this.n1 = ByteCode.str_codeptr;
			ByteCode.gen_if(">");
			break;
		case Token.LESSEQ_OP:
			Lexer.lex();
			this.e2 = new Expr();
			this.n1 = ByteCode.str_codeptr;
			ByteCode.gen_if("<=");
			break;
		case Token.GREATEREQ_OP:
			Lexer.lex();
			this.e2 = new Expr();
			this.n1 = ByteCode.str_codeptr;
			ByteCode.gen_if(">=");
			break;
		case Token.NOT_EQ:
			Lexer.lex();
			this.e2 = new Expr();
			this.n1 = ByteCode.str_codeptr;
			ByteCode.gen_if("!=");
			break;
		case Token.EQ_OP:
			Lexer.lex();
			this.e2 = new Expr();
			this.n1 = ByteCode.str_codeptr;
			ByteCode.gen_if("==");
			break;
		}
	}
}

// expr -> term (+ | -) expr | term
class Expr {
	Term t;
	Expr e;
	char op;

	public Expr() {
		// Fill in code here

		t = new Term();
		if (Lexer.nextToken == Token.ADD_OP || Lexer.nextToken == Token.SUB_OP) {
			this.op = Lexer.nextChar;
			Lexer.lex(); // scan over op
			this.e = new Expr();
			ByteCode.gen(this.op); // generate the byte-code for op
		}
	}
}

// term -> factor (* | /) term | factor
class Term {
	Factor f;
	Term t;
	char op;

	public Term() {
		// Fill in code here

		f = new Factor();
		if (Lexer.nextToken == Token.MULT_OP || Lexer.nextToken == Token.DIV_OP) {
			this.op = Lexer.nextChar;
			Lexer.lex(); // scan over op
			this.t = new Term();
			ByteCode.gen(this.op);
		}
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
		switch (Lexer.nextToken) {
		case Token.INT_LIT: // number
			this.i = Lexer.intValue;
			Lexer.lex(); // scan over int

			if (this.i < 6 && this.i > -1)
				ByteCode.gen("iconst", this.i);
			else if (this.i < 128) {
				ByteCode.gen("bipush", this.i);
				ByteCode.skip(1);
			} else {
				ByteCode.gen("sipush ", this.i);
				ByteCode.skip(2);
			}
			break;
		case Token.LEFT_PAREN:
			Lexer.lex(); // scan over '('
			this.e = new Expr();
			Lexer.lex(); // scan over ')'
			break;
		case Token.ID:
			// check left paren
			this.id = Lexer.ident;
			this.i = SymTab.index(this.id);
			ByteCode.gen("iload", this.i);
			Lexer.lex();
			break;
		default:
			break;
		}
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
		this.el = new ExprList();
		Lexer.lex(); // skip over the )
		int funid = FunTab.index(this.id);
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
