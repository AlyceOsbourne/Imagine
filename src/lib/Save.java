package lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public interface Save {
	Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();

	default <SERIALIZE> void serializeObject(SERIALIZE object, Class<SERIALIZE> type, String filename) {
		File file = new File(filename);
		try {
			if (((file.mkdirs() && file.createNewFile()) || file.exists()) && file.canWrite())
				try {
					gson.toJson(object, type, gson.newJsonWriter(new BufferedWriter(new FileWriter(file))));
				} catch (IOException e) {
					e.printStackTrace();
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	default <DESERIALIZE> DESERIALIZE deserializeObject(Class<?> type, String filename) {

		try {
			return gson.fromJson(gson.newJsonReader(new BufferedReader(new FileReader(filename))), type);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
