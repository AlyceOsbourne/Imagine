


/*
 * Do what the F**k you want
 */

package lib.math.voronoi;

import lib.math.voronoi.algorithm.CalculateBySubDivision;
import lib.math.voronoi.algorithm.JumpFlood;
import lib.math.voronoi.datasubtypes.Point;

import java.util.List;
import java.util.Random;

import static java.util.stream.IntStream.range;

public class Voronoi {

	static Voronoi create(Algorithm algorithm, int width, int height ,List<Point> sites) {

		Voronoi diagram;

		if (sites.isEmpty()) {
			Random r = new Random();
			range(0, r.nextInt(width * height / 4)).mapToObj(i ->
					new Point(0, 0).randomize(width, height)).forEach(sites::add);
		}

		switch (algorithm) {

			case CalculateByJumpFlood -> diagram = new JumpFlood(width, height, sites);

			case CalculateBySubDivision -> diagram = new CalculateBySubDivision(width, height, sites);

			default -> diagram = null;
		}

		return diagram;
	}
	enum Algorithm { CalculateByJumpFlood, CalculateBySubDivision }
	}




