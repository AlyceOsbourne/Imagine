



/*
 * Do what the F**k you want
 */

package lib.math.voronoi;

public class Utils {
	static void getPerpendicularBisector(Point a, Point b) {
		//todo perpendicular bisector math
		int XA, XB, YA, YB;
		int rise, run;
		XA = a.x;
		XB = b.x;
		YA = a.y;
		YB = b.y;

		rise = difference(YA, YB); //the difference in the Y coordinate
		run = difference(XA, XB); //the difference in the X coordinate
	}

	private static int difference(int a, int b) {
		return a - b;
	}

	private static int negativeReciprocal(int a, int b) {
		return -1 / (a / b);
	}

	private static int average(int a, int b) {
		return (a + b) / 2;
	}

	public static Point midpoint(Point a, Point b, Point[][] matrix) {
		return matrix[average(a.x, b.x)][average(a.y, b.y)];
	}

	private static int perpendicularSlope(int rise, int run) {
		return run / -rise;
	}


}



