
/*
 * Do what the F**k you want
 */

/*
 Based on and owned by : https://www.youtube.com/watch?v=8mjUUNi1AaA&ab_channel=MichiganSpaceGrantConsortium
 */

package lib.math.voronoi.algorithm;

import com.google.common.base.Stopwatch;
import lib.math.voronoi.Point;
import lib.math.voronoi.Util2;
import lib.math.voronoi.Utils;
import lib.math.voronoi.Voronoi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CalculateBySubDivision<Data extends Point> extends Voronoi {

	Logger log = Logger.getLogger(this.getClass().getSimpleName());

	int width, height;
	Point[][] voronoiMatrix;

	/**
	 * The inputted sites to process.
	 */
	List<Point> sites = new ArrayList<>();
	List<Quad> toProcess = new ArrayList<>();


	/**
	 * initiates calculating a voronoi diagram by a subdivision algorithm.
	 * checks sites to make sure they are in bounds, discards those outside and then adds the remaining to the matrix
	 * plus stores sites in a list for faster lookup
	 */
	@SuppressWarnings("UnstableApiUsage")
	public CalculateBySubDivision(int width, int height, List<Data> sitesIn) {

		Stopwatch s = Stopwatch.createStarted();

		voronoiMatrix = new Point[width][height];

		this.width = width;
		this.height = height;

		for (int i = 0; i < voronoiMatrix.length; i++) {
			for (int j = 0; j < voronoiMatrix[i].length; j++) {
				voronoiMatrix[i][j] = new Point(i, j);
			}
		}
		for (Data site : sitesIn) {
			if (site.x < width - 1 && site.y < height - 1 && site.x >= 0 && site.y >= 0) {
				Point p = voronoiMatrix[site.x][site.y];
				p.setSeed();
				sites.add(p);
				System.out.println("Added site (" + site.x + "," + site.x + ")");
			} else log.info("Discarded site due to it being out of range");
		}

		System.out.println("Initiated matrix with " + sites.size() + " points");
		System.out.println(Util2.arrayDebug2D(voronoiMatrix));

		start();

		s.stop();
		System.out.println("Finished calculating voronoi matrix");
		System.out.println(Util2.arrayDebug2D(voronoiMatrix));
		System.out.println("Time Took:" + s.elapsed(TimeUnit.MILLISECONDS) + " milliseconds.");
	}

	/**
	 * starts the algorithm, creates a starting Quad with the bounds of the image,
	 * passes said quad to #checkAndSubdivide
	 */
	private void start() {

		int xMax, yMax, xMin, yMin;

		yMin = 0;
		xMin = 0;
		xMax = voronoiMatrix.length - 1;
		yMax = voronoiMatrix[0].length - 1;

		toProcess.add(
				new Quad(
						voronoiMatrix[xMin][yMin],
						voronoiMatrix[xMin][yMax],
						voronoiMatrix[xMax][yMin],
						voronoiMatrix[xMax][yMax]
				)
		);

		int cycle = 0;
		while (!toProcess.isEmpty()) {
			cycle++;
			toProcess.stream().parallel().findFirst().ifPresent(this::checkAndSubdivide);
		}
		System.out.println("Total Cycles:" + cycle);

	}

	/**
	 * passes the quad the checkQuad, if it passes this should be the end of the line for that quad, otherwise it
	 * subdivides the quad into 4 sections and adds them to the process queue
	 **/
	private void checkAndSubdivide(Quad quad) {
		int qSize = (int) quad.size();
		toProcess.remove(quad);
		if (!checkQuad(quad)) {
			if (qSize > 1) subDivideQuad(quad);
			else {
				checkSingleCell(quad.nw.x, quad.nw.y);
			}
		}
	}

	/**
	 * checks quad corners for their nearest site, comparing against a list of sites to squeeze out some more speed.
	 * Then passes along to areSitesEqual to perform equality check
	 **/
	private boolean checkQuad(Quad quad) {
		//collect nearest site for each corner of quad
		Point nearestSiteNE = getNearestSite(quad.ne.x, quad.ne.y);
		Point nearestSiteNW = getNearestSite(quad.nw.x, quad.nw.y);
		Point nearestSiteSE = getNearestSite(quad.se.x, quad.se.y);
		Point nearestSiteSW = getNearestSite(quad.sw.x, quad.se.y);
		if (areSitesEqual(nearestSiteNE, nearestSiteNW, nearestSiteSE, nearestSiteSW)) {
			int xStart, xFinish, yStart, yFinish;
			xStart = quad.nw.x;
			xFinish = quad.ne.x;
			yStart = quad.ne.y;
			yFinish = quad.se.y;
			for (int i = xStart; i <= xFinish; i++)
				for (int j = yStart; j <= yFinish; j++)
					voronoiMatrix[i][j].nearestSeed = quad.se.nearestSeed;
			return true;
		}
		return false;
	}

	/**
	 * returns 4 quads in a list from inputted quad
	 **/
	private void subDivideQuad(Quad currentQuad) {

		ArrayList<Quad> subdivision = new ArrayList<>();
		//the 4 quads inputted quad will be split into
		Quad seCorner, swCorner, neCorner, nwCorner;
		//points for easier working out of quad placement
		Point center, n, e, s, w, ne, nw, se, sw;

		ne = currentQuad.ne;
		nw = currentQuad.nw;
		se = currentQuad.se;
		sw = currentQuad.sw;

		n = Utils.midpoint(nw, ne, voronoiMatrix);
		e = Utils.midpoint(se, ne, voronoiMatrix);
		s = Utils.midpoint(sw, se, voronoiMatrix);
		w = Utils.midpoint(sw, nw, voronoiMatrix);

		center = Utils.midpoint(Utils.midpoint(n, s, voronoiMatrix), Utils.midpoint(e, w, voronoiMatrix), voronoiMatrix);
		//creating the 4 quads and adding them to the list to return

		nwCorner = new Quad(nw, w, n, center);
		subdivision.add(nwCorner);
		swCorner = new Quad(w, sw, center, s);
		subdivision.add(swCorner);
		neCorner = new Quad(n, center, ne, e);
		subdivision.add(neCorner);
		seCorner = new Quad(center, s, e, se);
		subdivision.add(seCorner);

		//return constructed list
		toProcess.addAll(subdivision);
	}

	private void checkSingleCell(int x, int y) {
		voronoiMatrix[x][y].nearestSeed = getNearestSite(x, y);
	}

	private Point getNearestSite(int x, int y) {

		Point p = voronoiMatrix[x][y];

		//System.out.println("Searching for nearest site too (" + p.x + "," + p.y + ")");
		//starting with a node that should be well outside the diagram, just so we have something to check against in the first cycle
		Point currentClosestSite = null;

		//well clearly if this point is a site its nearest site is itsel
		//line to cute down some method calls, means each tested point only needs to do a distance check once
		for (Point site : sites) {
			Point s = voronoiMatrix[site.x][site.y];
			if (currentClosestSite == null) currentClosestSite = s;
			//if new site is closer than current replace current and assigns the points nearest site
			if (p.distance(s) < p.distance(currentClosestSite))
				currentClosestSite = s;
		}


		p.nearestSeed = currentClosestSite;

		//System.out.println("Nearest site found at (" + currentClosestSite.x + "," + currentClosestSite.y + ")");

		return currentClosestSite;
	}

	public Point[][] getMatrix() {
		return this.voronoiMatrix;
	}

	/**
	 * compares a quads sites and make sure they are equal, if true then assign all points in said quad to the site
	 **/
	private boolean areSitesEqual(Point nearestSiteNE, Point nearestSiteNW, Point nearestSiteSE, Point nearestSiteSW) {
		return (nearestSiteNE.areEqual(nearestSiteSE))
				&& (nearestSiteSE.areEqual(nearestSiteSW))
				&& (nearestSiteSW.areEqual(nearestSiteNW));

	}

	public static class Quad {
		public Point ne, nw, se, sw;

		double width, height;

		public Quad(Point nw, Point sw, Point ne, Point se) {
			this.ne = ne;
			this.nw = nw;
			this.se = se;
			this.sw = sw;
			width = se.distance(sw);
			height = se.distance(ne);
		}


		public double size() {
			return width * height;
		}
	}

}
