/*
 * Do what the F**k you want
 */

package lib.math.voronoi.algorithm;

import lib.math.voronoi.algorithm.data.nodes.Point;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VoronoiV2Builder {
	private int width;
	private int height;
	private @Nullable List<Point> sites;
	private double scale;

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
		return new VoronoiV2(width, height, sites, scale);
	}
}