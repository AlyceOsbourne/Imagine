/*
 * Do what the F**k you want
 */

package lib.javafx;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import lib.javafx.fxml.LoadsFXML;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Core window class for my easy JavaFX implementation framework
 * the general idea of this class is to automatically handle many
 * common functions and prepare an implementation for said function
 * then we can handle a bunch of instantiation automatically for
 * each extending class, with provision to remove redundant null
 * implementations. I am using a border pane due to the fact its
 * convenient for the window format, it allows users to drop in
 * controls along side their main content, and if needed they can
 * completely ignore everything besides the center pane.
 **/
public abstract class Window extends BorderPane {

	@FXML
	final MenuBar menubar;
	@FXML
	final Menu file, edit, tools, help, about;

	{
		//loads the classes FXML file if one is available. FXMLFile must be FX:root format
		if (this instanceof LoadsFXML) ((LoadsFXML) this).loadFXML();
		//menu bar
		{
			//file menu
			{
				file = new Menu("File");
				if (fileMenu() != null) file.getItems().addAll(fileMenu());
				final MenuItem close = new MenuItem("Close");
				close.setOnAction(event -> App.stage.close());
				file.getItems().addAll(Stream.of(newFile(), loadFile(), saveFile(), close).dropWhile(Objects::isNull).toList());
			}

			//edit menu
			{
				edit = new Menu("Edit");
				if (editMenu() != null) edit.getItems().addAll(editMenu());
				edit.getItems().addAll(Stream.of(undo(), redo(), copy(), cut(), paste()).dropWhile(Objects::isNull).toList());
				if (edit.getItems().isEmpty()) edit.setVisible(false);
			}

			//tool menu
			{
				tools = new Menu("Tools");
				if (toolsMenu() != null) tools.getItems().addAll(toolsMenu());
				else tools.setVisible(false);

			}

			//help menu
			{
				help = new Menu("Help");
				if (helpMenu() != null) help.getItems().addAll(helpMenu());
				if (help() != null) help.getItems().add(help());
				if (help.getItems().isEmpty()) help.setVisible(false);
			}

			//about menu
			{
				about = new Menu("About");
				if (aboutMenu() != null) about.getItems().addAll(aboutMenu());
				if (aboutTheDev() != null) about.getItems().add(aboutTheDev());
				if (about.getItems().isEmpty()) about.setVisible(false);
			}
			menubar = new MenuBar();
			menubar.getMenus().addAll(Stream.of(file, edit, tools, help, about).dropWhile(menu -> !menu.isVisible()).toList());
			menubar.autosize();
		}
		//content loading
		{
			if (content() == null) {
				TextArea contentNotFound = new TextArea("Error, content not found from window:" + this.getClass().getSimpleName());
				contentNotFound.autosize();
				contentNotFound.setEditable(false);
				setCenter(contentNotFound);
			} else {
				var content = content();
				if (content instanceof LoadsFXML) ((LoadsFXML) content).loadFXML();
				setCenter(content);
			}
			if (rightBar() != null) {
				var rightBar = rightBar();
				if (rightBar instanceof LoadsFXML) ((LoadsFXML) rightBar).loadFXML();
				setRight(rightBar);
			}
			if (leftBar() != null) {
				var leftBar = leftBar();
				if (leftBar instanceof LoadsFXML) ((LoadsFXML) leftBar).loadFXML();
				setLeft(leftBar);
			}
			if (bottomBar() != null) {
				var bottomBar = bottomBar();
				if (bottomBar instanceof LoadsFXML) ((LoadsFXML) bottomBar).loadFXML();
				setBottom(bottomBar());
			}
			setTop(this.menubar);
		}
	}

	protected abstract List<MenuItem> fileMenu();

	protected abstract MenuItem newFile();

	protected abstract MenuItem saveFile();

	protected abstract MenuItem loadFile();

	protected abstract List<MenuItem> editMenu();

	protected abstract MenuItem undo();

	protected abstract MenuItem redo();

	protected abstract MenuItem copy();

	protected abstract MenuItem cut();

	protected abstract MenuItem paste();

	protected abstract List<MenuItem> toolsMenu();

	protected abstract List<MenuItem> helpMenu();

	protected abstract MenuItem help();

	protected abstract List<MenuItem> aboutMenu();

	protected abstract MenuItem aboutTheDev();

	protected abstract Node content();

	protected abstract Node rightBar();

	protected abstract Node leftBar();

	protected abstract Node bottomBar();

}
