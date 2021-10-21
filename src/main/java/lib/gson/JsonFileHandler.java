



/*
 * Do what the F**k you want
 */

package lib.gson;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Takes any Type and converts to a Json file
 *
 * @param <Obj>
 * 		the type of object being written/read from file
 */
public interface JsonFileHandler<Obj> {

	/**
	 * The gson (object <=> string) converter
	 */
	Gson gson = new GsonBuilder()
			.enableComplexMapKeySerialization()
			.setLenient()
			.create();


	/**
	 * Save.
	 *
	 * @param file
	 * 		the file that you wish to write too
	 * @param object
	 * 		the object that is going to be written to file
	 * @param type
	 * 		the type of object being written to file
	 */
	default void save(File file, Obj object, Class<Obj> type) {
		try {
			Files.writeString(Path.of(file.toURI()), gson.toJson(object, type));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Load obj.
	 *
	 * @param file
	 * 		the file to be read
	 * @param type
	 * 		the type of the object that you are returning,
	 * 		ensures its read and returned correctly
	 * @return the object you wish to return
	 */
	default Obj load(File file, Class<Obj> type) {
		if (file != null) {

			try {
				return gson.fromJson(Files.readString(file.toPath()), type);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
