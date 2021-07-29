package imagine.scenes.characterbio;

import com.google.gson.annotations.Expose;
import imagine.scenes.core.Window;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import lib.HoldsSaveData;
import lib.LoadsFXML;

import java.util.HashMap;
import java.util.Map;

public class CharacterBioCore extends BorderPane implements LoadsFXML, HoldsSaveData {

	SaveData savedata = new SaveData();

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

	public CharacterBioCore(Window window) {
		loadFXML();
	}

	@Override
	public void loadControls() {
	}

	@Override
	public SaveData getSaveData() {
		 return this.savedata;
	}



	class SaveData extends SaveDataHolder{

		Map<String,CharacterBio> protagonists = new HashMap<>();
		Map<String,CharacterBio> antagonists = new HashMap<>();
		@Override
		public Map<String, Map<String, ?>> saveDataCollection() {
			return null;
		}


	}

	static class CharacterBio {
		@Expose
		private final String nickname;
		@Expose
		private final String title;
		@Expose
		private final String forename;
		@Expose
		private final String middlename;
		@Expose
		private final String surname;
		@Expose
		private final int age;
		@Expose
		private final String gender;
		@Expose
		private final String sex;
		@Expose
		private final String sexuality;

		CharacterBio() {
			this("AlyKat","Lady","Alyce","Kahlan", "Windsong", 30, "Femme", "Sexless", "Pan");
		}

		CharacterBio(String nickname, String title, String forename, String middlename, String surname, int age, String gender, String sex, String sexuality) {
			this.nickname = nickname;
			this.title = title;
			this.forename = forename;
			this.middlename = middlename;
			this.surname = surname;
			this.age = age;
			this.gender = gender;
			this.sex = sex;
			this.sexuality = sexuality;
		}


	}
}

