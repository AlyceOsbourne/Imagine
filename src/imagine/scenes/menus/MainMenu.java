


/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */


/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.menus;

import imagine.Main;
import imagine.scenes.characterbio.CharacterScreen;
import imagine.scenes.worldatlus.WorldAtlus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import lib.fxml.LoadsFXML;

public  class MainMenu extends SplitPane implements LoadsFXML {


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
		this.characterBioButton.setOnAction(event -> Main.window.changeContent(new CharacterScreen()));
		this.worldAtlasButton.setOnAction(event -> Main.window.changeContent(new WorldAtlus()));
	}
	{loadFXML();}
}
