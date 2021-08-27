
/*
 * Do what the F**k you want
 */

/*
 Based on and owned by : https://www.youtube.com/watch?v=8mjUUNi1AaA&ab_channel=MichiganSpaceGrantConsortium
 */

package lib.math.voronoi.algorithm;

import lib.math.voronoi.Utils;
import lib.math.voronoi.Voronoi;
import lib.math.voronoi.datasubtypes.Point;
import lib.math.voronoi.datasubtypes.Quad;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CalculateBySubDivision extends Voronoi {

	final Point[][] voronoiMatrix;

	/**
	 * The inputted sites to process.
	 */
	final List<Point> sites = new LinkedList<>();

	/**
	 * initiates calculating a voronoi diagram by a subdivision algorithm.
	 * checks sites to make sure they are in bounds, discards those outside and then adds the remaining to the matrix
	 * plus stores sites in a list for faster lookup
	 */
	public CalculateBySubDivision(int width, int height, List<Point> sitesIn) {

		//might have to minus width and height by 1 to account for the fact that array starts at 0

		voronoiMatrix = new Point[width][height];

		for (int i = 0; i < voronoiMatrix.length; i++)
			for (int j = 0; j < voronoiMatrix[i].length; j++)
				voronoiMatrix[i][j] = new Point(i, j);


		for (Point site : sitesIn) {
			if (site.x < width - 1 && site.y < height - 1 && site.x >= 0 && site.y >= 0) {
				sites.add(site);
				voronoiMatrix[site.x][site.y] = site;
			}
		}
		start(voronoiMatrix, sites);
	}

	/**
	 * starts the algorithm, creates a starting Quad with the bounds of the image,
	 * passes said quad to #checkAndSubdivide
	 */
	private void start(Point[][] voronoiMatrix, List<? extends Point> sites) {
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
		System.out.println("Checking quad.");
		if (!checkQuad(quad, sites, voronoiMatrix)) {
			System.out.println("Check Failed, proceeding to subdivide.");
			List<Quad> subdivision = subDivideQuad(quad);
			for (Quad sub : subdivision) {
				checkAndSubdivide(sub, sites);
			}
		} else System.out.println("Check Passed");
	}

	/**
	 * checks quad corners vs sites, if quad corners sites are all equal assigns all
	 * points in matrix to that site and returns true, otherwise fails and returns false
	 **/
	private boolean checkQuad(Quad quad, List<? extends Point> sites, Point[][] voronoiMatrix) {
		//collect nearest site for each corner of quad
		int xStart = quad.sw.x;
		int xFinish = quad.se.x;
		int yStart = quad.se.y;
		int yFinish = quad.ne.y;

		System.out.println("Checking range X:" + xStart + " " + xFinish);
		System.out.println("Checking range X:" + yStart + " " + yFinish);
		// this should only check sites that are located within the quad
		List<Point> cluster = getOptimizedCluster(xStart, xFinish, yStart, yFinish, sites);

		Point nearestSiteNE = getNearestSite(quad.ne, sites);
		System.out.println(nearestSiteNE + "found");
		Point nearestSiteNW = getNearestSite(quad.nw, sites);
		System.out.println(nearestSiteNW + "found");
		Point nearestSiteSE = getNearestSite(quad.se, sites);
		System.out.println(nearestSiteSE + "found");
		Point nearestSiteSW = getNearestSite(quad.sw, sites);
		System.out.println(nearestSiteSW + "found");

		return areSitesEqual(voronoiMatrix,
				xStart,
				xFinish,
				yStart,
				yFinish,
				nearestSiteNE,
				nearestSiteNW,
				nearestSiteSE,
				nearestSiteSW);
	}

	/**
	 * Should in theory return a smaller list for lookup as we have to compare this 4 times
	 * once for each corner of the quad, more quads = more runs and more lookups,
	 * so having a smaller list to check per lookup is better
	 **/
	private List<Point> getOptimizedCluster(int xStart, int xFinish, int yStart, int yFinish, List<? extends Point> sites) {

		List<Point> cluster = new ArrayList<>();

		for (Point p : sites)
			if ((p.x >= xStart && p.x <= xFinish) && (p.y >= yStart && p.y <= yFinish)) {
				cluster.add(p);
			}
		if (cluster.size() == 0) getOptimizedCluster(xStart - 1, xFinish + 1, yStart - 1, yFinish + 1, sites);

		return cluster;
	}

	/**
	 * compares a quads sites and make sure they are equal, if true then assign all points in said quad to the site
	 **/
	private boolean areSitesEqual(Point[][] voronoiMatrix, int xStart, int xFinish, int yStart, int yFinish, Point nearestSiteNE, Point nearestSiteNW, Point nearestSiteSE, Point nearestSiteSW) {
		boolean passCheck;
		System.out.println("Checking sites for equality");
		if ((nearestSiteNE == nearestSiteSE)
				&& (nearestSiteSE == nearestSiteSW)
				&& (nearestSiteSW == nearestSiteNW)) {
			passCheck = true;
			System.out.println("Sites are Equal, flagging points");
			for (int x = xStart; x <= xFinish; x++)
				for (int y = yStart; y <= yFinish; y++)
					voronoiMatrix[x][y].nearestSeed = nearestSiteNW;
		} else {
			System.out.println("Sites are Not Equal");
			passCheck = false;
		}
		return passCheck;
	}

	/**
	 * Gets nearest site to said point
	 */
	private Point getNearestSite(Point p, List<? extends Point> s) {
		System.out.println("Searching for nearest site too (" + p.x + "," + p.y + ")");

		if (p.isSeed) {
			return p;
		}

		Point currentClosestSite = p.nearestSeed;

		if (s.size() > 0) {
			currentClosestSite = s.get(s.size() - 1);
			for (Point site : s)
				if (p.distance(site) < p.distance(currentClosestSite)) {
					currentClosestSite = site;
				}
		}

		System.out.println("Nearest site found at (" + currentClosestSite.x + "," + currentClosestSite.y + ")");

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
		n = Utils.midpoint(nw, ne);
		e = Utils.midpoint(se, ne);
		s = Utils.midpoint(sw, se);
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
		for (Quad q : subdivision) {
			System.out.println(q + "created with corners");
			System.out.println("North East:" + q.ne);
			System.out.println("North West:" + q.nw);
			System.out.println("South East:" + q.se);
			System.out.println("South West:" + q.sw);
		}
		//return constructed list
		return subdivision;
	}

	public Point[][] getMatrix() {
		return this.voronoiMatrix;
	}


}
