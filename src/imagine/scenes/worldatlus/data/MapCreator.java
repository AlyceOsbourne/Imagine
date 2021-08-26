/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.worldatlus.data;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import lib.fxml.LoadsFXML;
import lib.image.ImageTools;
import lib.math.voronoi.Voronoi;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public abstract class MapCreator <CreatedMap extends MapImage> extends AnchorPane implements LoadsFXML, ImageTools {
	CreatedMap map;
	double width, height;
	Canvas[] layers;
	Voronoi v;
	protected MapCreator() {}

	void exportToSaveDataMap(Map<String, CreatedMap> saveDataMap){saveDataMap.put(map.mapName,map);}

	void exportToPng(File file){
		try {ImageIO.write(ImageTools.convertToBuffered(map.image),"png",file);}
		catch (IOException e) {e.printStackTrace();}
	}

	public abstract void draw();

	public abstract void clear();

	public abstract void random();

	public abstract void save();

	public abstract void load();
	{loadFXML();} //todo make sure that running it here in the abstract class doesn't break the FXMLLoading
}
