/*
 * Do what the F**k you want
 */

package lib.math.noise;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import lib.math.voronoi.algorithm.Voronoi;
import lib.math.voronoi.algorithm.data.nodes.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Noise {
	public final Image i;
	private final int width;
	private final int height;
	Voronoi noise;
	Color[][] noiseMatrix;

	public Noise(int width, int height, double rScale, double gScale, double bScale, double aScale, int step, int jitter, int passes) {
		this.width = width;
		this.height = height;
		noiseMatrix = new Color[width][height];
		List<Point> sites = new ArrayList<>();
		Point current;
		for (int x = 0; x < width; x = x + step)
			for (int y = 0; y < height; y = y + step) {
				if (jitter > 0) {
					Random r = new Random();
					int xOut, yOut;
					do {

						xOut = x;
						xOut = Math.max(0, xOut - r.nextInt(jitter));
						xOut = Math.min(width - 1, xOut + r.nextInt(jitter));

						yOut = y;
						yOut = Math.max(0, yOut - r.nextInt(jitter));
						yOut = Math.min(height - 1, yOut + r.nextInt(jitter));

						current = new Point(xOut, yOut);
					}
					while (sites.contains(current));
				} else current = new Point(x, y);
				sites.add(current);
			}
		noise = new Voronoi(width, height, sites, 1, true);
		noiseMatrix = new Color[width][height];
		Random random = new Random();
		for (Point site : noise.getSites()) {

			double r, g, b, a;
			r = Math.max(0, Math.min(1, random.nextDouble() * rScale));
			g = Math.max(0, Math.min(1, random.nextDouble() * gScale));
			b = Math.max(0, Math.min(1, random.nextDouble() * bScale));
			a = Math.max(0, Math.min(1, random.nextDouble() * aScale));
			Color c = Color.color(r, g, b, a);
			for (Point cell : noise.getCells().get(site)) {
				noiseMatrix[cell.x][cell.y] = c;
			}
		}
		smooth(passes);
		i = getNoiseMapImage();
	}


	void smooth(int passes) {
		int x, y;
		for (int pass = 0; pass <= passes; pass++)
			for (x = 0; x < width; x++)
				for (y = 0; y < height; y++) {
					int
							xMin = Math.max(0, x - 1),
							xPlus = Math.min(width - 1, x + 1),
							ymin = Math.max(0, y - 1),
							yplus = Math.min(height - 1, y + 1);
					Color
							node = noiseMatrix[x][y],
							n = noiseMatrix[x][ymin],
							e = noiseMatrix[xMin][y],
							s = noiseMatrix[x][yplus],
							w = noiseMatrix[xPlus][y];
					double r, g, b, a;
					r = node.getRed() + n.getRed() + e.getRed() + s.getRed() + w.getRed() / 0.2;
					g = node.getGreen() + n.getGreen() + e.getGreen() + s.getGreen() + w.getGreen() / 0.2;
					b = node.getBlue() + n.getBlue() + e.getBlue() + s.getBlue() + w.getBlue() / 0.2;
					a = node.getOpacity() + n.getOpacity() + e.getOpacity() + s.getOpacity() + w.getOpacity() / 0.2;

					if (r > 1) r = 1;
					else if (r < 0) r = 0;

					if (g > 1) g = 1;
					else if (g < 0) g = 0;

					if (b > 1) b = 1;
					else if (b < 0) b = 0;

					if (a > 1) a = 1;
					else if (a < 0) a = 0;

					noiseMatrix[x][y] = Color.color(r, g, b, a);
				}
	}

	public Image getNoiseMapImage() {
		WritableImage image = new WritableImage(width, height);
		PixelWriter writer = image.getPixelWriter();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				writer.setColor(x, y, noiseMatrix[x][y]);
			}
		}
		return image;
	}

}
