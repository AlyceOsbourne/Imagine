


/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.worldatlus.data;


import javafx.scene.image.WritableImage;
import lib.math.voronoi.algorithm.Voronoi;
import lib.math.voronoi.algorithm.Voronoi.Point;

import java.util.List;


public abstract class MapImage {
	Voronoi v;
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

	public MapImage(List<Point> sites) {
		v = generateVoronoi(sites);
	}

	enum Background {}

	enum LineColour {}

	enum LineStyle {}

	enum BorderStyle {}

	enum TitleFont {}

	enum TitleStyle {}

	Voronoi generateVoronoi(List<Point> sites) {
		return new Voronoi(width, height, sites, debugMode);
	}
}
