import java.lang.Math;

public class BinaryConverter {
	public static void main(String[] args) {
		System.out.println(decToBin(5001));
	}

	static long decToBin(long n) {
		if (n == 0) {
			return 0;
		}
		return (n % 2 + 10 * decToBin(n / 2));
	}
}
