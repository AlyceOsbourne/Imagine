





/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.worldatlus.maps.dungeon;

import imagine.scenes.worldatlus.data.MapImage;
import lib.math.voronoi.algorithm.Voronoi;

import java.util.List;

public class DungeonMapImage extends MapImage {

	public DungeonMapImage(List<Voronoi.Point> sites) {
		super(sites);
	}
}
