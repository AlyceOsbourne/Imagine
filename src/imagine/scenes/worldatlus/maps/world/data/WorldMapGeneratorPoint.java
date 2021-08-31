/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.worldatlus.maps.world.data;

import lib.math.voronoi.algorithm.Voronoi;

public class WorldMapGeneratorPoint extends Voronoi.Point {

	int
			SEA_LEVEL = 40,
			MOUNTAIN_HEIGHT = 80,
			IS_DRY = 40,
			IS_WET = 80,
			IS_FROZEN = 0,
			IS_COLD = 10,
			IS_TEMPERATE = 20,
			IS_HOT = 30;


	int moisture;

	int temperature;

	int elevation;

	TerrainType type;

	WorldMapGeneratorPoint(int x, int y, int moisture, int temperature, int elevation) {
		super(x, y);
		this.moisture = moisture;
		this.temperature = temperature;
		this.elevation = elevation;
		setTerrainType();
	}

	private void setTerrainType() {


		if (this.elevation < SEA_LEVEL) this.type = TerrainType.OCEAN;
		else if (this.elevation > MOUNTAIN_HEIGHT) this.type = TerrainType.MOUNTAIN;

		else {

			if (this.temperature <= IS_FROZEN) {
				if (this.moisture >= IS_WET) this.type = TerrainType.ARCTIC;
				else if (this.moisture <= IS_DRY) this.type = TerrainType.TIAGA;
				else this.type = TerrainType.TUNDRA;
			} else if (this.temperature <= IS_COLD) {
				if (this.moisture >= IS_WET) {
				} else if (this.moisture <= IS_DRY) {
				} else {
				}

			} else if (this.temperature <= IS_TEMPERATE) {
				if (this.moisture >= IS_WET) {
				} else if (this.moisture <= IS_DRY) {
				} else {
				}
			} else if (this.temperature <= IS_HOT) {
				if (this.moisture >= IS_WET) {
				} else if (this.moisture <= IS_DRY) {
				} else {
				}
			} else {
				if (this.moisture >= IS_WET) {
				} else if (this.moisture <= IS_DRY) {
				} else {
				}
			}
		}
	}

	@Override
	public String toString() {
		return this.type.name();
	}

	enum TerrainType {
		OCEAN,
		SAVANNA,
		TIAGA,
		TUNDRA,
		JUNGLE,
		PLAIN,
		ARCTIC,
		MOUNTAIN
	}

	class TerrainData extends PointData {
	}
}
