

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */


import imagine.Main;
import javafx.application.Application;
import lib.math.voronoi.Voronoi;
import lib.math.voronoi.algorithm.CalculateBySubDivision;

public class Launch {
	public static void main(String[] args) {
		runTests();
		Application.launch(Main.class, args);
	}

	private static void runTests() {
		var matrix = ((CalculateBySubDivision<?>) new Voronoi().create(Voronoi.Algorithm.CalculateBySubDivision, 1024, 768, null)).getMatrix();
		for (lib.math.voronoi.Point[] points : matrix) {
			for (lib.math.voronoi.Point point : points) {
				System.out.println(point.nearestSeed);
			}
		}
	}
}
