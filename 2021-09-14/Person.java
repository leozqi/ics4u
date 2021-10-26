public class Person {
	public static void main(String [] args) {
		String name = "Joseph";
		int age = 23;
		char gender = 'm';
		boolean uni_acceptance = false;

		if (uni_acceptance) {
			System.out.println("Hi I'm" + name
				+ ", of the " + gender
				+ " gender, age " + age
				+ "and I was accepted to UNI");
		} else {
			System.out.println("Hi I'm" + name
				+ ", of the " + gender
				+ " gender, age " + age
				+ " and I was not accepted to UNI");
		}
	}
}
