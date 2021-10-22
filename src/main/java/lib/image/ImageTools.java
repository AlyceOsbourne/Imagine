



/*
 * Do what the F**k you want
 */

package lib.image;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

/**
 * Image conversion tools, can convert BufferedImage to WritableImage and vise versa
 * and can convert both to and from json and convert an image to PNG
 */
public interface ImageTools {


	/**
	 * Export to image file.
	 *
	 * @param image
	 * 		the image being written to file
	 * @param stage
	 * 		the stage which is the root of the File Chooser
	 */
	static void exportToImageFile(WritableImage image, Stage stage) {
		exportToImageFile(convertToBuffered(image), stage);
	}

	static void exportToImageFile(BufferedImage image, Stage stage) {
		FileChooser chooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"image files", "*.png", "*.bmp", "*.jpeg");

		chooser.getExtensionFilters().add(extFilter);

		File v = chooser.showSaveDialog(stage);
		if (v != null) {
			String fileName = v.getName();
			System.out.println("Saving " + fileName);
			String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1, v.getName().length());
			try {
				ImageIO.write(image, fileExtension, v);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Convert to buffered buffered image.
	 *
	 * @param image
	 * 		the image
	 * @return the buffered image
	 */
	static BufferedImage convertToBuffered(WritableImage image) {
		return SwingFXUtils.fromFXImage(image, null);
	}

	/**
	 * Encode image string.
	 *
	 * @param i
	 * 		the image that will be encoded to base 64, lets us save an image for editing later
	 * @return the string
	 */
	static String encodeImage(WritableImage i) {
		return encodeImage(convertToBuffered(i));
	}

	/**
	 * Encode image string.
	 *
	 * @param i
	 * 		the image that will be encoded to base 64, lets us save an image for editing later
	 * @return the string
	 */
	static String encodeImage(BufferedImage i) {
		return Base64.getEncoder().encodeToString(((DataBufferByte) i.getRaster().getDataBuffer()).getData());
	}

	/**
	 * Decode writable image writable image.
	 *
	 * @param s
	 * 		the string that will be decoded from base 64 into an image
	 * @return the writable image
	 */
	static WritableImage decodeWritableImage(String s) {
		return convertToWritable(decodeBufferedImage(s));
	}

	/**
	 * Convert to writable writable image.
	 *
	 * @param image
	 * 		the image that will be converted from buffered to writable
	 * @return the writable image
	 */
	static WritableImage convertToWritable(BufferedImage image) {
		return SwingFXUtils.toFXImage(image, null);
	}

	/**
	 * Decode buffered image buffered image.
	 *
	 * @param s
	 * 		the string that will be decoded from base 64 into an image
	 * @return the buffered image
	 */
	static BufferedImage decodeBufferedImage(String s) {
		try {
			return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(s)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}



