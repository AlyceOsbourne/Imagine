/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.worldatlus;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import lib.image.ImageTools;
import lib.math.voronoi.algorithm.Voronoi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Map {
	private final int width, height;
	Voronoi v;
	WritableImage image;

	public Map(int width, int height, List<Voronoi.Point> sites) {
		this.width = width;
		this.height = height;
		v = new Voronoi(width, height, sites, true);
		image = new WritableImage(width, height);
	}

	public Map populateWithColor() {
		PixelWriter writer = image.getPixelWriter();
		for (Voronoi.Point site : v.getSites()) {
			Color c = getRandomColor();
			for (Voronoi.Point point : v.getCells().get(site)) {
				writer.setColor(point.x, point.y, c);
			}
		}
		return this;
	}

	public Map drawPoints() {
		PixelWriter writer = image.getPixelWriter();
		for (Voronoi.Point site : v.getSites()) {
			writer.setColor(site.x, site.y, Color.BLACK);
		}
		return this;
	}

	public Color getRandomColor() {
		Random rnd = new Random();
		return Color.color(rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble(), 1.0D);
	}

	public Map drawRegions() {
		PixelWriter writer = image.getPixelWriter();
		for (Region region : regions()) {
			for (Voronoi.Point point : region.cells) {
				writer.setColor(point.x, point.y, region.type.regionColor);
			}
		}
		return this;
	}

	public List<Region> regions() {
		List<RegionType> types = new ArrayList<>(regionTypes());
		List<Region> regions = new ArrayList<>(types.size());
		List<Voronoi.Point> regionSites = new ArrayList<>(types.size());
		Random r = new Random(((long) v.getSites().get(1).x * v.getSites().get(v.getSites().size()).y) * types.size() * new Random().nextInt());
		for (RegionType type : types) {
			int n = r.nextInt(v.getSites().size());
			if (regionSites.size() == 0) {
				regionSites.add(v.getSites().get(n));
				regions.add(new Region(type, v.getSites().get(n)));
			} else {
				while (regionSites.contains(v.getSites().get(n))) {
					n = r.nextInt(v.getSites().size());
				}
				regionSites.add(v.getSites().get(n));
				regions.add(new Region(type, v.getSites().get(n)));
			}
			//assign a site to the region to act as the parent, then populate secondary voronoi diagram from those sites, then collect all sites from noise map, then add all points in cells that own to noise maps site to regions cells
		} // assigning regions their Site
		Voronoi regionVoronoi = new Voronoi(width, height, regionSites, false);
		java.util.Map<Voronoi.Point, List<Voronoi.Point>> cells = regionVoronoi.getCells();
		for (Region region : regions) {

		}


		return regions;
	}

	//so have cells have a few basic attributes and maybe some abstract methods for collecting map specific data
	public abstract List<? extends RegionType> regionTypes();

	public void saveImage() {
		ImageTools.exportToImageFile(image);
	}

	public abstract static class RegionType {
		Color regionColor;

		RegionType(Color regionColor) {
			this.regionColor = regionColor;
		}
	}

	public static class Region {
		RegionType type;

		Voronoi.Point regionSite;
		List<Voronoi.Point> cells;

		Region(RegionType type, Voronoi.Point regionSite) {
			this.type = type;
			this.regionSite = regionSite;
		}

		void populateRegion(List<Voronoi.Point> cells) {
			this.cells = cells;
		}
	}

}
