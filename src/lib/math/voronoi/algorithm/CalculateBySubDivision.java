
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
	Queue<Quad> toProcess = new ArrayDeque<>();

	int quadsCreated = 0, quadsProcessed = 0;
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
			List<String> asList = Arrays.asList("Matrix width: " + width, "Matrix Height: " + height);
			for (int i = 0, asListSize = asList.size(); i < asListSize; i++) {
				String s1 = asList.get(i);
				System.out.println(s1);
			}
		}

		int totalPoints = width * height;
		if (this.debug) System.out.println("Total points in matrix: " + totalPoints);

		this.width = width;
		this.height = height;

		for (int i = 0; i < voronoiMatrix.length; i++) {
			for (int j = 0; j < voronoiMatrix[i].length; j++) {
				voronoiMatrix[i][j] = new Point(i, j);
			}
		}

		for (Data data : sitesIn) {
			Point p = voronoiMatrix[data.x][data.y] = data;
			p.setSeed();
			sites.add(p);
			//if (this.debug) System.out.println("Added site (" + data.x + "," + data.x + ")");
		}

		//lines for testing
		if (this.debug) System.out.println("Initiated matrix with " + sites.size() + " points");

		start();

		//lines for testing
		if (this.debug) {
			Objects.requireNonNull(s).stop();

			long nanos = s.elapsed(TimeUnit.MICROSECONDS);
			long totalTime = TimeUnit.NANOSECONDS.toMicros(nanos);
			for (String s1 : Arrays.asList("Finished calculating voronoi matrix", "Completed in " + formatTime(totalTime), "Each point took " + nanos / totalPoints + " nanoseconds to calculate", "Each cycle took " + nanos / cycle + " nanoseconds to calculate", "Each site took " + nanos / sites.size() + " nanoseconds to calculate")) {
				System.out.println(s1);
			}
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Util2.arrayDebug2D(voronoiMatrix));




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

		quadsCreated++;


		//if (debug) System.out.print("|");
		while (!toProcess.isEmpty()) {
			if (debug) {
				cycle++;
				progressPercentage(quadsProcessed, quadsCreated);
			}
			checkAndSubdivide(Objects.requireNonNull(toProcess.poll()));

		}
		if (debug) System.out.println();
		if (debug) System.out.println("Total Cycles:" + cycle);

	}

	public static String formatTime(long millis) {
		long secs = millis / 1000;
		return String.format("%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60);
	}

	public static void progressPercentage(int done, int total) {
		int size = 30;
		String iconLeftBoundary = "[";
		String iconDone = "=";
		String iconRemain = ".";
		String iconRightBoundary = "]";

		if (done > total) {
			throw new IllegalArgumentException();
		}
		int donePercents = (100 * done) / total;
		int doneLength = size * donePercents / 100;

		StringBuilder bar = new StringBuilder(iconLeftBoundary);
		for (int i = 0; i < size; i++) {
			if (i < doneLength) {
				bar.append(iconDone);
			} else {
				bar.append(iconRemain);
			}
		}
		bar.append(iconRightBoundary);

		System.out.print("\r" + bar + " " + donePercents + "%");

		if (done == total) {
			System.out.print("\n");
		}
	}

	/**
	 * passes the quad the checkQuad, if it passes this should be the end of the line for that quad, otherwise it
	 * subdivides the quad into 4 sections and adds them to the process queue
	 **/
	private void checkAndSubdivide(Quad quad) {
		quadsProcessed++;
		int qSize = (int) quad.size();
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
		if (areSitesEqual(nearestSiteNE,
				nearestSiteNW,
				nearestSiteSE,
				nearestSiteSW)) {
			int xStart = quad.nw.x;
			int xFinish = quad.ne.x;
			int yStart = quad.ne.y;
			int yFinish = quad.se.y;
			for (int i = xStart; i <= xFinish; i++) {
				for (int j = yStart; j <= yFinish; j++) {
					voronoiMatrix[i][j].data = quad.ne.data;
					voronoiMatrix[i][j].nearestSeed = quad.ne.nearestSeed;
				}
			}

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

		n = midpoint(nw, ne, voronoiMatrix);
		e = midpoint(se, ne, voronoiMatrix);
		s = midpoint(sw, se, voronoiMatrix);
		w = midpoint(sw, nw, voronoiMatrix);

		center = midpoint(midpoint(n, s, voronoiMatrix), midpoint(e, w, voronoiMatrix), voronoiMatrix);
		//creating the 4 quads and adding them to the list to return

		nwCorner = new Quad(nw, w, n, center);
		subdivision.add(nwCorner);
		quadsCreated++;
		swCorner = new Quad(w, sw, center, s);
		subdivision.add(swCorner);
		quadsCreated++;
		neCorner = new Quad(n, center, ne, e);
		subdivision.add(neCorner);
		quadsCreated++;
		seCorner = new Quad(center, s, e, se);
		subdivision.add(seCorner);
		quadsCreated++;

		//return constructed list
		toProcess.addAll(subdivision);
	}

	private void checkSingleCell(int x, int y) {
		voronoiMatrix[x][y].nearestSeed = getNearestSite(x, y);
		voronoiMatrix[x][y].data = voronoiMatrix[x][y].nearestSeed.data;
	}

	private Point getNearestSite(int x, int y) {
		Point p = voronoiMatrix[x][y];
		if (p.nearestSeed != null) return p.nearestSeed;
		Comparator<Point> comparator = Comparator.comparingDouble(p::distance);
		Point best = null;
		boolean seen = false;
		for (Point site : sites) {
			if (!seen || comparator.compare(site, best) < 0) {
				seen = true;
				best = site;
			}
		}
		var currentClosestSite = seen ? best : null;
		return p.nearestSeed = currentClosestSite;
	}

	/**
	 * compares a quads sites and make sure they are equal, if true then assign all points in said quad to the site
	 **/
	private boolean areSitesEqual(Point nearestSiteNE, Point nearestSiteNW, Point nearestSiteSE, Point nearestSiteSW) {
		return nearestSiteNE.areEqual(nearestSiteSE)
				&& nearestSiteSE.areEqual(nearestSiteSW)
				&& nearestSiteSW.areEqual(nearestSiteNW);

	}

	public Data[][] getMatrix() {
		return (Data[][]) this.voronoiMatrix;
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
