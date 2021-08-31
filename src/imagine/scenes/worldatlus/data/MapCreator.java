


/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.worldatlus.data;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import lib.fxml.LoadsFXML;
import lib.image.ImageTools;
import lib.math.voronoi.algorithm.Voronoi;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class MapCreator<CreatedMap extends MapImage, Data extends Voronoi.Point> extends AnchorPane implements LoadsFXML, ImageTools {
	CreatedMap map;
	int width, height;
	Canvas[] layers;


	@SuppressWarnings("unchecked")
	protected MapCreator(int width, int height, Data... sites) {
		this.width = width;
		this.height = height;
		List<Data> siteList = Arrays.stream(sites).toList();

	}

	void exportToSaveDataMap(Map<String, CreatedMap> saveDataMap) {
		saveDataMap.put(map.mapName, map);
	}

	void exportToPng(File file) {
		try {
			ImageIO.write(ImageTools.convertToBuffered(map.image), "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public abstract void draw();

	public abstract void clear();

	public abstract void random();

	public abstract void save();

	public abstract void load();
	{loadFXML();} //todo make sure that running it here in the abstract class doesn't break the FXMLLoading
}
