/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.worldatlus;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import lib.image.ImageTools;
import lib.math.voronoi.algorithm.Voronoi;

import java.util.List;
import java.util.Random;

public class Map {
	Voronoi v;
	WritableImage image;

	public Map(int width, int height, List<Voronoi.Point> sites) {
		v = new Voronoi(width, height, sites, true);
		image = new WritableImage(width, height);
	}

	public Map populateWithColor() {
		PixelWriter writer = image.getPixelWriter();
		for (Voronoi.Point site : v.getSites()) {
			Color c = getRandomColor();
			for (Voronoi.Point point : v.getRegions().get(site)) {
				writer.setColor(point.x, point.y, c);

			}
		}
		return this;
	}

	public Color getRandomColor() {
		Random rnd = new Random();
		return Color.color(rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble(), 1.0D);
	}

	public void saveImage() {
		ImageTools.exportToImageFile(image);
	}

}
