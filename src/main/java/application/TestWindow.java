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

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class TestWindow extends LazyWindow {
	static int
			width = 1980,
			height = 1080;
	static VoronoiV2 v;

	static {
		v = new VoronoiV2Builder().setWidth(width).setHeight(height).setSites(null).setScale(1).createVoronoiV2();
	}

	@Override
	protected Node content() {
		List<Point> sites = v.getSites();
		Map<Point, Color> palette = new ConcurrentHashMap<>();
		Random r = new Random();
		for (Point site : sites) {
			Color c = Color.color(r.nextDouble(), r.nextDouble(), r.nextDouble());
			palette.put(site, c);
		}
		WritableImage image = new WritableImage(width, height);
		PixelWriter buffer = image.getPixelWriter();
		Point[][] matrix = v.getMatrix();
		for (Point[] row : matrix)
			for (Point column : row)
				buffer.setColor(column.x, column.y, palette.get(column.nearestSeed));
		return new ImageView(image);
	}
}
