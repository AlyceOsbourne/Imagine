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

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public abstract class SaveData implements GsonDataSerialization {

	@Expose
	public static final Map<String,Object> storedData = new HashMap<>();

	public static <Data>void addToSave(String name, Data data, Class<Data> datatype){
		storedData.put(name,datatype.cast(data));
	}
	public static  <Data>Data retrieveFromSave(String name, Class<Data> datatype){return datatype.cast(storedData.get(name));}

	public void saveFile(String filename){
		serializeObject(storedData,Map.class,filename);
	}
	public void loadFile(String filename){
		storedData.clear();
		storedData.putAll(deserializeObject(Map.class,filename));
	}
	public void newFile(){storedData.clear();}

	public abstract String getFile();

}
