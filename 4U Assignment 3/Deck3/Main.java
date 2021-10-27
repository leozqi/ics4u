import java.util.*;

class Main {
	public static void main(String[] args) {
		long beforeUsedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		CardDeck d = new CardDeck();
		//System.out.println(CardDeck.toString(d));

		d.Deal(10);
		d.Deal(1);
		d.Deal(24);
		d.Deal(56);

		System.out.println(d.toString());
		System.out.println(CardDeck.toString(d));
		d.Deal(13);

		d.Deal();

		d.Deal(25, true);

		d.shuffle();

		d.Deal(true);

		//System.out.println(d.toString());

		long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		System.out.println("" + (afterUsedMem - beforeUsedMem));
	}

}
