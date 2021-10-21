/*
 * Do what the F**k you want
 */

/*
 * Do what the F**k you want
 */

/*
 * Do what the F**k you want
 */

package lib.math.noise;

import com.raylabz.opensimplex.NoiseDetail;
import com.raylabz.opensimplex.OpenSimplexNoise;
import com.raylabz.opensimplex.Range;
import com.raylabz.opensimplex.RangedValue;
import javafx.beans.NamedArg;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RGBANoise {
	final OpenSimplexNoise noise;
	final Colour[][] output;
	final int width, height;
	final long seed;
	final double intensity;
	RangedValue[][] values;
	Random random;
	Image image;

	public RGBANoise(
			int width,
			int height,
			long seed,
			@NamedArg("Noise Detail 0-3") int noiseDetail,
			@NamedArg("Feature Size") int featureSize,
			@NamedArg("Power") int power,
			@NamedArg("Smooth passes") int passes,
			@NamedArg(" RGBA Intensity") double intensity) {


		{
			this.width = width;
			this.height = height;
			this.seed = seed;
			this.intensity = intensity;
			{
				output = new Colour[width][height];
				random = new Random(seed);
				this.noise = new OpenSimplexNoise(seed);
				{
					this.noise.setNoiseDetail(NoiseDetail.values()[noiseDetail]);
					this.noise.setFeatureSize(featureSize);
					this.noise.setPower(power);
				}
				this.values = noise.getNoise2DArray(0, 0, width, height);
			}

		}

		{
			double r, g, b, a;
			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++) {
					r = slur(x, y);
					g = slur(x, y);
					b = slur(x, y);
					a = slur(x, y);
					output[x][y] = new Colour(r, g, b, a);
				}
			smoothPass(passes);
			this.image = imageCreate();
		}

	}

	double slur(int x, int y) {
		var val = values[x][y].getValue(new Range(0, 1));
		if (val != 0) {
			val = random.nextDouble() * val;
			val = average(random.nextDouble(), val);
			val = val * intensity;
		}
		return Math.max(Math.min(1, val), 0);
	}

	void smoothPass(int passes) {
		for (int p = 0; p < passes; p++) {
			for (int x = 0;
			     x < width;
			     x++)
				for (int y = 0; y < height; y++) {
					int
							xPlus = Math.min(x + 1, width - 1),
							xMinus = Math.max(0, x - 1),
							yPlus = Math.min(y + 1, height - 1),
							yMinus = Math.max(y - 1, 0);
					Colour
							center = output[x][y],
							north = output[x][yPlus],
							east = output[xMinus][y],
							south = output[x][yMinus],
							west = output[xPlus][y];

					double r = average(center.r, north.r, east.r, south.r, west.r);
					double g = average(center.g, north.g, east.g, south.g, west.g);
					double b = average(center.b, north.b, east.b, south.b, west.b);
					double a = average(center.a, north.a, east.a, south.a, west.a);

					output[x][y] = new Colour(r, g, b, a);
				}
		}
	}

	Image imageCreate() {
		WritableImage gen = new WritableImage(width, height);
		PixelWriter writer = gen.getPixelWriter();
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				Colour cI = output[x][y];
				Color cO = Color.color(cI.r, cI.g, cI.b, cI.a);
				writer.setColor(x, y, cO);
			}
		return gen;
	}

	double average(double @NotNull ... doubles) {
		double total = 0;
		for (double d : doubles) total = total + d;
		return total / doubles.length;
	}

	public final Image getImage() {
		return image;
	}

	static class Colour {
		final double r, g, b, a;

		Colour(double r, double g, double b, double a) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
		}
	}
}
