package ballester.gameworld.physics;

public class Dynamics {

    public Dynamics(Point vel, Point acc) {
	super();
	this.vel = new Point(vel);
	this.acc = new Point(acc);
    }

    public Dynamics() {
    }

    public Point vel = new Point(.0, .0), acc = new Point(.0, .0);
}
