



/*
 * Do what the F**k you want
 */

package lib.math.voronoi.datasubtypes;

public class Quad {
	public Point ne, nw, se, sw;

	double width, height;

	public Quad(Point nw, Point sw, Point ne, Point se) {
		this.ne = ne;
		this.nw = nw;
		this.se = se;
		this.sw = sw;
		width = se.distance(sw);
		height = se.distance(ne);
	}


	public double size() {
		return width * height;
	}
}

