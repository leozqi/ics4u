import java.util.*;

class Main {
	public static void main(String[] args) {
		Deck d = new Deck();
		//System.out.println(Deck.toString(d));
		d.shuffle();
		d.Deal();
	}
}
