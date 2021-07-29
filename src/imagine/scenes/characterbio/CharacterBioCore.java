/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */


package imagine.scenes.characterbio;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import lib.LoadsFXML;

import java.util.HashMap;
import java.util.Map;

public class CharacterBioCore extends BorderPane implements LoadsFXML{


	Map<String,CharacterBio> protagonists = new HashMap<>();
	Map<String,CharacterBio> antagonists = new HashMap<>();

	@FXML
	public TextField forename;
	@FXML
	public TextField middlename;
	@FXML
	public TextField surname;
	@FXML
	public TextField title;
	@FXML
	public TextField nickname;
	@FXML
	public TextField age;
	@FXML
	public TextField gender;
	@FXML
	public TextField sex;
	@FXML
	public TextField sexuality;
	@FXML
	public TabPane datapane;
	@FXML
	public ToolBar toolbar;
	@FXML
	public ScrollPane characterpane;

	public CharacterBioCore() {
		loadFXML();
	}

	@Override
	public void loadControls() {
	}


	static class CharacterBio {


	}
}

