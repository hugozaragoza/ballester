/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.gameworld.physics;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author hugoz
 *
 */
public class PoinTest {

    @Test
    public void testInside() {
	Point p1 = new Point(-1.0, +1.0);
	Point dim1 = new Point(2.0, 4.0);

	double d = 0.0;
	// Corners
	Assert.assertTrue((new Point(-2, 3)).insideOf(p1, dim1, d));
	Assert.assertTrue((new Point(-2, -1)).insideOf(p1, dim1, d));
	Assert.assertTrue((new Point(0, 3)).insideOf(p1, dim1, d));
	Assert.assertTrue((new Point(0, -1)).insideOf(p1, dim1, d));
	// Inside
	Assert.assertTrue((new Point(-.5, -.5)).insideOf(p1, dim1, d));
	Assert.assertTrue((new Point(-1.1, 1.1)).insideOf(p1, dim1, d));
	// Outside
	Assert.assertFalse((new Point(-2.5, 0.0)).insideOf(p1, dim1, d));
	Assert.assertFalse((new Point(1, 0.0)).insideOf(p1, dim1, d));
	Assert.assertFalse((new Point(-1.0, 4.0)).insideOf(p1, dim1, d));
	Assert.assertFalse((new Point(-1.0, -2.0)).insideOf(p1, dim1, d));

	d = 0.2;
	// Corners
	Assert.assertFalse((new Point(-2, 3)).insideOf(p1, dim1, d));
	Assert.assertFalse((new Point(-2, -1)).insideOf(p1, dim1, d));
	Assert.assertFalse((new Point(0, 3)).insideOf(p1, dim1, d));
	Assert.assertFalse((new Point(0, -1)).insideOf(p1, dim1, d));
	// Inside
	Assert.assertTrue((new Point(-.5, -.5)).insideOf(p1, dim1, d));
	Assert.assertTrue((new Point(-1.1, 1.1)).insideOf(p1, dim1, d));
	// Outside
	Assert.assertFalse((new Point(-2.5, 0.0)).insideOf(p1, dim1, d));
	Assert.assertFalse((new Point(1, 0.0)).insideOf(p1, dim1, d));
	Assert.assertFalse((new Point(-1.0, 4.0)).insideOf(p1, dim1, d));
	Assert.assertFalse((new Point(-1.0, -2.0)).insideOf(p1, dim1, d));

    }
}
