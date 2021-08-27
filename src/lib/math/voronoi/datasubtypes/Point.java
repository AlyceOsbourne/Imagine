





/*
 * Do what the F**k you want
 */

package lib.math.voronoi.datasubtypes;

import java.util.Random;

public class Point {
	//simply an x and y location
	public int x, y;

	public Point nearestSeed;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point randomize(int width, int height) {
		Random r = new Random();
		this.x = r.nextInt(width - 1);
		this.y = r.nextInt(height - 1);
		return this;
	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		result = 31 * result + (nearestSeed != null ? nearestSeed.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Point point)) return false;
		if (x != point.x || y != point.y) return false;
		return ((nearestSeed != null) && nearestSeed.equals(point.nearestSeed)) || ((nearestSeed == null) && (point.nearestSeed == null));
	}

	public <P extends Point> int distance(P point) {
		return distance(point.x, point.y);
	}

	private int distance(int x1, int y1) {
		int a = getX() - x1;
		int b = getY() - y1;
		return (int) Math.sqrt(a * a + b * b);
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
}
