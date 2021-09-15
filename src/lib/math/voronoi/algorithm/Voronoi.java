
/*
 * Do what the F**k you want
 */

/*
 Based on and owned by : https://www.youtube.com/watch?v=8mjUUNi1AaA&ab_channel=MichiganSpaceGrantConsortium
 */

package lib.math.voronoi.algorithm;

import com.google.common.base.Stopwatch;
import org.jetbrains.annotations.Contract;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static lib.math.voronoi.algorithm.Voronoi.Utils.*;

public class Voronoi {

	final boolean debug;
	final int width, height;
	final Point[][] voronoiMatrix;
	final List<Point> sites = new ArrayList<>();
	final Queue<Quad> toProcess = new ArrayDeque<>();
	int quadsCreated = 0, quadsProcessed = 0, cycle = 0;

	public final Map<Point, List<Point>> cells = new HashMap<>();

	public Voronoi(
			Resolution resolution,
			List<Point> sitesIn,
			boolean debug) {
		this(resolution.width, resolution.height, sitesIn, debug);
	}

	@SuppressWarnings("UnstableApiUsage")
	public Voronoi(
			int width,
			int height,
			List<Point> sitesIn,
			boolean debug) {
		Stopwatch s = null;
		this.debug = debug;
		//test line for performance testing
		if (this.debug) s = Stopwatch.createStarted();
		if (sitesIn == null || sitesIn.isEmpty()) {
			sitesIn = new ArrayList<>();
			Random r = new Random();
			int bound = (int) ((width * height) * 0.001);
			for (int i = 0; i < bound; i++) {
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

		for (Point data : sitesIn) {
			Point p = voronoiMatrix[data.x][data.y] = data;
			p.setSeed();
			sites.add(p);
			cells.put(p, new ArrayList<>());
		}

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
			for (String s1 : Arrays.asList("Finished calculating voronoi matrix", "Completed in " + formatTime(totalTime), "Each point took " + nanos / totalPoints + " nanoseconds to calculate", "Each cycle took " + nanos / cycle + " nanoseconds to calculate", "Each site took " + nanos / sites.size() + " nanoseconds to calculate")) {
				System.out.println(s1);
			}
			if (totalPoints <= (Resolution.TESTXXS.width * Resolution.TESTXXS.height)) {
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				System.out.println(Utils.Util2.arrayDebug2D(voronoiMatrix));
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

	public enum Resolution {
		HIGH(1920, 1200),
		MEDIUM(1024, 768),
		LOW(512, 384),
		TESTXXS(LOW.width / 4, LOW.height / 4),
		TESTXS(LOW.width / 2, LOW.height / 2),
		TESTXL(HIGH.width * 2, HIGH.height * 2),
		TESTXXL(HIGH.width * 4, HIGH.height * 4),
		TESTXXXL(HIGH.width * 8, HIGH.height * 8);


		final public int width, height;

		Resolution(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}

	public static class Quad {
		public final Point ne;
		public final Point nw;
		public final Point se;
		public final Point sw;

		final double width;
		final double height;

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

	public static class Point {
		public Point nearestSeed;
		public PointData data;

		//simply an x and y location
		public int x, y;
		public boolean isSeed;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}


		public double distance(Point point) {
			return distance(point.x, point.y);
		}

		private double distance(int x1, int y1) {
			int a = getX() - x1;
			int b = getY() - y1;
			return Math.hypot(a, b);
		}

		int getX() {
			return x;
		}

		int getY() {
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

		public boolean areEqual(Point p) {
			return (getX() == p.x && getY() == p.y);
		}

		public void setSeed() {
			this.isSeed = true;
			this.nearestSeed = this;
		}


		@Override
		public String toString() {
			if (this.data != null) return this.data.toString();
			else if (this.nearestSeed != null) return this.nearestSeed.printCoords();
			else return "null";
		}

		public String printCoords() {
			return "(" + getX() + "," + getY() + ")";
		}

		public static abstract class PointData {

		}
	}

	public static class Utils {

		public static Point midpoint(Point a, Point b, Point[][] matrix) {
			return matrix[average(a.x, b.x)][average(a.y, b.y)];
		}

		@Contract(pure = true)
		private static int average(int a, int b) {
			return (a + b) / 2;
		}

		public static void progressPercentage(int done, int total) {
			int size = 100;
			String iconLeftBoundary = "[", iconDone = "=", iconRemain = ".", iconRightBoundary = "]";

			if (done > total) {
				throw new IllegalArgumentException();
			}
			int donePercents = (100 * done) / total, doneLength = size * donePercents / 100;

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

		@Contract(pure = true)
		public static String formatTime(long millis) {
			long secs = millis / 1000;
			return String.format("%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60);
		}

		public static class Util2 {
			public static <T> String arrayDebug2D(T[][] arr) {

				int rows = arr.length;
				int columns = findLongestRow(arr);

				String[][] asStrings = new String[rows][columns];

				for (int i = 0; i < rows; ++i) {
					for (int j = 0; j < columns; ++j) {
						asStrings[i][j] = "";
						if (j < arr[i].length) {
							if (arr[i][j] == null) {
								asStrings[i][j] = "null";
							} else {
								asStrings[i][j] = arr[i][j].toString();
							}
						}
					}
				}
				return arrayDebug2D(asStrings);
			}

			private static String arrayDebug2D(String[][] arr) {

				if (arr == null || arr.length == 0)
					return "";

				StringBuilder x = new StringBuilder();
				int size = arr.length; //"depth" of the array

				int longestSubstring = addSpace(arr);


				for (String[] strings : arr) {

					for (String string : strings) {
						x.append("[").append(string).append("] "); //print all of the first row
					}

					x = new StringBuilder(x.substring(0, x.length() - 1)); //remove extra space
					x.append("\n"); //start a new line before going to the next row
				}
				//create top header
				int rows = findLongestRow(arr);
				//rows ==4 for this example
				StringBuilder top = new StringBuilder();

				for (int i = 0; i < rows; ++i) {
					String num = Integer.toString(i);
					int sizeI = num.length(); // 1 if <10, 2if <100, etc.
					int spacesToAdd = longestSubstring - sizeI; //add length - (i length) spaces, then place I in

					top.append(" ".repeat(Math.max(0, spacesToAdd)));

					top.append(num).append("] ");
				}
				//add starting brackets
				for (int i = 0; i < rows; ++i) {
					top = new StringBuilder(stringAdd(top.toString(), (i * (longestSubstring + 3)), "[")); //+3 because of "[] " = 3
				}

				top = new StringBuilder(top.substring(0, top.length() - 1)); //remove the final space

				//only works up to 99, but whatever)
				if (size < 10) {
					x.insert(0, "[0] ");
				} else {
					x.insert(0, "[ 0] ");
				}

				if (size < 10) {
					top.insert(0, "    ");
				} else {
					top.insert(0, "   ");
				}

				for (int i = 0; i < size - 1; ++i) {
					int b = indexOfInstance(x.toString(), i);
					x = new StringBuilder(stringAdd(x.toString(), b + 1, "[" + (i + 1) + "] "));
				}
				x.insert(0, "" + top + "\n"); //append add the header

				return x.toString();
			}

			private static <T> int findLongestRow(T[][] arr) {
				int retVal = 0;
				//int depth = arr.length ;
				for (T[] ts : arr) {
					int x = ts.length;
					if (x > retVal) {
						retVal = x;
					}
				}
				return retVal;
			}

			//adds spaces to the array (void)
			private static int addSpace(String[][] arr) {
				int a = findLongestString(arr);
				int height = arr.length;
				for (int i = 0; i < height; ++i) {
					int thisWidth = arr[i].length;
					for (int j = 0; j < thisWidth; ++j) {
						int thisLength = arr[i][j].length();
						for (int k = thisLength; k < a; ++k) {
							arr[i][j] = " " + arr[i][j];
						}
					}
				}
				return a; //return the longest substring
			}

			private static String stringAdd(String original, int position, String toAdd) {
				String temp = original;
				int tempSize = temp.length();
				String first = temp.substring(0, position);
				String second = temp.substring(position, tempSize);

				original = first + toAdd + second;
				return original;
			}

			//finds where an instance of a string (char) is (0 = first), (1 = second) etc
			private static int indexOfInstance(String string, int instance) {
				int size = string.length();
				char a = '\n';
				int instanceCount = -1;
				for (int i = 0; i < size; ++i) {
					if (string.charAt(i) == a) {
						++instanceCount;
						if (instanceCount == instance) {
							return i;
						}
					}
				}
				return -1;
			}

			private static int findLongestString(String[][] arr) {
				//"depth" of the array
				int longestSubstring = 0;

				for (String[] strings : arr) {
					for (String string : strings) {
						int y = string.length(); //size of the current substring
						if (y > longestSubstring) {
							longestSubstring = y;
						}
					}
				}
				return longestSubstring;
			}

		}
	}
}
