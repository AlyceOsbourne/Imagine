

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */


import imagine.Main;
import javafx.application.Application;
import lib.math.voronoi.algorithm.Voronoi;

public class Launch {

	static boolean debuggingMode = true;
	static boolean debugVoronoi = true;

	static Voronoi.Resolution testResolution = Voronoi.Resolution.HIGH;

	public static void main(String[] args) {

		if (debuggingMode) runTests();

		else Application.launch(Main.class, args);
	}

	private static <Data extends Voronoi.Point> void runTests() {

		if (debugVoronoi) {
			var v = new Voronoi<Data>(testResolution, null, true);
		}
	}
}
