import java.util.*;

class Main {
	public static void main(String[] args) {
		long beforeUsedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();


		Tamagotchi Bob = new Tamagotchi("Bob");
		System.out.println(Bob.toString());
		for(int i=0;i<1297;i++) {
		Bob.age();
		//
		if (i%2==0) {
		Bob.play();
		Bob.scold();
//		Bob.feed();
		}

		}
		System.out.println(Bob.toString());


		long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		System.out.println("" + (afterUsedMem - beforeUsedMem));
	}

}
