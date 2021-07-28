package imagine.scenes.core;

import com.google.gson.annotations.Expose;
import imagine.scenes.characterbio.CharacterBioCore;
import imagine.scenes.menus.MainMenu;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lib.LoadsFXML;
import lib.Save;

public class Window extends SplitPane implements LoadsFXML, Save {

	Window loadedWindow;
	@FXML
	@Expose
	public SceneLibrary library;
	private final Stage primaryStage;
	@FXML
	public MenuItem saveFile;
	@FXML
	public MenuItem loadFile;
	@FXML
	public MenuItem newFile;
	@FXML
	public MenuItem close;
	@FXML
	public MenuItem returnToMenu;
	@FXML
	public MenuItem about;
	@FXML
	public BorderPane content;

	public Window(Stage primaryStage) {
		loadFXML();
		loadedWindow = this;
		this.primaryStage = primaryStage;
		this.library = new SceneLibrary(this.loadedWindow);
		this.changeContent(library.menu);
	}

	public void changeContent(Node node) {
		this.content.setCenter(node);
		doToggles();
	}

	public void loadControls() {
		this.close.setOnAction(event -> this.primaryStage.close());
		this.saveFile.setOnAction(event -> this.serializeObject(this.library, SceneLibrary.class,"/save.json"));
		this.loadFile.setOnAction(event -> library = deserializeObject(SceneLibrary.class, "/save.json"));
		this.returnToMenu.setOnAction(event -> this.changeContent(this.library.menu));
		this.newFile.setOnAction(event -> {
			this.changeContent(this.library.menu);
			this.library = new SceneLibrary(this.loadedWindow);
		});
	}

	private void doToggles() {
		this.returnToMenu.setVisible(!(this.content.getCenter() instanceof MainMenu));
	}

	public static class SceneLibrary {
		MainMenu menu;
		@Expose
		CharacterBioCore characterBio;
		SceneLibrary(Window window) {
			menu = new MainMenu(window);
			characterBio = new CharacterBioCore(window);
		}
	}
}
