package ballester.gameworld.physics;

import java.util.ArrayList;

/**
 * Point with history
 * 
 * @author hugoz
 *
 */
public class HPoint extends Point {
    ArrayList<Point> history = new ArrayList<Point>();

    public HPoint(double x, double y) {
	super(x, y);
    }

    public HPoint(Point point) {
	super(point);
    }

    public void remember() {
	history.add(new Point(this));
    }

}
