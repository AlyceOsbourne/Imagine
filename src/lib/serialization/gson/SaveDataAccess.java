/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package lib.serialization.gson;

public interface SaveDataAccess {
	default <Data> void addToSave(String name, Data data, Class<Data> datatype) {
		SaveData.addToSave(name, data, datatype);
	}

	default <Data> Data retrieveFromSave(String name, Class<Data> datatype) {
		return SaveData.retrieveFromSave(name, datatype);
	}
}
