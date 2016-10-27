/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.gameworld.control;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.AgentWorld;
import ballester.gameworld.agent.characters.Princess;
import ballester.gameworld.physics.Geometry;
import ballester.gameworld.physics.HPoint;
import ballester.gameworld.physics.Point;

/**
 * @author hugoz
 *
 */
public class GameControllerTest {

    @Test
    public void TestOverlap() {
	GameController gc = new GameController(null, null, 0, 0, false);
	gc.world.overlapAllowed = 0.1;

	AgentWorld w = gc.world;
	Agent a1 = new Agent();
	Agent a2 = new Agent();
	w.createAgent(a1, true);
	w.createAgent(a2, true);
	a1.point.setXY(0, 0);
	a1.props.setSize(3, 4);

	a2.point.setXY(2.0, 0.0);
	a2.props.setSize(1, 1);

	assertTrue(Geometry.touches(a1, a2, 0.0));
	assertFalse(Geometry.touches(a1, a2, gc.world.overlapAllowed));

	gc.updateCollissions();
	assertEquals(0, gc.lastCollisions);

	a2.props.setSize(2, 1);
	gc.updateCollissions();
	assertEquals(1, gc.lastCollisions);
	assertPointsEquals(new HPoint(2.0, 0.0), a2.point); // didnt move
	assertPointsEquals(new HPoint(-0.4, 0.0), a1.point); // was pushed

    }

    public void assertPointsEquals(HPoint p1, HPoint p2) {
	assertTrue("Points are not the same: " + p1 + " <> " + p2, (new Point(p1)).minus(p2).length() < 0.0001);
    }

}
