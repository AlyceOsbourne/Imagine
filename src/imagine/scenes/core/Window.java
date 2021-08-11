/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.core;

import imagine.Main;
import imagine.data.SaveData;
import imagine.scenes.menus.MainMenu;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import lib.fxml.LoadsFXML;


public class Window extends SplitPane implements LoadsFXML {

	MainMenu menu = new MainMenu();

	FileChooser fileChooser = new FileChooser();


	@FXML
	public MenuBar menubar;
	@FXML
	public Menu edit;
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



	public Window() {
		loadFXML();
		this.changeContent(menu);
	}

	public void changeContent(Node node) {
		this.content.setCenter(node);
		doToggles();
	}

	public void loadControls() {
		fileChooser.setTitle("Load Imagine File");
		fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(".json",".json"));
		fileChooser.setInitialFileName("SaveData.json");
		this.saveFile.setOnAction(e -> SaveData.save(fileChooser.showSaveDialog(Main.stage)));
		this.loadFile.setOnAction(e -> {
			SaveData.load(fileChooser.showOpenDialog(Main.stage));
			this.changeContent(new MainMenu());
		});
		this.close.setOnAction(event -> Main.stage.close());
		this.returnToMenu.setOnAction(event -> {
			this.changeContent(menu);
			this.edit.getItems().clear();
			this.edit.setVisible(false);
		});
		this.newFile.setOnAction(event -> SaveData.data = new SaveData.Data());
	}

	public Menu getEditMenu(){
		return edit;
	}

	private void doToggles() {
		this.returnToMenu.setVisible(!(this.content.getCenter() instanceof MainMenu));
	}


	}
