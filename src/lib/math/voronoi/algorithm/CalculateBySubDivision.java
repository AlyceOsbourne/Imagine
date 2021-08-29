
/*
 * Do what the F**k you want
 */

/*
 Based on and owned by : https://www.youtube.com/watch?v=8mjUUNi1AaA&ab_channel=MichiganSpaceGrantConsortium
 */

package lib.math.voronoi.algorithm;

import lib.math.voronoi.Point;
import lib.math.voronoi.Utils;
import lib.math.voronoi.Voronoi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class CalculateBySubDivision<Data extends Point> extends Voronoi {

	Logger log = Logger.getLogger(this.getClass().getSimpleName());

	Point[][] voronoiMatrix;

	/**
	 * The inputted sites to process.
	 */
	List<Data> sites = new ArrayList<>();
	List<Quad> toProcess = new ArrayList<>();


	/**
	 * initiates calculating a voronoi diagram by a subdivision algorithm.
	 * checks sites to make sure they are in bounds, discards those outside and then adds the remaining to the matrix
	 * plus stores sites in a list for faster lookup
	 */
	public CalculateBySubDivision(int width, int height, List<Data> sitesIn) {
		voronoiMatrix = new Point[width][height];
		for (int i = 0; i < voronoiMatrix.length; i++)
			for (int j = 0; j < voronoiMatrix[i].length; j++)
				voronoiMatrix[i][j] = new Point(i, j);


		for (Data site : sitesIn) {
			if (site.x < width - 1 && site.y < height - 1 && site.x >= 0 && site.y >= 0) {
				sites.add(site);
				site.isSeed = true;
				voronoiMatrix[site.x][site.y] = site;
				System.out.println("Added site (" + site.x + "," + site.x + ")");
			} else log.info("Discarded site due to it being out of range");
		}
		start();
		log.info("Finished");
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
						voronoiMatrix[xMin][yMax],
						voronoiMatrix[xMin][yMin],
						voronoiMatrix[xMax][yMax],
						voronoiMatrix[xMax][yMin]
				)
		);

		int Cycle = 1;
		while (!toProcess.isEmpty()) {
			System.out.println("Starting Cycle: " + Cycle++);

			//Ok this line calculates all quads in parallel
			//may be a better function to use here, but we'll see
			toProcess.stream().parallel().findFirst().ifPresent(this::checkAndSubdivide);

			//this line is for testing, runs calculations in sequence, makes logs more readable for debugging
			//for (Quad q : toProcess) checkAndSubdivide(q);


		}

	}

	/**
	 * passes the quad the checkQuad, if it passes this should be the end of the line for that quad, otherwise it
	 * subdivides the quad into 4 sections and adds them to the process queue
	 **/
	private void checkAndSubdivide(Quad quad) {
		toProcess.remove(quad);
		if (!checkQuad(quad) && quad.size() > 1) subDivideQuad(quad);
		else {
			System.out.println("found quad with a size of 1, calculating single point");
			checkSingleCell(quad.nw);
		}
	}

	private void checkSingleCell(Point p) {
		getNearestSite(p);
	}

	/**
	 * checks quad corners for their nearest site, comparing against a list of sites to squeeze out some more speed.
	 * Then passes along to areSitesEqual to perform equality check
	 **/
	private boolean checkQuad(Quad quad) {
		//collect nearest site for each corner of quad
		Point nearestSiteNE = getNearestSite(quad.ne);
		Point nearestSiteNW = getNearestSite(quad.nw);
		Point nearestSiteSE = getNearestSite(quad.se);
		Point nearestSiteSW = getNearestSite(quad.sw);
		return areSitesEqual(nearestSiteNE, nearestSiteNW, nearestSiteSE, nearestSiteSW);
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

		n = Utils.midpoint(nw, ne);
		e = Utils.midpoint(se, ne);
		s = Utils.midpoint(sw, se);
		w = Utils.midpoint(sw, nw);

		center = Utils.midpoint(Utils.midpoint(n, s), Utils.midpoint(e, w));
		//creating the 4 quads and adding them to the list to return

		nwCorner = new Quad(nw, w, n, center);
		subdivision.add(nwCorner);
		swCorner = new Quad(w, sw, center, s);
		subdivision.add(swCorner);
		neCorner = new Quad(n, center, ne, e);
		subdivision.add(neCorner);
		seCorner = new Quad(center, s, e, se);
		subdivision.add(seCorner);

		for (Quad q : subdivision) {
			System.out.println(q + "created with corners");
			System.out.println("North East: (" + q.ne.x + "," + q.ne.y + ")");
			System.out.println("South East: (" + q.se.x + "," + q.se.y + ")");
			System.out.println("North West: (" + q.nw.x + "," + q.nw.y + ")");
			System.out.println("South West: (" + q.sw.x + "," + q.sw.y + ")");
		}
		//return constructed list
		toProcess.addAll(subdivision);
	}

	private Point getNearestSite(Point p) {

		System.out.println("Searching for nearest site too (" + p.x + "," + p.y + ")");
		//starting with a node that should be well outside the diagram, just so we have something to check against in the first cycle
		Point currentClosestSite = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

		//well clearly if this point is a site its nearest site is itself
		if (p.isSeed) {
			return p;
		}

		//line to cute down some method calls, means each tested point only needs to do a distance check once
		else if (p.nearestSeed != null) {
			return p.nearestSeed;
		} else {
			for (Point site : sites) {
				//if new site is closer than current replace current and assigns the points nearest site
				if (site == p.nearestSeed) {
					if (new Random().nextBoolean())
						continue;
				}
				if (p.distance(site) < p.distance(currentClosestSite))
					currentClosestSite = site;
			}
		}

		p.nearestSeed = currentClosestSite;

		System.out.println("Nearest site found at (" + currentClosestSite.x + "," + currentClosestSite.y + ")");

		return currentClosestSite;
	}

	/**
	 * compares a quads sites and make sure they are equal, if true then assign all points in said quad to the site
	 **/
	private boolean areSitesEqual(Point nearestSiteNE, Point nearestSiteNW, Point nearestSiteSE, Point nearestSiteSW) {

		boolean passCheck;
		System.out.println("Checking sites for equality");
		System.out.println("Sites:");
		System.out.println(nearestSiteNE.printCoords() + " " + nearestSiteNW.printCoords());
		System.out.println(nearestSiteSE.printCoords() + " " + nearestSiteSW.printCoords());
		if ((nearestSiteNE.areEqual(nearestSiteSE))
				&& (nearestSiteSE.areEqual(nearestSiteSW))
				&& (nearestSiteSW.areEqual(nearestSiteNW))) {
			passCheck = true;
			log.finer("Sites are Equal, flagging points");
		} else {
			log.severe("Sites are Not Equal");
			passCheck = false;
		}
		return passCheck;
	}

	public Point[][] getMatrix() {
		return this.voronoiMatrix;
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
