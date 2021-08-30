

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */


import imagine.Main;
import javafx.application.Application;
import lib.math.voronoi.Point;
import lib.math.voronoi.Voronoi;

public class Launch {

	static boolean debuggingMode = true;
	static boolean debugVoronoi = true;

	static Voronoi.Resolution testResolution = Voronoi.Resolution.TESTXL;

	public static void main(String[] args) {

		if (debuggingMode) runTests();

		else Application.launch(Main.class, args);
	}

	private static <Data extends Point> void runTests() {

		boolean debugCalculateByDivision = true;
		boolean debugCalculateByJumpFlood = false;

		if (debugVoronoi) {
			if (debugCalculateByDivision) {
				var caclculateByDivision = new Voronoi<Data>().create(
						Voronoi.Algorithm.CalculateBySubDivision,
						testResolution,
						null,
						debugVoronoi);
			}
			if (debugCalculateByJumpFlood) {
				var calculateByJumpFlood = new Voronoi<Data>().create(
						Voronoi.Algorithm.CalculateByJumpFlood,
						testResolution,
						null,
						debugVoronoi);
			}
		}

	}
}
