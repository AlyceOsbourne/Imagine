/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.core;

import imagine.scenes.menus.MainMenu;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lib.LoadsFXML;
import lib.DataSerialization;

public class Window extends SplitPane implements LoadsFXML, DataSerialization {

	Window loadedWindow;
	MainMenu menu;

	@FXML
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
		menu = new MainMenu();
		this.primaryStage = primaryStage;
		this.changeContent(menu);
	}

	public void changeContent(Node node) {
		this.content.setCenter(node);
		doToggles();
	}

	public void loadControls() {
		this.close.setOnAction(event -> this.primaryStage.close());
		this.returnToMenu.setOnAction(event -> this.changeContent(menu));
		this.newFile.setOnAction(event -> this.changeContent(menu));
	}

	private void doToggles() {
		this.returnToMenu.setVisible(!(this.content.getCenter() instanceof MainMenu));
	}



}
