/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */


package imagine.scenes.menus;

import imagine.scenes.ContentLibrary;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import lib.fxml.LoadsFXML;

public  class MainMenu extends SplitPane implements LoadsFXML {

	public MainMenu(){
		loadFXML();
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
		this.characterBioButton.setOnAction(event -> ContentLibrary.window.changeContent(ContentLibrary.CharacterBiograpies.LoadCharacterScreen.content));

	}
}
