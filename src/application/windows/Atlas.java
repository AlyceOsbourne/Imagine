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
import lib.javafx.fxml.LazyWindow;
import lib.math.voronoi.algorithm.Voronoi;
import lib.math.voronoi.algorithm.data.Utils;
import lib.math.voronoi.algorithm.data.nodes.Point;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static javafx.application.Platform.runLater;

/*
 * todo fix collector for spinners to it is refreshed on each generate, maybe see if there is a way to bind it?
 *  although, we only really need it for the generation runs, also investigate broken image generation when ran in off thread
 * */


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
	static final ProgressBar progressIndicator = new ProgressBar();
	static final StackPane stack = new StackPane(viewer, controls);
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
			spinnerSeaLevel = new Spinner<>(0, 1, 0.7, 0.1);
	final static Spinner<Integer>
			spinnerMapSize = new Spinner<>(1, sizes.length, 1),
			spinnerNumberOfTectonics = new Spinner<>(10, 200, 50),
			spinnerNumberOfContinents = new Spinner<>(1, 50, 10),
			spinnerNumberOfRegions = new Spinner<>(5, 30, 10),
			spinnerNumberOfBiomes = new Spinner<>(3, 10, 10),
			spinnerNumberOfCountries = new Spinner<>(1, 40, 10);
	private static final boolean debug = true;
	static Globe globe;
	static OpenSimplexNoise noise;
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
	private volatile static int
			numberOfSteps,
			completedSteps;
	/*
	 * this in theory should be a safe thread?
	 */
	static final Runnable runnableProgressBar = () -> {
		double percentDone;
		boolean updateBar = true;
		progressIndicator.setVisible(true);
		do {
			if (completedSteps == 0 || numberOfSteps == 0) percentDone = 0.0;
			else percentDone = ((1.0D * completedSteps) / (numberOfSteps));
			progressIndicator.setProgress(percentDone);
			if (percentDone == 1.0 || Thread.interrupted()) updateBar = false;
		} while (updateBar);
		progressIndicator.setVisible(false);
	};
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

		noise = new OpenSimplexNoise();

		numberOfSteps = layers.size();
		completedSteps = 0;
		//execute.execute(runnableProgressBar);
		//new Thread(runnableProgressBar);
		//Platform.runLater(runnableProgressBar);
		synchronized (Atlas.class) {
			sizeIndex = sizes.length - spinnerMapSize.getValue();
			res = sizes[sizeIndex];
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


		generateNoise();
		runLater(Atlas::drawNoise);
		completedSteps = 1;

		generateTectonics();
		runLater(Atlas::drawTectonics);

		completedSteps = 2;

		generateContinents();
		runLater(Atlas::drawContinents);

		completedSteps = 3;

		generateRegions();
		runLater(Atlas::drawRegions);

		completedSteps = 4;

		generateBiomes();
		runLater(Atlas::drawBiomes);

		completedSteps = 5;

		generateCountries();
		runLater(Atlas::drawCountries);

		completedSteps = 6;

		runningGenerator = false;
	};

	static {
		//b.setAlignment(Pos.BOTTOM_CENTER);
		//b.setFillHeight(true);
		//progressIndicator.setMaxSize(800, 100);
		//progressIndicator.setVisible(false);
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
	}

	private static void generateContinents() {
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
		var n = noise.getNoise2DArray(0, 0, res.width, res.height);
		globe.heightMap = n;
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
						plate.setPlateHeight(n[nCell.x][nCell.y].getValue(new Range(0, 1)) * elevationScale);
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
					Color c = Color.color(r.nextDouble(), r.nextDouble(), r.nextDouble());
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
		public RangedValue[][] heightMap, temperatureMap, moistureMap, heatMap, populationMap;

		static class Area {
			final Point site;
			final List<Point> cells = new ArrayList<>();

			Area(Point site) {
				this.site = site;
			}
		}

		static class TectonicPlate extends Area {
			double plateHeight;

			TectonicPlate(Point site) {
				super(site);
			}

			public void setPlateHeight(double height) {
				if (height > 1) plateHeight = 1;
				else if (height < 0) plateHeight = 0;
				else plateHeight = height;

			}
		}
	}
}