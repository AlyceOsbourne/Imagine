

/*
 * Do what the F**k you want
 */

/*
 * Do what the F**k you want
 */

/* Based on and owned by : https://www.youtube.com/watch?v=8mjUUNi1AaA&ab_channel=MichiganSpaceGrantConsortium*/

package lib.math.voronoi.algorithm;

import javafx.beans.NamedArg;
import lib.math.voronoi.Utils;
import lib.math.voronoi.Voronoi;
import lib.math.voronoi.datasubtypes.Point;
import lib.math.voronoi.datasubtypes.Quad;

import java.util.LinkedList;
import java.util.List;

/**
 * The type Calculate by sub division.
 */
public class CalculateBySubDivision extends Voronoi {
	/**
	 * The Voronoi matrix.
	 */

	Point[][] voronoiMatrix;

	/**
	 * The inputted sites to process.
	 */
	List<Point> sites = new LinkedList<>();

	/**
	 * initiates calculating a voronoi diagram by a subdivision algorithm.
	 * checks sites to make sure they are in bounds, discards those outside and then adds the remaining to the matrix
	 * plus stores sites in a list for faster lookup
	 */
	public CalculateBySubDivision(@NamedArg("Image Width") int width, @NamedArg("Image Height") int height, @NamedArg("List of sites") List<Point> sitesIn) {
		//might have to minus width and height by 1 to account for the fact that array starts at 0
		voronoiMatrix = new Point[width][height];
		System.out.println("Matrix contains " + (width * height) + " points and " + sitesIn.size() + " sites.");
		for (Point site : sitesIn) {
			if (site.x < width - 1 && site.y < height - 1 && site.x > 0 && site.y > 0) {
				sites.add(site);
				voronoiMatrix[site.x][site.y] = site;
				System.out.println(site + " added to matrix");
			} else System.out.println(site + " out of bounds, discarded.");
		}
		System.out.println("Running " + this + ".");
		start(voronoiMatrix, sites);
	}


	/**
	 * starts the algorithm, creates a starting Quad with the bounds of the image,
	 * passes said quad to #checkAndSubdivide
	 */
	private void start(@NamedArg("Matrix") Point[][] voronoiMatrix, @NamedArg("List of sites to be processed") List<? extends Point> sites) {
		int xMax, yMax, xMin, yMin;
		yMin = 0;
		xMin = 0;
		xMax = voronoiMatrix.length - 1;
		yMax = voronoiMatrix[0].length - 1;
		Quad startingQuad = new Quad(voronoiMatrix[xMax][xMin], voronoiMatrix[xMax][yMax], voronoiMatrix[xMin][yMin], voronoiMatrix[xMin][yMax]);
		checkAndSubdivide(startingQuad, sites);
	}

	/**
	 * checks to see if quad corners closest sites are the same, if they are not the subdivide the quad to 4
	 * new quads and pass each back to this method, this in theory should iterate through each quad until each
	 * point is assigned a nearest Site.
	 **/
	private void checkAndSubdivide(Quad quad, List<? extends Point> sites) {
		if (!checkQuad(quad, sites, voronoiMatrix)) {
			List<Quad> subdivision = subDivideQuad(quad);
			for (Quad sub : subdivision) {
				checkAndSubdivide(sub, sites);
			}
		}
	}


	/**
	 * checks quad corners vs sites, if quad corners sites are all equal assigns all
	 * points in matrix to that site and returns true, otherwise fails and returns false
	 **/
	private boolean checkQuad(@NamedArg("Quad to be checked") Quad quad, @NamedArg("List of Sites to compare against") List<? extends Point> sites, @NamedArg("Matrix") Point[][] voronoiMatrix) {
		//collect nearest site for each corner of quad
		int xStart = quad.sw.x;
		int xFinish = quad.se.x;
		int yStart = quad.sw.y;
		int yFinish = quad.nw.y;

		// this should only check sites that are located within the quad
		List<Point> cluster = getOptimizedCluster(xStart, xFinish, yStart, yFinish, sites, voronoiMatrix);
		Point nearestSiteNE = getNearestSite(quad.ne, cluster);
		Point nearestSiteNW = getNearestSite(quad.nw, cluster);
		Point nearestSiteSE = getNearestSite(quad.se, cluster);
		Point nearestSiteSW = getNearestSite(quad.sw, cluster);
		return areSitesEqual(voronoiMatrix, xStart, xFinish, yStart, yFinish, nearestSiteNE, nearestSiteNW, nearestSiteSE, nearestSiteSW);
	}

	/**
	 * compares a quads sites and make sure they are equal, if true then assign all points in said quad to the site
	 **/
	private boolean areSitesEqual(Point[][] voronoiMatrix, int xStart, int xFinish, int yStart, int yFinish, Point nearestSiteNE, Point nearestSiteNW, Point nearestSiteSE, Point nearestSiteSW) {
		boolean passCheck;
		if ((nearestSiteNE == nearestSiteSE) && (nearestSiteSE == nearestSiteSW) && (nearestSiteSW == nearestSiteNW)) {
			passCheck = true;
			for (int x = xStart; x < xFinish; x++)
				for (int y = yStart; y < yFinish; y++)
					voronoiMatrix[x][y].nearestSeed = nearestSiteNW;
		} else {
			passCheck = false;
		}
		return passCheck;
	}

	/**
	 * Should in theory return a smaller list for lookup as we have to compare this 4 times
	 * once for each corner of the quad
	 **/
	private List<Point> getOptimizedCluster(int xStart, int xFinish, int yStart, int yFinish, List<? extends Point> sites, Point[][] voronoiMatrix) {

		List<Point> cluster = new LinkedList<>();

		for (int x = xStart; x < xFinish; x++)
			for (int y = yStart; y < yFinish; y++)
				for (Point p : sites)
					if (voronoiMatrix[x][y] == p)
						cluster.add(p);

		return cluster;
	}

	/**
	 * Gets nearest site to said point
	 */
	private Point getNearestSite(Point p, List<? extends Point> s) {
		//I reckon this could be more efficient
		var currentClosestSite = s.stream().findFirst().orElseThrow();

		/*
		I feel this will get super inefficient with much higher numbers of sites, maybe need to find a way to cluster points together,
		then only check promising clusters
		@See Point.distance
		*/
		for (Point site : s) if (p.distance(site) < p.distance(currentClosestSite)) currentClosestSite = site;

		return currentClosestSite;
	}

	/**
	 * returns 4 quads in a list from inputted quad
	 **/
	private List<Quad> subDivideQuad(Quad currentQuad) {
		LinkedList<Quad> subdivision = new LinkedList<>();
		//the 4 quads inputted quad will be split into
		Quad seCorner, swCorner, neCorner, nwCorner;
		//points for easier working out of quad placement
		Point center, n, e, s, w, ne, nw, se, sw;
		ne = currentQuad.ne;
		nw = currentQuad.nw;
		se = currentQuad.se;
		sw = currentQuad.sw;
		n = Utils.midpoint(ne, nw);
		e = Utils.midpoint(se, ne);
		s = Utils.midpoint(se, sw);
		w = Utils.midpoint(sw, nw);
		center = Utils.midpoint(Utils.midpoint(n, s), Utils.midpoint(e, w));
		//creating the 4 quads and adding them to the list to return
		seCorner = new Quad(se, e, s, center);
		subdivision.add(seCorner);
		swCorner = new Quad(s, center, sw, w);
		subdivision.add(swCorner);
		neCorner = new Quad(e, ne, center, w);
		subdivision.add(neCorner);
		nwCorner = new Quad(center, n, w, nw);
		subdivision.add(nwCorner);
		//return constructed list
		return subdivision;
	}


}
