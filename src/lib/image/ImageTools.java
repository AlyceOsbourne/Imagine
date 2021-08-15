/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package lib.image;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public interface ImageTools {

	static String encodeImage(BufferedImage i) {return Base64.getEncoder().encodeToString(((DataBufferByte) i.getRaster().getDataBuffer()).getData());}
	static String encodeImage (WritableImage i){return encodeImage(convertToBuffered(i));}

	static BufferedImage decodeBufferedImage(String s){
		try {
			return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(s)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	static WritableImage decodeWritableImage(String s) {
		return convertToWritable(decodeBufferedImage(s));
	}

	static BufferedImage convertToBuffered(WritableImage image) {
		return SwingFXUtils.fromFXImage(image, null);
	}

	static WritableImage convertToWritable(BufferedImage image) {
		return SwingFXUtils.toFXImage(image, null);
	}

}



