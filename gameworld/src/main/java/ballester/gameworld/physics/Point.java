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

    public void add(double d) {
	add(d, d);
    }

    public double distanceTo(Point b) {
	Point l = (new Point(b)).minus(this);
	return l.length();
    }

    public double length() {
	return Math.sqrt(x * x + y * y);
    }

    /**
     * normalizes in-place so length is now 1
     */
    public Point norm() {
	double l = length();
	x /= l;
	y /= l;
	return this;
    }

    /**
     * scales in-place
     * 
     * @param l
     * @return
     */
    public Point scale(double l) {
	x *= l;
	y *= l;
	return this;
    }

    /**
     * substracts in-place and returns
     * 
     * @param point
     * @return
     */
    public Point minus(Point point) {
	this.x -= point.x;
	this.y -= point.y;
	return this;
    }

    public void setXY(double x, double y) {
	this.x = x;
	this.y = y;
    }

    public void setX(double x) {
	this.x = x;
    }

    public void setY(double y) {
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

    @Override
    public boolean equals(Object o) {
	if (!(o instanceof Point)) {
	    return false;
	}
	Point p = (Point) o;
	return p.getX() == this.getX() && p.getY() == this.getY();
    }

    /**
     * computes if this point is inside a rectangle centered around at p1 with
     * dim.
     * 
     * @param p1
     * @param dim
     * @param allowOverlapBandWidth
     *            : when >0 point is not considered insdide if it is within
     *            allowOverlapBandWidth of the border. when <0 then is
     *            considered within even if it is outside but only
     *            allowOverlapBandWidth around border
     * @return
     */
    public boolean insideOf(Point p1, Point dim, double allowOverlapBandWidth) {
	Point topleft = new Point(p1);
	topleft.add(new Point(-dim.getX() / 2.0, dim.getY() / 2.0));
	Point rel = new Point(this);
	rel.minus(topleft);
	// since // rectangle // is // centered
	return (rel.getX() >= allowOverlapBandWidth && rel.getX() <= (dim.getX() - allowOverlapBandWidth))
		&& (rel.getY() <= -allowOverlapBandWidth && rel.getY() >= (-dim.getY() + allowOverlapBandWidth));
    }

    /**
     * y/x
     * 
     * @return
     */
    public double gradY() {
	return getY() / getX();
    }

}
