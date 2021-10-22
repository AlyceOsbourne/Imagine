/*
 * Do what the F**k you want
 */

package lib.math.voronoi.algorithm;

import lib.math.voronoi.algorithm.data.nodes.Point;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VoronoiV2Builder {
	private int width = 1920;
	private int height = 1080;
	private @Nullable List<Point> sites = null;
	private double scale = 1;
	private double accuracy = 100;

	public VoronoiV2Builder setAccuracy(double accuracy) {
		this.accuracy = accuracy;
		return this;
	}

	public VoronoiV2Builder setWidth(int width) {
		this.width = width;
		return this;
	}

	public VoronoiV2Builder setHeight(int height) {
		this.height = height;
		return this;
	}

	public VoronoiV2Builder setSites(@Nullable List<Point> sites) {
		this.sites = sites;
		return this;
	}

	public VoronoiV2Builder setScale(double scale) {
		this.scale = scale;
		return this;
	}

	public VoronoiV2 createVoronoiV2() {
		return new VoronoiV2(width, height, sites, scale, accuracy);
	}
}