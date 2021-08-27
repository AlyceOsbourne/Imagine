

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */


import imagine.Main;
import javafx.application.Application;
import lib.math.voronoi.Voronoi;

public class Launch {
	public static void main(String[] args) {
		runTests();
		Application.launch(Main.class, args);
	}

	private static void runTests() {
		Voronoi v = Voronoi.create(Voronoi.Algorithm.CalculateBySubDivision, 1024, 768, null);
	}
}
