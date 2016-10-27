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
public class PhysicsTest {

    @Test
    public void testMoveTowardsTarget() {
	Physics phys = new Physics(100, 100);

	// (0,0) to (10,5) passing by target (7,3)
	Point p1 = new Point(0, 0);
	Point p2 = new Point(10, 5);
	Point t = new Point(7, 7 * p2.gradY());
	Dynamics d = new Dynamics(p2, new Point(0, 0));

	// move without target
	Point p = new Point(p1);
	phys.move(p, d, null);
	Assert.assertEquals(p2, p);

	// move and stop before target
	p = new Point(p1);
	phys.move(p, d, t);
	Assert.assertEquals(t, p);

	// move and stop in target
	p = new Point(p1);
	phys.move(p, d, t);
	Assert.assertEquals(t, p);

    }

    @Test
    public void testMoveTowardsTarget2() {
	Physics phys = new Physics(100, 100);

	// (0,0) to (10,5) passing by target (7,3)
	Point p1 = new Point(10, 0);
	Point p2 = new Point(1, 0);
	Dynamics d = new Dynamics(new Point(-5, 0), new Point(0, 0));

	// move to target
	Point p = new Point(p1);
	phys.move(p, d, p2);
	Assert.assertEquals(new Point(5, 0), p);
	phys.move(p, d, p2);
	Assert.assertEquals(new Point(1, 0), p);

    }

}
