


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

public class Voronoi {
	public Voronoi create(Algorithm algorithm, int width, int height, @Nullable List<Point> sites) {

		//these are some test lines to populate the site list.
		@Nullable List<Point> p = sites;
		if (p == null || p.isEmpty()) {
			p = new ArrayList<>();
			Random r = new Random();
			int bound = 4;
			for (int i = 0; i < bound; i++) {
				Point randomize = new Point(r.nextInt(width - 1), r.nextInt(height - 1));
				p.add(randomize);
			}
			System.out.println("Number of sites " + p.size());
		}

		switch (algorithm) {

			case CalculateByJumpFlood -> {
				return new JumpFlood<>(width, height, p);
			}

			case CalculateBySubDivision -> {
				return new CalculateBySubDivision<>(width, height, p);
			}

			default -> {
				return null;
			}
		}
	}
	public enum Algorithm {CalculateByJumpFlood, CalculateBySubDivision}
}




