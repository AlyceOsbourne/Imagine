package lib;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public interface HoldsSaveData {
	SaveDataHolder getSaveData();
	abstract class SaveDataHolder {
		//todo in saveDataCollection put all maps in extending classes to be loaded into central map.
		public final String SAVEDATANAME = getClass().getSimpleName();
		@Expose
		public final Map<String, Map<String, ?>> STOREDSAVEDATA = new HashMap<>(saveDataCollection());
		public abstract Map<String,Map<String,?>> saveDataCollection();
	}
}
