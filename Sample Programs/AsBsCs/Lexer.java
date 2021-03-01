import java.util.Scanner;

public class Lexer {

	static private char ch;
	static private  Buffer buffer;
	
	static public int nextToken = -1;

	public static void initialize() {
		buffer = new Buffer();
		ch = buffer.getChar();
	}

	public static int lex() {
		switch (ch) {
		case 'a':		
				nextToken = Token.Ta;
				break;
		case 'b':		
				nextToken = Token.Tb;
				break;
		case 'c':		
				nextToken = Token.Tc;
				break;	
		case '#':		
				nextToken = Token.TEND;
				break;
		}
		ch = buffer.getChar();
		return nextToken;
	} // lex

}
