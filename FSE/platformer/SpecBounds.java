// ------------------------------------------------------------------------- //
// The SpecBounds class stores individual bounding boxes for collision       //
// detection of individual areas, like item boxes.                           //
//                                                                           //
// All methods are by author unless otherwise stated in method header.       //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Game.java                                                       //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import java.util.ArrayList;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class SpecBounds {

	private ArrayList<Shape> bounds; // Store all boundaries in ArrayList

	/**
	 * Stores specific collision boxes for item boxes, etc.
	 *
	 * These collision boxes must be distinguished from ordinary collisions.
	 * Further, the game must be able to know where the collision box that
	 * has been hit is at.
	 */
	public SpecBounds() {
		bounds = new ArrayList<Shape>(); // Create storage space
	} /* End constructor */


	/**
	 * Check if shape intersects with any collision boxes stored.
	 *
	 * @param s shape to check.
	 */
	public boolean intersects(Shape s) {
		// Iterate through all stored collision boxes.
		for (int i = 0; i < bounds.size(); i++) {
			if (bounds.get(i).intersects(s.getBounds())) {
				return true; // Is hitting at least one box
			}
		}
		return false; // Not hitting any boxes
	} /* End method intersects */


	/**
	 * Check if shape intersects with any collision boxes stored.
	 *
	 * In addition, this method returns the first collision box it finds
	 * that is touching the input shape, and deletes it from storage.
	 *
	 * @param s shape to check.
	 * @return collision box touching shape, or null if none.
	 */
	public Shape intersectRemove(Shape s) {
		Shape toRemove = null;  // Store shape to be removed
		boolean toggle = false; // Store whether match has been found.

		// Iterate through all stored collision boxes.
		for (int i = 0; i < bounds.size(); i++) {
			Shape tmp = bounds.get(i); // Get collision box
			if (tmp.intersects(s.getBounds())) {
				toRemove = tmp;    // Box intersects with shape
				toggle = true;
			}
		}
		if (toggle && toRemove != null) {
			// Remove and return shape
			bounds.remove(toRemove);
			return toRemove;
		}
		return null; // No match, returns null.
	} /* End method intersectRemove */


	/**
	 * Check if point is contained within any collision boxes stored.
	 *
	 * The first collision box satisfying the condition is returned and
	 * deleted from within the SpecBounds. This is useful for item boxes
	 * that lose their effect after the first hit.
	 *
	 * @param x x coordinate of point
	 * @param y y coordinate of point
	 * @return Shape if point is contained, else null.
	 */
	public Shape containsRemove(double x, double y) {
		// Iterate over all collision boxes stored
		for (int i = 0; i < bounds.size(); i++) {
			if (bounds.get(i).contains(x, y)) {
				// Contains point: return and remove
				return (Shape) bounds.remove(i);
			}
		}
		return null; // Did not find any satisfying condition
	} /* End method intersectRemove */


	/**
	 * Add a shape bounding box.
	 *
	 * The shape will become one of the collision-boxes within the object.
	 *
	 * @param s shape to add.
	 */
	public void add(Shape s) {
		synchronized(bounds) {
			bounds.add((Shape) s);
		}
	} /* End method add */


	/**
	 * Remove a shape bounding box at a specific index.
	 *
	 * @param pos index within ArrayList of all collision boxes.
	 */
	public void remove(int pos) {
		synchronized(bounds) {
			bounds.remove(pos);
		}
	} /* End method remove */

} /* End class SpecBounds */
