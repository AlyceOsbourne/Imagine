/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package lib.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static lib.math.Voronoi.MathUtils.getPerpendicularBisector;


//intentions of this class: to create a generified voronoi algorithm that can use different distances such as manhatten distances.
//I guess then end product should be an abstract class so that we can wrap it to utilize for various uses such as image creation, pathfinding on a 2d plane,
//to calculate distances from vectors etc
//note, if this only works for a single use case I have failed

//a few rules for this class, it should be able to run at as close to realtime as possible, even with points in the 10,000-100,000 range
//should be able to be dynamic, so if a new site is added it does not need to update the whole graph but the local neighbours that are adjacent
//now, while I do like the look of the beach line approach I am not sure if that is actually the best approach given the intentions of the class.
//now, I have done some study into the jump flood algorithm and it seems hugely efficient, and there are several approaches so I am sure I can
//achieve a high level of accuracy while maintaining performance.
//now, we are taking an abstract approach here, jump flooding was intended fo gpu applications,
//but when you really abstract it its just a data processor for a 2d/3d dataset,
//so we should be able to make use of it to create voronoi data that can be applied to things later.
//I have chosen this approach due o the efficient nature of the data clustering

public abstract class Voronoi {

	// ok, lets break down what makes an voronoi diagram

	//it needs bounds, these bounds represent the visible image area
	float width, height;

	//within those bounds we have sites
	LinkedList<? extends Point> Sites;

	//these sites are the center of cells
	ArrayList<Cell> Cells;

	//each cell has bounding edges
	ArrayList<Edge<? extends Point>> Edges;

	//and those edges meet in vertices
	ArrayList<Vertex> Vertices;

	//we initialize with the bounds and our given points
	Voronoi(Float width, Float height, Point... sitesIn) {
		//we create our empty lists
		init(sitesIn);
		//we create 4 cells to act as points of infinity, they should be placed outside of the image bounds
		createBounds(this.Cells, width, height);
		//then we insert sites into the graph
		populateWithSites(this.Cells, this.Sites);
		//we calculate the perpendicular bisectors then create a line that meets the bounds of the points of infinity
		calculateCellEdges(this.Cells);
		//we now calculate where each of these lines intersect and "shorten" to the vertices point
		calculateIntersections(this.Cells, this.Vertices);

		//now, I think we can actually do the drawing by drawing from vert to vert instead of using the Edges
		//I think this will help with pathing as we only ever have to pass the next points x,y instead of processing the two points each line has
	}

	private void init(Point... p) {
		//will have to pop an out of bounds check here
		//initialize by adding input sites into list
		Sites = new LinkedList<>(Arrays.stream(p).toList());
		Edges = new ArrayList<>();
		Cells = new ArrayList<>();
		Vertices = new ArrayList<>();
	}

	//here we need to create 4 bounding cells for our "points of infinity"
	//these should be outside of the draw area to the N,E,S,W
	private void createBounds(ArrayList<? extends Cell> c, Float width, Float height) {
		//now, to figure out the best placement for the bound cells, this include the point and edge locations
		//these should be placed say half the distance of the axis out of bounds? so if width is 1024 then the cell would be 512 units outside of the bounds

		//after consideration it may be better to directly calculate the verts here, as we know which each edge is for the bounds we can take the image corners on that side as our inner verts and then just *1.5 the coords to get a scaled corner
	}

	//placing sites into graph
	private void populateWithSites(ArrayList<Cell> c, LinkedList<? extends Point> s) {
		for (Point p : Sites) {
			c.add(new Cell(p));
		}
	}

	//calculating the Cells Edges, this compares site to every other site currently
	private void calculateCellEdges(ArrayList<Cell> c) {
		//this for loop is hugely inefficient, as we get C.length*C.length runs.
		//that's like a million calls for a thousand points
		//we need a way to find nearest neighbours efficiently,
		//each cell has a Site which has an X and Y coordinate.
		//in reality it'd be better to take three points to do a delaney triangulation maybe?
		for (Cell left : c) {
			for (Cell right : c) {
				if (left != right) {
					getPerpendicularBisector(left, right);
				}
			}
		}
	}

//this should calculate the intersections of the edges, adjust their length and replace their points with vertices
	private void calculateIntersections(ArrayList<Cell> c, ArrayList<Vertex> v) {
		//we need to check each cells edges vs its intersecting edges to create verts.
		//replace edge points to verts linking everything in this pass

		//I think I should do a run to see if edges have been intersected, if they have add those edges to the queue, then process,
		// I think this'll allow me to plop in new points and only process the neighbours
	}

	private static class Point {
		//simply an x and y location
		float x, y;

		Point(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}

	private static class Edge<Link extends Point> {
		//an edge is a tuple of points essentially, also holds neighbour info, again data held for map generation
		//during the initial pass this holds Points, which get replaced with verts, this may be a good idea, it may not, we'll see
		Link a, b;

		Edge(Link a, Link b) {
			this.a = a;
			this.b = b;
		}
	}

	private static class Cell {

		//a cells edges
		List<Edge<? super Vertex>> edges;

		//its site
		Point site;

		Cell(Point site) {
			this.site = site;
		}

		Cell addEdge(Edge<? super Vertex> edge) {
			edges.add(edge);
			return this;
		}

		Cell addEdges(Edge<? super Vertex>... edges) {
			this.edges.addAll(Arrays.stream(edges).toList());
			return this;
		}

		List<Edge<? super Vertex>> getCellEdges() {
			return this.edges;
		}
	}

	private static class Vertex extends Point {

		//vertex location

		//this datatype may get deleted, refactored or reworked, depends purely on efficiency

		//we have this list set up as such so when we iterate we can skip already constructed verts, maybe?
		List<? super Vertex> adjoiningVertices;

		//each vertex has neighbours, so we are soring that data so we can alter the verts later if needed
		//plus gives us some data for the actual map generation
		List<Cell> neighbours;

		//is true if there is more then 3 adjoining vertices
		boolean isDegenerate;

		Vertex(Point p, Cell[] neighbours, Point... adjoiningVertices) {
			super(p.x, p.y);
			this.adjoiningVertices = Arrays.stream(adjoiningVertices).toList();
			this.neighbours = Arrays.stream(neighbours).toList();
			isDegenerate = this.adjoiningVertices.size() > 3;
		}

		void addNeighbour(Cell neighbor) {
			neighbours.add(neighbor);
		}

		void removeNeighbour(Cell neighbor) {
			neighbours.remove(neighbor);
		}

		void AddAdjoiningVertex(Vertex vertex) {
			adjoiningVertices.add(vertex);
		}

		void removeAdjoiningVertex(Vertex vertex) {
			adjoiningVertices.remove(vertex);
		}

		List<Cell> getNeighbours() {
			return this.neighbours;
		}

		List<? super Vertex> getAdjoiningVertices() {
			return this.adjoiningVertices;
		}
	}

	// here we are storing some utility functions
	static class MathUtils {
		//mainly math utils, such as finding the average, finding the midpoint, and perp bisector, split up to make sure each math component is correct

		static void getPerpendicularBisector(Cell left, Cell right) {
			//todo perpendicular bisector math
		}

		private static Point midpoint(Point a, Point b) {
			float mX = average(a.x, b.x);
			float mY = average(a.y, b.y);
			return new Point(mX, mY);
		}
		private static float rise_run(float a, float b){return a-b;}
		private static float slope(float rise,float run){return rise/run;}
		private static float perpendicularSlope(float rise, float run){return run/-rise;}
		private static float average(float a, float b) {
			return (a * b) / 2;
		}
	}
}
