


/*
 * Do what the F**k you want
 */

package lib.math.voronoi.datasubtypes;

import com.google.common.base.Objects;

public class Point {
	//simply an x and y location
	public int x, y;

	public Point nearestSeed;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Point point)) return false;
		return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(x, y);
	}

	public <P extends Point> int distance(P point){
		return distance(point.x, point.y);
	}
	private int distance(double x1, double y1) {
		double a = getX() - x1;
		double b = getY() - y1;
		return (int) Math.sqrt(a * a + b * b);
	}

	double getX() {
		return x;
	}

	double getY() {
		return y;
	}

	public double angle(Point p){
		return angle(p.x,p.y);
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
}
