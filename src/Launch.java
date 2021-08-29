

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */


import lib.math.voronoi.Voronoi;

public class Launch {
	public static void main(String[] args) {
		runTests();
		//Application.launch(Main.class, args);
	}

	private static void runTests() {
		Voronoi v = new Voronoi().create(Voronoi.Algorithm.CalculateBySubDivision, 10, 10, null);
	}
}
