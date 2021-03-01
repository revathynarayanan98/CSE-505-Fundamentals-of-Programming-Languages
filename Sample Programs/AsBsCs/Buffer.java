import java.util.Scanner;

public class Buffer {

	public String line = "";
	public int position = 0;
	private Scanner s;

	public Buffer() {
		s = new Scanner(System.in);
		line = s.nextLine() + ' ';  // add 1 more char
	}

	public char getChar() {
		position++;
		return line.charAt(position - 1);
	}

}
