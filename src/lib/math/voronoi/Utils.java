


/*
 * Do what the F**k you want
 */

package lib.math.voronoi;

import lib.math.voronoi.datasubtypes.Point;

public class Utils {
	static void getPerpendicularBisector(Point a, Point b) {
		//todo perpendicular bisector math
		int XA, XB, YA, YB, m, x1, y1, y, x;
		int rise, run;
		XA = a.x;
	XB = b.x;
	YA = a.y;
	YB = b.y;

	rise = difference(YA, YB); //the difference in the Y coordinate
	run = difference(XA, XB); //the difference in the X coordinate
	//slope is rise/run, but for the perpendicular bisector we need the negative reciprocal
	m = negativeReciprocal(rise, run); //slopes negative reciprocal
	x1 = average(XA, XB); //average midpoint x
	y1 = average(YA, YB); //average midpoint y

	//trying to solve (y - y1 = m(x - x1)) in the java sense

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

	public static Point midpoint(Point a, Point b) {
		return new Point(average(a.x, b.x), average(a.y, b.y)){};
	}

	private static int perpendicularSlope(int rise, int run) {
		return run / -rise;
	}


}



