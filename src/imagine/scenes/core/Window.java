/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.core;

import imagine.Main;
import imagine.scenes.ContentLibrary;
import imagine.scenes.menus.MainMenu;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import lib.fxml.LoadsFXML;
import lib.serialization.gson.GsonDataSerialization;
import lib.serialization.gson.SaveData;

public class Window extends SplitPane implements LoadsFXML, GsonDataSerialization {

	MainMenu menu = (MainMenu) ContentLibrary.Menus.MainMenu.content;

	SaveData savedata = new SaveData() {
		@Override
		public String getFile() {
			return "save.json";
		}
	};

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
		this.saveFile.setOnAction(event -> savedata.saveFile(savedata.getFile()));
		this.loadFile.setOnAction(event -> savedata.loadFile(savedata.getFile()));
		this.close.setOnAction(event -> Main.stage.close());
		this.returnToMenu.setOnAction(event -> this.changeContent(menu));
		this.newFile.setOnAction(event -> this.changeContent(menu));
	}

	private void doToggles() {
		this.returnToMenu.setVisible(!(this.content.getCenter() instanceof MainMenu));
	}



}
