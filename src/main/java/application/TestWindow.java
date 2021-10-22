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
import lib.math.voronoi.algorithm.VoronoiV2;
import lib.math.voronoi.algorithm.VoronoiV2Builder;
import lib.math.voronoi.algorithm.data.nodes.Point;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.stream;

public class TestWindow extends LazyWindow {
	static VoronoiV2 v;

	static {
		v = new VoronoiV2Builder().setScale(10).setAccuracy(1).createVoronoiV2();
	}

	@Override
	protected Node content() {
		Map<Point, Color> palette = new ConcurrentHashMap<>();
		Random r = new Random();
		Point[][] matrix = v.getMatrix();
		WritableImage image = new WritableImage(matrix.length, matrix[0].length);
		PixelWriter buffer = image.getPixelWriter();
		stream(matrix).parallel().forEachOrdered(array -> stream(array).parallel().forEachOrdered(entry -> buffer.setColor(
				entry.x,
				entry.y,
				palette.computeIfAbsent(entry.nearestSeed, point -> Color.color(r.nextDouble(), r.nextDouble(), r.nextDouble())))));
		return new ImageView(image);
	}
}
