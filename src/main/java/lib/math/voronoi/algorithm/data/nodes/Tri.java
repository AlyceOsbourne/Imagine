/*
 * Do what the F**k you want
 */

/*
 * Do what the F**k you want
 */

package lib.math.voronoi.algorithm.data.nodes;

import java.util.ArrayList;
import java.util.List;

public class Tri {
	public final Point a, b, c;
	List<Tri> subTris = new ArrayList<>();

	Tri(Point a, Point b, Point c) {

		this.a = a;
		this.b = b;
		this.c = c;

	}
}
