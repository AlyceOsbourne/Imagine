package imagine.scenes.menus;

import imagine.Main;
import imagine.scenes.characterbio.CharacterBioCore;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import lib.LoadsFXML;

public  class MainMenu extends SplitPane implements LoadsFXML {


	CharacterBioCore cb;

	public MainMenu(){
		loadFXML();
		cb = new CharacterBioCore();
	}
	@FXML
	public Button characterBioButton;
	@FXML
	public Button worldAtlasButton;
	@FXML
	public Button encyclopaediaButton;
	@FXML
	public Button optionsButton;


	@Override
	public void loadControls() {
		this.characterBioButton.setOnAction(event -> Main.window.changeContent(new CharacterBioCore()));

	}
}
