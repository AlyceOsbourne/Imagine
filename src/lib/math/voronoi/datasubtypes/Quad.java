


/*
 * Do what the F**k you want
 */

package lib.math.voronoi.datasubtypes;

public class Quad {
	public Point ne, nw, se, sw;

	int width, height;

	public Quad(Point se, Point ne, Point sw, Point nw) {
		this.ne = ne;
		this.nw = nw;
		this.se = se;
		this.sw = sw;
		width = se.distance(sw);
		height = se.distance(ne);
	}





}

