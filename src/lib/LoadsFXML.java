package lib;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public interface LoadsFXML {

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
		catch (IOException e) {e.printStackTrace();}
		return loader;
	}

	void loadControls() throws IOException;
}
