/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.characterbio;

import imagine.Main;
import imagine.data.SaveData;
import imagine.scenes.characterbio.data.Character;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import lib.fxml.LoadsFXML;

/**
 * The type Character screen.
 */
public class CharacterScreen extends SplitPane implements LoadsFXML {

	FXMLLoader loader = loadFXML();
	Character currentCharacter = null;
	@FXML
	private TreeView<String> characterlibrary;
	@FXML
	private TextField title;
	@FXML
	private TextField forename;
	@FXML
	private TextField middlename;
	@FXML
	private TextField surname;
	@FXML
	private TextField nickname;
	@FXML
	private ImageView characterimage;
	@FXML
	private TextField age;
	@FXML
	private TextField gender;
	@FXML
	private TextField sex;
	@FXML
	private TextField sexuality;
	@FXML
	private Button create;
	@FXML
	private Button edit;
	@FXML
	private Button delete;
	@FXML
	private Button random;

	/**
	 * Load controls.
	 */
	@Override
	public void loadControls() {
		create.setOnAction(e -> Main.window.changeContent(new CreateCharacter()));

		edit.setOnAction(e -> Main.window.changeContent(new CreateCharacter().loadCharacter(currentCharacter)));

		delete.setOnAction(e -> {
			SaveData.data.getCharacters().remove(currentCharacter.getNickname());
			Main.window.changeContent(new CharacterScreen());
		});

		TreeItem<String> root = new TreeItem<>("Characters");

		characterlibrary.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		characterlibrary.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, value) -> handle(value));
		for (Character character : SaveData.data.getCharacters().values())
		{
			TreeItem<String> child = new TreeItem<>(character.getNickname());
			root.getChildren().add(child);
		}
		characterlibrary.setRoot(root);
		characterlibrary.setShowRoot(false);
	}

	void handle(TreeItem<String> s){
		String name = s.getValue();
		if (SaveData.data.getCharacters().containsKey(name))
		{
			currentCharacter = SaveData.data.getCharacters().get(name);
			nickname.setText(currentCharacter.getNickname());
			title.setText(currentCharacter.getInfo().getTitle());
			forename.setText(currentCharacter.getInfo().getForename());
			middlename.setText(currentCharacter.getInfo().getMiddlename());
			surname.setText(currentCharacter.getInfo().getSurname());
			age.setText(currentCharacter.getInfo().getAge());
			gender.setText(currentCharacter.getInfo().getGender());
			sex.setText(currentCharacter.getInfo().getSex());
			sexuality.setText(currentCharacter.getInfo().getSexuality());
		}
	}
}
