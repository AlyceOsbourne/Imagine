package lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.util.logging.Logger;

public interface Save {

	Logger log = Logger.getLogger("SaveLogger");

	default <SERIALIZE> void serializeObject(SERIALIZE toSerialize, Class<SERIALIZE> type, String filename) {
		File file = new File(filename);
		log.info(file.getAbsolutePath());
		SERIALIZE serializeObject = type.cast(toSerialize);
		log.info(serializeObject.toString());
		try {
			if((file.exists()
					||(file.mkdirs()&&file.createNewFile()))
					&&(file.canWrite()&&file.canRead()))
			{
				GsonBuilder gsonBuilder = new GsonBuilder();
				gsonBuilder.setPrettyPrinting();
				gsonBuilder.enableComplexMapKeySerialization();
				gsonBuilder.excludeFieldsWithoutExposeAnnotation();
				gsonBuilder.serializeNulls();
				Gson gson = gsonBuilder.create();
				gson.newJsonWriter(new BufferedWriter(new FileWriter(file)));
				gson.newJsonReader(new BufferedReader(new FileReader(file)));

				//log.info(gson.toString());
				gson.toJson(serializeObject, type, new JsonWriter(new BufferedWriter(new FileWriter(file))));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	default <DESERIALIZE> DESERIALIZE deserializeObject(Class<?> type, String filename) {

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().serializeNulls().enableComplexMapKeySerialization().create();
			DESERIALIZE deserializeObject = null;
			File file = new File(filename);
			if((file.exists())) deserializeObject = (gson.fromJson(new JsonReader(new BufferedReader(new FileReader(file))), type));
			return deserializeObject;
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


}
