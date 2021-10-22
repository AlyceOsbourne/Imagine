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

import static lib.utilities.Utils.DistanceUtils.midpoint;

public class VoronoiV2 {
	private final int width;
	private final int height;
	private final Point[] @NotNull [] matrix;
	private final @NotNull Vector<Point> sites;

	public VoronoiV2(int width, int height, @Nullable List<Point> sites, double scale) {
		this.width = width;
		this.height = height;
		this.sites = new Vector<>();
		//construct matrix
		{
			matrix = new Point[width][height];
			for (int x = 0; x < matrix.length; x++)
				for (int y = 0; y < matrix[x].length; y++) {
					matrix[x][y] = new Point(x, y);
				}
		}
		//create random dataset if site list is null else check sites for validity and add to site vector
		{
			if (sites == null || sites.isEmpty()) {
				Random r = new Random();
				double divisor = 4.826E-04;
				int bound = (int) (((width * height) * divisor) * scale);
				System.out.println(bound);
				for (int i = 0; i <= bound; i++) {
					Point randomize = matrix[r.nextInt(width - 1)][r.nextInt(height - 1)].isSeed();
					this.sites.add(randomize);
				}
			} else {
				//check sites are valid, discard invalid sites
				for (Point site : sites) {
					if (
							((site.x >= 0) && (site.x < width))
									&&
									((site.y >= 0) && (site.y < height))
					) {
						matrix[site.x][site.y] = site.isSeed();
						this.sites.add(site);
					}
				}
			}
		}
		process();
	}

	private void process() {
		final Queue<Quad> processQueue = new ConcurrentLinkedDeque<>();
		//create initial quad
		processQueue.add(new Quad(matrix[0][0], matrix[0][height - 1], matrix[width - 1][0], matrix[width - 1][height - 1]));

		//process all sites within queue
		processQueue.parallelStream().forEach(quad -> {
			if (!checkQuad(quad)) processQueue.addAll(subdivideQuad(quad));
			else assignSeed(quad.nw, quad.se, quad.ne.nearestSeed);
		});
	}


	//checks to see if 4 points of quad are equal
	private boolean checkQuad(@NotNull Quad quad) {
		//line to prevent fighting when two sites are of equidistant
		if (quad.size() <= 1) {
			quad.points
					.forEach(this::findNearestSite);
			return true;
		}
		//this line should only return true if all sites are equal, cleaner than doing individual comparisons
		return quad.points
				.stream()
				.map(this::findNearestSite)
				.distinct()
				.count() == 1;
	}

	//takes quad, splits into 4, returns
	private @NotNull List<Quad> subdivideQuad(@NotNull Quad quad) {
		Point n, ne, nw, e, s, se, sw, w, c;
		Quad tr, tl, br, bl;
		List<Quad> subdivision = new ArrayList<>();
		//find points
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

		//create new quads
		{
			tl = new Quad(nw, w, n, c);
			tr = new Quad(n, c, ne, e);
			bl = new Quad(w, sw, c, s);
			br = new Quad(c, s, e, se);
		}

		//add to queue
		{
			subdivision.add(tl);
			subdivision.add(tr);
			subdivision.add(bl);
			subdivision.add(br);
		}

		return subdivision;
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
		return point.nearestSeed = sites.stream()
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
