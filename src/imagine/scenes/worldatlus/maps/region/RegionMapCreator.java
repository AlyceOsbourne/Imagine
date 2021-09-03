





/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.worldatlus.maps.region;

import imagine.scenes.worldatlus.data.MapCreator;
import lib.math.voronoi.algorithm.Voronoi;

import java.util.List;

public class RegionMapCreator extends MapCreator<RegionMapImage> {

	protected RegionMapCreator(int width, int height, List<Voronoi.Point> sites) {
		super(width, height, new RegionMapImage(sites));
	}

	@Override
	public void draw() {

	}

	@Override
	public void clear() {

	}

	@Override
	public void random() {

	}

	@Override
	public void save() {

	}

	@Override
	public void load() {

	}

	@Override
	public void loadControls() {

	}
}
