/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.data;

import com.google.gson.reflect.TypeToken;
import lib.serialization.gson.GsonDataSerialization;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Save data.
 *
 * @param <Key>
 * 		the chosen key to represent stored data
 * @param <Data>
 * 		the data being stored
 */
public abstract class SaveData <Key,Data> implements GsonDataSerialization {

	/**
	 * The Stored data.
	 */
	Map<Key,Data>StoredData = new HashMap<>();


	/**
	 * Clears the stored data.
	 */
	void clearData(){
		StoredData.clear();
	}

	/**
	 * Saves stored data map to a jason file;
	 */
	void save(File file){
		serializeObject(StoredData,Map.class, file);
	}


	/**
	 * Loads the map from the file.
	 *
	 */
	void load(File file){
		Type type = new TypeToken<Map<Key,Data>>(){}.getType();
		if(deserializeObject(type,file) != null)
		StoredData = deserializeObject(type,file);
	}

	/**
	 * Store data.
	 *
	 * @param key
	 * 		the key
	 * @param data
	 * 		the data
	 */
	void storeData(Key key, Data data){
		if (StoredData.containsKey(key)) StoredData.replace(key, data);
		else StoredData.put(key, data);
	}

	/**
	 * Retrieve data data.
	 *
	 * @param key
	 * 		the key
	 * @param dataType
	 * 		the data type
	 * @return the data
	 */
	Data retrieveData(Key key,Class<Data>dataType){
		if(StoredData.containsKey(key)){
			return dataType.cast(StoredData.get(key));
		} else return null;
	}

}
