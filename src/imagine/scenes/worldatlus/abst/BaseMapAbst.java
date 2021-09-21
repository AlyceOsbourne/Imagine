/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.worldatlus.abst;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import lib.image.ImageTools;
import lib.math.voronoi.algorithm.Voronoi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static lib.math.voronoi.algorithm.Voronoi.Point;

public abstract class BaseMapAbst {


	//this here is the abstract definition of what my Map will be (think a J,R Tolkien map :P )

	// in this class we know what som methods will do and HOW we will ge that result

	final int width, height;
	public final WritableImage image;
	final boolean debugMode = true;
	final double continentMapScale;
	final double regionMapScale;
	final double biomeMapScale;
	final double featureMapScale;
	final int numberOfContinents = 3;
	final PixelWriter writer;
	final Random rand = new Random();
	Voronoi noiseMap, continentMap, regionMap, biomeMap, featureMap;
	List<Point> sites;
	List<Area> continentAreas;
	List<Area> regionAreas;

	public BaseMapAbst(int width, int height, List<Point> sites) {
		this.width = width;
		this.height = height;
		this.sites = sites;
		continentMapScale = 0.002;
		regionMapScale = continentMapScale * 10;
		biomeMapScale = regionMapScale * 10;
		featureMapScale = regionMapScale * 10;
		image = new WritableImage(width, height);
		writer = image.getPixelWriter();
	}

	public BaseMapAbst startGeneration() {
		generateNoiseMap();
		drawNoiseMap();
		//generateContinentMap();
		//drawContinentMap();
		//generateRegionMap();
		//drawRegionMap();
		//generateFeatureMap();
		//drawFeatureMap();
		//applyToneMap();
		//decorate();
		return this;
	}

	public void generateNoiseMap() {
		noiseMap = new Voronoi(width, height, sites, 0.0002, debugMode);

	}

	public void drawNoiseMap() {
		noiseMap.getSites().forEach(p -> {
			Color c = Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
			noiseMap.getCells().get(p).forEach(cell -> writer.setColor(cell.x, cell.y, c));
		});
	}

	public void generateContinentMap() {
		List<Point> continentSites = new ArrayList<>();
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
		continentMap = new Voronoi(width, height, continentSites, 0.02, debugMode);
		for (Point site : continentMap.getSites()) {
			Area area = new Area(site);
			for (Point point : continentMap.getCells().get(site)) {
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

	public void drawContinentMap() {
		for (Area r : continentAreas) {
			for (Point p : r.cells) {
				if (r.isLand) writer.setColor(p.x, p.y, Color.BROWN);
				else writer.setColor(p.x, p.y, Color.SEAGREEN);
			}
		}
	}

	public void generateRegionMap() {
		List<Point> regionSites = new ArrayList<>();
		regionAreas = new ArrayList<>();
		int n = rand.nextInt((int) (continentMap.getSites().size() * regionMapScale));
		for (Area area : continentAreas) {
			if (area.isLand) {
				for (Point cell : area.cells) {
					if (noiseMap.getSites().contains(cell))
						regionSites.add(cell);
				}
			}
		}
		regionMap = new Voronoi(width, height, regionSites, 0.002, debugMode);

		for (Point site : regionMap.getSites()) {
			Area area = new Area(site);
			for (Point cell : regionMap.getCells().get(site)) {
				if (noiseMap.getSites().contains(cell)) {
					area.addCells(noiseMap.getCells().get(cell));
				}
			}
			regionAreas.add(area);
		}
	}

	public void drawRegionMap() {
	}

	public void generateFeatureMap() {

	}

	public void drawFeatureMap() {

	}

	public void applyToneMap() {

	}

	public void decorate() {

	}

	public void generateBiomeMap() {

	}

	public void drawBiomeMap() {

	}

	public void saveImage() {
		ImageTools.exportToImageFile(image);
	}

	class Area {

		final Point site;
		final List<Point> cells = new ArrayList<>();

		boolean isLand = false, isCold = false, isHot = false, isTemperate = false;

		Area(Point site) {
			this.site = site;
		}

		void addCells(List<Point> cells) {
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
