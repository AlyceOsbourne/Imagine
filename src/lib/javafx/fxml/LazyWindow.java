/*
 * Do what the F**k you want
 */

package lib.javafx.fxml;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import lib.javafx.Window;

import java.util.List;

/**
 * just a lazy implement for the Window class,
 * just override the functions as needed,
 * just makes for a tidier end class
 **/

public abstract class LazyWindow extends Window {

	@Override
	protected List<MenuItem> fileMenu() {
		return null;
	}

	@Override
	protected MenuItem newFile() {
		return null;
	}

	@Override
	protected MenuItem saveFile() {
		return null;
	}

	@Override
	protected MenuItem loadFile() {
		return null;
	}

	@Override
	protected List<MenuItem> editMenu() {
		return null;
	}

	@Override
	protected MenuItem undo() {
		return null;
	}

	@Override
	protected MenuItem redo() {
		return null;
	}

	@Override
	protected MenuItem copy() {
		return null;
	}

	@Override
	protected MenuItem cut() {

		return null;
	}

	@Override
	protected MenuItem paste() {
		return null;
	}

	@Override
	protected List<MenuItem> toolsMenu() {

		return null;
	}

	@Override
	protected List<MenuItem> helpMenu() {
		return null;
	}

	@Override
	protected MenuItem help() {

		return null;
	}

	@Override
	protected List<MenuItem> aboutMenu() {
		return null;
	}

	@Override
	protected MenuItem aboutTheDev() {
		return null;
	}

	@Override
	protected Node content() {
		return null;
	}

	@Override
	protected Node rightBar() {
		return null;
	}

	@Override
	protected Node leftBar() {
		return null;
	}

	@Override
	protected Node bottomBar() {
		return null;
	}
}
