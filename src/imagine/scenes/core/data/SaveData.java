





/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.core.data;

import imagine.scenes.characterbio.data.Character;

import java.util.HashMap;

public class SaveData {

	public static Data data = new Data();

	public static class Data {

		public HashMap<String, Character> getCharacters() {
			return Characters;
		}

		final HashMap<String, Character> Characters = new HashMap<>();
		final HashMap<String, String> StoredWorldMaps = new HashMap<>();
		final HashMap<String, String> StoredRegionMaps = new HashMap<>();
		final HashMap<String, String> StoredCityMaps = new HashMap<>();
		final HashMap<String, String> StoredDungeonMaps = new HashMap<>();

		public HashMap<String, String> getStoredWorldMaps() {
			return StoredWorldMaps;
		}

		public HashMap<String, String> getStoredRegionMaps() {
			return StoredRegionMaps;
		}

		public HashMap<String, String> getStoredCityMaps() {
			return StoredCityMaps;
		}

		public HashMap<String, String> getStoredDungeonMaps() {
			return StoredDungeonMaps;
		}


	}
}

