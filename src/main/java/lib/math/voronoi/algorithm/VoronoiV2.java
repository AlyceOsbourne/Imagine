/*
 * Do what the F**k you want
 */

package lib.math.voronoi.algorithm;

import lib.math.voronoi.algorithm.data.nodes.Point;
import lib.math.voronoi.algorithm.data.nodes.Quad;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static lib.utilities.Utils.DistanceUtils.midpoint;

public class VoronoiV2 {
	private final int width;
	private final int height;
	private final Point[][] matrix;
	private final Vector<Point> sites;
	private final double scale;
	double accuracy;

	public VoronoiV2(int width, int height, @Nullable List<Point> sites, double scale, double accuracy) {
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.accuracy = 101 - accuracy;
		this.sites = new Vector<>();
		matrix = constructMatrix(width, height);
		assignOrCreateSites(width, height, sites, scale);
		process();
	}

	private void assignOrCreateSites(int width, int height, @Nullable List<Point> sites, double scale) {
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

	@NotNull
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

	private void process() {

		final Queue<Quad> processQueue = new ConcurrentLinkedDeque<>(subdivideQuad(new Quad(matrix[0][0], matrix[0][height - 1], matrix[width - 1][0], matrix[width - 1][height - 1])));

		if (scale > 1)
			IntStream
					.iterate(0, i -> i < scale, i -> i+4)
					.forEach(i -> processQueue
							.forEach(quad -> processQueue.addAll(subdivideQuad(quad))));
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
	private @NotNull List<Quad> subdivideQuad(@NotNull Quad quad) {

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

	//checks to see if 4 points of quad are equal
	private boolean checkQuad(@NotNull Quad quad) {
		//line to prevent fighting when two sites are of equidistant
		if (quad.size() <= Math.max(1, accuracy)) {
			quad.points.forEach(this::findNearestSite);
			return true;
		}
		else return quad.points
				.stream()
				.map(this::findNearestSite)
				.unordered()
				.distinct()
				.count() == 1;

	}


	//sets seed for all points within range
	private void assignSeed(@NotNull Point nw, @NotNull Point se, @NotNull Point seed) {
		for (int x = nw.x; x <= se.x; x++) {
			for (int y = nw.y; y <= se.y; y++) {
				matrix[x][y].nearestSeed = seed;
			}
		}
	}

	//finds the nearest site to provided point
	private Point findNearestSite(@NotNull Point point) {
		Stream<Point> stream = sites.stream();
		if (sites.size() >= 10000) stream = stream.parallel();
		return point.nearestSeed = stream
				.min(Comparator.comparingDouble(value -> value.distance(point)))
				.orElseThrow(RuntimeException::new);

	}

	public @NotNull List<Point> getSites() {
		return Collections.unmodifiableList(sites);
	}

	public Point[] @NotNull [] getMatrix() {
		return matrix;
	}
}
