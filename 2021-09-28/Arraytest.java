import java.util.*;

public class Arraytest {
	public static void main(String[] args) {
		System.out.println("Hello, enter the numbers:")
		int[] store = new int[5]; // declares an array; creates one with 5 slots from 0-4

		Scanner stdin = new Scanner(System.in);

		for (int i = 0; i < store.length; i++) {
			store[i] = stdin.nextInt();
		}

		for (int j = 0; j < store.length; j++) {
			System.out.println(store[j]);
		}
		System.out.print(Arrays.toString(store));

		// The below syntax is exclusive to arrays
		for (int num : store) { // num points to each element in store
			System.out.print("" + num);
		}
	} // End method 'main'
} // End class 'Arrays'
