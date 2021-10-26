public class Basic3 {
	public static void main(String [] args)
	{
		// Formula is d = v_0 t + 0.5at^2
		double v0 = 0; // meters per second
		double t = 12; // seconds
		double a = 20; // meters per (second squared)
		double d = (v0 * t) + (0.5 * a * (t*t));
		System.out.println("The missile's displacement is " + d + "m");
	}
}
