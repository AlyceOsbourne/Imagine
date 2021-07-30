/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package lib.fxml;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

/**
 * The interface Loads fxml.
 */
public interface LoadsFXML {

	/**
	 * Returning loader is optional, provided for post load modification,
	 * but is also fire and forget if loadFXML() is called also
	 *
	 * @return fully loaded FXMLLoader.
	 */
	@FXML
	default FXMLLoader loadFXML(){
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		loader.setRoot(this);
		loader.setLocation(this.getClass().getResource(this.getClass().getSimpleName()+".fxml"));
		try {
			loader.load();
			loadControls();
		}
		catch (Exception e) {e.printStackTrace();}
		return loader;
	}

	/**
	 * Load controls.
	 *
	 *
	 */
	void loadControls() throws Exception;
}
