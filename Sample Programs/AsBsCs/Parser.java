/*  Grammar:
 *  		S ->  As Bs  Cs
 *  		As -> a | a As
 *  		Bs -> b | b Bs
 *  		Cs -> c | c Cs
 *  
 *  		+ attributes and semantic checks for {a^n b^n b^n | n >= 0}
 *  
 * Note: Java does not have output parameters, hence synthesized
 * 		 attributes are made fields of classes.
 */

class SyntaxError extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SyntaxError(String s) {
		super(s);
	}
}

public class Parser {

	public static void main(String[] args) throws SyntaxError {
		
		System.out.println("Enter input, end with #:");

		Lexer.initialize();
		Lexer.lex();  
		
		new S();  
	}
}

class S { 				 // S -> As Bs Cs
	As  as;
	Bs  bs;
	Cs  cs;
	public S() throws SyntaxError {	 
	   as = new As();
	   bs = new Bs();
	   cs = new Cs();   
	   if (Lexer.nextToken == Token.TEND) {
		   if (as.n == bs.n &&  bs.n == cs.n) 
			    System.out.println("VALID");
		   else 
			    System.out.println("INVALID");
	   }
	   else
		   throw new SyntaxError("did not find #");
	}
}

class As {			// As ->  a | a As
	
	As as;			// Code below incorporates left factoring
	int n;          // Synthesized attribute n declared as a field
	
	public As() throws SyntaxError {
		if (Lexer.nextToken == Token.Ta) {
			Lexer.lex();
			n = 1;
		}
		else throw new SyntaxError("did not find a");
		
	    if (Lexer.nextToken == Token.Ta) {
				as = new As();
			    n = as.n + 1;
		}
	}
}

class Bs {         // Bs -> b | b Bs
	
	Bs bs;         // Code below incorporates left factoring
	int n;         // Synthesized attribute n declared as a field
	
	public Bs() throws SyntaxError {
		if (Lexer.nextToken == Token.Tb) {
			Lexer.lex();
			n = 1;
		}
		else throw new SyntaxError("did not find b");
		
	    if (Lexer.nextToken == Token.Tb) {
				bs = new Bs();
			    n = bs.n + 1;
	    }
   }
}

class Cs {       // Cs -> c | c Cs
	
	Cs cs;       // Code below incorporates left factoring
	int n;       // Synthesized attribute n declared as a field
	
	public Cs() throws SyntaxError {
		if (Lexer.nextToken == Token.Tc) {
			Lexer.lex();
			n = 1;
		}
		else throw new SyntaxError("did not find c");
		
	    if (Lexer.nextToken == Token.Tc) {
				cs = new Cs();
			    n = cs.n + 1;
		}
	}
}