/**
 * Ballester Language Game Platform
 * 
 * Hugo Zaragoza (http://www.hugo-zaragoza.net) 
 *
 */
package ballester.gameworld.physics;

import ballester.gameworld.agent.Agent;

import javafx.geometry.BoundingBox;

/**
 * This class isolates code form actual geometry package used, JavaFX for now.
 * 
 * @author hugoz
 *
 */
public class Geometry {

    /**
     * @param agent
     * @param aa
     * @param overlapBandWitdh
     * @return
     */
    public static boolean touches(Agent agent, Agent aa, double overlapBandWitdh) {
	BoundingBox b1 = bounginxBox(agent, -overlapBandWitdh);
	BoundingBox b2 = bounginxBox(aa, -overlapBandWitdh);
	return (b1.intersects(b2) || b1.contains(b2) || b2.contains(b1));
    }

    /**
     * @param agent
     * @return
     */
    public static BoundingBox bounginxBox(Agent agent, double band) {
	return new BoundingBox(//
		agent.point.getX() - agent.props.getSize().getX() / 2 - band, //
		agent.point.getY() - agent.props.getSize().getY() / 2 - band, //
		agent.props.getSize().getX() + 2 * band, //
		agent.props.getSize().getY() + 2 * band //
	);
    }

    public static void pushOutOfCollision(Agent a, Agent aa, double overlapBandWitdh, boolean oneDimensional) {
	double band = overlapBandWitdh / 2;
	BoundingBox b1 = new BoundingBox(a.point.getX(), a.point.getY(), a.props.getSize().getX() - band,
		a.props.getSize().getY() - band);
	BoundingBox b2 = new BoundingBox(aa.point.getX(), aa.point.getY() - band, aa.props.getSize().getX() - band,
		aa.props.getSize().getY());

	boolean onRight = a.point.getX() > aa.point.getX();
	boolean onTop = a.point.getY() > aa.point.getY();
	double s1 = a.props.getSize().getX() / 2.0 + aa.props.getSize().getX() / 2.0 - overlapBandWitdh;
	double s2 = a.props.getSize().getY() / 2.0 + aa.props.getSize().getY() / 2.0 - overlapBandWitdh;
	if (onRight) {
	    a.point.setX(aa.point.getX() + s1);
	} else {
	    a.point.setX(aa.point.getX() - s1);
	}
	oneDimensional = true; // TODO push only sideways for now
	if (!oneDimensional) {
	    if (onTop) {
		a.point.setY(aa.point.getY() + s2);
	    } else {
		a.point.setY(aa.point.getY() - s2);
	    }
	}
    }
}
