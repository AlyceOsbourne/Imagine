

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.core;

import imagine.Main;
import imagine.scenes.core.data.SaveData;
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
import lib.gson.JsonFileHandler;

import java.io.File;


public class Window extends SplitPane implements LoadsFXML {


	final MainMenu menu = new MainMenu();

	final FileChooser fileChooser = new FileChooser();


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
		this.changeContent(menu);
	}

	public void changeContent(Node node) {
		this.content.setCenter(node);
		doToggles();
	}

	public void loadControls() {
		JsonFileHandler<SaveData.Data> handler = new JsonFileHandler<>(){};
		fileChooser.setTitle("Load Imagine File");
		fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Imagine Save Data",".data"));
		fileChooser.setInitialFileName("SaveData.data");
		this.saveFile.setOnAction(e -> handler.save(fileChooser.showSaveDialog(Main.stage),SaveData.data,SaveData.Data.class));
		this.loadFile.setOnAction(e -> {
			File file = fileChooser.showOpenDialog(Main.stage);
			if (handler.load(file,SaveData.Data.class) != null) {
				SaveData.data = handler.load(file,SaveData.Data.class);
				this.changeContent(new MainMenu());
			}
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

	{loadFXML();}
}
