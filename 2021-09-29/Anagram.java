public class Anagram {
	public static void main(String[] args) {
		anagram("word");
	}

	static void anagram(String s) {
		anagram("", s);
	}

	static void anagram(String f, String s) {
		System.out.println(s);
	}
}
