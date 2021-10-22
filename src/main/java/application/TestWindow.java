/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package application;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import lib.javafx.windows.LazyWindow;
import lib.math.voronoi.algorithm.Voronoi;
import lib.math.voronoi.algorithm.Voronoi.Algorithm.Point;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.stream;

public class TestWindow extends LazyWindow {
	static Point[][] matrix;

	static {
		matrix = new Voronoi()
				.setScale(1)
				.setAccuracy(100)
				.generateVoronoi();
	}


	@Override
	protected Node content() {
		Map<Point, Color> palette = new ConcurrentHashMap<>();
		Random r = new Random();
		WritableImage image = new WritableImage(matrix.length, matrix[0].length);
		PixelWriter buffer = image.getPixelWriter();
		stream(matrix)
				.parallel()
				.forEachOrdered(array -> stream(array)
						.parallel()
						.forEachOrdered(entry -> buffer.setColor(
								entry.x,
								entry.y,
								palette.computeIfAbsent(entry.nearestSeed, point -> Color.color(r.nextDouble(), r.nextDouble(), r.nextDouble())))));
		return new ImageView(image);
	}
}
