/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */


package lib.serialization.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.util.logging.Logger;

public interface GsonDataSerialization {
	Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().serializeNulls().enableComplexMapKeySerialization().create();

	Logger log = Logger.getLogger("SaveLogger");

	default <SERIALIZE> void serializeObject(SERIALIZE toSerialize, Class<SERIALIZE> type, File file) {

		log.info(file.getAbsolutePath());
		SERIALIZE serializeObject = type.cast(toSerialize);
		log.info(serializeObject.toString());
		try {
			if ((file.exists() || (file.createNewFile())) && (file.canWrite() && file.canRead())) {
				gson.toJson(serializeObject, type, new JsonWriter(new BufferedWriter(new FileWriter(file))));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	default <DESERIALIZE> DESERIALIZE deserializeObject(Class<DESERIALIZE> type, File file) {

		try {
			if ((file.exists()))
			return type.cast(gson.fromJson(new JsonReader(new BufferedReader(new FileReader(file))), type));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}




}
