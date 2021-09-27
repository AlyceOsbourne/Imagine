



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

public interface JsonFileHandler<C> {

	Gson gson = new GsonBuilder()
			.enableComplexMapKeySerialization()
			.setLenient()
			.create();


	default void save(File file, C o, Class<C> c) {
		try {
			Files.writeString(Path.of(file.toURI()), gson.toJson(o, c));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	default C load(File file, Class<C> c) {
		if (file != null)
			try {
				return gson.fromJson(Files.readString(Path.of(file.toURI())), c);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return null;
	}


}
