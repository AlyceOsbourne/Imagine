package lib;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public abstract class SaveData<Data> implements DataSerialization {

	@Expose
	Map<String,Object> storedData;

	SaveData(){
		storedData = new HashMap<>();
	}

	void addToSave(String name, Data data, Class<Data> datatype){
		storedData.put(name,datatype.cast(data));
	}
	Data retrieveFromSave(String name, Class<Data> datatype){
		return datatype.cast(storedData.get(name));
	}
	void saveFile(String filename){
		serializeObject(storedData,Map.class,filename);
	}

	void loadFile(String filename){
		storedData = deserializeObject(Map.class,filename);
	}
}
