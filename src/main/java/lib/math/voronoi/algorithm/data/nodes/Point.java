/*
 * Do what the F**k you want
 */

/*
 * Do what the F**k you want
 */

package lib.math.voronoi.algorithm.data.nodes;

public class Point {
	public Point nearestSeed;

	public PointData getData() {
		return data;
	}

	public void setData(PointData data) {
		this.data = data;
	}

	public PointData data;

	//simply an x and y location
	public final int x;
	public final int y;
	public boolean isSeed;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point isSeed() {
		isSeed = true;
		return this;
	}

	public double distance(Point point) {
		return distance(point.x, point.y);
	}

	private double distance(int x1, int y1) {
		int a = getX() - x1;
		int b = getY() - y1;
		return ((a * a) + (b * b));
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

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		return result;
	}

	@Override
	public boolean equals(Object o) {
		return this == o || o instanceof Point point && x == point.x && y == point.y;

	}

	@Override
	public String toString() {
		if (this.data != null) return this.data.toString();
		else if (this.nearestSeed != null) return this.nearestSeed.printCoords();
		else return "null";
	}

	public String printCoords() {
		return "(" + getX() + "," + getY() + ")";
	}

	public boolean areEqual(Point p) {
		return (getX() == p.x && getY() == p.y);
	}

	public void setSeed() {
		this.isSeed = true;
		this.nearestSeed = this;
	}

	public static abstract class PointData {

	}
}
