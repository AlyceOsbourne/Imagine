/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package lib.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Voronoi {

	// ok, lets break down what makes an voronoi diagram

	//it needs bounds
	float width, height;

	//within those bounds we have sites
	ArrayList<Point> Sites;

	//these sites are the center of cells
	ArrayList<Cell> Cells;

	//each cell has bounding edges
	ArrayList<Edge> Edges;

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
		//initialize by adding input sites into list
		Sites = new ArrayList<>(Arrays.stream(p).toList());
		Edges = new ArrayList<>();
		Cells = new ArrayList<>();
		Vertices = new ArrayList<>();
	}

	//here we need to create 4 bounding cells for our "points of infinity"
	//these should be outside of the draw area to the N,E,S,W
	private void createBounds(List<Cell> c, Float width, Float height) {
		//now, to figure out the best placement for the bound cells, this include the point and edge locations
	}

	//placing sites into graph
	private void populateWithSites(ArrayList<Cell> c, ArrayList<Point> s) {
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
					Utils.getPerpendicularBisector(left, right);
				}
			}
		}
	}

	//this should be the companion to the Edges
	private void calculateIntersections(ArrayList<Cell> c, ArrayList<Vertex> v) {
		//we need to check each cells edges vs its intersecting edges to create verts.
		//replace edge points to verts linking everything in this pass
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
		Link a, b;

		Edge(Link a, Link b) {
			this.a = a;
			this.b = b;
		}
	}

	private static class Cell {
		List<Edge<? super Vertex>> edges;
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
	private static class Utils {
		private static Edge clipEdges(Edge in, Vertex a, Vertex b) {
			return new Edge<>(a, b);
		}

		private static void getPerpendicularBisector(Cell left, Cell right) {
			//todo perpendicular bisector math
			Point a = left.site;
			Point b = right.site;
			Point midpoint = midpoint(a, b);
		}

		private static Point midpoint(Point a, Point b) {
			float mX = average(a.x, b.x);
			float mY = average(a.y, b.y);
			return new Point(mX, mY);
		}

		private static float average(float a, float b) {
			return (a * b) / 2;
		}
	}
}
