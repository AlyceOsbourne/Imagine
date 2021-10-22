/*
 * Do what the F**k you want
 */

package lib.math.voronoi.algorithm;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.IntStream;
import java.util.stream.Stream;
public class VoronoiV2 {
	private final int width;
	private final int height;
	private final Point[][] matrix;
	private final Vector<Point> sites;
	double accuracy;

	public VoronoiV2(int width, int height, List<Point> sites, double scale, double accuracy) {
		this.width = width;
		this.height = height;
		this.accuracy = 101 - accuracy;
		this.sites = new Vector<>();
		matrix = constructMatrix(width, height);
		assignOrCreateSites(width, height, sites, scale);
		process();
	}

	private Point[][] constructMatrix(int width, int height) {
		final Point[][] matrix;
		//construct matrix
		{
			matrix = new Point[width][height];
			for (int x = 0; x < matrix.length; x++)
				for (int y = 0; y < matrix[x].length; y++)
					matrix[x][y] = new Point(x, y);
		}
		return matrix;
	}

	private void assignOrCreateSites(int width, int height, List<Point> sites, double scale) {
		//create random dataset if site list is null else check sites for validity and add to site vector
		{
			if (sites == null || sites.isEmpty()) {
				Random r = new Random();
				double divisor = 4.823_1E-04; //at default res will produce sites that are a divisor of 1000
				int bound = (int) (((width * height) * divisor) * Math.max(scale, 0.001));
				IntStream.rangeClosed(0, bound)
						.mapToObj(i -> matrix[r.nextInt(width - 1)][r.nextInt(height - 1)].isSeed())
						.parallel()
						.forEach(this.sites::add);
			} else {
				//check sites are valid, discard invalid sites
				sites
						.stream()
						.filter(site -> ((site.x >= 0) && (site.x < width)) && ((site.y >= 0) && (site.y < height)))
						.parallel()
						.forEach(site -> this.sites.add(matrix[site.x][site.y] = site.isSeed()));
			}
		}
	}

	private void process() {

		final Queue<Quad> processQueue = new ConcurrentLinkedDeque<>(subdivideQuad(new Quad(matrix[0][0], matrix[0][height - 1], matrix[width - 1][0], matrix[width - 1][height - 1])));

		//process all quads within queue
		processQueue
				.parallelStream()
				.unordered()
				.forEach(quad -> {
					if (checkQuad(quad)) assignSeed(quad.nw, quad.se, quad.ne.nearestSeed);
					else processQueue.addAll(subdivideQuad(quad));
				});
	}

	//takes quad, splits into 4, returns
	private List<Quad> subdivideQuad(Quad quad) {

		Point n, ne, nw, e, s, se, sw, w, c;
		Quad tr, tl, br, bl;

		/* find points */
		{
			nw = quad.nw;
			ne = quad.ne;
			sw = quad.sw;
			se = quad.se;

			n = midpoint(ne, nw, matrix);
			w = midpoint(nw, sw, matrix);
			s = midpoint(se, sw, matrix);
			e = midpoint(ne, se, matrix);
			c = midpoint(ne, sw, matrix);
		}

		/* create new quads */
		{
			tl = new Quad(nw, w, n, c);
			tr = new Quad(n, c, ne, e);
			bl = new Quad(w, sw, c, s);
			br = new Quad(c, s, e, se);
		}

		return new ArrayList<>(Arrays.asList(tl, tr, bl, br));
	}

	public static Point midpoint(Point a, Point b, Point[][] matrix) {
		return matrix[average(a.x, b.x)][average(a.y, b.y)];
	}

	private static int average(int a, int b) {
		return (a + b) / 2;
	}

	//checks to see if 4 points of quad are equal
	private boolean checkQuad(Quad quad) {
		//line to prevent fighting when two sites are of equidistant
		if (quad.size() <= Math.max(1, accuracy)) {
			quad.points.forEach(this::findNearestSite);
			return true;
		} else return quad.points
				.stream()
				.map(this::findNearestSite)
				.unordered()
				.distinct()
				.count() == 1;

	}

	//finds the nearest site to provided point
	private Point findNearestSite(Point point) {
		Stream<Point> stream = sites.stream();
		if (sites.size() >= 10000) stream = stream.parallel();
		return point.nearestSeed = stream
				.min(Comparator.comparingDouble(value -> value.distance(point)))
				.orElseThrow(RuntimeException::new);

	}

	//sets seed for all points within range
	private void assignSeed(Point nw, Point se, Point seed) {
		for (int x = nw.x; x <= se.x; x++) {
			for (int y = nw.y; y <= se.y; y++) {
				matrix[x][y].nearestSeed = seed;
			}
		}
	}

	public Point[][] getMatrix() {
		return matrix;
	}

	public static class Point {
		//simply an x and y location
		public final int x;
		public final int y;
		private final Map<Object, Object> components = new HashMap<>();
		public Point nearestSeed;
		private boolean isSeed;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void addComponent(Object key, Object component) {
			components.put(key, component);
		}

		public void removeComponent(Object key) {
			components.remove(key);
		}

		public Object getComponent(Object string) {
			return components.get(string);
		}

		public Point isSeed() {
			nearestSeed = this;
			isSeed = true;
			return this;
		}

		private double distance(Point point) {
			return distance(point.x, point.y);
		}

		private double distance(int x1, int y1) {
			int a = getX() - x1;
			int b = getY() - y1;
			return ((a * a) + (b * b));
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public double angle(Point p) {
			return angle(p.x, p.y);
		}

		public double angle(double x, double y) {
			final double ax = getX();
			final double ay = getY();

			final double delta = (ax * x + ay * y) / Math.sqrt(
					(ax * ax + ay * ay) * (x * x + y * y));

			if (delta > 1.0) {
				return 0.0;
			}
			if (delta < -1.0) {
				return 180.0;
			}

			return Math.toDegrees(Math.acos(delta));
		}

		@Override
		public int hashCode() {
			int result = x;
			result = 31 * result + y;
			return result;
		}

		@Override
		public boolean equals(Object o) {
			return this == o || o instanceof Point point && x == point.x && y == point.y;

		}

		@Override
		public String toString() {
			if (this.nearestSeed != null) return this.nearestSeed.printCoords();
			else return "null";
		}

		public String printCoords() {
			return "(" + getX() + "," + getY() + ")";
		}

		public boolean areEqual(Point p) {
			return (getX() == p.x && getY() == p.y);
		}

	}

	public static final class Quad {
		public final Point ne;
		public final Point nw;
		public final Point se;
		public final Point sw;
		final double width;
		final double height;
		public List<Point> points = new ArrayList<>();

		public Quad(Point nw, Point sw, Point ne, Point se) {
			this.ne = ne;
			this.nw = nw;
			this.se = se;
			this.sw = sw;
			points.add(ne);
			points.add(nw);
			points.add(se);
			points.add(sw);
			width = se.distance(sw);
			height = se.distance(ne);
		}

		public double size() {
			return width * height;
		}
	}
}
