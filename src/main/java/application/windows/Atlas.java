package application.windows;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lib.javafx.windows.LazyWindow;
import lib.math.voronoi.algorithm.Voronoi;
import lib.math.voronoi.algorithm.Voronoi.Algorithm;
import lib.math.voronoi.algorithm.Voronoi.Algorithm.Point;

import java.util.*;
import java.util.stream.IntStream;

/*
 todo heat zones can work like the progressbar % math, if over/under the equator the distance gets calculates as a double between 0 and 1, using the heightMapNoise maps we can create some nice looking terrain, may need to rebuild the logic, but the window works nicely too now

 todo work out image wrapping so that continents line up east/west and north/southVoronoi
 */


class Atlas extends LazyWindow {

	static final Random random = new Random();
	static final Canvas canvas = new Canvas();
	static WorldNode[][] worldMatrix;

	Atlas() {
		int width = 1920 / 2, height = 1080 / 2;
		generate(width, height);

	}

	private void generate(int width, int height) {

		Algorithm regionLayer, biomeLayer;

		//prepare canvas
		canvas.setWidth(width);
		canvas.setHeight(height);

		//generate matrices
		worldMatrix = new WorldNode[width][height];
		IntStream.range(0, worldMatrix.length).forEachOrdered(x -> Arrays.setAll(worldMatrix[x], y -> new WorldNode(x, y)));

		regionLayer = createRegionLayer(width, height);
		biomeLayer = createBiomeLayer(width, height);
		String key = "elevation";

		Platform.runLater(() -> {
			prepare(regionLayer, biomeLayer);
			Platform.runLater(() -> {
				draw(key);
				//Platform.runLater(() -> ImageTools.exportToImageFile(canvas.snapshot(null, null), Imagine.stage));
			});
		});

	}

	Algorithm createRegionLayer(int width, int height) {
		var regionSites = generateSites(
				9,
				width,
				height);
		regionSites = applyJitter(regionSites, 10, width, height);
		return new Voronoi().setHeight(height).setWidth(width).setSites(regionSites).setAccuracy(100).generateVoronoi();

	}

	Algorithm createBiomeLayer(int width, int height) {
		var biomeSites = generateSites(
				60,
				width,
				height);
		biomeSites = applyJitter(biomeSites, 30, width, height);
		return new Voronoi().setHeight(height).setWidth(width).setSites(biomeSites).setAccuracy(50).generateVoronoi();
	}

	private void prepare(Algorithm regionLayer, Algorithm biomeLayer) {

		boolean wait;
		do wait = !(regionLayer.isComplete() && biomeLayer.isComplete());
		while (wait);

		Point[][]
				regionMatrix = regionLayer.getMatrix(),
				biomeMatrix = biomeLayer.getMatrix();

		WorldNode
				node, regionSiteNode, biomeSiteNode;

		Point
				region,
				regionSite,
				biome,
				biomeSite;

		String key;

		for (int x = 0; x < worldMatrix.length; x++) {
			for (int y = 0; y < worldMatrix[x].length; y++) {
				{
					node = worldMatrix[x][y];
					// apply regions and biomes
					{
						key = "region";
						{
							region = regionMatrix[x][y];
							regionSite = region.getNearestSeed();
							regionSiteNode = worldMatrix[regionSite.getX()][regionSite.getY()];
							applyRegions(node, regionSiteNode, key);
						}

						key = "biome";
						{
							biome = biomeMatrix[x][y];
							biomeSite = biome.getNearestSeed();
							biomeSiteNode = worldMatrix[biomeSite.getX()][biomeSite.getY()];
							applyBiomes(node, biomeSiteNode, key);
						}
					}
					key = "elevation";
					{
						applyElevation(regionSiteNode, biomeSiteNode, node, key);
					}

					key = "temperature";
					{
						applyTemperature(node, key, x);
					}

					key = "moisture";
					{
						applyMoisture(node, key);
					}
				}
			}
		}
	}

	private void draw(String key) {
		if (worldMatrix != null && worldMatrix.length > 0) {
			for (WorldNode[] matrix : worldMatrix)
				for (WorldNode node : matrix)
					node.getProperty(key).draw();
		}
	}

	List<Point> generateSites(int step, int width, int height) {
		List<Point> out = new Vector<>();
		int
				ratio = width / height,
				xStep = (width / step),
				yStep = (xStep * ratio);
		//starts half a step in, so it also ends a half a step from the end
		boolean doOffset = true;
		for (int x = xStep / 2; x <= worldMatrix.length; x += xStep) {
			int offset = 0;
			if (doOffset) offset = yStep / 2;
			for (int y = offset; y <= worldMatrix[x].length; y += yStep) {
				out.add(new Point(x, y));
			}
			doOffset = !doOffset;
		}
		return out;
	}

	List<Point> applyJitter(List<Point> sites, int jitterStrength, int width, int height) {
		int[] list = random.ints(sites.size(), -jitterStrength, jitterStrength).toArray();
		for (int i = 0, sitesSize = sites.size(); i < sitesSize; i++) {
			Point point = sites.get(i);
			int jitter = list[i];
			point.setX(Math.min(width - 1, Math.max(point.getX() + jitter, 0)));
			point.setY(Math.min(height - 1, Math.max(point.getY() + jitter, 0)));
		}
		return sites;
	}

	private void applyRegions(WorldNode node, WorldNode regionSiteNode, String key) {
		if (!regionSiteNode.hasProperty(key)) {
			regionSiteNode.addProperty(key,
					new Region(
							regionSiteNode,
							Color.color(
									random.nextDouble(),
									random.nextDouble(),
									random.nextDouble()),
							regionSiteNode.x,
							regionSiteNode.y));
		}
		if (!node.hasProperty(key)) {
			Color color = ((Region) regionSiteNode.getProperty(key)).color;
			node.addProperty(key, new Region(node, color, regionSiteNode.x, regionSiteNode.y));
		}
	}

	private void applyBiomes(WorldNode node, WorldNode biomeSiteNode, String key) {
		if (!biomeSiteNode.hasProperty(key)) {
			biomeSiteNode.addProperty(key,
					new Biome(
							biomeSiteNode,
							Color.color(
									random.nextDouble(),
									random.nextDouble(),
									random.nextDouble()),
							biomeSiteNode.x,
							biomeSiteNode.y));
		}
		if (!node.hasProperty(key)) {
			Biome biome = ((Biome) biomeSiteNode.getProperty(key));
			Color color = biome.color;
			node.addProperty(key, new Biome(node, color, biomeSiteNode.x, biomeSiteNode.y));
		}
	}

	private void applyElevation(WorldNode region, WorldNode biome, WorldNode node, String key) {
		if (!region.hasProperty(key)) region.addProperty(key, new Elevation(region, random.nextGaussian()));
		if (!biome.hasProperty(key)) biome.addProperty(key, new Elevation(biome, region));
		if (!node.hasProperty(key)) node.addProperty(key, new Elevation(node, biome));

	}

	private void applyTemperature(WorldNode node, String key, int x) {
		if (!node.hasProperty(key)) node.addProperty(key, new Temperature(node, worldMatrix[x].length));
	}

	private void applyMoisture(WorldNode node, String key) {
		if (!node.hasProperty(key)) node.addProperty(key, new Moisture(node));
	}

	void drawProperty(WorldNode node, String key) {
		Component component = node.getProperty(key);
		component.draw();

	}

	@Override
	protected Node content() {
		return canvas;
	}


	static class WorldNode {
		final int x;
		final int y;
		private final Map<String, Component> properties = new HashMap<>();

		WorldNode(int x, int y) {
			this.x = x;
			this.y = y;
		}

		void addProperty(String key, Component property) {
			properties.put(key, property);
		}

		void removeProperty(String key) {
			properties.remove(key);
		}

		Component getProperty(String key) {
			return properties.get(key);
		}

		@SuppressWarnings("BooleanMethodIsAlwaysInverted")
		boolean hasProperty(String key) {
			return properties.containsKey(key);
		}


	}

	abstract static class Component {
		static final GraphicsContext context = canvas.getGraphicsContext2D();
		final WorldNode node;
		final int x;
		final int y;

		Component(WorldNode node) {
			this.node = node;
			this.x = node.x;
			this.y = node.y;
		}

		public abstract void draw();
	}

	static class Region extends Component {
		final Color color;
		final WorldNode site;

		Region(WorldNode node, Color color, int siteX, int siteY) {
			super(node);
			this.color = color;
			this.site = worldMatrix[siteX][siteY];
		}

		@Override
		public void draw() {
			canvas.getGraphicsContext2D().getPixelWriter().setColor(this.x, this.y, this.color);
		}
	}

	static class Elevation extends Component {
		private final double elevation;

		Elevation(WorldNode node, double elevation) {
			super(node);

			double e = elevation;
			e = Math.tanh(e);
			e = 1 - e;
			if (e < 0.25) e = 0;
			e *= random.nextGaussian();
			e = Math.sinh(e);
			e = Math.max(0, Math.min(1, e));
			this.elevation = e;

		}

		Elevation(WorldNode node, WorldNode parent) {

			super(node);

			Elevation pProp = ((Elevation) parent.getProperty("elevation"));

			double elevation = pProp.elevation;

			if (elevation > 0) {
				elevation += random.nextGaussian() * 0.3;

				elevation = Math.sin(elevation);
			}

			if (elevation < 0.3) elevation = 0;

			this.elevation = Math.max(0, Math.min(1, elevation));
		}

		private double distance(int x1, int y1, int x2, int y2) {
			int a = x2 - x1;
			int b = y2 - y1;
			return ((a * a) + (b * b));
		}

		@Override
		public void draw() {
			context.getPixelWriter().setColor(x, y, Color.color(this.elevation, this.elevation, this.elevation));
		}

		public double getElevation() {
			return elevation;
		}
	}

	static class Temperature extends Component {
		double
				temp = 1;
		final double temperatureC = 51 * temp;
		double temperatureF = ((temperatureC * 9) / 5) + 32;

		Temperature(WorldNode node, double ySize) {
			super(node);
			double percent = (y * (1.0 / ySize));
			if (percent == 0 || percent == 100) temp = 0;
			else if (percent < 0.5) temp = percent;
			else if (percent > 0.5) temp = 1 - percent;
			temp = temp + 0.2 * random.nextDouble() - 0.4 * random.nextDouble();
			temp = Math.sqrt(temp * 1.5);
			temp = Math.sin(temp * 1.1);
			temp = Math.min(1, Math.max(0, temp));
		}

		@Override
		public void draw() {
			double r, b;
			r = temp;
			b = 1 - temp;

			context.getPixelWriter().setColor(x, y, Color.color(r, 0, b));
		}
	}

	static class Moisture extends Component {

		final double moisture;

		Moisture(WorldNode node) {
			super(node);
			double
					elevation = ((Elevation) node.getProperty("elevation")).elevation,
					temperature = ((Temperature) node.getProperty("temperature")).temp;
			moisture = Math.min(1, Math.max(0.01, 1 / elevation * temperature));

		}

		@Override
		public void draw() {
			context.getPixelWriter().setColor(x, y, Color.color(0, moisture / 2, moisture));
		}
	}

	static class Biome extends Component {
		final WorldNode site;
		final Color color;

		Biome(WorldNode node, Color color, int siteX, int siteY) {
			super(node);
			this.site = worldMatrix[siteX][siteY];
			this.color = color;
		}

		@Override
		public void draw() {
			context.getPixelWriter().setColor(x, y, color);
		}
	}

}