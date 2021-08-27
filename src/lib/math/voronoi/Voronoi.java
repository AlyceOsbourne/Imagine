





/*
 * Do what the F**k you want
 */





/*
 * Do what the F**k you want
 */

package lib.math.voronoi;

import lib.math.voronoi.algorithm.CalculateBySubDivision;
import lib.math.voronoi.algorithm.JumpFlood;
import lib.math.voronoi.datasubtypes.Point;

import java.util.List;

public class Voronoi {

	static Voronoi create(Algorithm algorithm, int width, int height ,List<Point> sites){

		Voronoi diagram;

		switch (algorithm){

			case CalculateByJumpFlood -> diagram = new JumpFlood(width,height,sites);

			case CalculateBySubDivision -> diagram = new CalculateBySubDivision(width,height,sites);

			default -> diagram = null;
		}

		return diagram;
	}
	enum Algorithm { CalculateByJumpFlood, CalculateBySubDivision }
	}




