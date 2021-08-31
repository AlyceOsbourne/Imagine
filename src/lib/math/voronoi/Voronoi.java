


/*
 * Do what the F**k you want
 */

package lib.math.voronoi;

import lib.math.voronoi.algorithm.CalculateBySubDivision;
import lib.math.voronoi.algorithm.JumpFlood;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Voronoi<Data extends Point> {

	boolean debug = false;

	int VORONOI_SCALE_FACTOR = 1;

	public Voronoi<Data> create(Algorithm algorithm, Resolution res, @Nullable List<Data> sites, boolean debug) {
		return create(algorithm, res.width, res.height, sites, debug);
	}

	@SuppressWarnings("unchecked")
	public Voronoi<Data> create(Algorithm algorithm, int widthIn, int heightIn, @Nullable List<Data> sites, boolean debug) {

		if (debug) {
			System.out.println("Processing image with width of " + widthIn + " and a height of " + heightIn);

			System.out.println("Voronoi scale is " + VORONOI_SCALE_FACTOR + ":1");
		}

		int width = widthIn / VORONOI_SCALE_FACTOR;

		int height = heightIn / VORONOI_SCALE_FACTOR;

		//these are some test lines to populate the site list.
		List<Data> p = sites;
		if (p == null || p.isEmpty()) {
			p = new ArrayList<>();
			Random r = new Random();
			int bound = width - height;
			for (int i = 0; i < bound; i++) {
				Data randomize = (Data) new Point(r.nextInt(width - 1), r.nextInt(height - 1));
				p.add(randomize);
			}
			if (debug) System.out.println("Number of sites " + p.size());
		}

		switch (algorithm) {

			case CalculateByJumpFlood -> {
				return new JumpFlood<Data>(width, height, p, debug);
			}

			case CalculateBySubDivision -> {
				return new CalculateBySubDivision<>(width, height, p, debug);
			}

			default -> {
				return null;
			}
		}
	}


	public enum Algorithm {CalculateByJumpFlood, CalculateBySubDivision}

	public enum Resolution {

		HIGH(1920, 1200),
		MEDIUM(1024, 768),
		LOW(512, 384),
		TESTXL(10000, 10000),
		TESTXS(200, 200);


		int width, height;

		Resolution(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}
}




