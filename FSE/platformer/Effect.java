// ------------------------------------------------------------------------- //
// The Effect class represents timed effects that may be applied to entities.//
// As of the submission date no effects have been implemented using this     //
// class. It is submitted for completeness.                                  //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Effect.java                                                     //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import java.util.concurrent.*;

abstract class Effect implements Delayed {

	private long startTime; // starting time of the effect

	/**
	 * Classes should implement their own Effect with this class.
	 *
	 * A delay is set through the constructor, while the abstract `apply`
	 * method should be overriden to apply the effect to an Entity. The
	 * actual "delay" is from use of the Java DelayQueue Collections object:
	 *
	 * <https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/DelayQueue.html>
	 *
	 * Delay-setting code, ie. how to implement the "Delayed" object type
	 * with the getDelay and compareTo methods, were taken almost verbatim
	 * from this blog post:
	 *
	 * <https://www.baeldung.com/java-delay-queue>
	 *
	 * Note that Ints.saturatedCast, a method from a Google library, is
	 * replaced with a Java standard library Math.toIntExact with a fallback
	 * returning zero (equal to other obj) if an error is thrown.
	 */
	public Effect(long delayMs) {
		this.startTime = System.currentTimeMillis() + delayMs;
	} /* End constructor */


	/**
	 * Overriden to implement the Delayed object.
	 *
	 * Returns milliseconds to delay finish.
	 */
	@Override
	public long getDelay(TimeUnit unit) {
		long diff = startTime - System.currentTimeMillis();
		return unit.convert(diff, TimeUnit.MILLISECONDS);
	} /* End method getDelay */


	/**
	 * Overriden to implement the Delayed object.
	 *
	 * Compares the difference in time to delay finish between two Effects.
	 */
	@Override
	public int compareTo(Delayed o) {
		try {
		return Math.toIntExact(
			this.startTime - ((Effect) o).startTime);
		} catch (ArithmeticException e) {
			return 0; 
		}
	} /* End method compareTo */

	/**
	 * Effects should implement this method to create their functionality.
	 */
	abstract void apply(Entity e);

} /* End class Effect */
