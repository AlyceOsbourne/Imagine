





/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.worldatlus.maps.city;

import imagine.scenes.worldatlus.data.MapImage;
import lib.math.voronoi.algorithm.Voronoi;

import java.util.List;

public class CityMapImage extends MapImage {


	CityMapImage(List<Voronoi.Point> sites) {
		super(sites);
	}
}
