/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.worldatlus;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import lib.math.voronoi.Voronoi;
import lib.math.voronoi.algorithm.CalculateBySubDivision;
import lib.math.voronoi.datasubtypes.Point;

public class Test {
	@FXML
	public TableView<Point> arrayView;
	Point[][] matrix;
	int width, height;

	@FXML
	Voronoi v;

	Test(int width, int height) {
		v = Voronoi.create(Voronoi.Algorithm.CalculateBySubDivision, width, height, null);
		matrix = ((CalculateBySubDivision) v).getMatrix();
	}
}
