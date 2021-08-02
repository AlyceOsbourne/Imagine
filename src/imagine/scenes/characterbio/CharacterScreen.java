/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.characterbio;

import imagine.scenes.ContentLibrary;
import imagine.scenes.characterbio.data.Character;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import lib.fxml.LoadsFXML;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Character screen.
 */
public class CharacterScreen extends SplitPane implements LoadsFXML {

	/**
	 * The Loader.
	 */
	FXMLLoader loader = loadFXML();
	/**
	 * The Protagonists.
	 */
	public Map<String,Character> protagonists = new HashMap<>();
	/**
	 * The Antagonists.
	 */
	public Map<String, Character> antagonists = new HashMap<>();
	@FXML
	private TreeItem<Map<String,Character>> protagonistslist = new TreeItem<>(this.protagonists);
	@FXML
	private TreeItem<Map<String,Character>> antagonistslist = new TreeItem<>(this.antagonists);
	@FXML
	private TreeView<Map<String, Character>> characterlibrary;
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
		create.setOnAction(e -> ContentLibrary.window.changeContent(ContentLibrary.CharacterBiograpies.LoadCreateCharacter.content));
 	}
}
