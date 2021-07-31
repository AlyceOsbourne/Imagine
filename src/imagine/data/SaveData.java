/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.data;

import imagine.Main;
import javafx.stage.FileChooser;
import lib.serialization.gson.GsonDataSerialization;

import java.util.HashMap;
import java.util.Map;

public abstract class SaveData <Key,Data> implements GsonDataSerialization {

	Map<Key,Data>StoredData = new HashMap<>();

	FileChooser chooser = new FileChooser();

	void clearData(){
		StoredData.clear();
	}

	void save(){
		serializeObject(StoredData,Map.class, chooser.showSaveDialog(Main.stage));
	}

	<M extends Map<Key,Data>>void load(Class<M>clazz){
		clearData();
		var tmp = deserializeObject(Map.class, chooser.showOpenDialog(Main.stage));
		if (clazz.isInstance(tmp)) StoredData.putAll(clazz.cast(tmp));
	}

	void storeData(Key key, Data data){
		if (StoredData.containsKey(key)) StoredData.replace(key, data);
		else StoredData.put(key, data);
	}

	Data retrieveData(Key key,Class<Data>dataType){
		if(StoredData.containsKey(key)){
			return dataType.cast(StoredData.get(key));
		} else return null;
	}

}
