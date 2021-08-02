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
import java.lang.reflect.Type;
import java.util.logging.Logger;

/**
 * Interface that provides object to json serialization,
 * intended to be extended by other interfaces to actually handle said data.
 */
public interface GsonDataSerialization {
	/**
	 * gson set up to pretty print, serialize nulls (for testing), and has complex map key serialization.
	 */
//todo remove .#serializeNulls() one confirmed working
	Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().enableComplexMapKeySerialization().create();

	/**
	 * Serializer logger for debugging.
	 */
	Logger log = Logger.getLogger("SaveLogger");

	/**
	 * Serialize object.
	 *
	 * @param <SERIALIZE>
	 * 		Type of object being serialized.
	 * @param toSerialize
	 * 		the object being serialized.
	 * @param type
	 * 		the class of the object being serialized.
	 * @param file
	 * 		which file to serialize too.
	 */
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

	/**
	 * Deserialize object deserialize.
	 *
	 * @param <DESERIALIZE>
	 * 		the type of object being deserialized, this is to make sure the data is cast back to the correct type.
	 * @param type
	 * 		the object class.
	 * @param file
	 * 		the file being collected from.
	 * @return the deserialized object cast to its type ready to be processed.  Make sure to check your casts with
	 * things like Maps as this will return untyped data.
	 */
	default <DESERIALIZE> DESERIALIZE deserializeObject(Type type, File file) {

		try {
			if ((file.exists()))
				if (file.getName().contains(".json"))
			return gson.fromJson(new JsonReader(new BufferedReader(new FileReader(file))), type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}




}
