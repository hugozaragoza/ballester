package ballester.gameworld.physics;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.Agent;
import ballester.gameworld.agent.actions.Eat;

public class Physics {
    final private static Logger logger = Logger.getLogger(Physics.class);
    private double width;
    private double height;

    public Physics(double width, double height) {
	this.width = width;
	this.height = height;
    }

    public void move(HPoint p, Dynamics d) {
	p.remember();
	move((Point) p, d);
    }

    public void move(Point p, Dynamics d) {
	d.vel.add(d.acc);
	p.add(d.vel);
	wrap(p);
    }

    private void wrap(Point p) {
	double x = p.getX();
	double y = p.getY();
	boolean update = false;
	while (x >= width) {
	    x -= width;
	    update = true;
	}
	while (x < 0) {
	    x += width;
	    update = true;
	}
	while (y >= height) {
	    y -= height;
	    update = true;
	}
	while (y < 0) {
	    y += height;
	    update = true;
	}
	if (update) {
	    p.setXY(x, y);
	}
    }

    // public void move(HPoint p, Dynamics d, double minSpeed) {
    // p.remember();
    // d.vel.add(d.acc);
    // Point v = new Point(d.vel);
    // if (v.length()<minSpeed) {
    // v.norm().scale(minSpeed);
    // }
    // p.add(v);
    // logger.trace("a:"+d.acc+", v:"+d.vel+" --> "+p);
    // }
    //

}
