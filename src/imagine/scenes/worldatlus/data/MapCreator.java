


/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.worldatlus.data;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import lib.fxml.LoadsFXML;
import lib.image.ImageTools;
import lib.math.voronoi.algorithm.Voronoi;
import lib.math.voronoi.algorithm.Voronoi.Point;

import java.util.List;
import java.util.Map;

public abstract class MapCreator<CreatedMap extends MapImage> extends AnchorPane implements LoadsFXML, ImageTools {
	CreatedMap map;
	Voronoi v;

	Point[][] matrix;
	int width, height;

	@FXML
	AnchorPane MenuBar, CanvasPane;

	Canvas canvas = new Canvas();

	Map<Point, List<Point>> regions;

	protected MapCreator(int width, int height, CreatedMap map) {
		this.width = width;
		this.height = height;
		this.map = map;
		v = map.v;
		matrix = v.getMatrix();
		regions = v.getRegions();
	}

	void exportToSaveDataMap(Map<String, CreatedMap> saveDataMap) {
		saveDataMap.put(map.mapName, map);
	}

	public abstract void draw();

	public abstract void clear();

	public abstract void random();

	public abstract void save();

	public abstract void load();


	{loadFXML();} //todo make sure that running it here in the abstract class doesn't break the FXMLLoading
}
