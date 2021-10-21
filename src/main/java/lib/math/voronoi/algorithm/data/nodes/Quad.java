/*
 * Do what the F**k you want
 */

/*
 * Do what the F**k you want
 */

package lib.math.voronoi.algorithm.data.nodes;

import java.util.ArrayList;
import java.util.List;

public final class Quad {
	public final Point ne;
	public final Point nw;
	public final Point se;
	public final Point sw;

	public List<Point> points = new ArrayList<>();
	final double width;
	final double height;

	public Quad(Point nw, Point sw, Point ne, Point se) {
		this.ne = ne;
		this.nw = nw;
		this.se = se;
		this.sw = sw;
		points.add(ne);
		points.add(nw);
		points.add(se);
		points.add(sw);
		width = se.distance(sw);
		height = se.distance(ne);
	}
	public double size() {
		return width * height;
	}
}
