/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package application.windows;

import javafx.geometry.Insets;
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
import javafx.scene.text.Font;
import lib.javafx.fxml.LazyWindow;
import lib.math.noise.Noise;
import lib.math.voronoi.algorithm.Voronoi;
import lib.math.voronoi.algorithm.data.nodes.Point;

import java.util.*;


enum MapSize {
	Huge(1.0),
	Large(0.8),
	Normal(0.6),
	Small(0.4);

	int width = 1920, height = 1080;

	MapSize(double scale) {
		this.width = (int) (this.width * scale);
		this.height = (int) (this.height * scale);
	}
}

public class Atlas extends LazyWindow {

	static ImageView viewer = new ImageView();
	static Spinner<Integer>
			mapSizeSpinner = new Spinner<>(1, MapSize.values().length, 1),
			numOfPlatesSpinner = new Spinner<>(10, 30, 12),
			numOfContinentsSpinner = new Spinner<>(1, 12, 5),
			numOfCountriesSpinner = new Spinner<>(0, 30, 15),
			numOfBiomesSpinner = new Spinner<>(1, 10, 5);
	static Spinner<Double> noiseScale = new Spinner<>(0.5, 3.0, 1.0, 0.05);
	static Label
			mapSizeLabel = new Label("Map Size"),
			numOfPlatesLabel = new Label("Number of Tectonic Plates"),
			numOfContinentsLabel = new Label("Number of Continents"),
			numOfCountriesLabel = new Label("Number of Countries per Continent"),
			numOfBiomesLabel = new Label("Number of Biomes per Continent"),
			noiseScaleLabel = new Label("Noise Map Scale");
	static double progress = 0.0D;
	static ProgressBar indicator = new ProgressBar(progress);
	Thread
			generatorThread,
			drawNoiseThread,
			drawTectonicsThread;
	boolean debug = true;
	MapSize size = MapSize.values()[MapSize.values().length - mapSizeSpinner.getValue()];
	Map<String, Voronoi> layers;
	Noise randomHTEmap;
	WritableImage image;
	PixelWriter writer;
	Globe globe;
	Random rand = new Random();
	int
			numberOfTectonicPlates = numOfPlatesSpinner.getValue(),
			numberOfContinents = numOfContinentsSpinner.getValue(),
			numberOfCountriesPerContinent = numOfCountriesSpinner.getValue(),
			numberOfBiomesPerContinent = numOfBiomesSpinner.getValue();

	@Override
	protected Node content() {
		{
			int width, height, divisor;
			divisor = 3;
			width = 1920 / divisor;
			height = 1080 / divisor;
			image = new WritableImage(width, height);
			PixelWriter writer = image.getPixelWriter();
			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++)
					writer.setColor(x, y, Color.BLACK);
		}
		var p = new StackPane(viewer, controls());
		{
			p.setPrefSize(getWidth(), getHeight());
			p.setAlignment(Pos.TOP_CENTER);
			viewer.setImage(image);
			viewer.fitHeightProperty().bind(p.heightProperty());
			viewer.fitWidthProperty().bind(p.widthProperty());
			viewer.setPreserveRatio(true);
		}
		return p;
	}

	@Override
	protected Node bottomBar() {
		indicator.setVisible(false);
		return indicator;
	}

	protected Node controls() {
		Button
				generate = new Button("Generate world!"),
				drawNoise = new Button("Draw noise layer."),
				drawTectonics = new Button("Draw tectonics."),
				drawContinents = new Button("Draw continents"),
				drawBiomes = new Button("Draw biomes."),
				drawCountries = new Button("Draw countries.");

		FlowPane buttonPane = new FlowPane(generate, drawNoise, drawTectonics, drawContinents, drawBiomes, drawCountries);

		generate.setOnAction(event -> {
			if (generatorThread != null && generatorThread.isAlive()) {
				generatorThread.interrupt();
				generatorThread.stop();
			}
			generatorThread = startGenerator();
			generatorThread.start();
		});
		drawNoise.setOnAction(event -> {
			if (generatorThread == null || !generatorThread.isAlive())
				if (drawNoiseThread != null && drawNoiseThread.isAlive()) {
					drawNoiseThread.interrupt();
					drawNoiseThread.stop();
				}
			drawNoiseThread = drawNoise();
			drawNoiseThread.start();
		});

		drawTectonics.setOnAction(event -> {
			if (generatorThread == null || !generatorThread.isAlive() || drawNoiseThread == null || !drawNoiseThread.isAlive())
				if (drawTectonicsThread != null && drawTectonicsThread.isAlive()) {
					drawTectonicsThread.interrupt();
					drawTectonicsThread.stop();
				}
			drawTectonicsThread = drawTectonics();
			drawTectonicsThread.start();
		});
		drawContinents.setOnAction(event -> {
			if (generatorThread == null || !generatorThread.isAlive())
				drawContinents();
		});
		drawBiomes.setOnAction(event -> {
			if (generatorThread == null || !generatorThread.isAlive())
				drawBiomes();
		});
		drawCountries.setOnAction(event -> {
			if (generatorThread == null || !generatorThread.isAlive())
				drawCountries();
		});

		var buttons = buttonPane.getChildren();
		buttons.forEach(node -> {
			if (node instanceof Button button) {
				button.setPrefSize(180, 30);
				button.setFont(Font.font(15));
			}
		});
		GridPane pane = new GridPane();
		VBox box = new VBox(pane, buttonPane);
		box.setPadding(new Insets(5));
		box.setAlignment(Pos.CENTER);
		box.setSpacing(20);
		pane.addColumn(0, mapSizeLabel, noiseScaleLabel, numOfPlatesLabel, numOfContinentsLabel, numOfCountriesLabel, numOfBiomesLabel);
		pane.addColumn(1, mapSizeSpinner, noiseScale, numOfPlatesSpinner, numOfContinentsSpinner, numOfCountriesSpinner, numOfBiomesSpinner);
		pane.setHgap(5);
		pane.setVgap(5);
		pane.setAlignment(Pos.TOP_CENTER);
		buttonPane.setAlignment(Pos.BOTTOM_CENTER);
		buttonPane.setVgap(5);
		buttonPane.setHgap(5);
		TitledPane controls = new TitledPane("Controls", box);
		controls.setExpanded(false);
		controls.autosize();

		return controls;
	}

	Thread startGenerator() {
		layers = new LinkedHashMap<>();
		image = new WritableImage(size.width, size.height);
		writer = image.getPixelWriter();
		viewer.setImage(image);
		return createGeneratorThread();

	}

	Thread createGeneratorThread() {
		return new Thread(() -> {
			progress = 0;
			indicator.setVisible(true);
			int steps = 11;
			for (int currentStep = 0; currentStep <= steps; currentStep++) {
				indicator.setProgress(progress);
				{
					switch (currentStep) {
						case 0 -> globe = new Globe();
						case 1 -> generateNoise();
						case 2 -> {
							if (drawNoiseThread != null && drawNoiseThread.isAlive()) {
								drawNoiseThread.interrupt();
								drawNoiseThread.stop();
							}
							drawNoiseThread = drawNoise();
							drawNoiseThread.start();
						}
						case 3 -> generateTectonics();
						case 4 -> {
							if (drawTectonicsThread != null && drawTectonicsThread.isAlive()) {
								drawTectonicsThread.interrupt();
								drawTectonicsThread.stop();
							}
							drawTectonicsThread = drawTectonics();
							drawTectonicsThread.start();

						}
						case 5 -> generateContinents();
						case 6 -> drawContinents();
						case 7 -> generateBiomes();
						case 8 -> drawBiomes();
						case 9 -> generateCountries();
						case 10 -> drawCountries();
					}
				}
				progress = progress + 0.1;
			}
			indicator.setVisible(false);
		});
	}

	private void drawHeightMap() {
	}

	private void drawHeatMap() {
	}

	private void drawMoistureMap() {
	}

	private void drawPopulaceMap() {
	}

	private Thread drawNoise() {
		if (layers == null || !layers.containsKey("Noise")) generateNoise();
		viewer.setImage(image);
		writer = image.getPixelWriter();
		return new Thread(() -> {
			Voronoi layer = layers.get("Noise");
			layer.getSites().forEach(point -> {
				Color c = Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
				layer.getCells().get(point).forEach(point1 -> writer.setColor(point1.x, point1.y, c));
			});
		});
	}

	private Thread drawTectonics() {
		if (globe.plates.isEmpty()) generateTectonics();
		return new Thread(() -> globe.plates.forEach((site, tectonicPlate) -> {
			Color c = new Color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), 1.0);
			tectonicPlate.cells.forEach(point -> writer.setColor(point.x, point.y, c));
		}));
	}

	private void drawContinents() {
	}

	private void drawBiomes() {
	}

	private void drawCountries() {
	}

	private void generateNoise() {
		layers.remove("Noise");
		layers.put("Noise", new Voronoi(size.width, size.height, null, noiseScale.getValue(), debug));
	}

	private void generateTectonics() {
		if (!layers.containsKey("Noise")) generateNoise();
		if (globe == null) globe = new Globe();
		List<Point> noiseSites = layers.get("Noise").getSites(), sites = new ArrayList<>();
		Map<Point, List<Point>> noiseCells = layers.get("Noise").getCells();
		for (int i = 0; i <= numberOfTectonicPlates; i++) {
			int index;
			do {
				index = rand.nextInt(noiseSites.size() - 1);
			}
			while (sites.contains(noiseSites.get(index)));
			sites.add(noiseSites.get(index));
		}
		Voronoi v = new Voronoi(size.width, size.height, sites, 1.0, debug);
		sites.forEach(site -> {
			Globe.TectonicPlate plate = new Globe.TectonicPlate(site);
			v.getCells().get(site).forEach(point -> {
				if (noiseSites.contains(point))
					plate.cells.addAll(noiseCells.get(point));
			});
			globe.plates.put(site, plate);
		});

		layers.put("Tectonic", v);
	}

	void generateContinents() {
	}

	void generateBiomes() {
	}

	void generateCountries() {
	}

}

class Globe {
	Map<Point, TectonicPlate> plates = new HashMap<>();
	Map<Point, Continent> continents = new HashMap<>();
	Map<Point, Country> countries = new HashMap<>();
	Map<Point, Biome> biomes = new HashMap<>();

	static class Area {
		Point site;
		List<Point> cells = new ArrayList<>();

		Area(Point site) {
			this.site = site;
		}
	}

	static class TectonicPlate extends Area {

		TectonicPlate(Point site) {
			super(site);
		}
	}

	static class Continent extends Area {
		Continent(Point site) {
			super(site);
		}
	}

	static class Country extends Area {
		Country(Point site) {
			super(site);
		}
	}

	static class Biome extends Area {
		Biome(Point site) {
			super(site);
		}
	}
}

