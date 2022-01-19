// ------------------------------------------------------------------------- //
// Stores special collision boxes for item boxes, etc.                       //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2022-01-18                                                   //
// Finish date: 2022-01-18                                                   //
// ------------------------------------------------------------------------- //

package platformer;

import java.util.ArrayList;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

class SpecBounds {

	private ArrayList<Shape> bounds;

	/**
	 * Stores specific collision boxes for item boxes, etc.
	 *
	 * These collision boxes must be distinguished from ordinary collisions.
	 */
	public SpecBounds() {
		bounds = new ArrayList<Shape>();
	}


	public boolean intersects(Shape s) {
		for (int i = 0; i < bounds.size(); i++) {
			if (bounds.get(i).intersects(s.getBounds())) {
				return true;
			}
		}
		return false;
	} /* End method intersects */


	public boolean intersectRemove(Shape s) {
		int toRemoveCnt = 0;
		Shape[] toRemove = new Rectangle2D.Double[bounds.size()];
		boolean toggle = false;

		for (int i = 0; i < bounds.size(); i++) {
			Shape tmp = bounds.get(i);
			if (tmp.intersects(s.getBounds())) {
				toRemove[toRemoveCnt] = tmp;
				toRemoveCnt++;
				toggle = true;
			}
		}
		if (toggle) {
			for (int j = 0; j < toRemove.length; j++) {
				bounds.remove(toRemove[j]);
			}
		}
		return toggle;
	} /* End method intersectRemove */


	public void add(Shape r) {
		synchronized(bounds) {
			bounds.add((Shape)r);
		}
	} /* End method add */


	public void remove(int pos) {
		synchronized(bounds) {
			bounds.remove(pos);
		}
	} /* End method remove */

} /* End class SpecBounds */

