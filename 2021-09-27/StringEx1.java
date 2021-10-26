/*
 * StringEx1.java
 *
 * By Leo Qi: 2021-09-27
 *
 * Gets rid of all four letter words.
 */

import java.util.*;

public class StringEx1 {
	/* Description */
	static final String CLASS_NAME = "StringEx1";
	static final String AUTHOR = "Leo Qi";
	static final String DATE_CREATED = "2021-09-27";
	static final String DESCRIPTION = "Gets rid of all four letter words in input.";
	static final String INSTRUCTIONS = """
		1. Enter a sentence to parse.
		2. Press enter to submit.
		3. Any four letter \"swear words\" will be removed and the finished
		   message will be printed back out, surrounded by "<>".
		""";

	/* Prints introductory message */
	static void introduction() {
		System.out.flush();
		System.out.println("\n--------------------------------------------------------------------------------");
		System.out.println(CLASS_NAME + ".java\n");
		System.out.println("By: " + AUTHOR + "\tDate: " + DATE_CREATED + "\n");
		System.out.println(DESCRIPTION + "\n");
		System.out.println("Instructions: ");
		System.out.println(INSTRUCTIONS);
		System.out.println("--------------------------------------------------------------------------------\n");
		System.out.flush();
	}

	static int lengthNoChars(String in) {
		return in.replaceAll("\\p{Punct}+", "").length();
	}

	/*
	 * Method main:
	 *
	 * Takes no arguments as parameters, returns void.
	 *
	 */
	public static void main(String[] args) {
		introduction();

		Scanner stdin = new Scanner(System.in);
		String input = stdin.nextLine();
		String output = "";
		String temp = "";

		char c;

		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			if (c == ' ') {
				if (lengthNoChars(temp) != 4) {
					output += (temp + " ");
				}
				temp = "";
			} else {
				temp += ("" + c);
			}
		}
		if (temp.length() != 4) {
			output += (temp);
		}
		stdin.close();

		System.out.println("Your new sentence is:\n<" + output + ">\n");
	}
}
