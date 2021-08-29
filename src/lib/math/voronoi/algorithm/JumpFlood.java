



/*
 * Do what the F**k you want
 */

package lib.math.voronoi.algorithm;

import lib.math.voronoi.Point;
import lib.math.voronoi.Voronoi;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
	intentions of this class:
	to create a generified voronoi algorithm that can use different distances such as manhatten distances.
	I guess then end product should be an abstract class so that we can wrap it to utilize for various uses
	such as image creation, pathfinding on a 2d plane,to calculate distances from vectors etc
	note, if this only works for a single use case I have failed

	a few rules for this class, it should be able to run at as close to realtime as possible,
	even with points in the 10,000-100,000 range
	should be able to be dynamic, so if a new site is added it does not need to update
	the whole graph but the local neighbours that are adjacent

	now, while I do like the look of the beach line approach I am not sure if that is actually the best approach given the intentions of the class.
	now, I have done some study into the jump flood algorithm and it seems hugely efficient, and there are several approaches so I am sure I can
	achieve a high level of accuracy while maintaining performance.

	now, we are taking an abstract approach here, jump flooding was intended fo gpu applications,
	but when you really abstract it its just a data processor for a 2d/3d dataset,
	so we should be able to make use of it to create voronoi data that can be applied to things later.
	I have chosen this approach due o the efficient nature of the data clustering
	 link to jump flood algorithm paper https://www.comp.nus.edu.sg/~tants/jfa/i3d06.pdf
	 link to video on voronoi data structures https://www.youtube.com/watch?v=

	 another promising approach is : https://www.youtube.com/watch?v=8mjUUNi1AaA&ab_channel=MichiganSpaceGrantConsortium
	 something to consider is splitting calculations into clusters via parallel streams the create the final image by concating said streams,
	 maybe using queues to add streams to process, this way we can do some clever subdividing of the data and get accurate data.
	*/
public class JumpFlood<Data extends Point> extends Voronoi {

	// ok, lets break down what makes an voronoi diagram

	//it needs bounds, these bounds represent the visible image area
	double width, height;

	//within those bounds we have sites
	LinkedList<Data> Sites;

	//these sites are the center of cells
	LinkedList<Cell> Cells;

	//each cell has bounding edges
	LinkedList<Edge> Edges;

	//we initialize with the bounds and our given points
	public JumpFlood(int width, int height, List<Data> sitesIn) {
		this.width = width;
		this.height = height;
		//we create our empty lists
		init(sitesIn);

		//we create 4 cells to act as points of infinity, they should be placed outside of the image bounds
		createBounds(this.Cells, width, height);

		//then we insert sites into the graph
		populateWithSites(this.Cells, this.Sites);

		//we calculate the perpendicular bisectors then create a line that meets the bounds of the points of infinity
		calculateCellEdges(this.Cells);

		//we now calculate where each of these lines intersect and "shorten" to the vertices point
		calculateIntersections(this.Cells);


		/*
		now, I think we can actually do the drawing by drawing from vert to vert instead of using the Edges
		I think this will help with pathing as we only ever have to pass the next points x,y instead of processing
		the two points each line has
		*/
	}

	private void init(List<Data> sitesIn) {


		/*
		will have to pop an out of bounds check here
		initialize by adding input sites into list
		*/
		Sites = new LinkedList<>(sitesIn);
		Edges = new LinkedList<>();
		Cells = new LinkedList<>();

	}

	//here we need to create 4 bounding cells for our "points of infinity"
	//these should be outside of the draw area to the N,E,S,W, this is cause
	// without bounds the math would be infinite thus the calculation would take infinite time
	private void createBounds(LinkedList<Cell> c, int width, int height) {
		/*
		now, to figure out the best placement for the bound cells,
		this includes the point and edge locations these should be placed
		say half the distance of the axis out of bounds? so if width is 1024
		then the cell would be 512 units outside of the bounds

		after consideration it may be better to directly calculate the verts here,
		as we know which each edge is for the bounds we can take the image corners
		on that side as our inner verts and then just *1.5 the coords to get a scaled corner
		*/

		//distance factor for math, should be able to scale by tweaking this
		int f = 2;

		int NX, NY, EX, EY, SX, SY, WX, WY;

		Cell N, E, S, W;

		Point nSite, eSite, sSite, wSite;

		Point iNW, iNE, iSE, iSW, oNW, oNE, oSE, oSW;

		Edge nRight, nLeft, nUp, nDown;
		Edge eRight, eLeft, eUp, eDown;
		Edge sRight, sLeft, sUp, sDown;
		Edge wRight, wLeft, wUp, wDown;


		//center point coords
		{
			NX = width / f;
			EY = height / f;
			SX = width / f;
			WY = height / f;

			//offsets
			SY = -(height * f) / 2;
			EX = -(width * f) / 2;
			WX = +(width * f) / 2;
			NY = +(height * f) / 2;
		}


		// coords for Cell Sites
		{
			nSite = new Point(NX, NY) {
			};
			eSite = new Point(EX, EY) {
			};
			sSite = new Point(SX, SY) {
			};
			wSite = new Point(WX, WY) {
			};
		}

		//SO MANY DEFINITIONS!!!  <-0->____<-0->

		//inner edge bounds
		{
			iNE = new Point(0, height) {
			};
			iNW = new Point(width, height) {
			};
			iSE = new Point(0, 0) {
			};
			iSW = new Point(width, 0) {
			};
		}

		//outer edge bounds
		{
			oNE = new Point(-(width / f), height / f) {
			};
			oNW = new Point(width / f, height / f) {
			};
			oSE = new Point(-(width / f), -height) {
			};
			oSW = new Point(width / f, -(height / f)) {
			};
		}

		//cell edges
		{
			nDown = new Edge(iNE, iNW) {
			};
			nUp = new Edge(oNE, oNW) {
			};
			nLeft = new Edge(iNE, oNE) {
			};
			nRight = new Edge(iNW, oNW) {
			};

			sDown = new Edge(oSE, oSW) {
			};
			sUp = new Edge(iSE, iSW) {
			};
			sLeft = new Edge(iSE, iSE) {
			};
			sRight = new Edge(iSW, oSW) {
			};

			eDown = new Edge(iSE, oSE) {
			};
			eUp = new Edge(iNE, oNE) {
			};
			eLeft = new Edge(oNE, oSE) {
			};
			eRight = new Edge(iSE, iNE) {
			};

			wDown = new Edge(iSW, oSW) {
			};
			wUp = new Edge(iNW, oNW) {
			};
			wLeft = new Edge(iNW, iSW) {
			};
			wRight = new Edge(oNW, oSW) {
			};
		}

		//add cells to list
		{
			N = new Cell(nSite) {
			};
			N.addEdges(nUp, nDown, nRight, nLeft);
			c.add(N);
			E = new Cell(eSite) {
			};
			E.addEdges(eUp, eDown, eRight, eLeft);
			c.add(E);
			S = new Cell(sSite) {
			};
			S.addEdges(sUp, sDown, sRight, sLeft);
			Cells.add(S);
			W = new Cell(wSite) {
			};
			W.addEdges(wUp, wDown, wRight, wLeft);
			c.add(W);
		}
	}

	//placing sites into graph
	private void populateWithSites(LinkedList<Cell> c, LinkedList<? extends Point> s) {
		for (Point p : s) {
			c.add(new Cell(p) {
			});
		}
	}

	//calculating the Cells Edges, this compares site to every other site currently
	private void calculateCellEdges(LinkedList<? extends Cell> c) {

	}

	//this should calculate the intersections of the edges, adjust their length and replace their points with vertices
	private void calculateIntersections(LinkedList<? extends Cell> c) {
		/*
		we need to check each cells edges vs its intersecting edges to create verts.
		replace edge points to verts linking everything in this pass

		I think I should do a run to see if edges have been intersected, if they have add those edges to the queue, then process,
		I think this'll allow me to plop in new points and only process the neighbours
		*/
	}

	public static class Cell {

		//its site
		public Point site;
		//a cells edges
		List<Edge> edges;

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

	public static class Edge {
		/* an edge is a tuple of points essentially, also holds neighbour info, again data held for map generation
		//during the initial pass this holds Points, which get replaced with verts, this may be a good idea, it may not, we'll see */
		Point a, b;

		public Edge(Point a, Point b) {
			this.a = a;
			this.b = b;
		}

		double length() {
			return a.distance(b);
		}

	}


}
