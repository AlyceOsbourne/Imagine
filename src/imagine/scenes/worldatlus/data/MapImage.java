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


import javafx.scene.image.WritableImage;



public abstract class MapImage {

	String mapName;

	float width;
	float height;
	float scale;

	boolean hasTitle;
	boolean hasBorder;

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
