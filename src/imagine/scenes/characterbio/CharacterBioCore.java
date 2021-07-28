package imagine.scenes.characterbio;

import com.google.gson.annotations.Expose;
import imagine.scenes.core.Window;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import lib.LoadsFXML;

import java.util.HashMap;

public class CharacterBioCore extends BorderPane implements LoadsFXML {

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

	@Expose
	@FXML
	public HashMap<String, CharacterBio> mainCharacters;



	public CharacterBioCore(Window window) {
		loadFXML();
		this.mainCharacters = new HashMap<>();
		if(mainCharacters.size() == 0)this.mainCharacters.put("Alykat",new CharacterBio());
	}

	@Override
	public void loadControls() {
	}


	static class CharacterBio {
		CharacterBio() {
			this("AlyKat","Lady","Alyce","Kahlan", "Windsong", 30, "Femme", "Sexless", "Pan");
		}

		CharacterBio(String nickname, String title, String forename, String middlename, String surname, int age, String gender, String sex, String sexuality) {
		}


	}
}

