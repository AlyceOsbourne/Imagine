/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.core;

import imagine.Main;
import imagine.data.SaveData;
import imagine.scenes.ContentLibrary;
import imagine.scenes.menus.MainMenu;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import lib.fxml.LoadsFXML;
import lib.serialization.gson.GsonDataSerialization;

import java.util.Map;

public class Window extends SplitPane implements LoadsFXML, GsonDataSerialization {

	MainMenu menu = (MainMenu) ContentLibrary.Menus.MainMenu.content;

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
		this.saveFile.setOnAction(e -> {});
		this.loadFile.setOnAction(e -> {});
		this.close.setOnAction(event -> Main.stage.close());
		this.returnToMenu.setOnAction(event -> this.changeContent(menu));
		this.newFile.setOnAction(event -> this.changeContent(menu));
	}

	private void doToggles() {
		this.returnToMenu.setVisible(!(this.content.getCenter() instanceof MainMenu));
	}

	public static final SaveData<String, Map<?, ?>> SaveData = new SaveData<>() {

	};}
