package imagine.scenes.menus;

import lib.LoadsFXML;
import imagine.scenes.characterbio.CharacterBioCore;
import imagine.scenes.core.Window;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;

public  class MainMenu extends SplitPane implements LoadsFXML {


	private final Window windowIn;
	CharacterBioCore cb;

	public MainMenu(Window windowIn){
		this.windowIn = windowIn;
		loadFXML();
		cb = new CharacterBioCore(this.windowIn);
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
		this.characterBioButton.setOnAction(event -> this.windowIn.changeContent(this.cb));

	}
}
