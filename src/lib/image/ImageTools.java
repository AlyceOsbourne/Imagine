



/*
 * Do what the F**k you want
 */

package lib.image;

import imagine.Main;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Image conversion tools, can convert BufferedImage to WritableImage and vise versa
 * and can convert both to and from json abd convert an image to PNG
 **/

public interface ImageTools {


	static void exportToPng(WritableImage image) {
		exportToPng(convertToBuffered(image));
	}

	static void exportToPng(BufferedImage image) {
		FileChooser chooser = new FileChooser();
		try {
			ImageIO.write(image, "png", chooser.showSaveDialog(Main.stage));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static BufferedImage convertToBuffered(WritableImage image) {
		return SwingFXUtils.fromFXImage(image, null);
	}

	static String encodeImage(WritableImage i) {
		return encodeImage(convertToBuffered(i));
	}

	static String encodeImage(BufferedImage i) {
		return Base64.getEncoder().encodeToString(((DataBufferByte) i.getRaster().getDataBuffer()).getData());
	}

	static WritableImage decodeWritableImage(String s) {
		return convertToWritable(decodeBufferedImage(s));
	}

	static WritableImage convertToWritable(BufferedImage image) {
		return SwingFXUtils.toFXImage(image, null);
	}

	static BufferedImage decodeBufferedImage(String s) {
		try {
			return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(s)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}



