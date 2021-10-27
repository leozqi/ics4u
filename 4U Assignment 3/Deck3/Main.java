import java.util.*;

class Main {
	public static void main(String[] args) {
		long beforeUsedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		Deck d = new Deck();
		//System.out.println(Deck.toString(d));

		d.Deal(10);
		d.Deal(1);
		d.Deal(24);
		d.Deal(56);

		System.out.println(d.toString());
		System.out.println(Deck.toString(d));
		d.Deal(13);

		long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		System.out.println("" + (afterUsedMem - beforeUsedMem));
	}

}
