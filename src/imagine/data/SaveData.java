/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imagine.scenes.characterbio.data.Character;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;

public class SaveData {

	public static Data data = new Data();
	static Gson gson = new GsonBuilder()
			.enableComplexMapKeySerialization()
			.setLenient()
			.create();

	//note for images need to use a base64 encoder to convert to string to then convert to json
	String encodeImage(BufferedImage i) {
		return Base64.getEncoder().encodeToString(((DataBufferByte) i.getRaster().getDataBuffer()).getData());
	}

	BufferedImage decodeImage(String s) throws IOException {
		return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(s)));
	}

	public static void save(File file) {
		try {
			Files.writeString(Path.of(file.toURI()), gson.toJson(data, Data.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void load(File file) {
		if (file != null)
		try {
			data = gson.fromJson(Files.readString(Path.of(file.toURI())), Data.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class Data {

		public HashMap<String, Character> getCharacters() {
			return Characters;
		}

		public Data setCharacters(HashMap<String, Character> characters) {
			Characters = characters;
			return this;
		}

		HashMap<String, Character> Characters = new HashMap<>();
		HashMap<String, String> StoredWorldMaps = new HashMap<>();
		HashMap<String, String> StoredRegionMaps = new HashMap<>();
		HashMap<String, String> StoredCityMaps = new HashMap<>();
		HashMap<String, String> StoredDungeonMaps = new HashMap<>();


		public HashMap<String, String> getStoredWorldMaps() {
			return StoredWorldMaps;
		}

		public Data setStoredWorldMaps(HashMap<String, String> storedWorldMaps) {
			StoredWorldMaps = storedWorldMaps;
			return this;
		}

		public HashMap<String, String> getStoredRegionMaps() {
			return StoredRegionMaps;
		}

		public Data setStoredRegionMaps(HashMap<String, String> storedRegionMaps) {
			StoredRegionMaps = storedRegionMaps;
			return this;
		}

		public HashMap<String, String> getStoredCityMaps() {
			return StoredCityMaps;
		}

		public Data setStoredCityMaps(HashMap<String, String> storedCityMaps) {
			StoredCityMaps = storedCityMaps;
			return this;
		}

		public HashMap<String, String> getStoredDungeonMaps() {
			return StoredDungeonMaps;
		}

		public Data setStoredDungeonMaps(HashMap<String, String> storedDungeonMaps) {
			StoredDungeonMaps = storedDungeonMaps;
			return this;
		}


	}
}

