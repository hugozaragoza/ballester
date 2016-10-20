package ballester.gameworld.physics;

/**
 * 
 * All operators are in-place to minimize garbage collection etc.
 * 
 * @author hugoz
 *
 */
public class Point {
    double x = 0.0, y = 0.0;

    public Point(Point p) {
	this(p.x, p.y);
    }

    public Point(double x, double y) {
	this.x = x;
	this.y = y;
    }

    public Point() {
	this(0.0, 0.0);
    }

    @Override
    public String toString() {
	return String.format("(%3.1f,%3.1f)", x, y);
    }

    /**
     * Modifies this point by addind a
     * 
     * @param a
     * @return
     */
    public Point add(Point a) {
	x += a.x;
	y += a.y;
	return this;
    }

    public double distanceTo(Point b) {
	Point l = (new Point(b)).minus(this);
	return l.length();
    }

    public double length() {
	return Math.sqrt(x * x + y * y);
    }

    public Point norm() {
	double l = length();
	x /= l;
	y /= l;
	return this;
    }

    public Point scale(double l) {
	x *= l;
	y *= l;
	return this;
    }

    public Point minus(Point point) {
	this.x -= point.x;
	this.y -= point.y;
	return this;
    }

    public void setXY(double x, double y) {
	this.x = x;
	this.y = y;
    }

    /**
     * @return
     */
    public int getXInt() {
	return (int) x;
    }

    /**
     * @return
     */
    public int getYInt() {
	return (int) y;
    }

    /**
     * @return
     */
    public double getX() {
	return x;
    }

    public double getY() {
	return y;
    }

    /**
     * @param d
     * @param e
     */
    public void add(double x, double y) {
	this.x += x;
	this.y += y;
    }
}
