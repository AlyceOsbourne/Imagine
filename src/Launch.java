

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */


import imagine.scenes.worldatlus.maps.world.data.WorldMapGeneratorPoint;
import lib.math.voronoi.Voronoi;

public class Launch {

	public static void main(String[] args) {
		runTests();
		//Application.launch(Main.class, args);
	}

	private static void runTests() {
		var v = new Voronoi<WorldMapGeneratorPoint>().create(
				Voronoi.Algorithm.CalculateBySubDivision,
				Voronoi.Resolution.TEST,
				null);

	}
}
