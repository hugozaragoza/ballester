package ballester.gameworld.physics;

import org.apache.log4j.Logger;

import ballester.gameworld.agent.actions.Action;

public class Physics {
    final private static Logger logger = Logger.getLogger(Physics.class);
    private double width;
    private double height;
    boolean wrapWorld = false;

    public Physics(double width, double height) {
	this.width = width;
	this.height = height;
    }

    public void move(Point p, Dynamics d) {
	move(p, d, null);
    }

    public void move(Point p, Dynamics d, Point target) {
	Point before = null, after = null;
	d.vel.add(d.acc);

	if (target != null) {
	    before = (new Point(target)).minus(p).norm();
	}
	p.add(d.vel);
	if (target != null) {
	    after = (new Point(target)).minus(p).norm();
	    if (before.getX() * after.getX() < 0) { // changed sign
		p.setX(target.getX());
	    }
	    if (before.getY() * after.getY() < 0) { // changed sign
		p.setY(target.getY());
	    }
	}

	wrap(p);
    }

    private void wrap(Point p) {
	double x = p.getX();
	double y = p.getY();
	boolean update = false;
	while (x >= width) {
	    if (wrapWorld) {
		x -= width;
	    } else {
		x = width - Action.D_TOUCHING;
	    }
	    update = true;
	}
	while (x < 0) {
	    if (wrapWorld) {
		x += width;
	    } else {
		x = 0 + Action.D_TOUCHING;
	    }
	    update = true;
	}
	while (y >= height) {
	    if (wrapWorld) {
		y -= height;
	    } else {
		y = height - Action.D_TOUCHING;
	    }
	    update = true;
	}
	while (y < 0) {
	    if (wrapWorld) {
		y += height;
	    } else {
		y = 0 + Action.D_TOUCHING;
	    }
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
