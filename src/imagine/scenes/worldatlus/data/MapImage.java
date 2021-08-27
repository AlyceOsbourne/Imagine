


/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.worldatlus.data;


import javafx.scene.image.WritableImage;


public abstract class MapImage {

	String mapName;

	double width;
	double height;
	double scale;

	boolean hasTitle;
	boolean hasBorder;

	Byte[][]Layers;

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
}
