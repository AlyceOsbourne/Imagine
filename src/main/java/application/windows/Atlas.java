package application.windows;

import com.raylabz.opensimplex.OpenSimplexNoise;
import com.raylabz.opensimplex.Range;
import com.raylabz.opensimplex.RangedValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lib.javafx.App;
import lib.javafx.windows.LazyWindow;
import lib.math.voronoi.algorithm.Voronoi;
import lib.math.voronoi.algorithm.data.nodes.Point;
import lib.utilities.Utils;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import static javafx.application.Platform.runLater;

/*
 todo heat zones can work like the progressbar % math, if over/under the equator the distance gets calculates as a double between 0 and 1, using the heightMapNoise maps we can create some nice looking terrain, may need to rebuild the logic, but the window works nicely too now

 todo work out image wrapping so that continents line up east/west and north/southVoronoi
 */


class Atlas extends LazyWindow {
	static final Executor execute = new ScheduledThreadPoolExecutor(10);
	static final Map<String, Voronoi> layers = new LinkedHashMap<>();
	static final GridPane spinners = new GridPane();
	static final Button buttonGenerate = new Button("Generate");
	static final Button buttonDrawNoise = new Button("Noise");
	static final Button buttonDrawHeightMap = new Button("Height");
	static final Button buttonDrawMoistureMap = new Button("Moisture");
	static final Button buttonDrawHeatMap = new Button("Temperature");
	static final Button buttonDrawTectonics = new Button("Tectonics");
	static final Button buttonDrawContinents = new Button("Continents");
	static final Button buttonDrawRegions = new Button("Regions");
	static final Button buttonDrawBiomes = new Button("Biomes");
	static final Button buttonDrawCountries = new Button("Countries");
	static final FlowPane buttons = new FlowPane();
	static final VBox pane = new VBox(spinners, buttons);
	static final TitledPane controls = new TitledPane("Controls", pane);
	final static ImageView viewer = new ImageView();
	static final Utils.Resolution[] sizes = Utils.Resolution.values();
	static final Label
			labelMapSize = new Label("Map Size"),
			labelVoronoiDensity = new Label("Noise Density"),
			labelNumOfTectonics = new Label("Tectonic Plates"),
			labelNumOfContinents = new Label("Continents"),
			labelNumOfRegions = new Label("Biome Regions"),
			labelNumOfBiomes = new Label("Biomes per Region"),
			labelNumOfCountries = new Label("Countries"),
			labelSealLevel = new Label("Sea Level"),
			labelTemperatureScale = new Label("Temperature"),
			labelMoistureScale = new Label("Moisture"),
			labelElevationScale = new Label("Land Elevation");
	final static Spinner<Double> spinnerVoronoiDensity = new Spinner<>(0.2, 5, 1, 0.1),
			spinnerElevationScale = new Spinner<>(0.1, 1.5, 1, 0.1),
			spinnerMoistureScale = new Spinner<>(0.1, 1.5, 1, 0.1),
			spinnerTemperatureScale = new Spinner<>(0.1, 1.5, 1, 0.1),
			spinnerSeaLevel = new Spinner<>(0, 1, 0.5, 0.01);
	final static Spinner<Integer>
			spinnerMapSize = new Spinner<>(1, sizes.length, 1),
			spinnerNumberOfTectonics = new Spinner<>(10, 300, 100),
			spinnerNumberOfContinents = new Spinner<>(1, 50, 10),
			spinnerNumberOfRegions = new Spinner<>(5, 30, 10),
			spinnerNumberOfBiomes = new Spinner<>(3, 10, 10),
			spinnerNumberOfCountries = new Spinner<>(1, 40, 10);
	final static AtomicInteger numberOfSteps = new AtomicInteger();
	final static AtomicInteger completedSteps = new AtomicInteger();
	private static final boolean debug = App.debug;
	static volatile ProgressBar progressIndicator = new ProgressBar();
	static final StackPane stack = new StackPane(viewer, controls, progressIndicator);
	static Globe globe;
	static OpenSimplexNoise heightMapNoise;
	static double voronoiDensity;
	static int sizeIndex = sizes.length - spinnerMapSize.getValue();
	static Utils.Resolution res = sizes[sizeIndex];
	static volatile WritableImage image;
	static volatile PixelWriter writer;
	volatile static int numTectonics;
	volatile static int numContinents;
	volatile static int numRegions;
	volatile static int numBiomes;
	volatile static int numCountries;
	/*
	 * this in theory should be a safe thread?
	 */
	volatile static int equator;
	private static volatile boolean runningGenerator = false;
	static final Runnable runnableControlPanel = () -> {
		boolean running, hidden = false;
		do {
			if (runningGenerator && !hidden) {
				hidden = runningGenerator;
				buttons.setVisible(!runningGenerator);
				spinners.setVisible(!runningGenerator);
				controls.setExpanded(!runningGenerator);
			}
			if (!runningGenerator && hidden) {
				hidden = runningGenerator;
				buttons.setVisible(!runningGenerator);
				spinners.setVisible(!runningGenerator);
				controls.setExpanded(true);
			}
			running = !Thread.interrupted();
		}
		while (running);
		controls.setVisible(true);
	};
	private static volatile double elevationScale, temperatureScale, moistureScale, seaLevel;
	static final Runnable runnableImageDraw = () -> {

		heightMapNoise = new OpenSimplexNoise();

		numberOfSteps.set(layers.size());
		completedSteps.set(0);
		synchronized (Atlas.class) {
			sizeIndex = sizes.length - spinnerMapSize.getValue();
			res = sizes[sizeIndex];
			equator = res.height / 2;
			voronoiDensity = spinnerVoronoiDensity.getValue();
			numTectonics = spinnerNumberOfTectonics.getValue();
			numContinents = spinnerNumberOfContinents.getValue();
			numRegions = spinnerNumberOfRegions.getValue();
			numBiomes = spinnerNumberOfBiomes.getValue();
			numCountries = spinnerNumberOfCountries.getValue();
			seaLevel = spinnerSeaLevel.getValue();
			moistureScale = spinnerMoistureScale.getValue();
			temperatureScale = spinnerTemperatureScale.getValue();
			elevationScale = spinnerElevationScale.getValue();
			image = new WritableImage(res.width, res.height);
			writer = image.getPixelWriter();
			viewer.setImage(image);
			runningGenerator = true;
			globe = new Globe();
		}
		runLater(() -> progressIndicator.setVisible(true));
		runLater(() -> progressIndicator.setProgress(0.0D));
		do {
			switch (completedSteps.get()) {
				case 0: {
					generateNoise();
					runLater(Atlas::drawNoise);
					runLater(() -> progressIndicator.setProgress(1.0 * completedSteps.get() / numberOfSteps.get()));
				}

				case 1: {
					generateTectonics();
					runLater(Atlas::drawTectonics);
					runLater(() -> progressIndicator.setProgress(1.0 * completedSteps.get() / numberOfSteps.get()));

				}

				case 2: {
					generateContinents();
					runLater(Atlas::drawContinents);
					runLater(() -> progressIndicator.setProgress(1.0 * completedSteps.get() / numberOfSteps.get()));

				}

				case 3: {
					generateRegions();
					runLater(Atlas::drawRegions);
					runLater(() -> progressIndicator.setProgress(1.0 * completedSteps.get() / numberOfSteps.get()));

				}
				case 4: {
					generateBiomes();
					runLater(Atlas::drawBiomes);
					runLater(() -> progressIndicator.setProgress(1.0 * completedSteps.get() / numberOfSteps.get()));

				}
				case 5: {
					generateCountries();
					runLater(Atlas::drawCountries);
					runLater(() -> progressIndicator.setProgress(1.0 * completedSteps.get() / numberOfSteps.get()));
				}
			}
			completedSteps.getAndIncrement();
			runLater(() -> progressIndicator.setProgress(1.0 * completedSteps.get() / numberOfSteps.get()));

		} while (completedSteps.get() < numberOfSteps.get());
		runLater(() -> progressIndicator.setVisible(false));
		runningGenerator = false;
	};

	static {
		progressIndicator.setMaxSize(stack.maxWidthProperty().get(), 25);
	}

	/*
	 * preload layers Map with keys, no voronoi is created at this stage
	 */
	static {
		String[] layerNames = {
				"Noise",
				"Tectonics",
				"Continents",
				"Regions",
				"Biomes",
				"Countries"
		};
		Arrays.stream(layerNames).forEachOrdered(layer -> layers.put(layer, null));
	}

	/*
	 * spinner assignment
	 */
	static {
		spinners.setAlignment(Pos.TOP_CENTER);
		spinners.setVgap(10);
		spinners.setHgap(10);
		spinners.addRow(0, labelMapSize, labelVoronoiDensity, labelSealLevel, labelElevationScale, labelMoistureScale, labelTemperatureScale);
		spinners.addRow(1, spinnerMapSize, spinnerVoronoiDensity, spinnerSeaLevel, spinnerElevationScale, spinnerMoistureScale, spinnerTemperatureScale);
		spinners.addRow(3, labelNumOfTectonics, labelNumOfContinents, labelNumOfRegions, labelNumOfBiomes, labelNumOfCountries);
		spinners.addRow(4, spinnerNumberOfTectonics, spinnerNumberOfContinents, spinnerNumberOfRegions, spinnerNumberOfBiomes, spinnerNumberOfCountries);
	}

	/*
	 * button assignments
	 */
	static {

		buttons.setHgap(10);
		buttons.setVgap(10);
		buttons.setAlignment(Pos.BOTTOM_CENTER);
		buttons.getChildren().addAll(
				buttonGenerate,
				buttonDrawNoise,
				buttonDrawHeightMap,
				buttonDrawMoistureMap,
				buttonDrawHeatMap,
				buttonDrawTectonics,
				buttonDrawContinents,
				buttonDrawRegions,
				buttonDrawBiomes,
				buttonDrawCountries);

		buttonGenerate.setOnAction(event -> {
			controls.setExpanded(false);
			//new Thread(runnableImageDraw).start();
			execute.execute(runnableImageDraw);
		});
		buttonDrawNoise.setOnAction(event -> drawNoise());
		buttonDrawHeightMap.setOnAction(event -> drawHeightMap());
		buttonDrawContinents.setOnAction(event -> drawContinents());
		buttonDrawTectonics.setOnAction(event -> drawTectonics());
	}


	/*
	 * setting up control panel
	 */
	static {
		pane.autosize();
		pane.setSpacing(20);
		controls.setExpanded(true);
	}


	/*
	 * setting up threads
	 */
	static {
		image = new WritableImage(res.width, res.height);
		writer = image.getPixelWriter();
		viewer.setImage(image);
		new Thread(runnableControlPanel).start();
		new Thread(runnableImageDraw).start();
	}

	/*
	 * setting viewer size constraints
	 */ {
		stack.setAlignment(Pos.TOP_CENTER);
		double divisor = 1.05;
		stack.maxWidthProperty().bind(widthProperty());
		stack.minWidthProperty().bind(widthProperty());
		stack.maxHeightProperty().bind(heightProperty());
		stack.minHeightProperty().bind(heightProperty());
		viewer.fitWidthProperty().bind(stack.widthProperty().divide(divisor));
		viewer.fitHeightProperty().bind(stack.heightProperty().divide(divisor));
		viewer.setPreserveRatio(true);
		viewer.setImage(image);
	}


	private static void drawCountries() {
	}

	private static void generateCountries() {
	}

	private static void drawBiomes() {
	}

	private static void generateBiomes() {
	}

	private static void drawRegions() {
	}

	private static void generateRegions() {
	}

	private static void drawContinents() {
		globe.continents.forEach((point, continent) -> {
			Random r = new Random();
			Color c = Color.color(r.nextDouble(), r.nextDouble(), r.nextDouble());
			continent.cells.forEach(point1 -> writer.setColor(point1.x, point1.y, c));
		});
	}

	private static void generateContinents() {
		Voronoi layer = layers.get("Tectonics");
		List<Point> sites = new ArrayList<>();
		Random r = new Random();
		int index;
		for (int i = 0; i <= numContinents; i++) {
			do {
				index = r.nextInt(layer.getSites().size());
			} while (sites.contains(layer.getSites().get(index)));
			sites.add(layer.getSites().get(index));
		}
		layers.replace("Continents", new Voronoi(res, sites, 1, debug));
		Voronoi continentLayer = layers.get("Continents");
		Map<Point, List<Point>> continentCells = continentLayer.getCells();
		sites.forEach(point -> {
			Globe.Continent continent = new Globe.Continent(point);
			continentCells.get(point).forEach(point1 -> {
				if (layer.getSites().contains(point1))
					globe.continents.put(point1, continent);
			});
		});

	}

	private static void generateNoise() {
		layers.replace("Noise", new Voronoi(res, null, voronoiDensity, debug));
	}

	private synchronized static void drawNoise() {
		var layer = layers.get("Noise");
		Random r = new Random();
		layer.getSites().forEach(point -> {
			Color c = Color.color(r.nextDouble(), r.nextDouble(), r.nextDouble());
			layer.getCells().get(point).forEach(point1 -> writer.setColor(point1.x, point1.y, c));
		});
	}

	private static void generateTectonics() {
		var noiseLayer = layers.get("Noise");
		var heightMap = heightMapNoise.getNoise2DArray(0, 0, res.width, res.height);
		globe.heightMap = heightMap;
		Random r = new Random();
		List<Point> sites = new ArrayList<>(),
				noiseSites = noiseLayer.getSites();
		Map<Point, List<Point>> noiseCells = noiseLayer.getCells(), tectonicCells;

		for (int i = 0; i <= numTectonics; i++) {
			int index;
			do {
				index = r.nextInt(noiseSites.size());
			}
			while (sites.contains(noiseSites.get(index)));
			sites.add(noiseSites.get(index));
		}
		layers.replace("Tectonics", new Voronoi(res, sites, 1, debug));
		var layer = layers.get("Tectonics");
		tectonicCells = layer.getCells();
		sites.forEach(tSite -> {
			Globe.TectonicPlate plate = new Globe.TectonicPlate(tSite);
			tectonicCells.get(tSite).forEach(tCell -> {
				if (noiseSites.contains(tCell)) {
					noiseCells.get(tCell).forEach(nCell -> {
						plate.cells.add(nCell);
						plate.setPlateHeight(heightMap[nCell.x][nCell.y].getValue(new Range(0, 1)) * elevationScale);
						plate.setPlateTemperature(heightMap[nCell.x][nCell.y].getValue(new Range(0, 1)) * temperatureScale);
					});
				}
			});
			globe.plates.put(tSite, plate);
		});
	}

	private synchronized static void drawTectonics() {
		var layer = globe.plates;
		Random r = new Random();
		layer.forEach(
				(point, tectonicPlate) -> {
					Color c;
					if (tectonicPlate.plateHeight < seaLevel) c = Color.SEAGREEN;
					else c = Color.BURLYWOOD;
					tectonicPlate.cells.forEach(
							point1 -> writer.setColor(
									point1.x,
									point1.y,
									c)
					);
				});
	}

	private synchronized static void drawHeightMap() {
		var layer = globe.plates;
		layer.forEach(
				(point, tectonicPlate) ->
						tectonicPlate.cells.forEach(
								point1 -> writer.setColor(
										point1.x,
										point1.y,
										Color.color(0, tectonicPlate.plateHeight, 0))));
	}

	@Override
	protected Node content() {
		return stack;
	}

	static class Globe {
		final Map<Point, TectonicPlate> plates = new HashMap<>();
		final Map<Point, Continent> continents = new HashMap<>();

		public RangedValue[][] heightMap, temperatureMap, moistureMap, heatMap, populationMap;

		static class Area {
			final Point site;
			final List<Point> cells = new ArrayList<>();

			Area(Point site) {
				this.site = site;
			}
		}

		static class TectonicPlate extends Area {
			private double plateHeight;
			private double plateTemperature;

			TectonicPlate(Point site) {
				super(site);
			}

			public double getPlateHeight() {
				return plateHeight;
			}

			public void setPlateHeight(double height) {
				if (height > 1) plateHeight = 1;
				else if (height < 0) plateHeight = 0;
				else plateHeight = height;

			}

			public double getPlateTemperature() {
				return plateTemperature;
			}

			public void setPlateTemperature(double noiseValue) {

				double scale;

				if (site.y < equator) scale = (1.0 * res.height / site.y);
				else if (site.y > equator) scale = (1.0 / res.height * site.y);
				else scale = 1;

				scale = scale + (plateHeight * 0.2);

				this.plateTemperature = noiseValue * scale;
			}
		}

		public static class Continent extends Area {
			Continent(Point site) {
				super(site);
			}
		}
	}
}