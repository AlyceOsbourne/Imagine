/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.worldatlus.abst;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import lib.math.voronoi.algorithm.Voronoi;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseMapAbst {

	final int width, height;
	boolean debugMode = true;
	Voronoi noiseMap, continentMap, regionMap, biomeMap, featureMap;
	WritableImage image;
	double continentMapScale, regionMapScale, biomeMapScale, featureMapScale;

	int numberOfContinents = 3;

	List<Voronoi.Point> sites;

	PixelWriter writer;
	Random rand = new Random();
	List<Area> continentAreas;
	List<Area> regionAreas;

	public BaseMapAbst(int width, int height, List<Voronoi.Point> sites) {
		this.width = width;
		this.height = height;
		this.sites = sites;
		continentMapScale = (0.002);
		regionMapScale = continentMapScale * 10;
		biomeMapScale = regionMapScale * 10;
		featureMapScale = regionMapScale * 10;
		image = new WritableImage(width, height);
		writer = image.getPixelWriter();

	}

	private void startGeneration() {
		generateNoiseMap(sites);
		generateContinentMap();
		drawContinents();
		generateRegionMap();
	}

	//creates a voronoi diagram that acts as our noise map, this will allow for random distribution, I may create a method to distort the matrix for more realistic terrain
	private void generateNoiseMap(@Nullable List<Voronoi.Point> sites) {

		noiseMap = new Voronoi(width, height, sites, debugMode);
		Voronoi.Point[][] matrix = noiseMap.getMatrix();
		for (Voronoi.Point p : noiseMap.getSites()) {
			for (Voronoi.Point cell : noiseMap.getCells().get(p)) {
				Voronoi.Point left, right, up, down;
				int x, y;
				x = cell.x;
				y = cell.y;
				if (++x <= width - 1)
					up = matrix[--x][y].nearestSeed;
				else up = p.nearestSeed;
				if (--x >= 0)
					down = matrix[--x][y].nearestSeed;
				else down = p.nearestSeed;
				if (y++ <= width - 1)
					right = matrix[x][++y].nearestSeed;
				else right = p.nearestSeed;
				if (--y >= 0)
					left = matrix[x][--y].nearestSeed;
				else left = p.nearestSeed;

				if ((up == down) && (left == right)) {
					int r = rand.nextInt(3);
					switch (r) {
						case 0:
							cell.nearestSeed = up;

						case 1:
							cell.nearestSeed = down;

						case 2:
							cell.nearestSeed = left;

						case 3:
							cell.nearestSeed = right;
					}
				} else if (up == down) cell.nearestSeed = up;

				else if (left == right) cell.nearestSeed = left;

				noiseMap.getCells().get(p).remove(cell);
				noiseMap.getCells().get(cell.nearestSeed).add(cell);
			}
		}
	}

	void generateContinentMap() {
		List<Voronoi.Point> continentSites = new ArrayList<>();
		continentAreas = new ArrayList<>();
		int n = rand.nextInt((int) (noiseMap.getSites().size() * continentMapScale));
		for (int i = 0; i < n; i++) {
			int siteIndex;
			do {
				siteIndex = rand.nextInt(noiseMap.getSites().size() - 1);
			}
			while (continentSites.contains(noiseMap.getSites().get(siteIndex)));
			continentSites.add(noiseMap.getSites().get(siteIndex));
		}
		continentMap = new Voronoi(width, height, continentSites, debugMode);
		for (Voronoi.Point site : continentMap.getSites()) {
			Area area = new Area(site);
			for (Voronoi.Point point : continentMap.getCells().get(site)) {
				if (noiseMap.getSites().contains(point)) {
					area.addCells(noiseMap.getCells().get(point));
				}
			}
			continentAreas.add(area);
		}
		for (int i = 0; i < numberOfContinents; i++) {
			int index;
			do {
				index = rand.nextInt(continentAreas.size() - 1);
			}
			while (continentAreas.get(index).isLand);
			continentAreas.get(index).setIsLand();
		}
	}

	void drawContinents() {
		for (Area r : continentAreas) {
			for (Voronoi.Point p : r.cells) {
				if (r.isLand) writer.setColor(p.x, p.y, Color.BROWN);
				else writer.setColor(p.x, p.y, Color.SEAGREEN);
			}
		}
	}

	void generateRegionMap() {
		List<Voronoi.Point> regionSites = new ArrayList<>();
		regionAreas = new ArrayList<>();
		int n = rand.nextInt((int) (continentMap.getSites().size() * regionMapScale));
		for (Area area : continentAreas) {
			if (area.isLand) {
				for (Voronoi.Point cell : area.cells) {
					if (noiseMap.getSites().contains(cell))
						regionSites.add(cell);
				}
			}
		}
		regionMap = new Voronoi(width, height, regionSites, debugMode);

		for (Voronoi.Point site : regionMap.getSites()) {
			Area area = new Area(site);
			for (Voronoi.Point cell : regionMap.getCells().get(site)) {
				if (noiseMap.getSites().contains(cell)) {
					area.addCells(noiseMap.getCells().get(cell));
				}
			}
			regionAreas.add(area);
		}
	}

	void DrawRegionMap() {
	}

	void generateBiomeMap() {
	}

	void drawBiomeMap() {
	}

	void generateFeatureMap() {
	}

	void drawFeatureMap() {
	}

	void applyToneMap() {
	}

	void decorate() {
	}

	class Area {

		Voronoi.Point site;
		List<Voronoi.Point> cells = new ArrayList<>();

		boolean isLand = false, isCold = false, isHot = false, isTemperate = false;

		Area(Voronoi.Point site) {
			this.site = site;
		}

		void addCells(List<Voronoi.Point> cells) {
			this.cells.addAll(cells);
		}

		void setIsLand() {
			isLand = true;
		}

		void setTemp() {
			int coldN, coldS, hotN, hotS;
			coldN = (int) (height * 0.80);
			coldS = (int) (height * 0.20);
			hotN = (int) (height * 0.60);
			hotS = (int) (height * 0.40);
			if (site.x > coldN || site.x < coldS) isCold = true;
			else if (site.x > hotS && site.x < hotN) isHot = true;
			else isTemperate = true;
		}


	}

}
