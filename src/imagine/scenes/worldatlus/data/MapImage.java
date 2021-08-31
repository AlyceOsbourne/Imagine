


/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.worldatlus.data;


import javafx.scene.image.WritableImage;
import lib.math.voronoi.algorithm.Voronoi;

import java.util.List;


public abstract class MapImage {

	boolean debugMode = false;
	String mapName;

	int width;
	int height;


	boolean hasTitle;
	boolean hasBorder;

	Byte[][] Layers;

	WritableImage image;

	BorderStyle borderstyle;

	Background background;

	LineColour linecolour;

	LineStyle linestyle;

	TitleStyle titlestyle;

	TitleFont titlefont;

	enum Background{}

	enum LineColour {}

	enum LineStyle {}

	enum BorderStyle {}

	enum TitleFont {}

	enum TitleStyle {}

	<Data extends Voronoi.Point> Voronoi<Data> generateVoronoi(List<Voronoi.Point> sites) {
		return new Voronoi<>(width, height, sites, debugMode);
	}
}
