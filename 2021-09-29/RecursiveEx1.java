public class RecursiveEx1 {
	public static void anagram(String pre, String word) {
		if (word.length() <= 1) {
			System.out.println(pre + word);
		} else {
			anagram(pre+word.charAt(0), word.substring(1));

			for (int i = 1; i <= word.length() - 2; i++) {
				anagram(pre+word.charAt(i), word.substring(0, i)+word.substring(i+1));
			}

			anagram(pre+word.charAt(word.length()-1), word.substring(0, word.length()-1));
		}
	}

	static void anagram(String s) {
		anagram("", s);
	}

	public static void main(String[] args) {
		anagram("hi");
	}
}
