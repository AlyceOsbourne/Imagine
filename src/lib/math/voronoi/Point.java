





/*
 * Do what the F**k you want
 */





/*
 * Do what the F**k you want
 */

package lib.math.voronoi;

public class Point {
	public Point nearestSeed;
	public PointData data;

	//simply an x and y location
	public int x;
	public int y;
	public boolean isSeed;

	public <DataHolder extends PointData> Point(int x, int y, DataHolder d) {
		new Point(x, y);
		this.data = d;
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public <D extends PointData> void propogateData(D dataIn) {
		this.data = dataIn;
	}


	public <P extends Point> double distance(P point) {
		return distance(point.x, point.y);
	}

	private double distance(int x1, int y1) {
		int a = getX() - x1;
		int b = getY() - y1;
		return Math.hypot(a, b);
	}

	int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	public double angle(Point p) {
		return angle(p.x, p.y);
	}

	public double angle(double x, double y) {
		final double ax = getX();
		final double ay = getY();

		final double delta = (ax * x + ay * y) / Math.sqrt(
				(ax * ax + ay * ay) * (x * x + y * y));

		if (delta > 1.0) {
			return 0.0;
		}
		if (delta < -1.0) {
			return 180.0;
		}

		return Math.toDegrees(Math.acos(delta));
	}

	public boolean areEqual(Point p) {
		return (getX() == p.x && getY() == p.y);
	}

	public String printCoords() {
		return "(" + getX() + "," + getY() + ")";
	}

	public void setSeed() {
		this.isSeed = true;
		this.nearestSeed = this;
	}

	@Override
	public String toString() {
		if (this.data != null) return this.data.toString();
		else if (this.nearestSeed != null) return this.nearestSeed.printCoords();
		else return "null";
	}

	public static abstract class PointData {

	}
}
