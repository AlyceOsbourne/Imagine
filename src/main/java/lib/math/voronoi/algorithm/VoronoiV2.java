/*
 * Do what the F**k you want
 */

package lib.math.voronoi.algorithm;

import lib.math.voronoi.algorithm.data.nodes.Point;
import lib.math.voronoi.algorithm.data.nodes.Quad;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicReference;

import static lib.utilities.Utils.DistanceUtils.midpoint;

public class VoronoiV2 {
	private final Vector<Point> sites;
	int width, height;
	Point[][] matrix;
	Queue<Quad> processQueue = new ConcurrentLinkedDeque<>();

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
				double divisor = 0.0333D;
				int bound = (int) (((width * divisor) * (height * divisor)) * scale);
				for (int i = 0; i <= bound; i++) {
					Point randomize = matrix[r.nextInt(width - 1)][r.nextInt(height - 1)].isSeed();
					this.sites.add(randomize);
				}
			} else {
				//check sites are valid, discard invalid sites
				for (Point site : sites) {
					if (
							(site.x >= 0 && site.x < width)
									&&
									(site.y >= 0 && site.y < height)
					) {
						matrix[site.x][site.y] = site.isSeed();
						this.sites.add(site);
					}
				}
			}
		}

		//create initial quad
		processQueue.add(new Quad(matrix[0][0], matrix[0][height - 1], matrix[width - 1][0], matrix[width - 1][height - 1]));

		process();

	}

	private void process() {
		while (!processQueue.isEmpty()) {
			Quad quad = processQueue.poll();
			if (!checkQuad(quad)) subdivideQuad(quad);
			else {
				Point
						cornerNW = quad.nw,
						cornerSE = quad.se,
						seed = quad.ne.nearestSeed;

				assignSeed(cornerNW, cornerSE, seed);
			}
		}
	}

	boolean checkQuad(Quad quad) {
		//line to prevent fighting when two sites are of equal distance
		if (quad.size() <= 1) {
			quad.points.forEach(this::findNearestSite);
			return true;
		}
		//this line should only return true if all sites are equal, cleaner than doing individual comparisons
		return quad.points.stream().map(this::findNearestSite).unordered().distinct().count() == 1;
	}

	void subdivideQuad(Quad quad) {
		Point n, ne, nw, e, s, se, sw, w, c;
		Quad tr, tl, br, bl;

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
			processQueue.add(tl);
			processQueue.add(tr);
			processQueue.add(bl);
			processQueue.add(br);
		}
	}

	void assignSeed(Point nw, Point se, Point seed) {
		for (int x = nw.x; x <= se.x; x++) {
			for (int y = nw.y; y <= se.y; y++) {
				matrix[x][y].nearestSeed = seed;
			}
		}
	}


	//this is the most costly operation, this would benefit most when it comes to multithreading
	Point findNearestSite(Point point) {
		AtomicReference<Point> current = new AtomicReference<>();
		sites.stream().filter(site ->
				current.get() == null || site.distance(point) < current.get().distance(point)).
				forEach(current::set);
		point.nearestSeed = current.get();
		return current.get();
	}

	public List<Point> getSites() {
		return Collections.unmodifiableList(sites);
	}

	public Point[][] getMatrix() {
		return matrix;
	}
}