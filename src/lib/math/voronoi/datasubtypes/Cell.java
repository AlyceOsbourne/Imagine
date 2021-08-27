



/*
 * Do what the F**k you want
 */

package lib.math.voronoi.datasubtypes;

import java.util.Arrays;
import java.util.List;

public class Cell {

	//a cells edges
	List<Edge> edges;
	//its site
	public Point site;
	public Cell(Point site) {
		this.site = site;
	}

	void addEdge(Edge edge) {
		edges.add(edge);
	}

	public void addEdges(Edge... edges) {
		this.edges.addAll(Arrays.asList(edges));
	}

	List<Edge> getCellEdges() {
		return this.edges;
	}
}

