
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
import lib.math.voronoi.Voronoi;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static lib.math.voronoi.Utils.midpoint;

public class CalculateBySubDivision<Data extends Point> extends Voronoi<Data> {

	boolean debug = false;
	int width, height;
	Point[][] voronoiMatrix;

	/**
	 * The inputted sites to process.
	 */
	List<Point> sites = new ArrayList<>();
	List<Quad> toProcess = new ArrayList<>();
	int cycle = 0;


	/**
	 * initiates calculating a voronoi diagram by a subdivision algorithm.
	 * checks sites to make sure they are in bounds, discards those outside and then adds the remaining to the matrix
	 * plus stores sites in a list for faster lookup
	 */
	@SuppressWarnings("UnstableApiUsage")
	public CalculateBySubDivision(int width, int height, List<Data> sitesIn, boolean debug) {
		Stopwatch s = null;
		this.debug = debug;
		//test line for performance testing
		if (this.debug) s = Stopwatch.createStarted();

		voronoiMatrix = new Point[width][height];

		if (this.debug) {
			System.out.println("Matrix width: " + width);
			System.out.println("Matrix Height: " + height);
		}

		var totalPoints = width * height;
		if (this.debug) System.out.println("Total points in matrix: " + totalPoints);

		this.width = width;
		this.height = height;

		for (int i = 0; i < voronoiMatrix.length; i++) {
			for (int j = 0; j < voronoiMatrix[i].length; j++) {
				voronoiMatrix[i][j] = new Point(i, j);
			}
		}

		for (int i = 0, sitesInSize = sitesIn.size(); i < sitesInSize; i++) {
			Data data = sitesIn.get(i);
			Point p = voronoiMatrix[data.x][data.y] = data;
			p.setSeed();
			sites.add(p);
			if (this.debug) System.out.println("Added site (" + data.x + "," + data.x + ")");
		}

		//lines for testing
		if (this.debug) System.out.println("Initiated matrix with " + sites.size() + " points");

		start();

		//lines for testing
		if (this.debug) {
			Objects.requireNonNull(s).stop();

			long nanos = s.elapsed(TimeUnit.MICROSECONDS);
			long totalTime = TimeUnit.NANOSECONDS.toMicros(nanos);

			System.out.println("Finished calculating voronoi matrix");

			for (String s1 : Arrays.asList(Util2.arrayDebug2D(voronoiMatrix), "Completed in " + formatTime(totalTime), "Each point took " + nanos / totalPoints + " nanoseconds to calculate", "Each cycle took " + nanos / cycle + " nanoseconds to calculate", "Each site took " + nanos / sites.size() + " nanoseconds to calculate")) {
				System.out.println(s1);
			}

		}
	}

	/**
	 * starts the algorithm, creates a starting Quad with the bounds of the image,
	 * passes said quad to #checkAndSubdivide
	 */
	private void start() {

		int yMin = 0;
		int xMin = 0;
		int xMax = voronoiMatrix.length - 1;
		int yMax = voronoiMatrix[0].length - 1;

		toProcess.add(
				new Quad(
						voronoiMatrix[xMin][yMin],
						voronoiMatrix[xMin][yMax],
						voronoiMatrix[xMax][yMin],
						voronoiMatrix[xMax][yMax]
				)
		);


		int loadingTicker = 0;
		if (debug) System.out.print("|");
		if (!toProcess.isEmpty()) {
			do {
				if (debug) {
					cycle++;
					loadingTicker++;
					if (loadingTicker == 1000) {
						loadingTicker = 0;
						System.out.print("=");
					}
				}
				for (int i = 0, toProcessSize = toProcess.size(); i < toProcessSize; i++) {
					Quad quad = toProcess.get(i);
					checkAndSubdivide(quad);
					toProcess.remove(quad);
					break;
				}
			} while (!toProcess.isEmpty());
		}
		if (debug) System.out.print("|");
		if (debug) System.out.println("Total Cycles:" + cycle);

	}

	/**
	 * passes the quad the checkQuad, if it passes this should be the end of the line for that quad, otherwise it
	 * subdivides the quad into 4 sections and adds them to the process queue
	 **/
	private void checkAndSubdivide(Quad quad) {
		int qSize = (int) quad.size();
		if (!checkQuad(quad)) {
			if (qSize > 1) subDivideQuad(quad);
			else {
				checkSingleCell(quad.nw.x, quad.nw.y);
			}
		}
	}

	public static String formatTime(long millis) {
		long secs = millis / 1000;
		return String.format("%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60);
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

		n = midpoint(nw, ne, voronoiMatrix);
		e = midpoint(se, ne, voronoiMatrix);
		s = midpoint(sw, se, voronoiMatrix);
		w = midpoint(sw, nw, voronoiMatrix);

		center = midpoint(midpoint(n, s, voronoiMatrix), midpoint(e, w, voronoiMatrix), voronoiMatrix);
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
		if (areSitesEqual(nearestSiteNE,
				nearestSiteNW,
				nearestSiteSE,
				nearestSiteSW)) {
			int xStart, xFinish, yStart, yFinish;
			xStart = quad.nw.x;
			xFinish = quad.ne.x;
			yStart = quad.ne.y;
			yFinish = quad.se.y;
			long limit = xFinish;
			long toSkip = xStart;
			for (Point[] points : voronoiMatrix) {
				if (toSkip > 0) {
					toSkip--;
					continue;
				}
				if (limit-- == 0) break;
				List<Point> list = new ArrayList<>();
				long limit1 = yFinish;
				long toSkip1 = yStart;
				for (Point point : points) {
					if (toSkip1 > 0) {
						toSkip1--;
						continue;
					}
					if (limit1-- == 0) break;
					list.add(point);
				}
				for (Point s : list) {
					voronoiMatrix[s.x][s.y].data = quad.ne.data;
				}
			}

			return true;
		}
		return false;

	}

	private Point getNearestSite(int x, int y) {

		Point p = voronoiMatrix[x][y];
		Comparator<Point> comparator = Comparator.comparingDouble(p::distance);
		Point best = null;
		boolean seen = false;
		for (Point site : sites) {
			if (!seen || comparator.compare(site, best) < 0) {
				seen = true;
				best = site;
			}
		}
		Point currentClosestSite = seen ? best : null;


		return p.data.nearestSeed = currentClosestSite;
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

	private void checkSingleCell(int x, int y) {
		voronoiMatrix[x][y].data.nearestSeed = getNearestSite(x, y);
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
