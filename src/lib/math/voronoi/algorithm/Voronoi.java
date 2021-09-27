
/*
 * Do what the F**k you want
 */

/*
 Based upon: https://www.youtube.com/watch?v=8mjUUNi1AaA&ab_channel=MichiganSpaceGrantConsortium
 */

package lib.math.voronoi.algorithm;

import com.google.common.base.Stopwatch;
import lib.math.voronoi.algorithm.data.Utils;
import lib.math.voronoi.algorithm.data.nodes.Point;
import lib.math.voronoi.algorithm.data.nodes.Quad;
import org.jetbrains.annotations.Contract;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static lib.math.voronoi.algorithm.data.Utils.ArrayUtils;
import static lib.math.voronoi.algorithm.data.Utils.DistanceUtils.midpoint;
import static lib.math.voronoi.algorithm.data.Utils.LoadingUtils.formatTime;
import static lib.math.voronoi.algorithm.data.Utils.LoadingUtils.progressPercentage;
import static lib.math.voronoi.algorithm.data.Utils.Resolution;

public class Voronoi {

	final boolean debug;
	final int width, height;
	public Map<Point, List<Point>> cells = new HashMap<>();
	final Point[][] voronoiMatrix;
	final List<Point> sites = new ArrayList<>();
	final Queue<Quad> toProcess = new ArrayDeque<>();
	int quadsCreated = 0, quadsProcessed = 0, cycle = 0;

	public Voronoi(
			Utils.Resolution resolution,
			List<Point> sitesIn,
			double scale,
			boolean debug) {
		this(resolution.width, resolution.height, sitesIn, scale, debug);
	}

	@SuppressWarnings("UnstableApiUsage")
	public Voronoi(
			int width,
			int height,
			List<Point> sitesIn,
			double scale,
			boolean debug) {
		Stopwatch s = null;
		this.debug = debug;
		//test line for performance testing
		if (this.debug) s = Stopwatch.createStarted();
		if (sitesIn == null || sitesIn.isEmpty()) {
			sitesIn = new ArrayList<>();
			Random r = new Random();
			double divisor = 0.0333D;
			int bound = (int) (((width * divisor) * (height * divisor)) * scale);
			for (int i = 0; i <= bound; i++) {
				Point randomize = new Point(r.nextInt(width - 1), r.nextInt(height - 1));
				sitesIn.add(randomize);
			}
			if (debug) System.out.println("Number of sites " + sitesIn.size());
		}

		voronoiMatrix = new Point[width][height];

		if (this.debug) {
			List<String> asList = Arrays.asList("Matrix width: " + width, "Matrix Height: " + height);
			for (String s1 : asList) {
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

		sitesIn.stream().map(data -> voronoiMatrix[data.x][data.y] = data).forEach(p -> {
			p.setSeed();
			sites.add(p);
			cells.put(p, new ArrayList<>());
		});

		//lines for testing
		if (this.debug) {
			System.out.println("Initiated matrix with " + sites.size() + " sites");
			System.out.println();
		}

		start();

		//lines for testing
		if (this.debug) {
			Objects.requireNonNull(s).stop();

			long nanos = s.elapsed(TimeUnit.MICROSECONDS);
			long totalTime = TimeUnit.NANOSECONDS.toMicros(nanos);
			Arrays.asList(
					"Finished calculating voronoi matrix",
					"Completed in " + formatTime(totalTime),
					"Each point took " + nanos / totalPoints + " nanoseconds to calculate",
					"Each cycle took " + nanos / cycle + " nanoseconds to calculate",
					"Each site took " + nanos / sites.size() + " nanoseconds to calculate",
					" ").forEach(System.out::println);
			if (totalPoints <= (Resolution.LOW.width * Utils.Resolution.LOW.height)) {
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				System.out.println(ArrayUtils.arrayDebug2D(voronoiMatrix));
			}


		}
	}

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

		++quadsCreated;

		//if (debug) System.out.print("|");
		while (!toProcess.isEmpty()) {
			checkAndSubdivide(Objects.requireNonNull(toProcess.poll()));
			if (debug) {
				cycle++;
				progressPercentage(quadsProcessed, quadsCreated);
			}

		}
		if (debug) System.out.println();
		if (debug) System.out.println("Total Cycles:" + cycle);



	}
	private void checkAndSubdivide(Quad quad) {
		++quadsProcessed;
		int qSize = (int) quad.size();
		if (!checkQuad(quad)) {
			if (qSize > 1) subDivideQuad(quad);
			else {
				checkSingleCell(quad.nw.x, quad.nw.y);
			}
		}
	}
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
					Point p = voronoiMatrix[i][j];
					p.data = quad.ne.data;
					p.nearestSeed = quad.ne.nearestSeed;
					cells.get(p.nearestSeed).add(p);
				}
			}
			return true;
		}
		return false;

	}

	private void checkSingleCell(int x, int y) {
		Point p = voronoiMatrix[x][y];
		p.nearestSeed = getNearestSite(x, y);
		p.data = p.nearestSeed.data;
		cells.get(p.nearestSeed).add(p);

	}

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

	private boolean areSitesEqual(Point nearestSiteNE, Point nearestSiteNW, Point nearestSiteSE, Point nearestSiteSW) {
		return nearestSiteNE.areEqual(nearestSiteSE)
				&& nearestSiteSE.areEqual(nearestSiteSW)
				&& nearestSiteSW.areEqual(nearestSiteNW);

	}


	@Contract(pure = true)
	public final Point[][] getMatrix() {
		return this.voronoiMatrix;
	}

	@Contract(pure = true)
	public final List<Point> getSites() {
		return this.sites;
	}

	@Contract(pure = true)
	public final Map<Point, List<Point>> getCells() {
		return this.cells;
	}

	public void setCells(Map<Point, List<Point>> cells) {
		this.cells = cells;
	}

}
