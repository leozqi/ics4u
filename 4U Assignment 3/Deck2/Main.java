import java.util.*;

class Main {
	public static void main(String[] args) {
		Deck d = new Deck();
		System.out.println(d.toString());
		d.shuffle();
		System.out.println(d.toString());

		System.out.println(Deck.toString(d));
	}
}
