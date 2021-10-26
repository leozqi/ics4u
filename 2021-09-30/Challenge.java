import java.util.*;
import java.lang.Math;

public class Challenge {
	public static void main(String[] args) {
		Scanner stdin = new Scanner(System.in);

		System.out.print("Enter how many names: ");
		int nameAmt = stdin.nextInt();

		System.out.print("Enter how many winners: ");
		int winAmt = stdin.nextInt();

		if (winAmt > nameAmt) {
			System.out.println("You can't have more winners than names!");
			return;
		}

		ArrayList<String> names = new ArrayList<String>();

		for (int i = 0; i < nameAmt; i++) {
			System.out.print("Enter the names: ");
			names.add(stdin.next());
		}
		stdin.close();

		ArrayList<String> winners = pickWinners(names, winAmt);

		System.out.println("\nThe winner(s) are: ");
		for (int i = 0; i < winners.size(); i++) {
			System.out.println("* " + winners.get(i));
		}
	} // End main

	static ArrayList<String> pickWinners(ArrayList<String> names, int winAmt) {
		ArrayList<String> winners = new ArrayList<String>();

		Random rand = new Random();

		int randElementPos = 0;

		for (int i = 0; i < winAmt; i++) {
			randElementPos = rand.nextInt(names.size());

			winners.add(names.get(randElementPos));

			names.remove(randElementPos);
		}

		return winners;
	} // End pickWinners
}
