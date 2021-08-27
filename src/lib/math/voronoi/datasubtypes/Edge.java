

/*
 * Do what the F**k you want
 */

/*
 * Do what the F**k you want
 */

package lib.math.voronoi.datasubtypes;

public class Edge {
	/* an edge is a tuple of points essentially, also holds neighbour info, again data held for map generation
	//during the initial pass this holds Points, which get replaced with verts, this may be a good idea, it may not, we'll see */
	Point a, b;

	public Edge(Point a, Point b) {
		this.a = a;
		this.b = b;
	}

	double length(){return a.distance(b);}

}
